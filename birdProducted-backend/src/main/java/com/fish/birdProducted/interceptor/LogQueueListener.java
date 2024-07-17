package com.fish.birdProducted.interceptor;

import com.fish.birdProducted.domain.entity.Log;
import com.fish.birdProducted.domain.entity.LoginLog;
import com.fish.birdProducted.mapper.LogMapper;
import com.fish.birdProducted.mapper.LoginLogMapper;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import com.fish.birdProducted.constants.RabbitConst;

import java.io.IOException;

/**
 * @author fish
 * <p>
 * @创建时间 2024/2/11 10:01
 * @content 日志队列
 */
@Component
@Slf4j
public class LogQueueListener {

    @Resource
    private LoginLogMapper loginLogMapper;

    @Resource
    private LogMapper logMapper;

    /**
     * 监听登录日志队列
     * 设置 消费队列和并发消费者的数量范围为5到101
     * 使用@Header接口获取messageProperties中的DELIVERY_TAG属性
     */
    @RabbitListener(queues = RabbitConst.LOG_LOGIN_QUEUE,concurrency = "5-10")
    public void handlerLoginLog(LoginLog loginLog, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
        log.info("监听登录日志队列,标识:{},数据：{}", tag, loginLog);
        System.out.println(loginLog+"监听日志消息队列");
        if (loginLog.getBrowser().startsWith("Unknown")) {
            loginLog.setBrowser("未知");
        }
        if (loginLog.getOs().startsWith("Unknown")) {
            loginLog.setOs("未知");
        }
        if (loginLog.getType() == null) {
            loginLog.setType(2);
        }
        loginLogMapper.insert(loginLog);
//        channel.basicAck(tag,false);

        log.info("登录日志标识:{}，数据库添加成功", tag);
        }catch (Exception e) {
            log.error("消费登录日志时发生异常: {}", e.getMessage());
        }
    }

    /**
     * 监听系统操作日志队列
     * 开启手动确认
     */
    @RabbitListener(queues = RabbitConst.LOG_SYSTEM_QUEUE,concurrency = "5-10")
    public void handlerSystemLog(Log logEntity) {
        log.info("--------------消费系统操作日志--------------");
        logMapper.insert(logEntity);
        log.info("--------------系统操作日志插入数据库成功--------------");
    }
}
