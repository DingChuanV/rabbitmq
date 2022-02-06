package com.uin.dead_Exchange.TTL;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.uin.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: TODO
 * @date 2022/2/1/7:17 PM
 */
public class Producer {

    private static final String NORMAL_EXCHANGE = "normal_exchange2";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);

        //设置消息的过期时间 TTL 参数
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration(
                "10000").build();

        //模拟发送消息
        for (int i = 1; i < 11; i++) {
            String message = "info_message " + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", properties,
                    message.getBytes("UTF-8"));
            System.out.println("消费者发送消息" + message);
        }
    }
}
