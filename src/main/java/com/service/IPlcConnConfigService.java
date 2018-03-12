package com.service;

import java.util.List;
import java.util.Map;

import com.entity.PlcConnConfig;

public interface IPlcConnConfigService {
	/**
	 * 手动测试连接(新增或修改IP地址到数据库)
	 * @param localIp 本地ip
	 * @param PLCIP plc ip
	 * @param PLCPort plc端口
	 * @param PLCId plcid
	 * @return
	 */
	Map<String, Object> saveIp(String localIp, String PLCIP, int PLCPort,String PLCId);
	
	/**
	 * 自动测试连接
	 * @return
	 */
	Map<String, Object> testConnect();
	
	
	/**
	 * 连接plc
	 * @return
	 */
    Map<String,Object> connectPLC();
    
    /**
     * 通过plcId连接plc
     * @param plcId plcId
     * @return
     */
    Map<String, Object> plcConnById(String plcId);
    
    /**
     * 通过plc名称查找plc配置
     * @param plcName plc名称
     * @return
     */
    PlcConnConfig searchPlcConfigByName(String plcName);
    
    /**
     * 通过plc类型查找plc配置
     * @param plcType plc类型
     * @return
     */
    List<PlcConnConfig> searchPlcConnConfigByType(String plcType);
    
    /**
     * 新增plc连接配置
     * @param plcConnConfig plc连接配置实体
     * @return
     */
    Map<String,Object> insertPlcConfig(PlcConnConfig plcConnConfig);
    
    /**
     * 通过id删除plc连接配置
     * @param id plcid
     * @return
     */
    Map<String,Object> deletePlcConfigById(String id);
}
