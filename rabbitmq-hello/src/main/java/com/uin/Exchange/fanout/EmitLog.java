package com.uin.Exchange.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.uin.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: TODO
 * @date 2022/1/31/7:50 PM
 */
public class EmitLog {
    //交换机的名称
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        Scanner scanner = new Scanner(System.in);
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        System.out.println("请输入消息：");
        while (scanner.hasNext()) {
            String next = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", null, next.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送的消息" + next);
        }

    }
}
