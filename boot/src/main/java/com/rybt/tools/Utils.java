package com.rybt.tools;

import com.rybt.Boot;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class Utils {
    public static String getJarPath(){
        URL localUrl = Boot.class.getProtectionDomain().getCodeSource().getLocation();
        String path = null;
        try {
            path = URLDecoder.decode(
                    localUrl.getFile().replace("+", "%2B"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("【ERROR】路径获取异常");
            e.printStackTrace();
        }
        return path.substring(1, path.lastIndexOf("/"));
    }
}
