package com.fish.birdProducted.config.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/16 20:26
 * <p>
 * RabbitMq配置类
 * <p>
 * 创建交换机，队列，并且绑定
 */
@Configuration
public class EmailRabbitConfig {
    /**
     * 创建邮件队列
     */
    @Value("${spring.rabbitmq.queue.email}")
    public String MAIL_QUEUE;

    /**
     * 创建邮件交换机
     */
    @Value("${spring.rabbitmq.exchange.email}")
    public String MAIL_EXCHANGE;

    /**
     * 创建邮件路由键
     */
    @Value("${spring.rabbitmq.routingKey.email}")
    public String MAIL_ROUTING_KEY;

    /**
     * 定义交换机
     * 1. 交换机名称
     * 2. 交换机类型， direct，topic，fanout和header
     * 3. 指定交换机是否持久化
     * 4. 指定交换机在没有队列绑定时，是否删除
     * 5. Map<String, Object>类型，用来指定交换机其他的一些结构化参数
     */
    @Bean
    public DirectExchange mailExchange() {
        return ExchangeBuilder.directExchange(MAIL_EXCHANGE).durable(true).build();
    }

    /**
     * 声明队列
     * 1. 队列名称
     * 2. 队列是否持久化，注意：只是队列名称等元数据持久化，不是队列中的消息持久化
     * 3. 队列是否是私有的，若为true 只有创建他的应用程序才能消费
     * 4. 队列没有消费者订阅是否自动删除
     * 5. 队列的一些结构化信息，如声明死信队列，磁盘队列会用到
     */
    @Bean
    public Queue mailQueue() {
        return QueueBuilder.durable(MAIL_QUEUE).build();
    }

    /**
     * 绑定队列跟交换机
     * 1. 队列名称
     * 2. 交换机
     * 3. 路由键，在直连模式下可以为队列名称
     */
    @Bean
    public Binding mailBinding(DirectExchange mailExchange,Queue mailQueue) {
        return BindingBuilder.bind(mailQueue).to(mailExchange).with(MAIL_ROUTING_KEY);
    }

    /**
     * 解决方法:添加这个类进行序列化解析
     * 会自动识别
     * @param objectMapper json序列化实现类
     * @return mq 消息序列化工具
     */
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
