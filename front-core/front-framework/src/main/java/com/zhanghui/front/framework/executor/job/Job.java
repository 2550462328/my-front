package com.zhanghui.front.framework.executor.job;

import com.zhanghui.front.framework.executor.bean.CmdAnswer;
import com.zhanghui.front.framework.executor.bean.CmdMesBean;
import com.zhanghui.front.framework.executor.handler.executor.HandlerExecutor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 16:37
 * @version：1.0
 */
@Getter
@Setter
public abstract class Job implements Runnable {
    private CmdMesBean cmdMesBean;
    private CmdAnswer cmdAnswer;
    private HandlerExecutor handler;

    public void execute() {
        aroundBegin();
        aroundHander();
        aroundFinish();
    }

    protected void aroundBegin() {
        begin();
    }

    protected void aroundHander() {
        doHander();
    }

    protected void aroundFinish() {
        finsh();
    }

    private void begin() {
        this.cmdAnswer.setTarget(this.cmdMesBean.getTarget());
        cmdAnswer.setTradeCode(cmdMesBean.getTradeCode());
        cmdAnswer.setTradeNo(cmdMesBean.getTradeNo());
    }

    private void doHander() {
        this.handler.execute(cmdMesBean, cmdAnswer);
    }

    private void finsh() {
        cmdAnswer.answer();
    }

    @Override
    public void run() {
        execute();
    }
}
