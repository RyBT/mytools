package com.rybt;


import com.rybt.constant.Constant;
import com.sun.tools.attach.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import com.rybt.tools.ToolUtils;

/**
 * Hello world!
 *
 */
public class Boot {

    static{
        System.loadLibrary("attach");
    }

    public static void main( String[] args ) {
        StringBuilder command = new StringBuilder();
        if (Constant.OS_NAME.indexOf("windows") > -1) {
            command.append("cmd.exe /c jps");
        } else if (Constant.OS_NAME.indexOf("linux") > -1){
            command.append("/bin/bash -c jps");
        } else {
            System.out.println("【ERROR】不兼容的操作系统版本");
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
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        System.out.println(Constant.SEPARATOR);
        System.out.println("java进程列表：");
        System.out.println(result);
        Scanner scanner = new Scanner(System.in);
        System.out.print("请选择进程号：");
        String s = scanner.nextLine();
        try {
            VirtualMachine attach = VirtualMachine.attach(s);
            attach.loadAgent(ToolUtils.getJarPath() + "/class-tool.jar");
        } catch (AttachNotSupportedException e) {
            System.out.println("【ERROR】attach出现异常");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AgentLoadException e) {
            System.out.println("【ERROR】load agent失败");
            e.printStackTrace();
        } catch (AgentInitializationException e) {
            System.out.println("【ERROR】agent初始化失败");
            e.printStackTrace();
        }

        System.out.println(Constant.SEPARATOR);
    }



}
