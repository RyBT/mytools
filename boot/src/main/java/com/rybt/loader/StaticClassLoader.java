package com.rybt.loader;

public class StaticClassLoader extends ClassLoader{
    public Class getTheClass(String name ,byte[] b){
        return defineClass(name ,b ,0,b.length,null);
    }
}
