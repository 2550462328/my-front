package com.zhanghui.front.framework.executor.job;

import com.zhanghui.front.framework.annotation.BeanIfMissing;
import com.zhanghui.front.framework.annotation.Need;
import com.zhanghui.front.framework.context.Container;
import com.zhanghui.front.framework.executor.bean.CmdAnswer;
import com.zhanghui.front.framework.executor.bean.CmdMesBean;
import com.zhanghui.front.framework.executor.handler.executor.HandlerExecutor;

/**
 * @author: ZhangHui
 * @date: 2020/11/20 11:21
 * @version：1.0
 */
@BeanIfMissing
public class DefaultJobContainer extends AbstractJobFactory implements Container {

    @Need
    private HandlerExecutor handlerExecutor;

    @Override
    public void init() {
        if (this.handlerExecutor instanceof Container) {
            ((Container) this.handlerExecutor).init();
        }
    }

    @Override
    public void start() {
        if (this.handlerExecutor instanceof Container) {
            ((Container) this.handlerExecutor).start();
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public void beforeCreate(CmdMesBean cmdMesBean) {
    }

    @Override
    public Job create(CmdMesBean cmdMesBean) {
        CmdAnswer cmdAnswer = new CmdAnswer();
        DefaultJob defaultJob = new DefaultJob();
        defaultJob.setCmdAnswer(cmdAnswer);
        defaultJob.setCmdMesBean(cmdMesBean);
        defaultJob.setHandler(handlerExecutor);
        return defaultJob;
    }

    @Override
    public void afterCreate(Job job, CmdMesBean cmdMesBean) {
    }

    @Override
    public void beforePost(Job job) {
    }
}
