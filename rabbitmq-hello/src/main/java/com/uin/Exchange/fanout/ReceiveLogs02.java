package com.uin.Exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.uin.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: TODO
 * @date 2022/1/31/7:49 PM
 */
public class ReceiveLogs02 {

    //交换机的名称
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        //拿到channel
        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //声明临时队列
        String queue = channel.queueDeclare().getQueue();
        //将临时队列绑定到我们的交换机中
        channel.queueBind(queue, EXCHANGE_NAME, "");
        System.out.println("等待接受消息，把接受到的消息打印在屏幕上。。");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String s = new String(message.getBody());
            System.out.println("R2消费的消息：" + s);
        };

        //消费消息
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {
            System.out.println("消费者取消消息的接口回调");
        });
    }
}
