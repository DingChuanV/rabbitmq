package com.uin.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工具类
 */
public class RabbitMQUtils {
    public static Channel getChannel() throws IOException, TimeoutException {
        // 1.引入连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        // 2.获取连接
        Connection connection = factory.newConnection();
        // 3.创建新的信道
        Channel channel = connection.createChannel();
        // 4.返回一个新的信道
        return channel;
    }
}
