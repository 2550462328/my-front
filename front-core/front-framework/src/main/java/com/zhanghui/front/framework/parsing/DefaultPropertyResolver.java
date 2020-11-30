package com.zhanghui.front.framework.parsing;

import com.zhanghui.front.framework.exception.InitException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

/**
 * @author: ZhangHui
 * @date: 2020/11/10 15:09
 * @version：1.0
 */
@Slf4j
public class DefaultPropertyResolver implements PropertyResolver {

    @Override
    public void resolve(Properties properties, InputStream inputStream) {
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new InitException("不能载入配置文件：" , e.getCause());
        }
    }

    @Override
    public void resolve(Properties properties, File file) {
        try {
            this.resolve(properties,new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new InitException("找不到配置文件:" + file.getAbsolutePath(), e);
        }
    }

    @Override
    public void resolve(Properties properties, String fileName) {
        File file = new File(fileName);
        if(file.exists()){
            this.resolve(properties,file);
        }else{
            throw new InitException("找不到配置文件："+fileName);
        }
    }
}
