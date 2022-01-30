package com.uin.hello;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: TODO
 * @date 2022/1/23/11:32 PM
 */
public class Consumer {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //引入连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //获取连接
        Connection connection = connectionFactory.newConnection();

        //获取连接中的信道
        Channel channel = connection.createChannel();
        //声明 消息回调的函数式接口 接受消息
        //lambdas 函数式 代替了 匿名内部类
        //https://cloud.tencent.com/developer/article/1787258
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        //声明 取消消息的回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费消息被中断");
        };
        /**
         * 消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true代表自动应答 false代表手动应答
         * 3.未成功消费的一个回调
         * 4.消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
