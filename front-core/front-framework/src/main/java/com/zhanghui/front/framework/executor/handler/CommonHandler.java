package com.zhanghui.front.framework.executor.handler;

import com.zhanghui.front.framework.annotation.Need;
import com.zhanghui.front.framework.executor.bean.InBean;
import com.zhanghui.front.framework.executor.bean.OutBean;
import com.zhanghui.front.framework.executor.handler.executor.HisEndPoint;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: ZhangHui
 * @date: 2020/11/21 14:01
 * @version：1.0
 */
@Slf4j
public class CommonHandler extends AbstractHandler {

    @Need(value="HsEndPoint")
    HisEndPoint hsEndPoint;

    @Override
    public void doCmd(InBean inBean, OutBean outBean) throws Exception {
        // todo(缺省处理方式)
        outBean = hsEndPoint.send(inBean);
        log.info("缺省处理：[{}]，结果为：[{}]",inBean,outBean);
    }
}

