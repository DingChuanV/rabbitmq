package com.uin.dead_Exchange.max_length;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.uin.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: TODO
 * @date 2022/2/1/7:17 PM
 */
public class Consumer1 {
    //普通的交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange3";
    //死信的交换机
    public static final String DEAD_EXCHANGE = "dead_exchange3";

    //普通的队列
    public static final String NORMAL_QUEUE = "normal_queue3";
    //死信的队列
    public static final String DEAD_QUEUE = "dead_queue3";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //普通队列要转发到死信交换机需要一些参数 arguments
        /**
         *  消息拒绝
         *  消息TTl过期
         *  队列达到最长的长度
         */
        Map<String, Object> arguments = new HashMap<>();
        //设置过期时间
        //arguments.put("x-dead-ttl",10000);
        //设置正常队列为死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //设置死信的routingkey
        arguments.put("x-dead-letter-routing-key", "lisi");
        //设置队列放消息的最大长度
        arguments.put("x-max-length", 6);

        //声明队列
        channel.queueDeclare(NORMAL_QUEUE, true, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, true, false, false, null);
        //普通的队列和普通的交换进行绑定
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        //死信的交换机和死信的队列 进行绑定
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("C1等待接受消息。。。");
        //消费消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String s = new String(message.getBody(), "UTF-8");
            System.out.println("Consumer01 接收到消息" + s);
        };
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> {
            System.out.println("消息取消消费的接口回调");
        });
    }
}
