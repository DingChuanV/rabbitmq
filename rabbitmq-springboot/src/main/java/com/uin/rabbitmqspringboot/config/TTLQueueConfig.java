package com.uin.rabbitmqspringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wanglufei
 * @description: 延迟队列配置类
 * @date 2022/2/5/5:11 PM
 */
@Configuration
public class TTLQueueConfig {
    //普通队列
    public static final String QUEUE_A_NAME = "Qa";
    public static final String QUEUE_B_NAME = "Qb";
    //增加优化队列
    public static final String QUEUE_C_NAME = "QC";
    //死信队列
    public static final String QUEUE_D_NAME = "Qd";
    //普通交换机
    public static final String EXCHANGE_NAME_X = "X";
    //死信交换机
    public static final String EXCHANGE_NAME_Y_DEAD_LETTER = "Y";

    //声明交换机
    @Bean("exchange_x")
    public DirectExchange exchange_x() {
        return new DirectExchange(EXCHANGE_NAME_X);
    }

    //声明交换机
    @Bean("exchange_y")
    public DirectExchange exchange_y() {
        return new DirectExchange(EXCHANGE_NAME_Y_DEAD_LETTER);
    }

    //声明队列
    @Bean("queue_a")
    public Queue queue_a() {
        //普通队列和死信的交换机绑定
        //参数的设置
        Map<String, Object> arguments = new HashMap<>();
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", EXCHANGE_NAME_Y_DEAD_LETTER);
        //路由
        arguments.put("x-dead-letter-routing-key", "YD");
        //设置消息的过期时间 ms
        arguments.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QUEUE_A_NAME).withArguments(arguments).build();
    }

    @Bean("queue_b")
    public Queue queue_b() {
        //普通队列和死信的交换机绑定
        //参数的设置
        Map<String, Object> arguments = new HashMap<>();
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", EXCHANGE_NAME_Y_DEAD_LETTER);
        //路由
        arguments.put("x-dead-letter-routing-key", "YD");
        //设置消息的过期时间 ms
        arguments.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QUEUE_B_NAME).withArguments(arguments).build();
    }

    //声明优化队列
    @Bean("queue_c")
    public Queue queue_c() {
        //普通队列和死信的交换机绑定
        //参数的设置
        Map<String, Object> arguments = new HashMap<>();
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", EXCHANGE_NAME_Y_DEAD_LETTER);
        //路由
        arguments.put("x-dead-letter-routing-key", "YD");
//      设置消息的过期时间 ms
//      arguments.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QUEUE_C_NAME).withArguments(arguments).build();
    }

    @Bean("queue_d")
    public Queue queue_d() {
        return QueueBuilder.durable(QUEUE_D_NAME).build();
    }

    //绑定 普通队列和普通交换机的绑定
    @Bean
    public Binding Binding_qC_x(@Qualifier("queue_c") Queue queue_c,
                                @Qualifier("exchange_x") DirectExchange exchange_x) {
        return BindingBuilder.bind(queue_c).to(exchange_x).with("XC");
    }

    @Bean
    public Binding Binding_qA_x(@Qualifier("queue_a") Queue queue_a,
                                @Qualifier("exchange_x") DirectExchange exchange_x) {
        return BindingBuilder.bind(queue_a).to(exchange_x).with("XA");
    }

    @Bean
    public Binding Binding_qB_x(@Qualifier("queue_b") Queue queue_b,
                                @Qualifier("exchange_x") DirectExchange exchange_x) {
        return BindingBuilder.bind(queue_b).to(exchange_x).with("XB");
    }

    //死信队列和死信交换机的绑定
    @Bean
    public Binding Binding_qD_y(@Qualifier("queue_d") Queue queue_d,
                                @Qualifier("exchange_y") DirectExchange exchange_y) {
        return BindingBuilder.bind(queue_d).to(exchange_y).with("YD");
    }


}
