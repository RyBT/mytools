package com.rybt;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class MvnMenu
{
    public static void main( String[] args ) {
        String osShell = "";
        if (Constant.OS_NAME.indexOf("windows") > -1) {
            osShell = "cmd.exe /c";
        } else if (Constant.OS_NAME.indexOf("linux") > -1) {
            osShell = "/bin/sh -c";
        } else {
            System.err.println("不支持的操作系统");
            return;
        }
        //获取参数
        Scanner scanner = new Scanner(System.in);
        System.out.println(Constant.SEPARATOR);
        System.out.print("请输入jar包路径：");
        String jarPath = scanner.nextLine();
        System.out.print("请输入groupId：");
        String groupId = scanner.nextLine();
        System.out.print("请输入artifactId：");
        String artifactId = scanner.nextLine();
        System.out.print("请输入version：");
        String version = scanner.nextLine();
        System.out.print("请输入packaging：");
        String packaging = scanner.nextLine();
        System.out.println(Constant.SEPARATOR);
        System.out.println("参数校验中...");
        if (StringUtils.isEmpty(jarPath) || jarPath.indexOf(Constant.JAR_SUFFIX) == -1) {
            System.out.println("\033[31m错误!jar包路径输入有误，请确认jar包路径!");
            return;
        }
        if (StringUtils.isEmpty(groupId)) {
            System.out.println("\033[31m请输入groupId!");
            return;
        }
        if (StringUtils.isEmpty(artifactId)) {
            System.out.println("\033[31m请输入artifactId!");
            return;
        }
        if (StringUtils.isEmpty(version)) {
            System.out.println("\033[31m请输入version!");
            return;
        }
        if (StringUtils.isEmpty(packaging)) {
            System.out.println("\033[31m请输入packaging!");
            return;
        }
        if (!Constant.PACKAGE_TYPE.contains(packaging)) {
            System.out.println("\033[31m请输入正确的packaging!(jar、war或pom)");
            return;
        }
        System.out.println("\033[32m参数校验通过!");
        System.out.println(Constant.SEPARATOR);
        String command = String.format("%s mvn install:install-file -Dfile=%s -DgroupId=%s -DartifactId=%s -Dversion=%s -Dpackaging=%s",
                osShell, jarPath, groupId, artifactId, version, packaging);
        System.out.println("待执行的命令为：");
        System.out.println("  " + command);
        StringBuilder result = new StringBuilder();
        System.out.println(Constant.SEPARATOR);
        System.out.println("命令执行中...");
        try {
            Process exec = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    exec.getInputStream()), 5120);
            String str = "";
            while ((str = br.readLine()) != null) {
                if (str.indexOf("[ERROR]") > -1) {
                    str = "\033[31m" + str + "\033[0m";
                }
                result.append(str + "\n\r");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result.indexOf("BUILD SUCCESS") > -1) {
            System.out.println("\033[32m上传本地仓库成功！\033[0m");
            System.out.println("<dependency>\n" +
                    "    <groupId>"+ groupId +"</groupId>\n" +
                    "    <artifactId>" + artifactId +"</artifactId>\n" +
                    "    <version>" + version + "</version>\n" +
                    "</dependency>");
        } else if (result.indexOf("BUILD FAILURE") > -1) {
            System.out.println("\033[31m构建失败，详细信息如下：\033[0m\n" + result.substring(result.indexOf("[ERROR]", result.indexOf("[INFO] BUILD")) - 7));
        } else {
            System.err.println("未知错误");
        }
        System.out.println(Constant.SEPARATOR);
    }
}
