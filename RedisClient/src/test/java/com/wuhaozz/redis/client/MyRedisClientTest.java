package com.wuhaozz.redis.client;

import org.junit.Test;

public class MyRedisClientTest {

    @Test
    public void myRedisClientTest() throws Exception {
        MyRedisClient myRedisClient = new MyRedisClient();

        myRedisClient.set("hello", "service");

        String value = myRedisClient.get("hello");

        System.out.println(value);

        myRedisClient.writer.close();
        myRedisClient.reader.close();
        myRedisClient.socket.close();
    }

    @Test
    public void batchTest() throws Exception {
        MyRedisClient myRedisClient = new MyRedisClient();

        long now = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            myRedisClient.set("counter_batch", i + "");
        }
        String value = myRedisClient.get("counter_batch");
        System.out.println("##########开始打印普通结果");
        System.out.println(value);
        System.out.println("未使用管道执行时间：" + (System.currentTimeMillis() - now));
        System.out.println("##########打印结束");
    }

    @Test
    public void batchPipelineTest() throws Exception {
        MyRedisClient myRedisClient = new MyRedisClient();

        long now = System.currentTimeMillis();

        Pipeline pipeline = myRedisClient.pipeline();

        for (int i = 0; i < 10000; i++) {
            pipeline.set("counter_pipeline_batch", i + "");
        }

        pipeline.response();
        String value = myRedisClient.get("counter_pipeline_batch");
        System.out.println("##########开始打印pipeline结果");
        System.out.println(value);
        System.out.println("使用管道执行时间：" + (System.currentTimeMillis() - now));
        System.out.println("##########打印结束");
    }

    @Test
    public void subscribeTest() throws Exception {
        MyRedisClient myRedisClient = new MyRedisClient();

        myRedisClient.subscribe().sub("wuhaozz");
    }
}
