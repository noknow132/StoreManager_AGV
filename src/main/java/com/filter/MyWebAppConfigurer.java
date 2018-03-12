package com.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebAppConfigurer  extends WebMvcConfigurerAdapter {
	  @Override
	    public void addInterceptors(InterceptorRegistry registry) {
	        // 多个拦截器组成一个拦截器链
	        // addPathPatterns 用于添加拦截规则
	        // excludePathPatterns 用户排除拦截
		
		  //c
/*		  registry.addInterceptor(new UrlFilterTest()).addPathPatterns("/UserInfoController/userLogin");
 * 
*/	  
		  registry.addInterceptor(new UrlFilter()).addPathPatterns("/**").excludePathPatterns ("/UserInfoController/**").excludePathPatterns("/ConfigParamController/**");
	      
	      //账号禁用拦截
	      registry.addInterceptor(new SessionCheckFilter()).addPathPatterns("/**")
	      .excludePathPatterns ("/UserInfoController/userLogin")//用户登录 除外
	      .excludePathPatterns ("/ConfigParamController/warn")//报警提示 除外
	      .excludePathPatterns ("/workStep/getNowPlace");//获取机械臂的当前状态 除外
	      
	      //注册码拦截（不允许在没有输入正确的注册码之前，通过url访问除了login.html以外的其他页面）
	      registry.addInterceptor(new MacRegCheckFilter()).addPathPatterns("/UserInfoController/checkLoginInfo");
	      
	      super.addInterceptors(registry);
	      
	    }
}
