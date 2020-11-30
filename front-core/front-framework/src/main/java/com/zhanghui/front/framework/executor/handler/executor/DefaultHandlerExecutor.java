package com.zhanghui.front.framework.executor.handler.executor;

import com.zhanghui.front.framework.annotation.BeanIfMissing;
import com.zhanghui.front.framework.annotation.Need;
import com.zhanghui.front.framework.context.Container;
import com.zhanghui.front.exception.UniformException;
import com.zhanghui.front.framework.executor.bean.CmdAnswer;
import com.zhanghui.front.framework.executor.bean.CmdMesBean;
import com.zhanghui.front.framework.executor.bean.InBean;
import com.zhanghui.front.framework.executor.bean.OutBean;
import com.zhanghui.front.framework.executor.handler.Handler;
import com.zhanghui.front.framework.executor.handler.mapping.BusinessHandlerBean;
import com.zhanghui.front.framework.executor.handler.mapping.HandlerMapping;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author: ZhangHui
 * @date: 2020/11/20 11:39
 * @version：1.0
 */
@Slf4j
@BeanIfMissing
public class DefaultHandlerExecutor implements HandlerExecutor, Container{

    @Need
    private HandlerMapping handlerMapping;

    @Override
    public void execute(CmdMesBean cmdMesBean, CmdAnswer cmdAnswer) {
        log.info("cmdMesBean:" + cmdMesBean.toString() + " , cmdAnswer: " + cmdAnswer.toString());
        InBean inBean = new InBean();
        inBean.setData(cmdMesBean.getData());
        inBean.setTradeNo(cmdMesBean.getTradeNo());
        inBean.setPriority(cmdMesBean.getPriority());
        inBean.setTradeCode(cmdMesBean.getTradeCode());
        inBean.setUrl(cmdMesBean.getUrl());
        inBean.setAreaCode(cmdMesBean.getAreaCode());
        OutBean outBean = new OutBean(cmdMesBean.getTradeCode(), "",cmdMesBean.getUrl());
        outBean.setTradeCode(cmdMesBean.getTradeCode());
        BusinessHandlerBean businessHandler = handlerMapping.getHandler(cmdMesBean.getTradeCode());
        try {
            if (businessHandler.getHandlers() != null) {
                List<Handler> handlers = businessHandler.getHandlers();
                for (Handler handler : handlers) {
                    try {
                        handler.doCmd(inBean, outBean);
                    } catch (UniformException e) {
                        log.error("", e);
                        if (businessHandler.getPrinciple().equals("registerSettle")
                                || businessHandler.getPrinciple().equals("settle")) {
                        }
                        if (handler.getExceptionHandler() != null) {
                            try {
                                handler.getExceptionHandler().doCmd(inBean, outBean);
                            } catch (Exception e1) {
                                log.error(e1.getMessage());
                                throw e1;
                            }
                        }
                        throw e;
                    }

                }

            } else {
                throw new RuntimeException("找不到对应指令配置的handler");
            }
            if ("-1".equals(outBean.getResult())) {
                cmdAnswer.setResult(-1);
                cmdAnswer.setErrCode(outBean.getErrCode());
            } else {
                cmdAnswer.setResult(0);
            }

            cmdAnswer.setTradeCode(inBean.getTradeCode());
            cmdAnswer.setMessage(outBean.getMessage());
            cmdAnswer.setTradeNo(inBean.getTradeNo());
            cmdAnswer.setPriority(inBean.getPriority());

        } catch (Exception e) {
            log.error(e.getMessage() + "--" + e.getLocalizedMessage(), e);
            outBean.setResult("-1");
            if (e instanceof UniformException){
                outBean.setMessage(String.format(e.getMessage(), cmdMesBean.getTradeCode()) + "]");
                outBean.setErrCode(((UniformException)e).getCode());
            }else{
                outBean.setMessage(e.getMessage());
            }

            cmdAnswer.setResult(-1);
            cmdAnswer.setTradeCode(inBean.getTradeCode());
            cmdAnswer.setMessage(outBean.getMessage());
            cmdAnswer.setTradeNo(inBean.getTradeNo());
            cmdAnswer.setPriority(inBean.getPriority());
            cmdAnswer.setErrCode(outBean.getErrCode());
        } finally {
            cmdAnswer.setData(outBean.getData());
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
