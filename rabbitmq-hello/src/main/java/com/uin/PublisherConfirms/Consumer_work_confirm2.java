package com.uin.PublisherConfirms;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.MessageProperties;
import com.uin.utils.RabbitMQUtils;
import com.uin.utils.SleepUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: TODO
 * @date 2022/1/30/11:55 PM
 */
public class Consumer_work_confirm2 {

    private static final String TASK_QUEUE_CONFIRM = "hello";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("c2等待接受消息处理：");

        SleepUtils.sleep(30);

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String s = new String(message.getBody());
            System.out.println("接收到消息" + s);
            //配置手动确认消息
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        int prefetchCount = 5;//预设值
        channel.basicQos(5);//配置不公平的分发
        boolean autoAck = true; //手动确认

        channel.basicConsume(TASK_QUEUE_CONFIRM, autoAck, deliverCallback, (consumerTag) -> {
                    System.out.println("消费者取消消费接口的回调");
                });
    }
}
