package com.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.entity.OutLet;
/**
 * @author 罗欢欢
 * @date 2018-1-11
 * @remark 出口对应的数据逻辑接口
 */

public interface IOutLetDao {
	
	/**
	 * 根据类型查找出口
	 * @param type类型   0是查询全部出口  1是查询出货口  2是查询分拣口  
	 * @return 出口集合
	 */
    List< OutLet>  selectOutLetByType(@Param("type")Integer type);

	/**
	 * 查询所有出口
	 * @param 出口类型 
	 * @return
	 */
	List<Map<String,Object>> searchOutLet(@Param("outType")String outType);
	
	/**
	 * 新建出口
	 * @param outLet 出口实体
	 * @return
	 */
	int insertOutLet(OutLet outLet);
	
	/**
	 * 修改出口
	 * @param outLet 出口实体
	 * @return
	 */
	int updateOutLet(OutLet outLet);
	
	/**
	 * 根据id查找出口
	 * @param outletId 出口id
	 * @return
	 */
	OutLet searchOutLetById(String outletId);
	
	/**
	 * 根据出库单选择出口
	 * @param outputStoreNo 出库单号
	 * @return  出口实体
	 */
	OutLet selectOutLetByOutputStoreNo( String outputStoreNo);
	
	/**
	 * 检查出口编号是否存在
	 * @param outNo 出口编号
	 * @param outType 出口类型
	 * @return
	 */
	int searchOutLetCountByOutNo(int outNo,int outType);
}