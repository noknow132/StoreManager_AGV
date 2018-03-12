package com.dao;

import com.entity.ConfigParam;
/**
 * @author 罗欢欢
 * @date 2018-1-8
 * @remark 配置参数对应的数据逻辑接口
 */
public interface IConfigParamDao {
    /**
     * 查询配置参数
     * @return 配置参数实体
     */
    ConfigParam selectConfigParamOne();
    
    /**
     * 修改配置参数
     * @param configParam 配置
     * @return
     */
    int updateConfigParam(ConfigParam configParam);
    /**
     * 一键初始化 调用存储过程
     */
    void  initByOnekey();
    
}