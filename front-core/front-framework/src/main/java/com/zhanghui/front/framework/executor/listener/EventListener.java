package com.zhanghui.front.framework.executor.listener;

import com.zhanghui.front.framework.executor.bean.CmdMesBean;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 10:48
 * @version：1.0
 */
public interface EventListener {
    void handle(CmdMesBean cmdMesBean);
}
