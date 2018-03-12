package com.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.entity.UserInfo;

public interface IUserInfoDao {
	/**
	 * 登录
	 * @param loginName 登录名称
	 * @param password 密码
	 * @return UserInfo 登录对象
	 */
	UserInfo userLogin(String loginName,String password);
	
	/**
	 * 检查登录名称是否存在
	 * @param loginName 登录名称
	 * @return 数字
	 */
	int checkLoginNameExist(String loginName);
	
	/**
	 * 添加新用户
	 * @param userInfo 用户对象
	 * @return 数字
	 */
	int addUserInfo(UserInfo userInfo);
	
	/**
	 * 查询所有用户数目 分页
	 * @return 数字
	 */
	int searchAllUserInfoCount();
	
	/**
	 * 查询所有用户 分页
	 * @param param 分页参数
	 * @return
	 */
	List<Map<String,Object>> searchAllUserInfoPage(Map<String,Object> param);
	
	/**
	 * 账号管理 单选删除
	 * @param id 账号id
	 * @return 数字
	 */
	int deleteUserInfoById(String id);
	
	/**
	 * 账号管理 多选删除
	 * @param ids 账号id数组
	 * @return 数字
	 */
	int deleteManyUserInfoById(@Param("ids")String[] ids);
	
	/**
	 * 通过id查找账号
	 * @param id 账号ID
	 * @return 账号
	 */
	UserInfo searchUserInfoById(String id);
	
	/**
	 * 修改账号
	 * @param userInfo 账号
	 * @return 数字
	 */
	int updateUserInfo(UserInfo userInfo);

	
}