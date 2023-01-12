package com.uin.work_queues;

import com.rabbitmq.client.Channel;
import com.uin.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 工作队列模式 ： 多个任务（生产者发送多条消息模拟多条任务）
 */
public class Producer_task01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /**
             * 发送一个消息
             *  1.发送到那个交换机
             *  2.路由的 key 是哪个
             *  3.其他的参数信息
             *  4.发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());//以二进制传输
            System.out.println("发送消息完成：" + message);
        }
    }
}
