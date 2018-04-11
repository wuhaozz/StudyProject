package com.wuhaozz.redis.client;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class MyRedisProxyTest {

    /**
     * 通过不同长度的key，测试数据是否均匀的分布在三个redis实例
     */
    @Test
    public void proxyTest() {
        Jedis jedis = null;

        String response = null;

        // 代理服务器
        jedis = new Jedis("127.0.0.1", 19000);
        response = jedis.set("a", "wuhao_a");
        jedis.close();
        System.out.println("返回结果：" + response);

        jedis = new Jedis("127.0.0.1", 19000);
        response = jedis.set("ab", "wuhao_ab");
        jedis.close();
        System.out.println("返回结果：" + response);

        jedis = new Jedis("127.0.0.1", 19000);
        response = jedis.set("abc", "wuhao_abc");
        jedis.close();
        System.out.println("返回结果：" + response);

        jedis = new Jedis("127.0.0.1", 19000);
        response = jedis.set("abcd", "wuhao_abcd");
        jedis.close();
        System.out.println("返回结果：" + response);


    }
}
