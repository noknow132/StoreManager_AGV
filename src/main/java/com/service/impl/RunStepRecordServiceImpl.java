package com.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.IRunStepRecordDao;
import com.entity.RunStepRecord;
import com.service.IRunStepRecordService;

@Service
public class RunStepRecordServiceImpl implements IRunStepRecordService{
	@Autowired
	private IRunStepRecordDao runStepRecordDao;

	//查询运行步骤记录
	@Override
	public List<List<Map<String,Object>>> searchRunStepRecord(int type,String no) {
		List<RunStepRecord> runStepRecordGoupBy = runStepRecordDao.searchRunStepRecordGroupBy(type,no);//分组运行步骤记录
		List<List<Map<String,Object>>> runStepRecordList = new ArrayList<List<Map<String,Object>>>();
		for (RunStepRecord runStepRecord : runStepRecordGoupBy) {
			runStepRecordList.add(runStepRecordDao.searchRunStepRecordByWorkId(runStepRecord.getWorkId())) ;
		}
		return runStepRecordList;
	}
}
