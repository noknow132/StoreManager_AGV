package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.entity.OutinPlace;

public interface IOutinPlaceDao {
    int deleteByPrimaryKey(String id);

    int insert(OutinPlace record);

    int insertSelective(OutinPlace record);

    OutinPlace selectByPrimaryKey(String id);
    
    /**
     * 根据主键查找取放货台
     * @param id
     * @return
     */
    OutinPlace selectOutinPlaceById(String id);

    /**
     * 根据类型查找出入口配置
     * @param type 类型
     * @return
     */
    List<OutinPlace> selectOutinPlaceByType(@Param("type")Integer type);
    
    int updateByPrimaryKeySelective(OutinPlace record);

    int updateByPrimaryKey(OutinPlace record);
}