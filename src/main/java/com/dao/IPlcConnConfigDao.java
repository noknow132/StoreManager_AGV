package com.dao;

import java.util.List;
import java.util.Map;

import com.entity.PlcConnConfig;

public interface IPlcConnConfigDao {
	/**
	 * 通过plc的名字，查找plc的配置
	 * @param name plc名称
	 * @return
	 */
	PlcConnConfig searchPLCByName(String name);
	
	/**
     * 通过plc类型查找plc配置
     * @param plcType plc类型
     * @return
     */
    List<PlcConnConfig> searchPlcConnConfigByType(String plcType);
	
	/**
	 * 通过plc的id，查找plc的配置
	 * @param id 
	 * @return
	 */
	PlcConnConfig searchPLCById(String id);
	
	/**
	 * 修改plc配置
	 * @param plcConnConfig plc配置实体
	 */
	void updatePlcConnConfig(PlcConnConfig plcConnConfig);
	
	/**
	 * 查找所有plc配置
	 */
	List<PlcConnConfig> searchPlcConnConfigAll();
	
	/**
	 * 新增plc连接配置
	 * @param plcConnConfig plc连接实体
	 * @return
	 */
	int insertPlcConfig(PlcConnConfig plcConnConfig); 
	
	/**
     * 通过id删除plc连接配置
     * @param id plcid
     * @return
     */
    int deletePlcConfigById(String id);
}