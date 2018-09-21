package org.example.jmeter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 测试类，每个线程将会创建一个测试类的实例。
 */
public class HelloTest implements JavaSamplerClient {

    private static final Logger logger = LoggerFactory.getLogger(HelloTest.class);

    private static final AtomicLong threadCounter = new AtomicLong(0);

    private static final AtomicLong idIncrementer = new AtomicLong(0);

    private static OkHttpClient httpClient = creteHttpClient();

    private String url;

    private static OkHttpClient creteHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        builder.followRedirects(true);
        return builder.build();
    }

    public Arguments getDefaultParameters() {
        // 这些添加的参数将在界面中显示
        Arguments args = new Arguments();
        args.addArgument("initialId", "1001");
        args.addArgument("url", "https://www.baidu.com");
        return args;
    }

    /**
     * 线程初始化，每个线程会执行一次，仅执行一次。
     */
    public void setupTest(JavaSamplerContext context) {
        logger.info("setupTest");
        threadCounter.incrementAndGet();

        // 多个线程共享的静态变量，只设置一次
        idIncrementer.compareAndSet(0, context.getLongParameter("initialId"));

        url = context.getParameter("url");
    }

    /**
     * 线程循环执行的方法
     * @param context
     * @return
     */
    public SampleResult runTest(JavaSamplerContext context) {
        logger.info("runTest start");

        SampleResult result = new SampleResult();
        result.sampleStart();

        long id = idIncrementer.getAndIncrement();

        try {
            // 可以在这里写任何形式的测试代码：RPC/HTTP/TCP...
            // 这里以一个简单HTTP请求为为例
            HttpUrl httpUrl = HttpUrl.parse(url)
                    .newBuilder()
                    .addQueryParameter("q", String.valueOf(id))
                    .build();

            Request request = new Request.Builder()
                    .url(httpUrl)
                    .build();

            Response response = httpClient.newCall(request).execute();

            result.sampleEnd();
            if (response.isSuccessful()) {
                logger.info(response.headers().toString());
                response.body().string();

                result.setResponseCodeOK();
                result.setResponseOK();
                result.setSuccessful(true);
            } else {
                result.setResponseCode("" + response.code());
                result.setResponseMessage(response.message());
                result.setSuccessful(false);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            result.sampleEnd();
            result.setResponseCode("400");
            result.setResponseMessage(e.getMessage());
            result.setSuccessful(false);
        }

        logger.info("runTest end");

        return result;
    }

    /**
     * 测试结束时执行的方法，可做一些清理处理（如果需要的话）
     */
    public void teardownTest(JavaSamplerContext context) {
        logger.info("teardownTest");
        
        if (threadCounter.decrementAndGet() <= 0) {
            // 最后一个线程执行完毕，测试结束，可以做一些测试结束后的清理
        }
    }

}
