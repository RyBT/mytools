package com.rybt.constant;

import java.util.Arrays;
import java.util.List;

public class Constant {

    public static String OS_NAME;

    public static final String SEPARATOR = "===============================================================";

    static {
        OS_NAME = System.getProperty("os.name").toLowerCase();
    }
}
