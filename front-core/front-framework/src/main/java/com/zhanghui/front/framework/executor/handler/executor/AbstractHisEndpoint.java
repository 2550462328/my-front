package com.zhanghui.front.framework.executor.handler.executor;

import com.zhanghui.front.framework.context.ContextUtil;
import com.zhanghui.front.framework.exception.InitException;
import com.zhanghui.front.framework.executor.bean.InBean;
import com.zhanghui.front.framework.executor.bean.OutBean;
import com.zhanghui.front.framework.client.SendClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;
import static com.zhanghui.front.framework.boot.FrontBootstrap.*;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 10:08
 * @version：1.0
 */
@Slf4j
public abstract class AbstractHisEndpoint implements HisEndPoint {

    private SendClient sendClient;

    @Override
    public void init() {
        Properties properties = ContextUtil.getProperty();

        ClassLoader cl = SendClient.class.getClassLoader();
        if(cl == null){
            cl = ClassLoader.getSystemClassLoader();
        }

        String hisClientClassName = properties.getProperty(HIS_CLIENT_CLASS);

        try {
            Class clazz = cl.loadClass(hisClientClassName);
            Object o = clazz.newInstance();

            if(o instanceof SendClient){
                sendClient = (SendClient) o;
            }else{
                throw new InitException(String.format("[{}]不是SendClient的实例，无法进行实例化",hisClientClassName));
            }
            this.sendClient.configUrl(properties.getProperty(CLIENT_HOST_URL));
        } catch (InitException e) {
            log.error("初始化[{}]异常,[{}]",hisClientClassName,e.getMessage(),e.getCause());
        } catch (IllegalAccessException e){
            log.error("初始化[{}]异常，非法权限",hisClientClassName,e.getCause());
        } catch (ClassNotFoundException e){
            log.error("初始化[{}]异常，找不到类",hisClientClassName,e.getCause());
        } catch (InstantiationException e){
            log.error("初始化[{}]异常，实例化失败",hisClientClassName,e.getCause());
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public OutBean send(InBean inBean) {
        String s = packageIn(inBean);
        log.info(String.format("[T-%s-%s-HIS-IN][%s]", inBean.getTradeCode(), inBean.getTradeNo(), s));
        String result = null;
        try {
            result = this.sendClient.send(s);
            log.info(String.format("[T-%s-%s-HIS-OUT][%s]", inBean.getTradeCode(), inBean.getTradeNo(), result));
        } catch (Exception e) {
            log.error("请求经办系统失败,[{}]",e.getMessage(),e.getCause());
        }
        return packageOut(result,inBean);
    }
}
