package com.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.entity.ChangeStore;
/**
 * @author 罗欢欢
 * @date 2018-1-8
 * @remark 配置参数对应的数据逻辑接口
 */
public interface IChangeStoreDao {

    /**
     * 插入调库单
     * @param cs 调库单实体
     * @return
     */
    int insertChangeStore(ChangeStore cs);
    
    /**
     * 根据id查找
     * @param changeId
     * @return 调库单实体
     */
    ChangeStore selectChangeStoreByChangeId(String changeId);
    
    /**
     * 根据调库单号查找
     * @param changeNo
     * @return 调库单实体
     */
    ChangeStore selectChangeStoreByNo(@Param("changeNo")String changeNo);
    /**
     * 根据状态查找
     * @param status
     * @return
     */
    List<Map<String,Object>> selectByStatue(@Param("status")Integer status);
    
    /**
     * 根据主键id删除
     * @param changeId
     * @return 删除的行数
     */
    int deleteByChangeId(@Param("changeId") String changeId);
    
    /**
     * 根据条件修改
     * @param cs 调库单实体
     * @return 修改的条数
     */
    int updateCSByCSIdSelective(ChangeStore cs);
    
    /**
     * 根据起始仓库id删除调库单
     * @param storeIdFrom 起始仓库id
     */
    void deleteChangeStoreByStoreIdFrom(String storeIdFrom);
    
    /**
	 * 通过仓库id查找调库单记录的单号
	 * @param storeId 仓库id
	 * @return
	 */
	List<String> searchChangeStoreNoByStoreId(String storeId);

    int deleteByPrimaryKey(String changeId);

    int insert(ChangeStore record);
    
    int insertSelective(ChangeStore record);

    ChangeStore selectByPrimaryKey(String changeId);

    int updateByPrimaryKeySelective(ChangeStore record);

    int updateByPrimaryKey(ChangeStore record);
}