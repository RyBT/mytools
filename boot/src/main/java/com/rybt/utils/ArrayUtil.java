package com.rybt.utils;

public class ArrayUtil {

    public static boolean isEmpty(Object[] array){
        if (array == null || array.length <= 0) {
            return false;
        }
        return true;
    }

    public static boolean isNotEmpty(Object[] array){
        if (array != null && array.length > 0) {
            return true;
        }
        return false;
    }
}
