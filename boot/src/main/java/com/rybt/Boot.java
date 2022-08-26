package com.rybt;


import com.rybt.constant.Constant;
import com.rybt.tools.ClassTool;
import com.rybt.tools.MavenTool;

import java.util.Scanner;

/**
 * 工具入口
 * @author RyBT
 */
public class Boot {

    private static ClassTool classTool ;

    private static MavenTool mavenTool ;

    static {
        classTool = new ClassTool();
        mavenTool = new MavenTool();
    }

    public static void main( String[] args ) {
        System.out.println(Constant.SEPARATOR);
        System.out.println("【INFO】请选择使用的工具编号:");
        System.out.println("【1】class tool");
        System.out.println("【2】maven tool");
        System.out.println("【0】退出");
        Scanner scanner = new Scanner(System.in);
        int tool;
        while((tool = scanner.nextInt()) > 0) {
            switch(tool){
                case 1:
                    classTool();
                    break;
                case 2:
                    mavenTool();
                    break;
                default:
                    System.out.println("【ERROR】未知编号!");
            }
            if (tool == 0) {
                break;
            }
        }

        System.out.println(Constant.SEPARATOR);
    }

    private static void mavenTool(){

    }

    private static void classTool(){

    }
}
