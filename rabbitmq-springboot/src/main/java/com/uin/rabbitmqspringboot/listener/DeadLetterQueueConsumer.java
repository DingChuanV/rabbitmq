package com.uin.rabbitmqspringboot.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


import java.util.Date;

/**
 * @author wanglufei
 * @description: 队列TTl的消费值 --监听器
 * @date 2022/2/5/6:16 PM
 */
@Component
@Slf4j
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = "Qd")
    public void receiveD(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody());
        log.info("当前时间：{},接受到的死信消息：{}", new Date().toString(), msg);
    }
}
