package com.wuhaozz.redis.client;

import java.io.InputStream;
import java.io.OutputStream;

public class Subscribe {
    InputStream reader;
    OutputStream writer;

    public Subscribe(InputStream reader, OutputStream writer) {
        this.reader = reader;
        this.writer = writer;
    }

    /**
     * 订阅
     * @param topic
     * @throws Exception
     */
    public void sub(String topic) throws Exception {
        StringBuffer command = new StringBuffer();
        command.append("*2").append("\r\n");
        command.append("$9").append("\r\n");
        command.append("SUBSCRIBE").append("\r\n");
        command.append("$").append(topic.getBytes().length).append("\r\n");
        command.append(topic).append("\r\n");

        writer.write(command.toString().getBytes());

        while (true) {
            byte[] response = new byte[1024];
            reader.read(response);
            System.out.println("最新动态：");
            System.out.println(new String(response));
        }

    }

}
