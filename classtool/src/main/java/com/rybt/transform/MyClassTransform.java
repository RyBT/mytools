package com.rybt.transform;

import com.rybt.Agent;

import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.net.URLDecoder;
import java.security.ProtectionDomain;
import java.util.regex.Pattern;

public class MyClassTransform implements ClassFileTransformer {
    private Instrumentation ins;

    private String regex;

    private String jarPath;


    public MyClassTransform(String regex, Instrumentation ins) {
        this.ins = ins;
        this.regex = regex;
        URL location = Agent.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            jarPath = URLDecoder.decode(
                    location.getFile().replace("+", "%2B"), "UTF-8");
            jarPath = jarPath.substring(1, jarPath.lastIndexOf("/") + 1);
        } catch (UnsupportedEncodingException e) {
            System.out.println("【ERROR】jar路径获取失败");
            e.printStackTrace();
        }
        System.out.println("【INFO】jar包路径为:" + jarPath);
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        String name = className.replace('/','.');
        if (!Pattern.matches(regex, name)) {
            return classfileBuffer;
        }
        String[] split = name.split("\\.");
        StringBuilder path = new StringBuilder(jarPath);
        for (int index = 0;index < split.length - 1;index ++)
            path.append(split[index] + File.separator);
        File dir = new File(path.toString());
        dir.mkdirs();
        path.append(split[split.length - 1] + ".class");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path.toString());
            fileOutputStream.write(classfileBuffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return classfileBuffer;
    }

    public void retransform(){
        Class[] allLoadedClasses = ins.getAllLoadedClasses();
        for (Class clazz : allLoadedClasses) {
            if (Pattern.matches(regex, clazz.getName())) {
                try {
                    ins.retransformClasses(clazz);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
