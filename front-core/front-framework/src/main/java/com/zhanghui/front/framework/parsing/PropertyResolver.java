package com.zhanghui.front.framework.parsing;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author: ZhangHui
 * @date: 2020/11/10 15:09
 * @version：1.0
 */
public interface PropertyResolver {

    void resolve(Properties properties, InputStream inputStream);

    void resolve(Properties properties, File file);

    void resolve(Properties properties, String fileName);
}
