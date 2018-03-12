package com.service;

import java.util.List;
import java.util.Map;

import com.entity.OutLet;


/**
 * @author 罗欢欢
 * @date 2018-1-12
 * @remark 出口对应的业务逻辑接口
 */
public interface IOutLetService {

	/**
	 * 查询出口根据类型
	 * @param type 0是查询全部出口  1是查询出货口  2是查询分拣口  
	 * @return
	 */
	Map<String, Object> searchOutLetByType(Integer type);
	
	/**
	 * 查询所有出口
	 * @param 出口类型 
	 * @return
	 */
	List<Map<String,Object>> searchOutLet(String outType);
	
	
	/**
	 * 新建出口 
	 * @param outletName 出口名
	 * @param outNo 出口编号
	 * @param outType 出口类型
	 * @param outletId 出口id
	 * @return
	 */
	Map<String,Object> saveOutLet(String outletName,Integer outNo,Integer outType,String outletId);
	
	/**
	 * 启用或禁用出口
	 * @param isUsed 是否禁用
	 * @param outletId 出口id
	 * @return
	 */
	Map<String, Object> updateOutLetIsUsed(String isUsed,String outletId);
	
	/**
	 * 根据id查找出口
	 * @param outletId 出口id
	 * @return
	 */
	OutLet searchOutLetById(String outletId);
}