package com.zhanghui.front.utils;

import java.util.Objects;

/**
 * String工具类
 *
 * @author ZhangHui
 * @date 2020/11/10
 */
public class StringUtils {
    // 首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (isBlank(s)) {
            return s;
        }
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    // 首字母转大写
    public static String toUpperCaseFirstOne(String s) {
        if (isBlank(s)) {
            return s;
        }
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    public static boolean isBlank(String s) {
        return s == null || s.isEmpty() || s.replace(" ", "").isEmpty();
    }

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    public static String replaceFirst(String text, String regex, String replacement) {
        return text != null && regex != null && replacement != null ? text.replaceFirst(regex, replacement) : text;
    }

    public static String removeFirst(String text, String regex) {
        return replaceFirst(text, regex, "");
    }

    public static String removeFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(1);
    }

    public static boolean equals(Object o1, Object o2){
        return Objects.equals(o1,o2);
    }
}
