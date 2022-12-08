package com.rybt.bean;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class JavaSourceFromString extends SimpleJavaFileObject {
    protected final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private String code;

    public JavaSourceFromString(String name, JavaFileObject.Kind kind) {
        // windows下File.separator为"\"，处理时会replace被当成转义字符，需要通过Mather.quoteReplacement()方法获取
        super(URI.create("string:///" + name.replaceAll("\\.", "/") + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
    }

    public JavaSourceFromString(String name, String content) {
        // windows下File.separator为"\"，处理时会replace被当成转义字符，需要通过Mather.quoteReplacement()方法获取
        super(URI.create("string:///" + name.replaceAll("\\.", "/") + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
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
