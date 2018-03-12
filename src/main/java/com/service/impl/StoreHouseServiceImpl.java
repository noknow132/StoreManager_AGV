/**  
 * Project Name:FaceDistinguish  
 * File Name:TempServiceImpl.java  
 * Package Name:com.service.impl  
 * Date:2017年11月30日上午10:06:10  
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.  
 *  
 */  

package com.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.dao.ICreateStoreAreaDao;
import com.dao.ICreateStoreHouseDao;
import com.dao.IStoreHouseDao;
import com.entity.CreateStoreHouse;
import com.entity.StoreHouse;
import com.entity.StoreHouseRecord;
import com.service.IStoreHouseService;


/**
 * @author 罗欢欢
 * @date 2018-1-4
 * @remark 建库对应的业务逻辑实现
 */
@Service
public class StoreHouseServiceImpl implements IStoreHouseService {

	@Resource
	private IStoreHouseDao storeHouseDao;
	@Resource
	private ICreateStoreAreaDao createStoreAreaDao;
	@Resource
	private ICreateStoreHouseDao createStoreHouseDao;

	
	//查询仓库库信息
	@Override
	public Map<String, Object> searchStoreHouse(String createstoreareaId,String search,String storeName) {
		 Map<String, Object> result =new HashMap<String ,Object>();
		 Map<String, Object> params =new HashMap<String ,Object>();
		 
		 if(search != "" && search != null) {
			 search+=";";
			 List<String> seachStr = new ArrayList<String>();
			 String[] str = search.replace("，", ";").replace(",", ";").replace("；", ";").split(";");
			 for(String s : str) {
				 if(s.trim() != "" && s!=null) {
					 seachStr.add(s); 
				 }
			 }
			 params.put("search", seachStr);
		 }
		 
		 params.put("csId", createstoreareaId);
		 params.put("storeName", storeName);

		List<Map<String, Object>> list = storeHouseDao.selectAllBySearch(params);
		if(list!=null&&list.size()>0){
			result.put("list", list);
		}else{
			result.put("total", 0);
		}
		return result;
	}

	//查询空仓位
	@Override
	public List<Map<String,Object>> searchEmptyStoreHouse(String storeNo) {
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		List<CreateStoreHouse> createStoreHouseList = createStoreHouseDao.searchCreateStoreHouseAll();//所有建库
		for (CreateStoreHouse createStoreHouse : createStoreHouseList) {
			//当前建库下的仓库
			List<StoreHouse> storeHouseList = storeHouseDao.searchEmptyStoreHouse(createStoreHouse.getCreatestorehouseId(),storeNo);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("storeList", storeHouseList);
			map.put("createStoreHouseStoreName", createStoreHouse.getStoreName());
			result.add(map);
		}
		return result;
	}

	//查询仓库信息
	@Override
	public List<Map<String, Object>> searchStoreHouseByStoreStatus(String storeStatus,String condition) {	
		return storeHouseDao.searchStoreHouseByStoreStatus(storeStatus,condition);
	}

	//查询不在预移库范围内的仓库(移库使用)
	@Override
	public List<Map<String, Object>> searchStoreHouseByNotInMoveStore(String condition) {
		return storeHouseDao.searchStoreHouseByNotInMoveStore(condition);
	}

	//查询指定建库下的空仓位信息
	@Override
	public List<StoreHouse> searchEmptyStoreHouseByCreateStoreHouseId(String storeId,String storeNo) {
		StoreHouse storeHouse = storeHouseDao.selectByStoreId(storeId);//当前仓库
		String createId = storeHouse.getCreatestorehouseId();//当前仓库的建库id
		//当前建库下的仓库
		List<StoreHouse> storeHouseList = storeHouseDao.searchEmptyStoreHouse(createId,storeNo);
		return storeHouseList;
	}

	//查询仓位信息（纠正仓位信息功能查询）
	@Override
	public Map<String, Object> searchStoreHouseByIdOnStorageMessage(String storeId) {
		return storeHouseDao.searchStoreHouseByIdOnStorageMessage(storeId);
	}
	
	//修改仓位信息
	@Override
	@Transactional
	public Map<String, Object> updateStoreHouse(StoreHouse storeHouse) {
		Map<String, Object> result =new HashMap<String ,Object>();
		try {
			//修改仓位信息
			StoreHouse sh = storeHouseDao.selectByStoreId(storeHouse.getStoreId());
			sh.setStoreStatue(storeHouse.getStoreStatue());
			sh.setGoodNo(storeHouse.getGoodNo());
			sh.setCount(storeHouse.getCount());
			sh.setOrderId(storeHouse.getOrderId());
			sh.setRemark(storeHouse.getRemark());
			storeHouseDao.updateStoreHouse(sh);
			result.put("stus", 200);
		} catch (Exception e) {
			result.put("stus", 500);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
		}
		
		return result;
	}

	

}

