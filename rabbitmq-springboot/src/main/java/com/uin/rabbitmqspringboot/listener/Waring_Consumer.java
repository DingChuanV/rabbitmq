package com.uin.rabbitmqspringboot.listener;

import com.rabbitmq.client.Channel;
import com.uin.rabbitmqspringboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author wanglufei
 * @description: 负责报警的消费者
 * @date 2022/2/6/7:38 PM
 */
@Component
@Slf4j
public class Waring_Consumer {

    @RabbitListener(queues = ConfirmConfig.WARN_QUEUE_NAME)
    public void waring(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.error("报警!发现不可路由的消息：{}", msg);
    }
}
