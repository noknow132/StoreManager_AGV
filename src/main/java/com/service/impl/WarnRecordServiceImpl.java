package com.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.IWarnRecordDao;
import com.service.IWarnRecordService;

@Service
public class WarnRecordServiceImpl implements IWarnRecordService{
	@Autowired
	private IWarnRecordDao warnRecordDao;

	//查询报警记录
	@Override
	public List<Map<String,Object>> searchWarnRecord(String type,String no) {
			return warnRecordDao.searchWarnRecord(type,no);
	}

}
