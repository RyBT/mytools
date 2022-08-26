package com.rybt.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件工具类
 * @author RyBT
 */
public class FileUtils {
    /**
     * 文件写入
     * @param jarPath
     * @param className
     * @param bytes
     */
    public static void writeClassToFile(String jarPath, String className, byte[] bytes){
        String[] split = className.split("\\.");
        StringBuilder path = new StringBuilder(jarPath);
        for (int index = 0;index < split.length - 1;index ++)
            path.append(split[index] + File.separator);
        File dir = new File(path.toString());
        dir.mkdirs();
        path.append(split[split.length - 1] + ".class");
        writeFile(path.toString(), bytes);
    }

    /**
     * 文件写入
     * @param filePath
     * @param bytes
     */
    public static void writeFile(String filePath,byte[] bytes){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(bytes);
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
    }
}
