package com.inria.spirals.mgonzale.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTasks {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Autowired
	@Qualifier(value = "taskExecutor")
	private TaskExecutor taskExecutor;
	
	  @Autowired
	  private OpenStackConnection osc; 
	  
    @Scheduled(cron = "${cron.expression}")
	public void reportCurrentTime()   throws IOException {
		System.out.println("The time is now: " + dateFormat.format(new Date()));
		osc.test();
		
	}
   
}
