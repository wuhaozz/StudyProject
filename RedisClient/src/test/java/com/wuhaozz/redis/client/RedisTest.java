package com.wuhaozz.redis.client;


import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class RedisTest {

    @Test
    public void jedisTest() {
        Jedis jedis = new Jedis("127.0.0.1", 6380);

        jedis.set("hello", "wuhao");

//        *3
//        $3
//        SET
//        $5
//        hello
//        $5
//        wuhao

//        String replication = jedis.info("replication");
//        System.out.println(replication);

        jedis.close();
    }

    @Test
    public void SocketTest() throws IOException {
        Socket socket = new Socket("127.0.0.1", 6382);
        socket.getOutputStream().write("".getBytes());

        byte[] response = new byte[1024];
        socket.getInputStream().read(response);
    }


}

