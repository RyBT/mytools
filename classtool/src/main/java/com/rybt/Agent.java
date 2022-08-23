package com.rybt;

import com.rybt.transform.MyClassTransform;

import java.lang.instrument.Instrumentation;

/**
 * Hello world!
 *
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
        MyClassTransform transformer = new MyClassTransform(agentArg, inst);
        inst.addTransformer(transformer, true);
        transformer.retransform();
    }

    public static void main(String[] args) {
    }
}
