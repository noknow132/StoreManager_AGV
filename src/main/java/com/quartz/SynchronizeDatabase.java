package com.quartz;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SynchronizeDatabase {
	
	  @Scheduled(cron="0 0/2 8-20 * * ?") 
	    public void executeFileDownLoadTask() {

	        // 间隔2分钟,执行工单上传任务     
	       /* Thread current = Thread.currentThread();  
	        System.out.println("定时任务1:"+current.getId());*/
	        ///logger.info("ScheduledTest.executeFileDownLoadTask 定时任务1:"+current.getId()+ ",name:"+current.getName());
	    }
}
