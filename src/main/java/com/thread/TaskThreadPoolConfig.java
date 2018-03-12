/**  
 * Project Name:StoreManager  
 * File Name:TaskThreadPoolConfig.java  
 * Package Name:com.util.ThreadPool  
 * Date:2018年1月30日上午9:14:43  
 * Copyright (c) 2018, chenzhou1025@126.com All Rights Reserved.  
 *  
*/  
  
package com.thread;  

import org.springframework.boot.context.properties.ConfigurationProperties;  

//@ConfigurationProperties(prefix = "spring.task.pool") // 该注解的locations已经被启用，现在只要是在环境中，都会优先加载  
public class TaskThreadPoolConfig {  
    private int corePoolSize;  //线程池维护线程的最少数量
    private int maxPoolSize;  //线程池维护线程的最大数量
    private int keepAliveSeconds;  //允许的空闲时间
    private int queueCapacity;  //缓存队列数量

	public int getCorePoolSize() {
		return this.corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return this.maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getKeepAliveSeconds() {
		return this.keepAliveSeconds;
	}

	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}

	public int getQueueCapacity() {
		return this.queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}  
    
}  
  
