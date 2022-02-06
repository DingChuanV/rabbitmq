package com.uin.Publish_Subscribe.fanout;

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
public class ReceiveLogs01 {
    //交换机的名称
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        //声明一个交换机(交换机的名称，类型)
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //声明临时队列
        String queue = channel.queueDeclare().getQueue();
        //将临时队列绑定到我们的交换机中
        channel.queueBind(queue, EXCHANGE_NAME, "");
        System.out.println("等待接受消息，把接受到的消息打印在屏幕上。。");

        //处理消息的接口回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //去拿消息体
            String s = new String(message.getBody());
            System.out.println("R1接受到的消息：" + s);
        };
        //消费消息
        boolean autoAck = true;//手动应答
        channel.basicConsume(queue, autoAck, deliverCallback, consumerTag -> {
            System.out.println("取消处理消息的接口回调");
        });

    }
}
