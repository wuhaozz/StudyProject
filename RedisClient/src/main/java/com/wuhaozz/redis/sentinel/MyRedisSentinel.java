package com.wuhaozz.redis.sentinel;

import redis.clients.jedis.Jedis;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MyRedisSentinel {
    // master
    static String master;

    // 所有slave
    static final Vector<String> slaveRedisServers = new Vector<>();

    // 坏掉的实例
    static final Vector<String> badRedisServers = new Vector<>();

    public static void main(String[] args) throws Exception {
        // 配置redis master
        config("127.0.0.1:6380");

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                // 检测master是否正常
                checkMaster();
                // 更新slave列表
                updateSlaves();
                // 检测坏掉的实例是否恢复
                checkBadServer();
            }

        }, 1000, 3000);

        // 开启端口接受请求
        open();

    }

    private static void config(String masterAddr) {
        master = masterAddr;
    }

    private static void checkMaster() {
        System.out.println("检查master状态：" + master);
        String masterHost = master.split(":")[0];
        int masterPort = Integer.parseInt(master.split(":")[1]);

        try {
            Jedis jedis = new Jedis(masterHost, masterPort);
            jedis.ping();
            jedis.close();
        } catch (Exception e) {
            //e.printStackTrace();
            // master挂掉了
            badRedisServers.add(master);
            // 切换master
            changeMaster();
        }
    }


    private static void changeMaster() {
        Iterator<String> iterator = slaveRedisServers.iterator();
        while (iterator.hasNext()) {
            String slave = iterator.next();
            try {
                String slaveHost = slave.split(":")[0];
                int slavePort = Integer.parseInt(slave.split(":")[1]);
                Jedis jedis = new Jedis(slaveHost, slavePort);
                jedis.slaveofNoOne();
                jedis.close();
                master = slave;
                System.out.println("产生新的master：" + master);
                break;
            } catch (Exception e) {
                //e.printStackTrace();
                badRedisServers.add(slave);
            } finally {
                iterator.remove();
            }
        }

        // 所有slave切换到新的master
        for (String slave : slaveRedisServers) {
            String slaveHost = slave.split(":")[0];
            int slavePort = Integer.parseInt(slave.split(":")[1]);
            Jedis jedis = new Jedis(slaveHost, slavePort);
            jedis.slaveof(master.split(":")[0], Integer.parseInt(master.split(":")[1]));
            jedis.close();
        }
    }

    private static void updateSlaves() {
        // 获取所有slave
        try {
            String masterHost = master.split(":")[0];
            int masterPort = Integer.parseInt(master.split(":")[1]);
            Jedis jedis = new Jedis(masterHost, masterPort);

            String info_replication = jedis.info("replication");
            // 解析 info_replication
            String[] lines = info_replication.split("\r\n");
            int slaveCount = Integer.parseInt(lines[2].split(":")[1]);
            if (slaveCount > 0) {
                slaveRedisServers.clear();
                for (int i = 0; i < slaveCount; i++) {
                    String port = lines[3 + i].split(",")[1].split("=")[1];
                    slaveRedisServers.add("127.0.0.1:" + port);
                }
            }
            System.out.println("更新slave列表：" + Arrays.toString(slaveRedisServers.toArray()));
            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("更新slave失败：" + e.getMessage());
        }
    }

    private static void checkBadServer() {
        // 获取所有slave
        Iterator<String> iterator = badRedisServers.iterator();
        while (iterator.hasNext()) {
            String badServerAddr = iterator.next();
            try {
                String badServerHost = badServerAddr.split(":")[0];
                int badServerPort = Integer.parseInt(badServerAddr.split(":")[1]);
                Jedis badServer = new Jedis(badServerHost, badServerPort);

                // ping坏掉的实例
                badServer.ping();

                //如果ping通没有问题，则挂在当前的master下
                badServer.slaveof(master.split(":")[0], Integer.parseInt(master.split(":")[1]));
                badServer.close();

                slaveRedisServers.add(badServerAddr);

                iterator.remove();
                System.out.println(badServerAddr + " 恢复正常，当前master：" + master);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }


    private static void open() throws Exception {
        // SENTINEL get-master-addr-by-name
        // tcp port
        ServerSocket sentinel = new ServerSocket(26380);
        Socket socket;

        while ((socket = sentinel.accept()) != null) {
            try {

                while (true) {
                    System.out.println("一个链接......");
                    InputStream inputStream = socket.getInputStream();
                    byte[] request = new byte[1024];
                    inputStream.read(request);
                    //解析 get-master-addr-by-name 请求
                    String req = new String(request);
                    System.out.println("收到请求：");
                    System.out.println(req);
                    System.out.println();


                    String[] params = req.split("\r\n");

                    if ("get-master-addr-by-name".equals(params[4])) {
                        //返回结果
                        String result = "*2\r\n" +
                                "$9\r\n" +
                                master.split(":")[0] + "\r\n" +
                                "$4\r\n" +
                                master.split(":")[1] + "\r\n";
                        socket.getOutputStream().write(result.getBytes());
                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
