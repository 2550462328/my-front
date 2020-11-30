package com.zhanghui.front.framework.executor.handler.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhanghui.front.framework.context.ContextUtil;
import com.zhanghui.front.utils.ClassUtils;
import com.zhanghui.front.utils.StringUtils;
import com.zhanghui.front.framework.client.SendClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Properties;
import static com.zhanghui.front.framework.boot.FrontBootstrap.*;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 14:07
 * @version：1.0
 */
@Slf4j
public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

    private HttpRequest httpRequest;

    private final StringBuilder bufString = new StringBuilder();

    private final String SICARD_CHECK_TRADECODE="ssc.front.siCardCheck";
    private final String PUSH= "2";

    private final String MSG_TYPE_PUSHJOB = "/pushJob";
    private final String MSG_TYPE_SICARDCHECK = "/siCardCheck";
    private final String HTTP_CLIENT_LOCATION = "com.zhanghui.remote.client.HisHttpClient";

    private static final AsciiString HTTP_HEADER_CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString HTTP_HEADER_CONTENT_LENGTH = AsciiString.cached("Content-Length");
    private static final AsciiString HTTP_HEADER_CONNECTION = AsciiString.cached("Connection");
    private static final AsciiString HTTP_HEADER_KEEP_ALIVE = AsciiString.cached("keep-alive");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        log.info("nettyj接收到消息：[{}]",msg);

        if(msg instanceof HttpRequest){
            httpRequest = (HttpRequest) msg;
            return;
        }

        if(msg instanceof HttpContent){
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();

            bufString.append(content.toString(CharsetUtil.UTF_8));

            // 当前已经全部接收Http消息
            if(msg instanceof LastHttpContent){

                ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
                Properties properties = ContextUtil.getProperty();

                String respBody = "";
                SendClient sendClient;
                Class<?> inClass;

                if(StringUtils.equals(httpRequest.uri(),MSG_TYPE_PUSHJOB)){
                    try {
                        inClass = classLoader.loadClass(HTTP_CLIENT_LOCATION);
                        sendClient = (SendClient) inClass.newInstance();
                        respBody = sendClient.send(bufString.toString());
                    } catch (ClassNotFoundException e) {
                        log.error("前置代码异常，找不到[{}]",HTTP_CLIENT_LOCATION);
                    } catch (Exception e) {
                        log.error("httpClient远程调用出现异常,[{}]",e.getMessage(),e.getCause());
                    }
                    doResponse(respBody, ctx);
                }else if(StringUtils.equals(httpRequest.uri(),MSG_TYPE_SICARDCHECK)){
                    Map reqMap = JSON.parseObject(this.bufString.toString(),Map.class);
                    sendClient = getSendClient(properties);

                    if(sendClient != null){
                        JSONObject reqJson = requestContent(reqMap);
                        sendClient.configUrl(properties.getProperty(CLIENT_CMD_URL));
                        log.info("请求社保云参数{}",reqJson);
                        respBody = sendClient.send(reqJson.toString());
                        JSONObject answerObject = JSON.parseObject(respBody);
                        log.info("服务端接收响应参数{}",answerObject);
                        if(answerObject.getInteger("result").intValue() == 0) {
                            doResponse("{\"result\":\"0\",\"data\":\"" + JSON.parseArray(answerObject.getString("tradeList"), Map.class).get(0).get("data") + "\"}", ctx);
                            return;
                        }
                        doResponse("{\"result\":\"-1\",\"errMessage\":\"" + answerObject.getString("errMessage") + "\"}",
                                ctx);
                    }

                } else{
                    doResponse("{\"status\":\"ok\"}", ctx);
                }

                // 释放容器
                ReferenceCountUtil.release(content);

            }

        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
    }

    public void doResponse(String result, ChannelHandlerContext ctx) {

        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK, Unpooled.wrappedBuffer(result.getBytes(CharsetUtil.UTF_8)));
        httpResponse.headers().set(HTTP_HEADER_CONTENT_TYPE,"application/json;charset=utf-8");
        httpResponse.headers().set(HTTP_HEADER_CONTENT_LENGTH,httpResponse.content().readableBytes());

        if(!HttpUtil.isKeepAlive(httpRequest)){
            ctx.write(httpResponse).addListener(ChannelFutureListener.CLOSE);
        }else{
            httpResponse.headers().set(HTTP_HEADER_CONNECTION,HTTP_HEADER_KEEP_ALIVE);
            ctx.write(httpResponse);
        }
    }

    public SendClient getSendClient(Properties properties) throws Exception {
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        String pushClient = properties.getProperty(CMD_CLIENT_CLASS);
        try {
            Class<?> aClass = classLoader.loadClass(pushClient);
            return 	 (SendClient) aClass.newInstance();
        } catch (Exception e) {
            log.error("获取配置文件[{}]失败","cmd.client.class", e.getCause());
            return null;
        }
    }

    private JSONObject requestContent(Map reqMap) {
        JSONObject json = new JSONObject();
        json.put("tradeCode", SICARD_CHECK_TRADECODE);
        json.put("appId", ContextUtil.getProperty("appId"));
        json.put("tradeMethod", reqMap.get("tradeCode"));
        json.put("areaCode", reqMap.get("areaCode"));
        json.put("push",PUSH);
        json.putAll((Map)reqMap.get("data"));
        return json;
    }
}
