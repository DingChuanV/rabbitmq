package com.uin.helloWord;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Hello-Word模式：消息生产者
 */
public class Producer {
    // 队列
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        // 1.引入连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        try {
            // 2.获取连接
            Connection connection = factory.newConnection();
            // 3.在连接中获取信道
            Channel channel = connection.createChannel();
            /**
             ** 生成一个队列
             *  1.队列名称
             *  2.队列里面的消息是否持久化 默认消息存储在内存中
             *  3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
             *  4.是否自动删除 最后一个消费者断开连接以后 该队列是否自动删除 true 自动删除
             *  5.其他参数
             */
            // Declare 声明一个队列
            // queue队列的名字  durable是否持久化 exclusive是否排他  autoDelete是否自动删除  argument给队列传递的参数
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello word BearBrick0";
            /**
             * 发送一个消息
             *  1.发送到那个交换机
             *  2.路由的 key 是哪个
             *  3.其他的参数信息
             *  4.发送消息的消息体
             */
            //exchange指定哪个交换机  props
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送完毕！");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
