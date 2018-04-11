package com.wuhaozz.redis.client;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

public class MyRedisSentinelTest {

    // 哨兵机制测试
    @Test
    public void sentinelTest() {
        // SENTINEL get-master-addr-by-name master
        Set<String> sentinels = new HashSet<>();

        //添加哨兵服务器
        sentinels.add("127.0.0.1:26380");

        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool("master", sentinels);
        Jedis jedis = jedisSentinelPool.getResource();

        jedis.set("hello_sentinel_1", "wuhao_sentinel_1");
    }

}
