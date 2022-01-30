package com.uin.MessageResponse;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.uin.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: 消息在手动应答是不会丢失的、重新放进队列中重新消费
 * @date 2022/1/30/2:05 PM
 */
public class Producer_ack {
    //队列的名字
    private static final String TASK_QUEUE_NAME = "ack_queue1";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //声明队列
        //声明队列的持久化
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);

        //从控制台输入消息
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入消息：");
        //循环
        while (scanner.hasNext()) {
            String s = scanner.next();

            //设置生产者消息为持久化--要求保存到磁盘上
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, s.getBytes(
                    "UTF-8"));
            System.out.println("生产者发出消息：" + s);
        }
    }
}
