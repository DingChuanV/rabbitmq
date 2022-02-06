package com.uin.work_queues.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.uin.utils.RabbitMQUtils;
import com.uin.utils.SleepUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: TODO
 * @date 2022/1/30/11:55 PM
 */
public class Consumer_work_confirm1 {

    private static final String TASK_QUEUE_CONFIRM = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("c1等待接受消息处理：");

        //处理消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String s = new String(message.getBody());
            try {
                SleepUtils.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("接收到消息:" + s);
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        int prefetchCount = 5;// 预设值
        channel.basicQos(prefetchCount); //不公平分发
        boolean autoAck = true;// 配置手动确认
        channel.basicConsume(TASK_QUEUE_CONFIRM, autoAck, deliverCallback, consumerTag -> {
            System.out.println("消费者取消消费接口回调");
        });
    }
}
