package com.zhanghui.front.framework.executor.job;

import com.zhanghui.front.framework.context.ContextUtil;
import com.zhanghui.front.framework.executor.CmdExecutor;
import com.zhanghui.front.framework.executor.bean.CmdMesBean;
import com.zhanghui.front.framework.executor.listener.EventListener;

import java.util.List;

/**
 * @author: ZhangHui
 * @date: 2020/11/20 11:27
 * @version：1.0
 */
public abstract class AbstractJobFactory implements JobFactory, EventListener {

    private List<CmdExecutor> cmdExecutors;

    @Override
    public void post(Job job) {
        if(cmdExecutors == null){
            cmdExecutors = ContextUtil.getBeans(CmdExecutor.class);
        }
        for(CmdExecutor cmdExecutor : cmdExecutors){
            cmdExecutor.submit(job);
        }
    }

    @Override
    public void handle(CmdMesBean cmdMesBean) {
        beforeCreate(cmdMesBean);
        Job job = create(cmdMesBean);
        afterCreate(job,cmdMesBean);
        beforePost(job);
        post(job);
    }
}
