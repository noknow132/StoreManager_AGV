package com.filter;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dao.IUserInfoDao;
import com.entity.UserInfo;

@Component
public class SessionCheckFilter  implements HandlerInterceptor{
	@Autowired
	private IUserInfoDao userInfoDao;
	private static SessionCheckFilter  sessionCheckFilter ;
	
	//通过@PostConstruct实现初始化bean之前进行的操作
	@PostConstruct
	public void init() {
		sessionCheckFilter = this;
		sessionCheckFilter.userInfoDao = this.userInfoDao;

	}

	//方法会在请求处理前被调用。这个方法返回boolean值，如果返回true则继续往下执行，如果返回false则中断。
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return true;
	}

	//方法会在请求处理后，继续调用。
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		try {
			Object userSession = request.getSession().getAttribute("LOGINEDUSER");
			if(userSession != null){
				UserInfo sessionUf = (UserInfo)((Map<String, Object>)userSession).get("userBase");
				//通过session中的userInfo ID 查找数据库中的账号
				UserInfo userInfo = sessionCheckFilter.userInfoDao.searchUserInfoById(sessionUf.getUserId());
				if(userInfo.getIsUsed() == 0){
					//删除自身的session
					request.getSession().invalidate();
				}
			}
		} catch (Exception e) {
			//System.out.println("SessionCheckFilter：用户session不存在");
			//e.printStackTrace();
		}
		
	}

	//方法会在视图渲染之后调用。
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
