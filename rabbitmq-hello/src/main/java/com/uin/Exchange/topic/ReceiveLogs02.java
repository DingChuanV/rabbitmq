package com.uin.Exchange.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.uin.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: TODO
 * @date 2022/1/31/10:31 PM
 */
public class ReceiveLogs02 {
    //交换机的名称
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明队列
        channel.queueDeclare("Q2", true, false, false, null);
        //将队列与交换机绑定
        channel.queueBind("Q2", EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind("Q2", EXCHANGE_NAME, "lazy.#");
        System.out.println("Q2等待接受消息");
        //发送消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String s = new String(message.getBody());
            System.out.println("Q2消费的消息" + s + "======" + message.getEnvelope().getRoutingKey());
        };
        channel.basicConsume("Q2", true, deliverCallback, consumerTag -> {
            System.out.println("取消消费消息接口回调");
        });

    }
}
