package com.rybt.utils;

public class StringUtil {

    public static boolean isEmpty(String str){
        if (str == null || "".equals(str) || str.length() <= 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str){
        if (str != null && str.length() > 0) {
            return true;
        }
        return false;
    }
}
