/**  
 * Project Name:FaceDistinguish  
 * File Name:TempServiceImpl.java  
 * Package Name:com.service.impl  
 * Date:2017年11月30日上午10:06:10  
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.  
 *  
 */  

package com.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.dao.IChangeStoreDao;
import com.dao.IConfigParamDao;
import com.dao.ICreateStoreHouseDao;
import com.dao.IOutputStoreDao;
import com.dao.IStoreHouseDao;
import com.dao.IWorkStepDao;
import com.entity.ChangeStore;
import com.entity.ConfigParam;
import com.entity.CreateStoreHouse;
import com.entity.OutputStore;
import com.entity.OutputStoreDto;
import com.entity.StoreHouse;
import com.entity.WorkStep;
import com.service.IChangeStoreService;


/**
 * @author 罗欢欢
 * @date 2018-1-4
 * @remark 出库对应的业务逻辑实现
 */
@Service
public class ChangeStoreServiceImpl implements IChangeStoreService {

	@Resource
	private IOutputStoreDao outputStoreDao;
	@Resource
	private IStoreHouseDao storeHouseDao;
	@Resource
	private IWorkStepDao workStepDao;
	@Resource
	private IConfigParamDao configParamDao;
	@Resource
	private ICreateStoreHouseDao createStoreHouseDao;
	@Resource
	private IChangeStoreDao changeStoreDao;

	//查询仓库中有货物的商品信息
	@Override
	public Map<String, Object> searchChangeStoreList(String search) {
		Map<String, Object> result=new HashMap<String, Object>();
		try{
			
			 List<String> seachStr = new ArrayList<String>();

			 if(search != "" && search != null) {
				 search+=";";
				 String[] str = search.replace("，", ";").replace(",", ";").replace("；", ";").split(";");
				 for(String s : str) {
					 if(s.trim() != "" && s!=null) {
						 seachStr.add(s); 
					 }
				 }
			 }
			//List<Map<String, Object>> list = storeHouseDao.selectHasGoodsBySearchList(seachStr);
			
			List<Map<String, Object>> list = storeHouseDao.selectHasGoodsBySearchList(seachStr);
			if(list!=null && list.size()>0){
				result.put("list",list);
			}
			result.put("code",200);
		}catch(Exception e){
			result.put("code",500);
		}
		return result;
	}

	//调库处理
	@Transactional
	@Override
	public Map<String, Object> addChangeStores(String[] changeIds) {
		Map<String, Object> result=new HashMap<String,Object>();
		if (changeIds != null) {
			try{
				List<WorkStep> wsList=new ArrayList<WorkStep>();
				for (String  changeId : changeIds) {
					ChangeStore cs = this.changeStoreDao.selectChangeStoreByChangeId(changeId);
					StoreHouse sh = storeHouseDao.selectByStoreId(cs.getStoreIdFrom());
					CreateStoreHouse csh = createStoreHouseDao.selectCreateStoreHouseById(sh.getCreatestorehouseId());
					if(csh!=null){
						WorkStep ws = getWorkStep(sh,csh.getRobotNo(),cs.getChangeNo(),cs.getStoreIdTo());
						wsList.add(ws);
						cs.setStatue(1);//更改状态已确认待执行
						this.changeStoreDao.updateCSByCSIdSelective(cs);
					}
				}
				workStepDao.insertWorkSteps(wsList);
				result.put("code",200);
			}catch(Exception e){
				e.printStackTrace();
				result.put("code",500);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}		
		return result;
	}

	//作业实体初始化
	public WorkStep getWorkStep(StoreHouse sh,String rebotCode,String orderNo,String  toStoreId){
		WorkStep ws=new WorkStep();
		String workId = UUID.randomUUID().toString().replaceAll("-", "");
		ws.setWorkId(workId);
		ws.setCount(sh.getCount());
		ws.setFringeCode(sh.getGoodNo());
		//ws.setGetPlace(sh.getStoreNo());
		ws.setGetPlace(sh.getStoreId());
		ws.setOrderId(sh.getOrderId());
		//StoreHouse tosh = this.storeHouseDao.selectByStoreId(toStoreId);
		//ws.setPutPlace(tosh.getStoreNo());
		ws.setPutPlace(toStoreId);
		ws.setWorkType(3);
		ws.setWorkStatue(0);
		//System.out.println("调库");

		ws.setRebotCode(rebotCode);
		ws.setOrderNo(orderNo);
		ws.setInsertTime(new Date());
		ws.setInputStoreTime(new Date());
		return ws;
	}

	public String getNo(){
		Date now =new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(now);
	}
	//增加调库单
	@Transactional
	@Override
	public Map<String, Object> insertChangeStore(String storeIdFrom, String storeIdTo) {
		Map<String, Object> result=new HashMap<String, Object>();
		StoreHouse sh = storeHouseDao.selectByStoreId(storeIdFrom);
		StoreHouse tosh = storeHouseDao.selectByStoreId(storeIdTo);
		ChangeStore cs=new ChangeStore();
		cs.setBarCode(sh.getGoodNo());
		cs.setChangeId(UUID.randomUUID().toString().replaceAll("-", ""));
		cs.setChangeNo(getNo());
		cs.setChangeTime(new Date());
		cs.setStatue(0);
		cs.setStoreIdIng(null);
		cs.setStoreIdTo(storeIdTo);
		cs.setStoreIdFrom(storeIdFrom);
		sh.setStoreStatue(1);//仓库状态改成预占用
		tosh.setStoreStatue(1);
		try{
			changeStoreDao.insertChangeStore(cs);
			storeHouseDao.updateStoreHouse(tosh);
			storeHouseDao.updateStoreHouse(sh);
			result.put("code",200);
		}catch(Exception e){
			e.printStackTrace();
			result.put("code",500);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		}
		return result;
	}
	//查询调库单列表
	@Override
	public Map<String, Object> searchChangeStoreOrderList(Integer status) {
		Map<String, Object> result=new HashMap<String, Object>();
		try{
			List<Map<String, Object>> list = changeStoreDao.selectByStatue(status);
			if(list!=null && list.size()>0){
				result.put("list",list);
			}
			result.put("code",200);
		}catch(Exception e){
			e.printStackTrace();
			result.put("code",500);
		}
		return result;
	}
	//删除调库单
	@Transactional
	@Override
	public Map<String, Object> delChangeStore(String changeId) {
		Map<String, Object> result=new HashMap<String, Object>();
		try{
			ChangeStore cs = changeStoreDao.selectChangeStoreByChangeId(changeId);
			cs.setChangeId(changeId);
			cs.setStatue(5);//出库单状态更改为删除
			changeStoreDao.updateCSByCSIdSelective(cs);
			StoreHouse fromsh = this.storeHouseDao.selectByStoreId(cs.getStoreIdFrom());
			StoreHouse tosh = this.storeHouseDao.selectByStoreId(cs.getStoreIdTo());
			fromsh.setStoreStatue(2);//恢复仓位的状态
			tosh.setStoreStatue(2);
			storeHouseDao. updateStoreHouse(fromsh);
			storeHouseDao. updateStoreHouse(tosh);
			result.put("code",200);
		}catch(Exception e){
			e.printStackTrace();
			result.put("code",500);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		}
		return result;
	}
}

