package org.example.jmeter;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;

/**
 * 用于在IDE中调试测试程序
 */
public class HelloTestRunner {

    public static void main(String[] args) {

        try {
            HelloTest test = new HelloTest();
            Arguments arguments = test.getDefaultParameters();
            arguments.clear();
            arguments.addArgument("initialId", "10002000");
            arguments.addArgument("url", "https://www.baidu.com");

            JavaSamplerContext context = new JavaSamplerContext(arguments);
            test.setupTest(context);
            test.runTest(context);
            test.teardownTest(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
