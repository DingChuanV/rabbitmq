package com.uin.rabbitmqspringboot.controller;

import com.uin.rabbitmqspringboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanglufei
 * @description: 确认的控制层
 * @date 2022/2/6/9:50 AM
 */
@RestController
@RequestMapping("/confirm")
@Slf4j
public class Producer_ConfirmController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/sendMessage/{message}", method = RequestMethod.GET)
    public void sendMessage(@PathVariable("message") String message) {

        CorrelationData correlationData = new CorrelationData("1");

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY, message + "key1", correlationData);
        log.info("发送消息内容为：{}", message + "key1");

        //测试信道出问题 路由key指定错误
        CorrelationData correlationData1 = new CorrelationData("2");

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY + "2", message + "key2", correlationData1);
        log.info("发送消息内容为：{}", message + "key2");
    }

}
