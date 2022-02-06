package com.uin.rabbitmqspringboot.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author wanglufei
 * @description: 生产者发送给交换机的消息的确认接口回调
 * @date 2022/2/6/10:13 AM
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    //将自己写的接口实现注入到RabbitTemplate.ConfirmCallback
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //注入
    @PostConstruct
    public void invoke() {
        rabbitTemplate.setConfirmCallback(this::confirm);
        rabbitTemplate.setReturnsCallback(this::returnedMessage);
    }

    /**
     * 生产者给交换机发送消息 确认是否发送成功的接口回调
     * 交换机不管是否接受到消息 都要给生产者一个回应
     * correlationData  消息的相关数据
     * ack 交换机是否接受到消息
     * cause 交换机失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机接受到的消息，id:{}", id);
        } else {
            log.info("交换机没有接受到id:{}消息！原因为：{}", id, cause);
        }
    }

    /**
     * 生产者发消息 如果消息没有被对应的交换机进队列
     * 就会把消息回退给生产者 进行重发
     * 在发送消息的过程中不可达目的地时将消息返回给生产者
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("消息{}，被交换机{}退回，退回的原因是{},路由key是{}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getExchange(), returnedMessage.getReplyText(), returnedMessage.getRoutingKey());
    }


}
