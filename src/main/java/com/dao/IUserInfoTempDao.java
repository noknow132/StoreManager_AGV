package com.dao;

import java.util.List;

import com.entity.UserInfoTemp;

public interface IUserInfoTempDao {
	
	List<UserInfoTemp> searchUserInfoAll();
	
	int updateUserInfoTemp(UserInfoTemp userInfoTemp);
}