package com.inria.spirals.mgonzale.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer{

	@Value("${scheduler.poolSize}")
	private int POOL_SIZE;
	
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
         taskRegistrar.setScheduler(taskScheduler());    
    }

     @Bean(destroyMethod="shutdown")
     public TaskScheduler taskScheduler() {

         ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
         scheduler.setPoolSize(POOL_SIZE);
         scheduler.setThreadNamePrefix("sched-");
         return scheduler;
     }

}

