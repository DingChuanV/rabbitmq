package com.uin.rabbitmqspringboot.controller;

import com.uin.rabbitmqspringboot.config.DelayExchange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author wanglufei
 * @description: 生产者--发送延迟消息
 * @date 2022/2/5/5:59 PM
 */
@RestController
@RequestMapping("/ttl")
@Slf4j
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/sendMsg/{message}", method = RequestMethod.GET)
//    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable("message") String message) {
        //{} 相当于占位符 会被后面的两个参数替换掉
        log.info("当前时间：{},发送消息给两个延时队列：{}", new Date().toString(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl队列为10s的队列" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl队列为40s的队列" + message);
    }

    //开始发送 消息 TTL
    //注解的value、method、params及headers分别指定“请求的URL、请求方法、请求参数及请求头”。
    @RequestMapping(value = "/sendExpirationMsg/{message}/{ttlTime}", method = RequestMethod.GET)
    public void sendMsg(@PathVariable("message") String message,
                        @PathVariable("ttlTime") String ttlTime) {
        log.info("当前时间：{},发送时长:{}毫秒，消息给时间没有限制的TTL队列：{}", new Date().toString(), ttlTime, message);
        //MessagePostProcessor 函数式接口
        rabbitTemplate.convertAndSend("X", "XC", "消息来自ttl队列为10s的队列" + message,
                msg -> {
                    //设置发送消息的时间延迟
                    msg.getMessageProperties().setExpiration(ttlTime);
                    return msg;
                });
    }

    //基于插件的TTL消息的发送 消息 及 延迟的时间
    @RequestMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable("message") String message,
                        @PathVariable("delayTime") Integer delayTime) {
        log.info("当前时间：{},发送时长:{}毫秒，信息给延迟队列delayed.queue：{}", new Date().toString(), delayTime,
                message);
        rabbitTemplate.convertAndSend(DelayExchange.DELAY_EXCHANGE_NAME,
                DelayExchange.DELAY_EXCHANGE_ROUTING_KEY,
                "消息来自于delayed.queue：" + message + "，时长：" + delayTime,
                msg -> {
                    //设置延迟时长
                    msg.getMessageProperties().setDelay(delayTime);
                    return msg;
                });

    }




}
