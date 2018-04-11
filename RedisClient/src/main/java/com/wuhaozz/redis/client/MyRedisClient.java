package com.wuhaozz.redis.client;

import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MyRedisClient {

    Socket socket;
    InputStream reader;
    OutputStream writer;

    public MyRedisClient() throws Exception {
        socket = new Socket("127.0.0.1", 6381);
        reader = socket.getInputStream();
        writer = socket.getOutputStream();
    }

    public String set(String key, String value) throws Exception {
        StringBuffer command = new StringBuffer();
        command.append("*3").append("\r\n");
        command.append("$3").append("\r\n");
        command.append("SET").append("\r\n");
        command.append("$").append(key.getBytes().length).append("\r\n");
        command.append(key).append("\r\n");
        command.append("$").append(value.getBytes().length).append("\r\n");
        command.append(value).append("\r\n");

        writer.write(command.toString().getBytes());

        byte[] response = new byte[1024];
        reader.read(response);

        return new String(response);
    }

    public String get(String key) throws Exception {
        StringBuffer command = new StringBuffer();
        command.append("*2").append("\r\n");
        command.append("$3").append("\r\n");
        command.append("GET").append("\r\n");
        command.append("$").append(key.getBytes().length).append("\r\n");
        command.append(key).append("\r\n");

        writer.write(command.toString().getBytes());

        byte[] response = new byte[1024];
        reader.read(response);

        return new String(response);
    }


    public Pipeline pipeline() {
        return new Pipeline(reader, writer);
    }

    public Subscribe subscribe() {
        return new Subscribe(reader, writer);
    }


}
