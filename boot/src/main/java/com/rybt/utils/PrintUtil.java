package com.rybt.utils;

import com.rybt.constant.Constant;
import com.rybt.enumeration.Level;

public class PrintUtil {


    public static void info(String msg) {
        info(msg,null);
    }

    public static void info(String msg, String ...args) {
        print(msg, Level.info.name(), args);
    }

    public static void error(String msg, String ...args) {
        print(msg, Level.error.name(), args);
    }

    public static void print(String msg, String level, String ...args){
        msg = msg.replaceFirst("\\^\\?\\^", level);
        if (ArrayUtil.isEmpty(args)) {
            for (String arg : args) {
                msg = msg.replaceFirst("%\\?%", arg);
            }
        }
        System.out.println(msg);
    }

    public static void separater() {
        System.out.println(Constant.SEPARATOR);
    }
}
