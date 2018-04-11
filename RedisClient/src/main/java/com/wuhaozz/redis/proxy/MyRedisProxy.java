package com.wuhaozz.redis.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyRedisProxy {

    static List<String> servers = new ArrayList<>();

    static {
        servers.add("127.0.0.1:6380");
        servers.add("127.0.0.1:6381");
        servers.add("127.0.0.1:6382");
    }

    /**
     * 用一个简单的代理实现负载均衡
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 监听端口
        ServerSocket serverSocket = new ServerSocket(19000);
        Socket socket;

        while ((socket = serverSocket.accept()) != null) {
            try {

                while (true) {
                    System.out.println("一个链接......");
                    InputStream inputStream = socket.getInputStream();

                    // 获取报文
                    byte[] request = new byte[1024];
                    inputStream.read(request);
                    String req = new String(request);
                    System.out.println("收到请求：");
                    System.out.println(req);

                    // 解析key的长度
                    String[] params = req.split("\r\n");
                    int keyLength = Integer.valueOf(params[3].split("\\$")[1]);

                    // 根据key的长度取模
                    int mod = keyLength % servers.size();

                    // 根据取模结果获取地址
                    System.out.println("根据算法选择服务器：" + servers.get(mod));
                    String[] serverInfo = servers.get(mod).split(":");

                    // 处理请求
                    Socket client = new Socket(serverInfo[0], Integer.valueOf(serverInfo[1]));
                    client.getOutputStream().write(request);

                    //返回结果
                    byte[] response = new byte[1024];
                    client.getInputStream().read(response);
                    client.close();
                    socket.getOutputStream().write(response);

                    System.out.println("##########打印结束");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
