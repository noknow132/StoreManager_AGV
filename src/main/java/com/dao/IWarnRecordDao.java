package com.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.entity.WarnRecord;

public interface IWarnRecordDao {
	/**
	 * 添加报警记录
	 * @param warnRecord 报警记录
	 */
    void insertWarnRecord(WarnRecord warnRecord);
    
    /**
     * 查找最新的报警记录
     * @return
     */
    WarnRecord searchLastWarnRecord();
    
    /**
     * 修改报警记录
     * @param warnRecord 报警记录实体
     */
    void updateWarnRecord(WarnRecord warnRecord);
    
    /**
     * 查询报警记录
     * @param type 作业类型
     * @param no 搜索条件编号
     * @return
     */
    List<Map<String,Object>> searchWarnRecord(@Param("type")String type,@Param("no")String no);
}