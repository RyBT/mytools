package com.rybt;


import com.rybt.constant.Constant;
import com.rybt.exception.ExitException;
import com.rybt.tools.ClassTool;
import com.rybt.tools.MavenTool;
import com.rybt.tools.MemShellTool;
import com.rybt.utils.PrintUtil;
import com.sun.tools.javah.Util;

import java.util.Properties;
import java.util.Scanner;

/**
 * 工具入口
 * @author RyBT
 */
public class Boot {

    private static ClassTool classTool ;

    private static MavenTool mavenTool ;

    private static MemShellTool memShellTool ;

    private static Scanner scanner = new Scanner(System.in);

    static {
        classTool = new ClassTool();
        mavenTool = new MavenTool();
        memShellTool = new MemShellTool();
    }

    public static void main( String[] args ) {
        PrintUtil.info(Constant.MENU_TOTAL, "1", "2", "0");
        String tool;
        while((tool = scanner.next()) != null) {
            try {
                switch (tool) {
                    case "1":
                        classTool();
                        break;
                    case "2":
                        mavenTool();
                        break;
                    case "0":
                        PrintUtil.separater();
                        return;
                    default:
                        PrintUtil.error(Constant.MESSAGE_PLACEHOLDER + " Unknown number!");
                        throw new ExitException();
                }
                PrintUtil.info(Constant.MENU_TOTAL, "1", "2", "0");
            }catch (ExitException e) {
                PrintUtil.info(Constant.MESSAGE_PLACEHOLDER + " Exit tools.");
                return;
            }
        }
    }

    private static void mavenTool() throws ExitException {
        PrintUtil.separater();
        PrintUtil.info(Constant.MAVEN_TOOL_MENU, "1", "0");
        String num;
        while((num = scanner.next()) != null) {
            scanner.nextLine();
            switch(num){
                case "1":
                    PrintUtil.info(Constant.MESSAGE_PLACEHOLDER + " Input jar path:");
                    String path = scanner.nextLine();
                    PrintUtil.info(Constant.MESSAGE_PLACEHOLDER + " Input group id:");
                    String groupId = scanner.nextLine();
                    PrintUtil.info(Constant.MESSAGE_PLACEHOLDER + " Input artifact id:");
                    String artifactId = scanner.nextLine();
                    PrintUtil.info(Constant.MESSAGE_PLACEHOLDER + " Input version:");
                    String version = scanner.nextLine();
                    PrintUtil.info(Constant.MESSAGE_PLACEHOLDER + " Input packaging:");
                    String packaging = scanner.nextLine();
                    mavenTool.deployJar(path,groupId,artifactId,version,packaging);
                    throw new ExitException();
                case "0":
                    return;
                default:
                    PrintUtil.error(Constant.MESSAGE_PLACEHOLDER + " 未知编号!");
                    throw new ExitException();
            }
        }
    }

    private static void classTool() throws ExitException {
        PrintUtil.separater();
        PrintUtil.info(Constant.CLASS_TOOL_MENU, "1", "2", "3", "0");
        String num;
        while((num = scanner.next()) != null) {
            scanner.nextLine();
            switch(num){
                case "1":
                    throw new ExitException();
                case "2":
                    PrintUtil.info(Constant.MESSAGE_PLACEHOLDER + " Input Java code:");
                    String str = "";
                    StringBuilder javaCode = new StringBuilder();
                    String fileName = "";
                    if (str == null || str.length() == 0) {
                        while (!"".equals((str = scanner.nextLine()))) {
                            javaCode.append(str + "\n");
                            if (str.indexOf("public class") > -1) {
                                fileName = str.split("\\s")[2];
                            }
                        }
                    }
                    PrintUtil.info(Constant.MESSAGE_PLACEHOLDER + " Input file path:");
                    String filepath = scanner.nextLine();
                    classTool.compiler(javaCode.toString(), fileName, filepath);
                    throw new ExitException();
                case "3":
                    PrintUtil.info(Constant.MESSAGE_PLACEHOLDER + " Input base64 encrypted string:");
                    String clazz = scanner.nextLine();
                    PrintUtil.info(Constant.MESSAGE_PLACEHOLDER + " Input file path:");
                    String path = scanner.nextLine();
                    classTool.decodeClass(clazz,path);
                    throw new ExitException();
                case "0":
                    return;
                default:
                    PrintUtil.error(Constant.MESSAGE_PLACEHOLDER + " 未知编号!");
                    throw new ExitException();
            }
        }
    }
}
