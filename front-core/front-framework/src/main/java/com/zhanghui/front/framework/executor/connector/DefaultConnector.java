package com.zhanghui.front.framework.executor.connector;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhanghui.front.framework.annotation.BeanIfMissing;
import com.zhanghui.front.framework.annotation.Need;
import com.zhanghui.front.framework.annotation.Value;
import com.zhanghui.front.framework.context.ContextUtil;
import com.zhanghui.front.framework.exception.InitException;
import com.zhanghui.front.framework.executor.bean.CmdAnswer;
import com.zhanghui.front.framework.executor.bean.CmdMesBean;
import com.zhanghui.front.framework.executor.listener.EventListener;
import com.zhanghui.front.utils.StringUtils;
import com.zhanghui.front.framework.client.SendClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.*;

import static com.zhanghui.front.framework.boot.FrontBootstrap.*;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 10:40
 * @version：1.0
 */
@Slf4j
@BeanIfMissing
public class DefaultConnector implements Connector, AnswerAble, Runnable {

    private SendClient cmdClient;
    private volatile boolean pause = false;
    private volatile boolean run = false;

    @Need
    private EventListener[] eventListeners;

    @Value("cmd.client.class")
    private String classname;
    @Value("poll.mode.status")
    private String status;
    @Value("pollTime")
    private Long pollTime;
    @Value("appId")
    private String appId;
    @Value("pollNum")
    private Integer integer;

    private final Thread cmdSendThread = new Thread(this,"cmdSendThread");

    @Override
    public void start() {
        if (this.run || !StringUtils.equals(CONNECTOR_STATUS_OPEN, status)) {
            return;
        }
        if(cmdClient == null){
            buildSendClient();
        }
        this.pause = false;
        this.run = true;
        cmdSendThread.start();
    }

    @Override
    public void pause()  {
        this.pause = true;
    }

    @Override
    public void resume()  {
        this.pause = false;
    }

    @Override
    public void destroy() {
        this.pause = false;
        this.run = false;
        cmdSendThread.interrupt();
    }

    @Override
    public void answer(CmdAnswer cmdAnswer, String tradeCode) {
        String request = JSON.toJSONString(cmdAnswer);
        log.info(String.format("[T-%s-%s-1002-IN][%s]", cmdAnswer.getTradeCode(), cmdAnswer.getTradeNo(), request));
        try {
            String result = this.cmdClient.send(request);
            log.info(String.format("[T-%s-%s-1002-OUT][%s]", cmdAnswer.getTradeCode(), cmdAnswer.getTradeNo(), result));
        } catch (Exception e) {
            log.error("调用云平台回调方式失败，回调参数为:{},错误信息为{]", JSON.toJSONString(cmdAnswer), e);
        }
    }

    @Override
    public void run() {
        while (this.run) {
            try {
                Thread.sleep(pollTime);
                String requestBody = getSscTrade();
                log.info(String.format("[HEART|IN|%s|%d][%s]",
                        DateFormatUtils.format(new Date(), "yyyyMMdd hh:mm:ss"), integer, requestBody));
                String respBody = this.cmdClient.send(requestBody);
                log.info(String.format("[HEART|OUT|%s][%s]",
                        DateFormatUtils.format(new Date(), "yyyyMMdd hh:mm:ss"), respBody));
                analyzeResp(respBody);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void analyzeResp(String respBody) {
        if (!this.pause && respBody != null) {
            JSONObject jsonObject = JSON.parseObject(respBody);
            if (!jsonObject.containsKey("result")) {
                log.error("无法解析传入数据{}", respBody);
                return;
            }
            if (!"0".equals(jsonObject.getString("result"))) {
                log.error("获取指令失败，错误代码：{},错误信息：{}", jsonObject.getString("errCode"),
                        jsonObject.getString("errMessage"));
                return;
            }
            if (jsonObject.getJSONArray("tradeList") != null) {
                List<CmdMesBean> list = JSONObject.parseArray(jsonObject.getJSONArray("tradeList").toJSONString(), CmdMesBean.class);

                if (!list.isEmpty()) {
                    for (CmdMesBean cmdMesBean : list) {
                        for (EventListener listener : this.eventListeners) {
                            cmdMesBean.setTarget(this);
                            listener.handle(cmdMesBean);
                        }
                    }
                }
            }
        }
    }

    private String getSscTrade() {
        return "{\"tradeCode\":\"ssc.front.queryCommand\",\"tradeNum\":\"" + integer.toString() + "\"}";
    }

    private void buildSendClient() {
        Properties properties = ContextUtil.getProperty();

        ClassLoader cl = SendClient.class.getClassLoader();
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }

        String cmdClientClassName = properties.getProperty(CMD_CLIENT_CLASS);

        try {
            Class clazz = cl.loadClass(cmdClientClassName);
            Object o = clazz.newInstance();

            if (o instanceof SendClient) {
                cmdClient = (SendClient) o;
            } else {
                throw new InitException(String.format("[{}]不是SendClient的实例，无法进行实例化", cmdClientClassName));
            }
            this.cmdClient.configUrl(properties.getProperty(CLIENT_CMD_URL));
        } catch (InitException e) {
            log.error("初始化[{}]异常,[{}]", cmdClientClassName, e.getMessage(), e.getCause());
        } catch (IllegalAccessException e) {
            log.error("初始化[{}]异常，非法权限", cmdClientClassName, e.getCause());
        } catch (ClassNotFoundException e) {
            log.error("初始化[{}]异常，找不到类", cmdClientClassName, e.getCause());
        } catch (InstantiationException e) {
            log.error("初始化[{}]异常，实例化失败", cmdClientClassName, e.getCause());
        }

    }
}
