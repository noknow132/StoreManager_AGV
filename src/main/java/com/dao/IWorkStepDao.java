package com.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.entity.WorkStep;
/**
 * @author 罗欢欢
 * @date 2018-1-8
 * @remark 当前作业对应的数据逻辑接口
 */
public interface IWorkStepDao {


	int insert(WorkStep record);
	/**
	 * 插入要执行的作业
	 * @param ws
	 * @return
	 */
	int insertWorkStep(WorkStep ws);
	/**
	 *批量 插入要执行的作业
	 * @param wss
	 * @return
	 */
	int insertWorkSteps(@Param("wss")List<WorkStep> wss);

	/**
	 * 根据状态查找作业相关信息
	 * @param workStatue 执行状态
	 * @param workType 作业类型
	 * @param no 搜索条件编号
	 * @return
	 */
	List<Map<String ,Object>> selectWorkStepsByWorkStatue(@Param("workStatue") Integer workStatue, @Param("workType")Integer workType,@Param("no")String no);
	
	/**
	 * 根据条形码查找作业
	 * @param params
	 * @return
	 */
	WorkStep selectWorkStepByFringeCode(Map<String,Object> params);
	
	/**
	 * 根据仓位id和作业状态查找作业
	 * @param params 
	 * @return
	 */
	  List<WorkStep> selectWorkStepByStoreIdAndStatus(Map<String,Object> params);
	
	/**
	 * 根据状态查找作业数量
	 * @param params
	 * @return
	 */
	int selectWorkStepCountByStatus(Map<String,Object> params);

	/**
	 * 根据主键查找作业类型
	 * @param workId
	 * @return
	 */
	WorkStep selectWorkStepByWorkId(String workId);
	
	/**
	 * 根据条形码查找作业实体（已完成的作业除外）
	 * @param fringeCode 条形码
	 * @return
	 */
	WorkStep selectWorkStepByCode(String fringeCode);
	
	/**
	 * 批量删除作业
	 * @param workIds  作业id数组
	 * @return 删除的条数
	 */
	int delectWorkStepsByWorkIds(String[] workIds);
	
   /**
    * 根据主键删除作业
    * @param workId 作业id
    * @return 删除的条数
    */
	int delectWorkStepByWorkId(String workId);
	
	/**
	 * 根据执行状态查找作业
	 * @param statue 执行状态
	 * @return
	 */
	List<WorkStep> searchWorkStepByStatue(String statue);
	
	/**
	 * 根据多个状态查找
	 * @param statue 执行状态
	 * @return
	 */
	List<WorkStep> searchWorkStepByStatues(Map<String,Object> params);
		
	/**
	 * 根据执行状态和作业类型查找
	 * @param statue 执行状态
	 * @param workType 作业类型
	 * @return
	 */
	WorkStep searchWorkStepByStatueAndType(Map<String,Object> params );
	
	/**
	 * 根据执行状态查找
	 * @return
	 */
	WorkStep searchWorkStepByStatueAndTypeZZ();
	
	/**
	 * 修改作业表
	 * @param workStep 作业
	 * @return
	 */
	int updateWorkStep(WorkStep workStep);
	
	/**
	 * 根据单号删除作业
	 * @param orderNoList 单号集合
	 */
	void deleteWorkStepByOrderNo(@Param("orderNoList")List<String> orderNoList);
}