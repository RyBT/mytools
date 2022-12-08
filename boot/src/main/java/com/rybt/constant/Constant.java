package com.rybt.constant;

public class Constant {

    public static String OS_NAME;

    public static final String SEPARATOR = "===============================================================";

    public static final String MESSAGE_PLACEHOLDER = "#^?^#";

    public static final String NUMBER_PLACEHOLDER = "*%?%*";

    public static final String MENU_TOTAL = Constant.MESSAGE_PLACEHOLDER + " Please enter tool number:\n" +
            Constant.NUMBER_PLACEHOLDER + " Class Tool\n" +
            Constant.NUMBER_PLACEHOLDER + " Maven Tool\n" +
            Constant.NUMBER_PLACEHOLDER + " Exit";

    public static final String CLASS_TOOL_MENU = Constant.MESSAGE_PLACEHOLDER + " Load `Class-Tool` successful.Please enter operation number:\n" +
            Constant.NUMBER_PLACEHOLDER + " Dump class from jvm\n" +
            Constant.NUMBER_PLACEHOLDER + " Compiler a java string into a byte array and encode it by base64\n" +
            Constant.NUMBER_PLACEHOLDER + " Decode a byte array which encode by base64\n" +
            Constant.NUMBER_PLACEHOLDER + " Back to previous menu";

    public static final String MAVEN_TOOL_MENU = Constant.MESSAGE_PLACEHOLDER + " Load `Maven-Tool` successful.Please enter operation number:\n" +
            Constant.NUMBER_PLACEHOLDER + " Deploy jar to repository\n" +
            Constant.NUMBER_PLACEHOLDER + " Back to previous menu";

    static {
        OS_NAME = System.getProperty("os.name").toLowerCase();
    }
}
