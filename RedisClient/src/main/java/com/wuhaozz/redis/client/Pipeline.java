package com.wuhaozz.redis.client;

import java.io.InputStream;
import java.io.OutputStream;

public class Pipeline {
    InputStream reader;
    OutputStream writer;

    public Pipeline(InputStream reader, OutputStream writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public void set(String key, String value) throws Exception {
        StringBuffer command = new StringBuffer();
        command.append("*3").append("\r\n");
        command.append("$3").append("\r\n");
        command.append("SET").append("\r\n");
        command.append("$").append(key.getBytes().length).append("\r\n");
        command.append(key).append("\r\n");
        command.append("$").append(value.getBytes().length).append("\r\n");
        command.append(value).append("\r\n");

        writer.write(command.toString().getBytes());
    }

    public String response() throws Exception {
        byte[] response = new byte[1024 * 100];
        reader.read(response);
        return new String(response);
    }

}
