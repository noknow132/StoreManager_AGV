package com.service;

import java.util.List;
import java.util.Map;

import com.entity.RunStepRecord;

public interface IRunStepRecordService {
	/**
	 * 查询运行步骤记录
	 * @param type 作业类型
	 * @param no 搜索条件编号
	 * @return
	 */
	List<List<Map<String,Object>>> searchRunStepRecord(int type,String no);
}
