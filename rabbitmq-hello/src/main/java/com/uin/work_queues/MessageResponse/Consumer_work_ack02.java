package com.uin.work_queues.MessageResponse;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.uin.utils.RabbitMQUtils;
import com.uin.utils.SleepUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作队列： 消费者消息手动应答
 */
public class Consumer_work_ack02 {
    //队列的名字
    private static final String TASK_QUEUE_NAME = "ack_queue1";

    public static void main(String[] args) throws IOException, TimeoutException {


        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("C2等待接受消息处理较长:");
        //消费消息的时候如何处理消息
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            try {
                SleepUtils.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("接收到消息:" + message);
            /**
             * 1.消息标记 tag
             * 2.是否批量应答未应答消息
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        // 配置为不公平的分发 0默认为轮训(公平的分发) 1不公的分发(不公平的分发)
        // int prefetchCount = 1  // 配置为不公平的分发
        int prefetchCount = 5;//欲取值
        channel.basicQos(prefetchCount);
        // 配置手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
            System.out.println("消费者取消消费接口回调");
        });

    }
}
