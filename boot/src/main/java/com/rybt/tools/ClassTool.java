package com.rybt.tools;

import com.rybt.bean.JavaSourceFromString;
import com.rybt.constant.Constant;
import com.rybt.exception.ExitException;
import com.rybt.manager.ClassFileManager;
import com.rybt.utils.PrintUtil;
import com.rybt.utils.StringUtil;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClassTool {

    public void decodeClass(String clazz,String path) {
        if (StringUtil.isEmpty(clazz) ) {
            PrintUtil.error(Constant.MESSAGE_PLACEHOLDER + " Input null!");
        }
        if (StringUtil.isEmpty(path)) {
            path = System.getProperty("user.dir");
        }
        File file = new File(path);
        if (file.isDirectory()) {
            path = path + File.separator + "deocde.class";
        }
        byte[] bytes ;
        try {
            bytes = new BASE64Decoder().decodeBuffer(clazz);
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintUtil.info(Constant.MESSAGE_PLACEHOLDER + " Decode success!Path is : " + path);
    }

    private static String formatMessage(Diagnostic diagnostic) {
        StringBuffer res = new StringBuffer();
        //res.append("Type:" + diagnostic.getKind() + "\n");
        res.append("LineNumber:" + diagnostic.getLineNumber() + ", ");
        res.append("ColumnNumber:" + diagnostic.getColumnNumber() + ", ");
        res.append("Error:" + diagnostic.getMessage(null) + "; \t\r\n");
        return res.toString();
    }

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

    public byte[] compiler(String javaCode, String fileName, String path) throws ExitException{
        // 1. 获取系统编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // 2. 获得诊断器
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        // 3. 获得自定义类文件管理器
        ClassFileManager classFileManager = new ClassFileManager(
                compiler.getStandardFileManager(null, null, null));
        // 4. 将字符串封装到JavaFileObject对象中
        if (StringUtil.isEmpty(fileName)) {
            PrintUtil.error(Constant.MESSAGE_PLACEHOLDER + " Check whether the class name is public class modifier and whether there are extra spaces.");
            throw new ExitException();
        }
        JavaSourceFromString javaSourceFromString = new JavaSourceFromString(fileName, javaCode);
        Iterable javaSourceFromStrings = Arrays.asList(javaSourceFromString);
        /**
         * 5. 生成编译任务。
         *  Task方法参数详解：
         *      Writer out:错误输出流，增加输出内容
         *      JavaFileManager:文件管理器，如果为null则使用compiler默认的管理器
         *      DiagnosticListener<? super JavaFileObject> diagnosticListener:诊断器，如果为空会使用compiler默认的诊断器
         *      Iterable<String> options:编译选项，可以设置一些jvm参数之类的
         *      Iterable<String> classes:参与编译的class
         *      Iterable<? extends JavaFileObject> compilationUnits:待编译的java，它是Java文件抽象成的类对象
         */
        List<String> optionList = new ArrayList<>();
        JavaCompiler.CompilationTask task = compiler.getTask(null, classFileManager, diagnosticCollector, optionList, null, javaSourceFromStrings);
        // 6. 使用编译任务编译字符串
        Boolean call = task.call();
        // 7. 根据编译结果执行对应得操作
        byte[] bytes = null;
        if (call) {
            try {
                JavaSourceFromString javaClassObject = classFileManager.getJavaClassObject();
                encodeClass(javaClassObject.getBytes(), path);
                bytes = javaClassObject.getBytes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            //如果编译失败，使用诊断器分析错误原因
            StringBuilder error = new StringBuilder("\n");
            for (Diagnostic diagnostic : diagnosticCollector.getDiagnostics()) {
                error.append(formatMessage(diagnostic));
            }
            PrintUtil.error(Constant.MESSAGE_PLACEHOLDER + error);
        }
        // 8. 关闭文件管理器
        try {
            classFileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public void encodeClass(byte[] bytes, String path) {
        //当编码的字节较长时，base64加密字符串，rfc规范规定每76个字符换一次行
        String encode = (new BASE64Encoder()).encodeBuffer(bytes).replaceAll("\r\n","");
        String project = System.getProperty("user.dir");
        if (StringUtil.isEmpty(path)) {
            path = project + File.separator + "encode.txt";
        } else {
            File file = new File(path);
            if (!file.exists()) {
                path = project + File.separator + "encode.txt";
            }
            if (file.isDirectory()) {
                path = path + File.separator + "encode.txt";
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            fileOutputStream.write(encode.getBytes("utf-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(encode);
        PrintUtil.info(Constant.MESSAGE_PLACEHOLDER + " encode successful!Path is : " + path);
    }
}
