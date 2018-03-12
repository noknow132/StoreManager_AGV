package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//import com.thread.TaskThreadPoolConfig;

/**
 * @description Springboot的启动类
 */
@SpringBootApplication
@EnableTransactionManagement //开启事务
//@EnableAsync  
//@EnableConfigurationProperties({TaskThreadPoolConfig.class} ) // 开启配置属性支持  
public class Application extends SpringBootServletInitializer{
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }	
	
//	public static void main(String[] args){
//    	SpringApplication application = new SpringApplication(Application.class); 
//    	application.run(args);  
//    }
}
