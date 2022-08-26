package com.rybt.constant;

public class Constant {

    public static String OS_NAME;

    public static final String SEPARATOR = "===============================================================";

    static {
        OS_NAME = System.getProperty("os.name").toLowerCase();
    }
}
