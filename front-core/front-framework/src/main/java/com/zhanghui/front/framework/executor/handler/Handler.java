package com.zhanghui.front.framework.executor.handler;

import com.zhanghui.front.framework.executor.bean.InBean;
import com.zhanghui.front.framework.executor.bean.OutBean;

/**
 * @author: ZhangHui
 * @date: 2020/11/10 16:34
 * @version：1.0
 */
public interface Handler {
    void doCmd(InBean inBean, OutBean outBean) throws Exception;

    void setExceptionHandler(Handler exceptionHandler);

    Handler getExceptionHandler();
}
