package com.uin.rabbitmqspringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wanglufei
 * @description: 确认配置类
 * @date 2022/2/6/9:35 AM
 */
@Configuration
public class ConfirmConfig {


    //交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    //队列
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    public static final String WARN_QUEUE_NAME = "warning.queue";
    public static final String CONFIRM_ROUTING_KEY = "key1";

    //声明交换机
    @Bean
    public DirectExchange directExchange() {

        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                //绑定主交换机和备份交换机
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME).build();
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //声明队列
    @Bean
    public Queue confirm_queue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean
    public Queue backup_queue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    @Bean
    public Queue warning_queue() {
        return QueueBuilder.durable(WARN_QUEUE_NAME).build();
    }


    //绑定
    @Bean
    public Binding confirmBinding(@Qualifier("directExchange") DirectExchange directExchange,
                                  @Qualifier("confirm_queue") Queue queue) {
        return BindingBuilder.bind(queue).to(directExchange).with("key1");
    }

    //备份交换机和备份队列绑定
    @Bean
    public Binding confirm_backup_exchange(@Qualifier("backup_queue") Queue queue,
                                           @Qualifier("fanoutExchange") FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    //备份交换机和报警队列绑定
    @Bean
    public Binding confirm_backup_waring_exchange(@Qualifier("warning_queue") Queue queue,
                                                  @Qualifier("fanoutExchange") FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
}
