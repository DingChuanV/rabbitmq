package com.uin.helloWord;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Hello-Word模式：消费者
 */
public class Consumer {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1.引入连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        // 2.获取连接
        Connection connection = connectionFactory.newConnection();

        // 3.获取连接中的信道
        Channel channel = connection.createChannel();
        System.out.println("等待接收消息.........");
        // 4.声明 消息回调的函数式接口 接受消息
        // 推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        // 声明 取消消息的回调
        // 取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费消息被中断");
        };
        /**
         * 消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false代表手动应答
         * 3.未成功消费的一个回调
         * 4.消费者取消消费的回调
         */
        // 5.最基本的消费
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
