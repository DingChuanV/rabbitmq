package com.uin.Publish_Subscribe.reject_message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.uin.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: TODO
 * @date 2022/2/1/7:17 PM
 */
public class Consumer2 {
    //死信的队列
    public static final String DEAD_QUEUE = "dead_queue3";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        System.out.println("C2等待接受消息。。。");
        //消费消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String s = new String(message.getBody(), "UTF-8");
            System.out.println("C2接收到的消息" + s);
        };
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> {
            System.out.println("消息取消消费的接口回调");
        });
    }
}
