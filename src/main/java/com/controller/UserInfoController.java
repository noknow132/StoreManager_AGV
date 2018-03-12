package com.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.entity.UserInfo;
import com.service.IUserInfoService;

@RestController
@RequestMapping("/UserInfoController")
public class UserInfoController {
	@Autowired
	IUserInfoService userInfoService;
	
	//登录
	@RequestMapping("/userLogin")
	public Map<String,Object> userLogin(String loginName, String password,HttpServletRequest request){
		return userInfoService.userLogin(loginName,password,request);
	}
		
	//注册
	@RequestMapping("/register")
	public Map<String, Object> register(UserInfo userInfo){
		return userInfoService.register(userInfo);
	}
	
	// 检查登录信息
	@SuppressWarnings("unchecked")
	@RequestMapping("/checkLoginInfo")
	public Map<String, Object> checkLoginInfo(HttpServletRequest request) {
		Map<String, Object> loginInfo = (Map<String, Object>) request.getSession().getAttribute("LOGINEDUSER");
		return loginInfo;
	}
	
	// 退出登录 清空session
	@RequestMapping(value = "/initSession",method = RequestMethod.GET)
	public void initSession(HttpServletRequest request) {
		request.getSession().invalidate();
	}
	
	//账户管理 查询所有用户
	@RequestMapping("/userManager")
	public Map<String,Object> userManager(String userName,String nowPage,int pageSize){	
		return userInfoService.userManager(userName,nowPage,pageSize);
	}
	
	//账号管理 添加或修改用户
	@RequestMapping("/saveUserInfo")
	public Map<String,Object> saveUserInfo(UserInfo userInfo){	
		return userInfoService.saveUserInfo(userInfo);
	}
	
	//账号管理 单选删除
	@RequestMapping("/deleteUserInfoById")
	public Map<String, Object> deleteUserInfoById(String id) {
		return userInfoService.deleteUserInfoById(id);
	}
	
	//账号管理 多选删除
	@RequestMapping("/deleteManyUserInfoById")
	public Map<String, Object> deleteManyUserInfoById(String[] ids) {
		return userInfoService.deleteManyUserInfoById(ids);
	}
	
	//账号管理 禁用或启用
	@RequestMapping("/updateUserInfoIsUsedById")
	public Map<String, Object> updateUserInfoIsUsedById(String id,String isUsed) {
		return userInfoService.updateUserInfoIsUsedById(id,isUsed);
	}
	
	//根据id查找账号
	@RequestMapping("/searchUserInfoById")
	public UserInfo searchUserInfoById(String id) {
		return userInfoService.searchUserInfoById(id);
	}
	
	//检查登录名称是否存在
	@RequestMapping("/checkLoginNameExist")
	public Map<String,Boolean> checkLoginNameExist(String loginName,String userInfoId){
		return userInfoService.checkLoginNameExist(loginName, userInfoId);	
	}
	
	/** 
     * md5加密方法 
     * @param password 
     * @return 
     */  
    public static String md5Password(String password) {  
  
        try {  
            // 得到一个信息摘要器  
            MessageDigest digest = MessageDigest.getInstance("md5");  
            byte[] result = digest.digest(password.getBytes());  
            StringBuffer buffer = new StringBuffer();  
            // 把没一个byte 做一个与运算 0xff;  
            for (byte b : result) {  
                // 与运算  
                int number = b & 0xff;// 加盐  
                String str = Integer.toHexString(number);  
                if (str.length() == 1) {  
                    buffer.append("0");  
                }  
                buffer.append(str);  
            }  
  
            // 标准的md5加密后的结果  
            return buffer.toString();  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
            return "";  
        }  
  
    } 
	
	@RequestMapping(value="/updatePasswordMd5")
	public void updatePasswordMd5(){
    	userInfoService.updatePasswordMd5();
	}
	
	//检查登录名称是否存在
	@RequestMapping("/updatePassWord")
	public Map<String, Object> updatePassWord(String userId,String oldPwd,String newPwd) {
		return userInfoService.updatePassWord(userId, oldPwd, newPwd);
	}
}
