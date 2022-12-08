package com.rybt;

import sun.misc.BASE64Encoder;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 使用JavaCompiler编译字符串，步骤如下:
 * 1. 获取系统编译器
 * 2. 获得DiagnosticListener的实现类DiagnosticCollector。它可以控制标准输出/输入流，并且可以监控编译过程，获得诊断信息
 * 3. 获得自定义文件管理器
 * 4. 将字符串封装到JavaFileObject对象中
 * 5. 生成编译任务
 * 6. 编译源码
 * 7. 根据结果进行对应得操作
 * 8. 关闭StandardFileManager
 */
public class ClassCompiler {

    public void compiler(){
		System.out.println("编译字符串");
        // 1. 获取系统编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // 2. 获得诊断器
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        // 3. 获得自定义类文件管理器
        ClassFileManager classFileManager = new ClassFileManager(
                compiler.getStandardFileManager(null, null, null));
        // 4. 将字符串封装到JavaFileObject对象中
        String javaCode = "import javax.servlet.ServletException;\n" +
                        "import javax.servlet.http.HttpServlet;\n" +
                        "import javax.servlet.http.HttpServletRequest;\n" +
                        "import javax.servlet.http.HttpServletResponse;\n" +
                        "import java.io.IOException;\n" +
                        "public class Main6 extends HttpServlet {\n" +
                        "    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {\n" +
                        "        System.out.println(\"内存马\");\n" +
                        "    }\n" +
                        "}";
        String fileName = "Main6";
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
        Properties properties = System.getProperties();
        JavaCompiler.CompilationTask task = compiler.getTask(null, classFileManager, diagnosticCollector, optionList, null, javaSourceFromStrings);
        // 6. 使用编译任务编译字符串
        Boolean call = task.call();
        // 7. 根据编译结果执行对应得操作
        if (call) {
            try {
                JavaSourceFromString javaClassObject = classFileManager.getJavaClassObject();
                byte[] bytes = javaClassObject.getBytes();
                //当编码的字节较长时，base64加密字符串，rfc规范规定每76个字符换一次行
                String encode = (new BASE64Encoder()).encodeBuffer(bytes).replaceAll("\r\n","");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            //如果编译失败，使用诊断器分析错误原因
            StringBuilder error = new StringBuilder("编译失败:");
            for (Diagnostic diagnostic : diagnosticCollector.getDiagnostics()) {
                error.append(formatMessage(diagnostic));
            }
            System.out.println(error.toString());
        }
        // 8. 关闭文件管理器
        try {
            classFileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("我执行了");
	}
	
    // 格式化诊断器中的数据
    private static String formatMessage(Diagnostic diagnostic) {
        StringBuffer res = new StringBuffer();
        //res.append("Type:" + diagnostic.getKind() + "\n");
        res.append("LineNumber:" + diagnostic.getLineNumber() + ", ");
        res.append("ColumnNumber:" + diagnostic.getColumnNumber() + ", ");
        res.append("Error:" + diagnostic.getMessage(null) + "; \t\r\n");
        return res.toString();
    }
}


/**
 * 是JavaFileObject的子类。JavaFileObject是java和class文件的抽象类
 */
class JavaSourceFromString extends SimpleJavaFileObject{

    protected final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private String code;

    JavaSourceFromString(String name, Kind kind) {
        // windows下File.separator为"\"，处理时会replace被当成转义字符，需要通过Mather.quoteReplacement()方法获取
        super(URI.create("string:///" + name.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
    }

    JavaSourceFromString(String name, String content) {
        // windows下File.separator为"\"，处理时会replace被当成转义字符，需要通过Mather.quoteReplacement()方法获取
        super(URI.create("string:///" + name.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = content;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        if (this.code == null)
            throw new IllegalArgumentException("code == null");
        return code;
    }

    public byte[] getBytes() {
        return bos.toByteArray();
    }

    public OutputStream openOutputStream() throws IOException {
        return bos;
    }
}

/**
 * 自定义类文件管理器，继承ForwardingJavaFileManager类，将程序的调用转发到自定义的管理器中
 */
class ClassFileManager extends ForwardingJavaFileManager{

    private JavaSourceFromString javaSourceFromString;
    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     */
    ClassFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    //重写getJavaFileForOutput方法，使获得的java文件是被封装在自定义的类中
    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        JavaSourceFromString javaSourceFromString = new JavaSourceFromString(className, kind);
        this.javaSourceFromString = javaSourceFromString;
        return javaSourceFromString;
    }

    //获得自定义格式的java文件
    public JavaSourceFromString getJavaClassObject() {
        return javaSourceFromString;
    }
}

// 自定义类加载器，根据字节码流加载类
class MyClassLoader extends ClassLoader{
    public Class getTheClass(String name ,byte[] b){
        return defineClass(name ,b ,0,b.length,null);
    }
}
