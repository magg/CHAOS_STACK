package com.inria.spirals.mgonzale.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

@Configuration
public class ExecutorConfig {

    @Value("${executor.corePoolSize}")
    private int CORE_POOL_SIZE;

    @Value("${executor.maxPoolSize}")
    private int MAX_POOL_SIZE;

    @Value("${executor.queueCapacity}")
    private int QUEUE_CAPACITY;

    @Bean
    public ThreadPoolExecutorFactoryBean executor() {
    	ThreadPoolExecutorFactoryBean executor = new ThreadPoolExecutorFactoryBean();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("worker-");
        executor.initialize();
        return executor;
    }

}
