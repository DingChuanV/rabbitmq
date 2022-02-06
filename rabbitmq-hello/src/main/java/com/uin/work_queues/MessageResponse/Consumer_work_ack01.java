package com.uin.work_queues.MessageResponse;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.uin.utils.RabbitMQUtils;
import com.uin.utils.SleepUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: TODO
 * @date 2022/1/30/2:15 PM
 */
public class Consumer_work_ack01 {

    //队列的名字
    private static final String TASK_QUEUE_NAME = "ack_queue1";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("C1等待接受消息处理较短:");
        //消费消息的时候如何处理消息
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            try {
                SleepUtils.sleep(1);
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
        //配置为不公平的分发 0默认为轮训(公平的分发) 1不公的分发(不公平的分发)
        int prefetchCount = 2; //欲取值
        channel.basicQos(prefetchCount);
        //配置手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
            System.out.println("消费者取消消费接口回调");
        });
    }
}
