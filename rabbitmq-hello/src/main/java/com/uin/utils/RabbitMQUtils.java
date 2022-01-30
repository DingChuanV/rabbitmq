package com.uin.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: 工具类
 * @date 2022/1/24/12:29 AM
 */
public class RabbitMQUtils {
    public static Channel getChannel() throws IOException, TimeoutException {
        //引入连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        //获取连接
        Connection connection = factory.newConnection();
        //创建新的信道
        Channel channel = connection.createChannel();
        //返回一个新的信道
        return channel;
    }
}
