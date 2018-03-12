package com.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.controller.UserInfoController;
import com.dao.IConfigParamDao;
import com.dao.IUserInfoDao;
import com.dao.IUserInfoTempDao;
import com.entity.PageBean;
import com.entity.UserInfo;
import com.entity.UserInfoTemp;
import com.service.IUserInfoService;

@Service
public class UserInfoServiceImpl implements IUserInfoService{
	@Autowired
	IUserInfoDao userInfoDao;
	@Autowired
	IUserInfoTempDao userInfoTempDao;
	@Autowired
	IConfigParamDao configParamDao;

	//登录
	@Override
	public Map<String, Object> userLogin(String loginName, String password, HttpServletRequest request) {
		UserInfo userInfo = userInfoDao.userLogin(loginName, password);
		Map<String,Object> result = new HashMap<String,Object>();
		HttpSession session = request.getSession();
		Map<String, Object> loginedUser = new HashMap<String, Object>();
		String path = request.getContextPath();
		if(userInfo != null ){
			//判断是否禁用
			if(userInfo.getIsUsed() == 0){
				result.put("statu", "300");
			}else{
				loginedUser.put("userBase", userInfo);
				result.put("statu", "200");//登录成功
				result.put("location", path + "/currentTask/currentTask.html");//登录后跳转的页面
				session.setMaxInactiveInterval(60*60);//60个60秒 = 1小时
				session.setAttribute("LOGINEDUSER", loginedUser);
				result.put("userinfo", loginedUser);
			}
		}else{
			//失败
			result.put("statu", "500");
			result.put("location", "login.html");
		}
		return result;
	}
	
//	//登录
//	@Override
//	@Transactional
//	public Map<String, Object> userLogin(String loginName, String password, HttpServletRequest request) {
//		UserInfo userInfo = userInfoDao.userLogin(loginName, password);
//		Map<String,Object> result = new HashMap<String,Object>();
//		HttpSession session = request.getSession();
//		Map<String, Object> loginedUser = new HashMap<String, Object>();
//		String path = request.getContextPath();
//		if(userInfo != null ){
//			//判断是否禁用
//			if(userInfo.getIsUsed() == 0){
//				result.put("statu", "300");
//			}else{
//			ConfigParam configParam = configParamDao.selectConfigParamOne();
//			String mac = AuthorizationUtils.getMac();//本机地址
//			if(configParam.getMac() == null || configParam.getMac().equals("")){
//				//首次登录，不需要验证是否为本机地址
//				loginedUser.put("userBase", userInfo);
//				result.put("statu", "200");//登录成功
//				result.put("location", path + "/currentTask/currentTask.html");//登录后跳转的页面
//				session.setMaxInactiveInterval(60*60);//60个60秒 = 1小时
//				session.setAttribute("LOGINEDUSER", loginedUser);
//				result.put("userinfo", loginedUser);
//				//修改本机地址
//				configParam.setMac(mac);
//				configParamDao.updateConfigParam(configParam);
//			}else{				
//				if(mac.equals(configParam.getMac())){
//					//是本机地址，即可登录
//					loginedUser.put("userBase", userInfo);
//					result.put("statu", "200");//登录成功
//					result.put("location", path + "/currentTask/currentTask.html");//登录后跳转的页面
//					session.setMaxInactiveInterval(60*60);//60个60秒 = 1小时
//					session.setAttribute("LOGINEDUSER", loginedUser);
//					result.put("userinfo", loginedUser);
//				}else{
//					//不是本机地址，不可登录
//					result.put("statu", "350");
//				}
//			}	
//			}
//		}else{
//			//失败
//			result.put("statu", "500");
//			result.put("location", "login.html");
//		}
//		return result;
//	}

	//注册
	@Override
	public Map<String, Object> register(UserInfo userInfo) {
		Map<String,Object> result = new HashMap<String,Object>();
		int num = userInfoDao.checkLoginNameExist(userInfo.getLoginName());
		if(num > 0){
			result.put("status", "301");//手机号已存在
		}else{
			userInfo.setUserId(UUID.randomUUID().toString().replace("-", ""));
			userInfoDao.addUserInfo(userInfo);//保存教师
			result.put("status", "200");//注册成功
		}
		return result;
	}

