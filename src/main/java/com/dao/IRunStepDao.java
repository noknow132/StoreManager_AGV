package com.dao;

import org.apache.ibatis.annotations.Param;

import com.entity.RunStep;

/**
 * @author 罗欢欢
 * @date 2018-2-3
 * @remark 运行步骤对应的数据逻辑接口
 */
public interface IRunStepDao {
    int deleteByPrimaryKey(String runStepId);

    int insert(RunStep record);

    int insertSelective(RunStep record);

    RunStep selectByPrimaryKey(String runStepId);
    
    /**
     * 根据运行值查找运行步骤实体
     * @param runStepId 
     * @return
     */
    RunStep selectRunStepByRunCode(@Param("runCode")Integer runCode);


    int updateByPrimaryKeySelective(RunStep record);

    int updateByPrimaryKey(RunStep record);
}