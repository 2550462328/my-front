package com.zhanghui.front.framework.parsing;

import java.util.List;

/**
 * @author: ZhangHui
 * @date: 2020/11/10 15:54
 * @version：1.0
 */
public interface PackageScanner {
    void doScan(List<Class> classes, String basePackage);
}
