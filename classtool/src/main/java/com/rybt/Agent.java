package com.rybt;

import com.rybt.transform.MyClassTransform;

import java.lang.instrument.Instrumentation;

/**
 * agent入口
 * @author Rybt
 */
public class Agent {

    /**
     * agent机制
     */
    public static void premain(String agentArg, Instrumentation inst) {

    }

    /**
     * attach机制
     */
    public static void agentmain(String agentArg, Instrumentation inst) {
        System.out.println("【INFO】jar包attach成功!");
        MyClassTransform transformer = new MyClassTransform(agentArg, inst);
        inst.addTransformer(transformer, true);
        transformer.retransform();
    }
}
