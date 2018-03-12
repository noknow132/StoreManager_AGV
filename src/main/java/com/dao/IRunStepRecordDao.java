package com.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.entity.RunStepRecord;
/**
 * @author 罗欢欢
 * @date 2018-2-3
 * @remark 运行记录对应的数据逻辑接口
 */
public interface IRunStepRecordDao {
	
	/**
	 * 插入运行步骤记录表
	 * @param rsr 运行步骤实体
	 * @return
	 */
    int insertRunStepRecord(RunStepRecord rsr);
    
    /**
     * 插入运行记录表
     * @param rsr
     * @return
     */
    int insertRunStepRecordSelective(RunStepRecord rsr);
    
    /**
     * 查询分组运行步骤记录
     * @param type 作业类型
     * @return
     */
    List<RunStepRecord> searchRunStepRecordGroupBy(@Param("type")int type,@Param("no")String no);
    
    /**
     * 根据作业id查询运行步骤记录
     * @param workId 作业id
     * @return
     */
    List<Map<String,Object>> searchRunStepRecordByWorkId(String workId);

    /**
     * 通过单号删除运行步骤记录
     * @param orderNo 单号
     * @return
     */
    int deleteRunStepRecordByOrderNo(String orderNo);
    
    int deleteByPrimaryKey(String runRecordId);

    int insert(RunStepRecord record);

    int insertSelective(RunStepRecord record);

    RunStepRecord selectByPrimaryKey(String runRecordId);

    int updateByPrimaryKeySelective(RunStepRecord record);

    int updateByPrimaryKey(RunStepRecord record);
}