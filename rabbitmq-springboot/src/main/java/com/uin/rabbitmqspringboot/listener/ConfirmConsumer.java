package com.uin.rabbitmqspringboot.listener;

import com.rabbitmq.client.Channel;
import com.uin.rabbitmqspringboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;



/**
 * @author wanglufei
 * @description: 接受消息
 * @date 2022/2/6/9:57 AM
 */
@Component
@Slf4j
public class ConfirmConsumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void received(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("确认的消息：{}", msg);
    }

}