	//账户管理  查询所有账号
	@Override
	public Map<String,Object> userManager(String userName,String nowPage,int pageSize) {
		Map<String,Object> param = new HashMap<String,Object>();
		Map<String,Object> result = new HashMap<String,Object>();
		int count = userInfoDao.searchAllUserInfoCount();
		if(count >0){
			// 分页对象
			PageBean<Map<String, Object>> pageBean = PageBean.newPageBean(pageSize, nowPage,count);
			param.put("userName", userName);
			param.put("startRow", pageBean.getStartRow());
			param.put("endRow", pageBean.getEndRow());
			result.put("list", userInfoDao.searchAllUserInfoPage(param));
			result.put("total", count);
		}
		return result;
	}

	//账号管理 添加账号
	@Override
	@Transactional
	public Map<String, Object> saveUserInfo(UserInfo userInfo) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			if(userInfo.getUserId()==null || userInfo.getUserId().equals("")){
				//添加账号
				userInfo.setUserId(UUID.randomUUID().toString().replace("-", ""));
				userInfo.setIsUsed(1);//启用
				userInfoDao.addUserInfo(userInfo);
			}else{
				//修改账号
				UserInfo ui = userInfoDao.searchUserInfoById(userInfo.getUserId());
				ui.setPassword(userInfo.getPassword());
				ui.setUserName(userInfo.getUserName());
				ui.setLoginName(userInfo.getLoginName());
				ui.setRoleLevel(userInfo.getRoleLevel());
				userInfoDao.updateUserInfo(ui);
			}
			result.put("staus", "200");
		} catch (Exception e) {
			result.put("staus", "500");
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}	
		return result;
	}

	//账号管理 单选删除
	@Override
	public Map<String, Object> deleteUserInfoById(String id) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			userInfoDao.deleteUserInfoById(id);
			result.put("staus", "200");
		} catch (Exception e) {
			result.put("staus", "500");
			e.printStackTrace();
		}	
		return result;
	}

	//账号管理 多选删除
	@Override
	@Transactional
	public Map<String, Object> deleteManyUserInfoById(String[] ids) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			userInfoDao.deleteManyUserInfoById(ids);
			result.put("staus", "200");
		} catch (Exception e) {
			result.put("staus", "500");
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}	
		return result;
	}

	//账号管理 禁用或启用
	@Override
	public Map<String, Object> updateUserInfoIsUsedById(String id,String isUsed) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			UserInfo userInfo = userInfoDao.searchUserInfoById(id);//查找账号
			userInfo.setIsUsed(Integer.parseInt(isUsed));
			userInfoDao.updateUserInfo(userInfo);//保存账号
			result.put("staus", "200");
		} catch (Exception e) {
			result.put("staus", "500");
			e.printStackTrace();
		}	
		return result;
	}

	//根据id查找账号
	@Override
	public UserInfo searchUserInfoById(String id) {
		UserInfo userInfo = userInfoDao.searchUserInfoById(id);//查找账号
		return userInfo;
	}
	
	//检查登录名称是否存在
	@Override
	public Map<String, Boolean> checkLoginNameExist(String loginName,String userInfoId) {
		int num = userInfoDao.checkLoginNameExist(loginName);
		Map<String,Boolean> map = new HashMap<String,Boolean>();
		if(num == 0){
			map.put("valid", true);
		}else{
			if(userInfoId != null && userInfoId != ""){
				if(loginName.equals(userInfoDao.searchUserInfoById(userInfoId).getLoginName())){
					map.put("valid", true);
				}else{
					map.put("valid", false);
				}
			}else{
				//存在
				map.put("valid", false);
			}
		}
		return map;
	}

	@Override
	public void updatePasswordMd5() {
		List<UserInfoTemp> userInfoList = userInfoTempDao.searchUserInfoAll();
		UserInfoTemp userInfo = null;
		for (int i = 0; i < userInfoList.size(); i++) {
			userInfo = userInfoList.get(i);
			if(userInfo.getPassword() != null){
				userInfo.setPassword(UserInfoController.md5Password(userInfo.getPassword()));				
				userInfoTempDao.updateUserInfoTemp(userInfo);
			}	
		}
		
	}

	//修改用户密码
	@Override
	public Map<String, Object> updatePassWord(String userId,String oldPwd,String newPwd) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			UserInfo userInfo = userInfoDao.searchUserInfoById(userId);
			if(userInfo.getPassword().equals(oldPwd)){
				userInfo.setPassword(newPwd);
				userInfoDao.updateUserInfo(userInfo);
				result.put("stus", 200);
			}else{
				result.put("stus", 300);
			}
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
		}
		return result;
	}

	

}
