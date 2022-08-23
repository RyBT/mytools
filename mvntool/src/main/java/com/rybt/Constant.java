package com.rybt;

import java.util.Arrays;
import java.util.List;

public class Constant {

    public static final String JAR_SUFFIX = ".jar";

    public static String OS_NAME;

    public static final String SEPARATOR = "\033[0m===========================================================================================================================";

    public static final List<String> PACKAGE_TYPE = Arrays.asList("jar","war","pom");

    static {
        OS_NAME = System.getProperty("os.name").toLowerCase();
    }
}
