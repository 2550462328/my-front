package com.zhanghui.front.framework.executor.handler.executor;

import com.alibaba.fastjson.JSONObject;
import com.zhanghui.front.framework.annotation.Bean;
import com.zhanghui.front.framework.executor.bean.InBean;
import com.zhanghui.front.framework.executor.bean.OutBean;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 10:30
 * @version：1.0
 */
@Bean(value = "HsEndPoint")
public class DefaultHisEndpoint extends AbstractHisEndpoint{

    @Override
    public String packageIn(InBean inBean) {
        return JSONObject.toJSONString(inBean);
    }

    @Override
    public OutBean packageOut(String result, InBean inBean) {
        return JSONObject.parseObject(result,OutBean.class);
    }

}
