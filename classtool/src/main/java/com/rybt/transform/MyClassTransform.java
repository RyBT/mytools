package com.rybt.transform;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class MyClassTransform implements ClassFileTransformer {
    private Instrumentation ins = null;

    private String targetClass = null;

    public MyClassTransform(String targetClass, Instrumentation ins) {
        this.ins = ins;
        this.targetClass = targetClass;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return new byte[0];
    }

    public void retransform(){
        Class[] allLoadedClasses = ins.getAllLoadedClasses();
        for (Class clazz : allLoadedClasses) {
            System.out.println(clazz.getName());
        }
    }
}
