package com.uin.PriorityQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.uin.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: TODO
 * @date 2022/2/6/9:32 PM
 */
public class Consumer {

    public static final String QUEUE_NAME = "priority_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMQUtils.getChannel();
        Map<String, Object> arguments = new HashMap<>();
        //设置队列的最大优先级 最大可以设置到 255 官网推荐 1-10 如果设置太高比较吃内存和 CPU
        arguments.put("x-max-priority", 10);

        channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);
        System.out.println("消费者启动等待消费..............");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String receivedMessage = new String(delivery.getBody());
            System.out.println("接收到消息:" + receivedMessage);
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, (consumerTag) -> {
            System.out.println("消费者无法消费消息时调用，如队列被删除");
        });
    }
}
