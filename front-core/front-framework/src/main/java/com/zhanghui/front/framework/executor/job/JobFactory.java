package com.zhanghui.front.framework.executor.job;

import com.zhanghui.front.framework.executor.bean.CmdMesBean;

/**
 * @author: ZhangHui
 * @date: 2020/11/20 11:26
 * @version：1.0
 */
public interface JobFactory {

    void beforeCreate(CmdMesBean cmdMesBean);

    Job create(CmdMesBean cmdMesBean);

    void afterCreate(Job job, CmdMesBean cmdMesBean);

    void beforePost(Job job);

    void post(Job job);
}
