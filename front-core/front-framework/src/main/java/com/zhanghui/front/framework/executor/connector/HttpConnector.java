package com.zhanghui.front.framework.executor.connector;

import com.zhanghui.front.framework.annotation.Bean;
import com.zhanghui.front.framework.annotation.Value;
import com.zhanghui.front.framework.executor.handler.netty.HttpServerInboundHandler;
import com.zhanghui.front.utils.StringUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

import static com.zhanghui.front.framework.boot.FrontBootstrap.*;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 11:51
 * @version：1.0
 */
@Slf4j
@Bean("httpConnector")
public class HttpConnector implements Connector, Runnable {

    @Value("http.mode.status")
    private String status;
    @Value("http.port")
    private int PORT;

    private final Thread httpSendThread = new Thread(this,"httpSendThread");

    @Override
    public void start() {
        if (StringUtils.equals(CONNECTOR_STATUS_OPEN, status)) {
            httpSendThread.start();
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .localAddress(PORT)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch){
                            ch.pipeline().addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            // 具体业务处理
                            ch.pipeline().addLast(new HttpServerInboundHandler());
                        }
                    });
        } catch (Exception e) {
            log.error("httpSendThread线程启动失败：[{}]",e.getMessage(), e.getCause());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
