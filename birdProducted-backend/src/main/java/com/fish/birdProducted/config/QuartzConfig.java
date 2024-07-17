package com.fish.birdProducted.config;

import com.fish.birdProducted.quartz.RefreshTheCache;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.spi.MutableTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * TODO 定义任务描述和具体的执行时间
 */
@Slf4j
@Configuration
public class QuartzConfig {

    @Bean
    @Primary
    public JobDetail jobDetail() {
        // 指定任务描述具体的实现类
        return JobBuilder.newJob(RefreshTheCache.class)
                // 指定任务的名称
                .withIdentity("refreshTheCache")
                // 任务描述
                .withDescription("任务描述：用于每6分钟刷新一次常用数据缓存")
//                .withDescription("康师傅喝开水")
                .storeDurably(true)
                .build();
    }
    
    @Bean
    public Trigger trigger() {
        //创建触发器
        return TriggerBuilder.newTrigger()
                // 绑定工作任务
                .forJob(jobDetail())
                // 每小时触发一次
//                .withSchedule(CronScheduleBuilder.cronSchedule("0 */5 * ? * * *"))
//                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 * ? * * *"))    // 每小时触发一次
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(600)  // 6min
                        .repeatForever())
                .build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setJobDetails(jobDetail());
        scheduler.setTriggers(trigger());
        try {
            scheduler.afterPropertiesSet(); // 手动调用此方法以确保初始化
            scheduler.start();
            log.info("Scheduler started successfully.");
        } catch (Exception e) {
            log.error("Error starting scheduler: {}", e.getMessage());
        }

        return scheduler;
    }
}
