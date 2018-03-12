package com.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dao.IConfigParamDao;
import com.entity.ConfigParam;
import com.google.gson.Gson;

@Component
public class UrlFilterTest  implements HandlerInterceptor {

	@Resource	
	private IConfigParamDao configParamDao;
	private static UrlFilterTest  urlFilter ;

	//通过@PostConstruct实现初始化bean之前进行的操作
	@PostConstruct 
	public void init() {  
		urlFilter = this;  
		urlFilter.configParamDao = this.configParamDao;        

	} 
	//只有返回true才会继续向下执行，返回false取消当前请求  在请求处理之前进行调用（Controller方法调用之前）
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
      boolean flag=false;
		Map map=new HashMap();
		if(!flag){
			map.put("code",500);
			map.put("statu", "300");
			response.setContentType("text/plain");
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding("UTF-8");
			try {
				Gson gs=new Gson();
				String mapJson=gs.toJson(map);
				PrintWriter writer = response.getWriter();
				writer.print(mapJson);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	//请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	//在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 获得请求中的url
	 * 
	 * @param request
	 *            请求对象
	 * @return 返回url
	 */
	public static  String getURL(HttpServletRequest request) {
		// 获得
		String contextPath = request.getContextPath().equals("/") ? "" : request.getContextPath();//获取项目名字
		String url = "http://" + request.getServerName();//http://localhost
		// 若当前请求的端口号不等于80
		if (null2Int(Integer.valueOf(request.getServerPort())) != 80)
			// 将url和当前请求的端口号拼接
			url = url + ":" + null2Int(Integer.valueOf(request.getServerPort())) + contextPath;
		else {
			url = url + contextPath;
		}
		return url;
	}

	/**
	 * 获得对象的整数表示
	 * 
	 * @param s
	 *            对象
	 * @return 返回对象的整数表示
	 */
	public static int null2Int(Object s) {
		int v = 0;
		if (s != null)
			try {
				v = Integer.parseInt(s.toString());
			} catch (Exception localException) {
			}
		return v;
	}
}
