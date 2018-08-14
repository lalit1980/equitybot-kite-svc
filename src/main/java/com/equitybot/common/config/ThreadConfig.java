package com.equitybot.common.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadConfig {

    @Bean
    @Qualifier("dataProviderTaskPool")
    public TaskExecutor threadPoolTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(260);
        executor.setThreadNamePrefix("DataProviderTaskPool-");
        executor.initialize();
        return executor;
    }

    @Bean
    @Qualifier("hlocTaskPool")
    public TaskExecutor threadPoolTaskExecutorHLOC() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(260);
        executor.setThreadNamePrefix("HLOCTaskPool-");
        executor.initialize();
        return executor;
    }

    @Bean
    @Qualifier("superTrendTaskPool")
    public TaskExecutor threadPoolTaskExecutorSuperTrend() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        executor.setMaxPoolSize(260);
        executor.setThreadNamePrefix("superTrendTaskPool-");
        executor.initialize();
        return executor;
    }


}