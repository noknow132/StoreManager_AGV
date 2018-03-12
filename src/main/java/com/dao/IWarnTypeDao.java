package com.dao;

import java.util.List;

import com.entity.WarnType;

public interface IWarnTypeDao {
	/**
	 * 查询报警类型
	 * @return
	 */
	List<WarnType> searchWarnTypeAll();
}