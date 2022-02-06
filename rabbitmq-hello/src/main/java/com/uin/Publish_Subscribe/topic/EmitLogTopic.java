package com.uin.Publish_Subscribe.topic;

import com.rabbitmq.client.Channel;
import com.uin.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: TODO
 * @date 2022/1/31/10:27 PM
 */
public class EmitLogTopic {
    //交换机的名称
    private static final String EXCHANGE_NAME = "logs_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入发送的消息");
        while (scanner.hasNext()) {
            String next = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "*.orange.rabbit", null,
                    next.getBytes(StandardCharsets.UTF_8));
        }
    }
}
