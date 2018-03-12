package com.service;

import java.util.List;

import com.entity.WarnType;

public interface IWarnTypeService {
	/**
	 * 查询报警类型
	 * @return
	 */
	List<WarnType> searchWarnTypeAll();

}
