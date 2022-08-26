package com.rybt.tools;

import com.rybt.constant.Constant;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ClassTool {

    public void dumpClass(){
        Scanner scanner = new Scanner(System.in);
        StringBuilder command = new StringBuilder();
        if (Constant.OS_NAME.indexOf("windows") > -1) {
            command.append("cmd.exe /c jps");
        } else if (Constant.OS_NAME.indexOf("linux") > -1){
            command.append("/bin/bash -c jps");
        } else {
            System.out.println("【ERROR】不兼容的操作系统版本!");
            return;
        }

        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        try {
            Process exec = Runtime.getRuntime().exec(command.toString());
            br = new BufferedReader(new InputStreamReader(
                    exec.getInputStream()), 5120);
            String str = "";
            while ((str = br.readLine()) != null) {
                result.append("  " + str + "\n\r");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(Constant.SEPARATOR);
        System.out.println("【INFO】java进程列表：");
        System.out.println(result);
        System.out.print("【INFO】请选择进程号：");
        String s = scanner.nextLine();
        System.out.println("【INFO】请选择进程号：请输入dump的class：");
        String clazzReg = scanner.nextLine();
        try {
            VirtualMachine attach = VirtualMachine.attach(s);
            attach.loadAgent(Utils.getJarPath() + "/class-tool.jar", clazzReg);
        } catch (AttachNotSupportedException e) {
            System.out.println("【ERROR】attach出现异常!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AgentLoadException e) {
            System.out.println("【ERROR】load agent失败!");
            e.printStackTrace();
        } catch (AgentInitializationException e) {
            System.out.println("【ERROR】agent初始化失败!");
            e.printStackTrace();
        }

        System.out.println(Constant.SEPARATOR);
    }
}
