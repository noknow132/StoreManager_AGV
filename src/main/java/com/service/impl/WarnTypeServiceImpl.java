package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.IWarnTypeDao;
import com.entity.WarnType;
import com.service.IWarnTypeService;

@Service
public class WarnTypeServiceImpl implements IWarnTypeService{
	@Autowired
	private IWarnTypeDao warnTypeDao;

	//查询报警类型
	@Override
	public List<WarnType> searchWarnTypeAll() {
		return warnTypeDao.searchWarnTypeAll();
	}

}
