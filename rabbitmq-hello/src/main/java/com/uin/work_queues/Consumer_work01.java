package com.uin.work_queues;


import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.uin.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: TODO
 * @date 2022/1/24/12:40 AM
 */
public class Consumer_work01 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //接受消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接受到的消息:" + new String(message.getBody()));
        };
        //取消消息的回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消息被取消消费者接口的回调逻辑！");
        };
        /**
         * 消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true代表自动应答 false代表手动应答
         * 3.未成功消费的一个回调
         * 4.消费者取消消费的回调
         */
        System.out.println("第一个工作线程！等待接受消息。。。。");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
