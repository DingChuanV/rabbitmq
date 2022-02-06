package com.uin.work_queues.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.uin.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * @author wanglufei
 * @description: 发布确认--生产者
 * @date 2022/1/30/11:55 PM
 */
public class Producer_confirm {
    private static final String TASK_QUEUE_CONFIRM = "hello";

    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

//        boolean durable = true;//队列的持久化
//        //声明队列
//        channel.queueDeclare(TASK_QUEUE_CONFIRM, durable, false, false, null);
//        //从控制台输入
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("生产者发送消息，请输入：");
//        while (scanner.hasNext()) {
//            String s = scanner.next();
//            // 发送消息
//            // 配置MessageProperties.PERSISTENT_TEXT_PLAIN队列中的消息持久化
//            channel.basicPublish("", TASK_QUEUE_CONFIRM, MessageProperties.PERSISTENT_TEXT_PLAIN,
//                    s.getBytes("UTF-8"));
//
//            System.out.println("生产者发出消息：" + s);
//        }

        //单个确认
        //Producer_confirm.publishMessageIndividually();
        //批量确认
//        Producer_confirm.publishMessageIndividually();
        //异步确认
        Producer_confirm.publishMessageAsync();

    }

    public static void publishMessageIndividually() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();
        channel.confirmSelect();//开启消息的发布确认
        UUID uuid = UUID.randomUUID();
        boolean durable = true; //开启队列的持久化
        channel.queueDeclare(TASK_QUEUE_CONFIRM, durable, false, false, null);

        //批量确认消息的大小
        int batchSize = 100;
        //未确认消息个数
        int outstandingMessageCount = 0;

        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", TASK_QUEUE_CONFIRM, null, message.getBytes());
            outstandingMessageCount++;

            if (outstandingMessageCount == batchSize) {
                channel.waitForConfirms();
                outstandingMessageCount = 0;
            }
        }
        //确保还有消息没有被处理
        if (outstandingMessageCount > 0) {
            channel.waitForConfirms();
        }
        long end = System.currentTimeMillis();

        System.out.println("发布" + MESSAGE_COUNT + "批量确认消息，" + "耗时：" + (end - begin) + "ms");
    }

    //异步确认消息
    public static void publishMessageAsync() throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();

        channel.queueDeclare(TASK_QUEUE_CONFIRM, true, false, false, null);
        channel.confirmSelect();//开启消息确认

        /**
         * 准备一个线程安全的哈希表 适用于高并发的情况下
         *  1.轻松的将序号(key)与消息(value)进行关联
         *  2.轻松批量删除条目 只要给到序列号(key)
         *  3.支持并发访问
         */
        ConcurrentSkipListMap<Long, String> map = new ConcurrentSkipListMap<>();


        //消息确认成功 回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            //2.删除掉一定确认的消息 剩下的就是未确认的消息
            if (multiple) {//如果是批量
                ConcurrentNavigableMap<Long, String> confirmMap = map.headMap(deliveryTag);//删掉一定异步确认的消息
            } else {
                map.remove(deliveryTag);
            }
            System.out.println("消息监听成功的消息:" + deliveryTag);
        };
        //消息确认失败 回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            //3.打印一下为确认的消息都有哪些
            String s = map.get(deliveryTag);
            System.out.println("未确认的消息是" + s + "，" + "消息监听失败的消息tag:" + deliveryTag);
        };
        //开启消息监听器 监听哪些成功 哪些失败了
        channel.addConfirmListener(ackCallback, nackCallback); //异步通知
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息:" + i;
            channel.basicPublish("", TASK_QUEUE_CONFIRM, null, message.getBytes("UTF-8"));
            /**
             * 就是把未确认的消息放到一个基于内存的能被发布线程访问的队列
             * 因为消息监听器和发送消息是两个线程
             * 可以用ConcurrentLinkedQueue来进行线程的传递信息
             */
            //1.此处记录下所有要发送的消息 消息的总和
            map.put(channel.getNextPublishSeqNo(), message);

        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，" + "耗时：" + (end - begin) + "ms");
    }
}
