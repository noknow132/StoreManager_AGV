package com.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.entity.UserInfo;

public interface IUserInfoService {
	/**
	 * 登录
	 * @param loginName 登录名称
	 * @param password 密码
	 * @param session session
	 * @return
	 */
	Map<String,Object> userLogin(String loginName,String password,HttpServletRequest request);
	
	/**
	 * 注册
	 * @param userInfo 用户实体
	 * @return map结果集
	 */
	Map<String, Object> register(UserInfo userInfo);
	
	/**
	 * 账户管理 查询所有账号
	 * @param userName 联系人
	 * @param nowPage 当前页
	 * @param pageSize 分页大小
	 * @return 分页结果
	 */
	Map<String,Object> userManager(String userName,String nowPage,int pageSize);
	
	/**
	 * 账号管理 添加用户
	 * @param userInfo 用户对象
	 * @return 操作结果
	 */
	Map<String,Object> saveUserInfo(UserInfo userInfo);
	
	/**
	 * 账号管理 单选删除
	 * @param id 账号id
	 * @return 结果集
	 */
	Map<String, Object> deleteUserInfoById(String id);
	
	/**
	 * 账号管理 多选删除
	 * @param ids 账号id数组
	 * @return 结果集
	 */
	Map<String, Object> deleteManyUserInfoById(String[] ids);
	
	/**
	 * 账号管理 禁用或启用
	 * @param id 账号id
	 * @param isUsed 是否启用
	 * @return
	 */
	Map<String, Object> updateUserInfoIsUsedById(String id,String isUsed);
	
	/**
	 * 根据id查找账号
	 * @param id 账号id
	 * @return 账号
	 */
	UserInfo searchUserInfoById(String id);
	
	/**
	 * 检查登录名称是否存在
	 * @param loginName 登录名
	 * @param userInfoId 账号id
	 * @return
	 */
	Map<String,Boolean> checkLoginNameExist(String loginName,String userInfoId);
	
	/**
	 * 账号加密
	 */
	void updatePasswordMd5();
	
	/**
	 * 修改用户密码
	 * @param userId 用户id
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 * @return
	 */
	Map<String, Object> updatePassWord(String userId,String oldPwd,String newPwd);
}
