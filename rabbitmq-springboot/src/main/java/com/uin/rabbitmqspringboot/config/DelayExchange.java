package com.uin.rabbitmqspringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wanglufei
 * @description: 插件--延迟队列交换机
 * @date 2022/2/5/10:31 PM
 */
@Configuration
public class DelayExchange {
    public static final String DELAY_QUEUE_NAME = "delayed.queue";
    public static final String DELAY_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAY_EXCHANGE_ROUTING_KEY = "delayed.routingkey";

    //自定义延迟交换机
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
    }

    //声明延迟队列
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAY_QUEUE_NAME);
    }

    //绑定队列和自定义的交换机
    @Bean
    public Binding delayedExchangeQueue(@Qualifier("delayedQueue") Queue queue,
                                   @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(queue).to(delayedExchange).with(DELAY_EXCHANGE_ROUTING_KEY).noargs();
    }


}
