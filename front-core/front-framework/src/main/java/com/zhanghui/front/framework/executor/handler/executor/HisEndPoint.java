package com.zhanghui.front.framework.executor.handler.executor;

import com.zhanghui.front.framework.context.Container;
import com.zhanghui.front.framework.executor.bean.InBean;
import com.zhanghui.front.framework.executor.bean.OutBean;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 10:07
 * @version：1.0
 */
public interface HisEndPoint extends Container {

    OutBean send(InBean inBean);

    String packageIn(InBean inBean);

    OutBean packageOut(String result, InBean inBean);
}
