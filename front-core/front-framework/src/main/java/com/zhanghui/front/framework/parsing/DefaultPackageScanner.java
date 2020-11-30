package com.zhanghui.front.framework.parsing;

import com.zhanghui.front.utils.ClassUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author: ZhangHui
 * @date: 2020/11/10 15:55
 * @version：1.0
 */
@Slf4j
public class DefaultPackageScanner implements PackageScanner {

    @Override
    public void doScan(List<Class> classes, String basePackage) {

        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

        URL url = classLoader.getResource(basePackage.replace(".", "/"));
        String filepath = url.getFile();
        try {
            filepath = URLDecoder.decode(filepath, "utf-8");
            log.info("开始扫描目录：[{}]", basePackage);
            if (filepath.contains("!")) {
                filepath = filepath.substring(5, filepath.indexOf("!"));
                JarInputStream jarIn = new JarInputStream(new FileInputStream(filepath));
                JarEntry entry = jarIn.getNextJarEntry();

                while (null != entry) {
                    String name = entry.getName();
                    if (name.startsWith(basePackage.replace(".", "/")) && name.endsWith(".class")) {
                        classes.add(Class.forName(name.replace("/", ".").replace(".class", "")));
                    }

                    entry = jarIn.getNextJarEntry();
                }
            } else {
                File file = new File(filepath);
                if (file != null) {
                    String[] names = file.list();
                    for (String name : names) {
                        if (name.endsWith(".class")) {
                            classes.add(Class.forName(basePackage + "." + name.replace(".class", "")));
                        } else {
                            doScan(classes, basePackage + "." + name);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("扫描[{}]出现异常",basePackage,e.getCause());
        }
    }
}
