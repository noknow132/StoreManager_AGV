package com.service;

import java.util.List;
import java.util.Map;

import com.entity.ConfigParam;

public interface IConfigParamService {
	
	/**
	 * 一键初始化
	 * @return
	 */
	Map<String, Object> initByOnekey();
    /**
     * 查询配置参数
     * @return 配置参数实体
     */
    ConfigParam selectConfigParamOne();
	
	/**
	 * 修改系统名称
	 * @param name 名称
	 * @return
	 */
    Map<String,Object> updateConfigParamName(String name);
    
    
	/**
	 * 修改控制plc启动运行的开关
	 * @param isRun 是否运行
	 * @return
	 */
    Map<String,Object> updateConfigParamIsRun(Integer isRun);
    /**
     * 报警系统
     * @return
     */
    Map<String,Object> warn();
    
    /**
     * 解决报警
     * @param warnCode 报警码
     * @param warnRecordId 报警记录id
     * @return
     */
    Map<String,Object> solveWarn(String warnCode,String warnRecordId);
    
    /**
     * 入口复位
     * @return
     */
    Map<String,Object> reset();
    
    
	/**
	 * 手动判断注册码
	 * @param regEdit 注册码
	 * @return
	 */
	Map<String, Object> createRegFile(List<String> info);
	
	/**
	 * 自动判断注册码
	 * @return
	 */
	Map<String, Object> autoRegEdit();
	
}
