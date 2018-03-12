package com.service;

import java.util.List;
import java.util.Map;

public interface IWarnRecordService {
	/**
	 * 查询报警记录
	 * @param type 作业类型
	 * @param no 搜索条件编号
	 * @return
	 */
	List<Map<String,Object>> searchWarnRecord(String type,String no);

}
