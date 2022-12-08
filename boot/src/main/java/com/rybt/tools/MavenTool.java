package com.rybt.tools;

import com.rybt.constant.Constant;
import com.rybt.exception.ExitException;
import com.rybt.utils.PrintUtil;
import com.rybt.utils.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MavenTool {

    public void deployJar(String path,String groupId,String artifactId,String version,String packaging) throws ExitException {
        if (StringUtil.isEmpty(path) || StringUtil.isEmpty(groupId) || StringUtil.isEmpty(artifactId)
                || StringUtil.isEmpty(version) || StringUtil.isEmpty(packaging)) {
            PrintUtil.error(Constant.MESSAGE_PLACEHOLDER + " parameter is null.");
            throw new ExitException();
        }
        String shell = "";
        String option = "";
        String os = System.getProperty("os.name").toLowerCase();
        if(os.startsWith("windows")) {
            shell = "cmd.exe";
            option = "/c";
        } else if (os.startsWith("linux")) {
            shell = "/bin/sh";
            option = "-c";
        } else {
            PrintUtil.error(Constant.MESSAGE_PLACEHOLDER, " Unknown system.");
        }
        //存在系统对命令分隔符会截断错误
        String[] command = {shell,option,"mvn","install:install-file","-Dfile=" + path,"-DgroupId=" + groupId
                ,"-DartifactId=" + artifactId,"-Dversion=" + version,"-Dpackaging=" + packaging};
        Process exec = null;
        try {
            exec = Runtime.getRuntime().exec(command);
            InputStreamReader isr = new InputStreamReader(exec.getInputStream(), "utf-8");
            if (isr == null ){
                isr = new InputStreamReader(exec.getErrorStream(), "utf-8");
            }
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
