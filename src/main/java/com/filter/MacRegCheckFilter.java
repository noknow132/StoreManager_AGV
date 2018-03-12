package com.filter;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dao.IConfigParamDao;
import com.entity.ConfigParam;
import com.google.gson.Gson;
import com.util.AuthorizationUtils;


@Component
public class MacRegCheckFilter implements HandlerInterceptor{
	@Autowired
	private IConfigParamDao configParamDao;
	private static MacRegCheckFilter  macRegCheckFilter ;
	
	//通过@PostConstruct实现初始化bean之前进行的操作
	@PostConstruct
	public void init() {
		macRegCheckFilter = this;
		macRegCheckFilter.configParamDao = this.configParamDao;

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		ConfigParam configParam = macRegCheckFilter.configParamDao.selectConfigParamOne();
//		String regCopy = configParam.getRegEditCopy();//用户注册码
//		String reg = configParam.getRegEdit();//本机注册码
//		String mac = AuthorizationUtils.getMac();//本机地址
//		if(reg != null && !reg.equals("") && reg.equals(regCopy)){
//			return true;
//		}
//		Map map=new HashMap();
//		map.put("regEdit", false);
//		Gson gs=new Gson();
//		String mapJson=gs.toJson(map);
//		PrintWriter writer = response.getWriter();
//		writer.print(mapJson);
//		String XRequested =request.getHeader("X-Requested-With");//如果值为 XMLHttpRequest ，表示是ajax请求
		
		Map map=new HashMap();
		Map<String,Object> result = new HashMap<String,Object>();
		//获取当前文件的所在路径
		File f2 = new File(this.getClass().getResource("").getPath());
        String projectDir = f2.toString().substring(0,f2.toString().indexOf("WEB-INF"));
        List<String> regInfo = AuthorizationUtils.checkRegFile(projectDir); //检查注册文件是否存在，返回文件内容
        //判断注册码文件是否存在
        if(regInfo != null && regInfo.size() >0 ){
			if(!configParam.getRegEditCopy().equals("") && configParam.getRegEditCopy().equals(regInfo.get(regInfo.size()-1))){
				//注册码正确
				map.put("regEdit", true);
				return true;
			}else{
				//注册码存在，但不正确
				map.put("regEdit", false);
	        }
        }else{
        	//注册码不存在
        	map.put("regEdit", false);
        }
        
		Gson gs=new Gson();
		PrintWriter writer = response.getWriter();
		//如果为false 返回json数据给请求的ajax
    	String mapJson=gs.toJson(map);
		writer.print(mapJson);
        return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
