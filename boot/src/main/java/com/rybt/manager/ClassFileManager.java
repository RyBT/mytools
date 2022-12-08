package com.rybt.manager;

import com.rybt.bean.JavaSourceFromString;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;

/**
 * 自定义类文件管理器，继承ForwardingJavaFileManager类，将程序的调用转发到自定义的管理器中
 */
public class ClassFileManager extends ForwardingJavaFileManager {
    private JavaSourceFromString javaSourceFromString;
    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     */
    public ClassFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    //重写getJavaFileForOutput方法，使获得的java文件是被封装在自定义的类中
    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        JavaSourceFromString javaSourceFromString = new JavaSourceFromString(className, kind);
        this.javaSourceFromString = javaSourceFromString;
        return javaSourceFromString;
    }

    //获得自定义格式的java文件
    public JavaSourceFromString getJavaClassObject() {
        return javaSourceFromString;
    }
}
