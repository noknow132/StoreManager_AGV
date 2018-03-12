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

import com.dao.IConfigParamDao;
import com.dao.ICreateStoreHouseDao;
import com.dao.IOutputStoreDao;
import com.dao.IStoreHouseDao;
import com.dao.IWorkStepDao;
import com.entity.CreateStoreHouse;
import com.entity.OutputStore;
import com.entity.OutputStoreDto;
import com.entity.StoreHouse;
import com.entity.WorkStep;
import com.service.IOutputStoreService;


/**
 * @author 罗欢欢
 * @date 2018-1-4
 * @remark 出库对应的业务逻辑实现
 */
@Service
public class OutputStoreServiceImpl implements IOutputStoreService {

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
	
	//查询仓库中有货物的商品信息
	@Override
	public Map<String, Object> searchOutputStoreHouseList(String search) {
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

	//出库处理
	@Transactional
	@Override
	public Map<String, Object> addOutputStores(OutputStoreDto[] opds) {
		Map<String, Object> result=new HashMap<String,Object>();
		if (opds != null) {
			try{
			List<OutputStore> osList=new ArrayList<OutputStore>();
			List<WorkStep> wsList=new ArrayList<WorkStep>();
			int i=0;
			//ConfigParam cp = configParamDao.selectConfigParamOne();
			for (OutputStoreDto opd : opds) {
				StoreHouse sh = storeHouseDao.selectByStoreId(opd.getStoreId());
				sh.setStoreStatue(1);
				CreateStoreHouse cs = createStoreHouseDao.selectCreateStoreHouseById(sh.getCreatestorehouseId());
				OutputStore os =getOutputStore(sh,i,opd.getOutletId());//写入出货单
				osList.add(os);
				i++;
				//outputStoreDao.insertOutputStore(os);
				WorkStep ws = getWorkStep(sh,cs.getRobotNo(),os.getOutputStoreNo());
				wsList.add(ws);
				//workStepDao.insertWorkStep(ws);
				storeHouseDao.updateStoreHouse(sh);
			}
			outputStoreDao.insertOutputStores(osList);
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

	//出库单初始化
	public OutputStore getOutputStore(StoreHouse sh,Integer i,String letoutId){
		String createstorehouseId = sh.getCreatestorehouseId();
		OutputStore os =new OutputStore();
		String osId = UUID.randomUUID().toString().replaceAll("-", "");
		os.setOutputStoreId(osId);
		os.setCreatestorehouseId(createstorehouseId);	
		os.setBarCode(sh.getGoodNo());
		os.setCount(sh.getCount());
		os.setOrderId(sh.getOrderId());
		os.setOutputStoreNo(getNo()+i);
		os.setOutputTime(new Date());
		os.setStoreId(sh.getStoreId());
		os.setUnit(sh.getUnit());
		os.setOutletId(letoutId);
		os.setStatue(0);
		return os;

	}

	//作业实体初始化
	public WorkStep getWorkStep(StoreHouse sh,String rebotCode,String orderNo){
		WorkStep ws=new WorkStep();
		String workId = UUID.randomUUID().toString().replaceAll("-", "");
		ws.setWorkId(workId);
		ws.setCount(sh.getCount());
		ws.setFringeCode(sh.getGoodNo());
		//ws.setGetPlace(sh.getStoreNo());
		ws.setGetPlace(sh.getStoreId());
		ws.setOrderId(sh.getOrderId());
		//ws.setOutputStoreTime(outputStoreTime);
		 ws.setInputStoreTime(new Date());	
		 ws.setWorkType(1);
		 ws.setWorkStatue(0);
		// System.out.println("出库");

		 ws.setInsertTime(new Date());
		 ws.setRebotCode(rebotCode);
		 ws.setOrderNo(orderNo);
		 return ws;

	}

	public String getNo(){
		Date now =new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(now);
	}

}

