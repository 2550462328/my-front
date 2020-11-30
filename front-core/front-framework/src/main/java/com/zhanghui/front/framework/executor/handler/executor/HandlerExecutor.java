package com.zhanghui.front.framework.executor.handler.executor;


import com.zhanghui.front.framework.executor.bean.CmdAnswer;
import com.zhanghui.front.framework.executor.bean.CmdMesBean;

public interface HandlerExecutor {

    void execute(CmdMesBean cmdMesBean, CmdAnswer cmdAnswer);
}
