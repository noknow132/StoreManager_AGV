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
import com.dao.ICreateStoreAreaDao;
import com.dao.ICreateStoreHouseDao;
import com.dao.IInputStoreDao;
import com.dao.IMoveStoreDao;
import com.dao.IOutputStoreDao;
import com.dao.IRunStepRecordDao;
import com.dao.IStoreHouseDao;
import com.dao.IStoreHouseRecordDao;
import com.dao.IUserInfoDao;
import com.dao.IWorkStepDao;
import com.entity.CreateStoreArea;
import com.entity.CreateStoreHouse;
import com.entity.StoreHouse;
import com.entity.StoreHouseRecord;
import com.entity.UserInfo;
import com.mysql.fabric.xmlrpc.base.Array;
import com.service.ICreateStoreHouseService;


/**
 * @author 罗欢欢
 * @date 2018-1-4
 * @remark 建库对应的业务逻辑实现
 */
@Service
public class CreateStoreHouseServiceImpl implements ICreateStoreHouseService {

	@Resource
	private ICreateStoreHouseDao createStoreHouseDao;
	@Resource
	private IStoreHouseDao StoreHouseDao;
	@Resource
	private  IUserInfoDao userInfoDao;
	@Resource
	private  IInputStoreDao inputStoreDao;
	@Resource
	private  IOutputStoreDao outputStoreDao;
	@Resource
	private  IMoveStoreDao moveStoreDao;
	@Resource
	private  IChangeStoreDao changeStoreDao;
	@Resource
	private  IStoreHouseRecordDao storeHouseRecordDao;
	@Resource
	private  IWorkStepDao workStepDao;
	@Resource
	private IRunStepRecordDao runStepRecordDao;
	@Resource
	private ICreateStoreAreaDao createStoreAreaDao;
	
	//新建仓库
/*	@Transactional
*/	
	@Override
	@Transactional
	public Map<String, Object>  addCreateStoreHouse(CreateStoreHouse cStoreHouse,String userId)  {
		cStoreHouse.getCreatestorehouseId();
		
		
		Map<String, Object> result=new HashMap<String, Object>();
		String cshId = UUID.randomUUID().toString().replaceAll("-", "");
		cStoreHouse.setCreatestorehouseId(cshId);
		cStoreHouse.setCreateTime(new Date());
		cStoreHouse.setCreator(userId);
		String areaName = cStoreHouse.getAreaName().toUpperCase()+"1";//区位名称
		cStoreHouse.setAreaName(areaName);
		Integer columnsStart = cStoreHouse.getColumnsStart()!=null?cStoreHouse.getColumnsStart():cStoreHouse.getRowsStart();//列起始值
		Integer columnsCount = cStoreHouse.getColumnsCount(); //列数
		Integer rowsCount = cStoreHouse.getRowsCount();//行数
		Integer rowsStart = cStoreHouse.getRowsStart()==null?cStoreHouse.getColumnsStart():cStoreHouse.getRowsStart();//行起始值
		Integer sequence = cStoreHouse.getSequence();	//行列初始顺序
		Integer startLength=(sequence==0? rowsCount:columnsCount);//外层循环的次数
		Integer nextLength=(sequence==0? columnsCount:rowsCount);//内层循环的次数
		Integer startStart=(sequence==0? rowsStart:columnsStart);//外层开始值
		Integer nextStart=(sequence==0? columnsStart:rowsStart);//内层开始值
		cStoreHouse.setColumnsStart(columnsStart);
		cStoreHouse.setRowsStart(rowsStart);
		try{
			 CreateStoreHouse csh = createStoreHouseDao.selectCreateStoreHouseByAreaName(areaName);
             if(csh==null){
			createStoreHouseDao.insertCreateStoreHouse(cStoreHouse); 
			List<StoreHouse> list=new ArrayList<StoreHouse>();
			for(int i=0;i<startLength;i++){
				Integer nextStart2=nextStart;
				for(int j=0;j<nextLength;j++){
					//nextStart2=nextStart2+j;
					String storeNo=areaName+""+(startStart>9?startStart:"0"+startStart)+(nextStart2>9?nextStart2:"0"+nextStart2);
					StoreHouse sh = initStoreHouse(cshId,storeNo);
					//StoreHouseDao.insertStoreHouse(sh);
					list.add(sh);
					nextStart2++;
				}
				startStart++;
			}
			StoreHouseDao.insertStoreHouses(list);
            }
			result.put("code", 200);
		}catch(Exception e){
			e.printStackTrace();
			result.put("code", 500);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
          return result;
	}
	/**
	 * 初始化仓库
	 * @param cshId 建库实体id
	 * @param storeNo 库位码
	 * @return 仓库
	 */
	public StoreHouse initStoreHouse(String cshId,String storeNo ){
		String shId = UUID.randomUUID().toString().replaceAll("-", "");
		StoreHouse sh=new StoreHouse();
		sh.setCount(0);
		sh.setCreatestorehouseId(cshId);
		sh.setStoreStatue(0);//仓库状态  0 是未占用
		sh.setGoodNo(null);
		sh.setOrderDetailId(null);
		sh.setStoreId(shId);
		sh.setStoreNo(storeNo);
		sh.setOrderId(null);
		return sh;
	}
	//查询建库信息
	@Override
	public Map<String, Object> searchCreateStoreHouse() {
		Map<String, Object> result=new HashMap<String, Object>();
		try{
			CreateStoreHouse cs = createStoreHouseDao.selectCreateStoreHouseOne();
			
			if(cs!=null){
				String creatorId = cs.getCreator();
				UserInfo creator = userInfoDao.searchUserInfoById(creatorId);
				if(creator!=null){
					cs.setCreator(creator.getUserName());
				}else{
					cs.setCreator("");
				}
				result.put("cs", cs);
				result.put("total", 1);
			}else{
				result.put("total", 0);
			}
		}catch(Exception e){
			result.put("code", 500);
			e.printStackTrace();
		}
		return result;
	}
	//编辑仓库
	@Override
	public Map<String, Object> editCreateStoreHouse(CreateStoreHouse cStoreHouse) {
		Map<String, Object> result=new HashMap<String, Object>();
		try{
			CreateStoreHouse createStoreHouse = createStoreHouseDao.selectCreateStoreHouseById(cStoreHouse.getCreatestorehouseId()) ;
			createStoreHouse.setStoreName(cStoreHouse.getStoreName());
			createStoreHouse.setStoreType(cStoreHouse.getStoreType());
			createStoreHouse.setStoreMaster(cStoreHouse.getStoreMaster());
			createStoreHouse.setMasterTel(cStoreHouse.getMasterTel());
			createStoreHouse.setStoreAddress(cStoreHouse.getStoreAddress());
			createStoreHouseDao.updateCreateStoreHouse(createStoreHouse);
			result.put("code", 200);
		}catch(Exception e){
			result.put("code", 500);
			e.printStackTrace();
		}
		return result;
	}
	//查找所有的区位名称
	@Override
	public Map<String, Object> searchAreaNames() {
		Map<String, Object> result=new HashMap<String, Object>();
//		Map<String, Object> params=new HashMap<String, Object>();
		try{
			List<Map<String,Object>> list=createStoreHouseDao.selectAreaNames();
			if(list!=null&& list.size()>0){
				result.put("list", list);
			}else{
				result.put("total", 0);
			}
		}catch(Exception e){
			result.put("code", 500);
			e.printStackTrace();
		}
		return result;		
	}

	@Override
	public Map<String, Object> searchCreateStoreHouseByCshId(String createstorehouseId) {
		
		return null;
	}
	//根据区位查找建库信息
	@Override
	public Map<String, Object> searchCreateStoreHouseByAreaName(String areaName) {
		Map<String, Object> result=new HashMap<String, Object>();
		try{
			CreateStoreHouse cs = createStoreHouseDao.selectCreateStoreHouseByAreaName(areaName);
			if(cs!=null){
				String creatorId = cs.getCreator();
				UserInfo creator = userInfoDao.searchUserInfoById(creatorId);
				if(creator!=null){
					cs.setCreator(creator.getUserName());
				}else{
					cs.setCreator("");
				}
				result.put("cs", cs);
				result.put("total", 1);
			}else{
				result.put("total", 0);
			}
		}catch(Exception e){
			result.put("code", 500);
			e.printStackTrace();
		}
		return result;	
	}
	//检查区位是否存在
	@Override
	public Map<String, Boolean> checkAreaNameExist(String areaName,String createstorehouseId) {
		areaName=areaName.toUpperCase()+"1";
		Map<String,Boolean> map = new HashMap<String,Boolean>();
		if(createstorehouseId.equals("")){
			//新增区位时的验证
			CreateStoreHouse cs = createStoreHouseDao.selectCreateStoreHouseByAreaName(areaName);
			if(cs == null){
			map.put("valid", true);
		}else{
			map.put("valid", false);
			}
		}else{
			//编辑区位时的验证
			CreateStoreHouse cs1 = createStoreHouseDao.selectCreateStoreHouseById(createstorehouseId);
			CreateStoreHouse cs2 = createStoreHouseDao.selectCreateStoreHouseByAreaName(areaName);
			if((cs2 !=null && cs1.getAreaName().equals(areaName)) || cs2 == null){
				map.put("valid", true);
			}else{
				map.put("valid", false);
			}
		}
		
		return map;		
	}
	
	//根据名称分组查询仓库
	@Override
	public List<Map<String,Object>> searchCreateStoreHouseGroupByStoreName() {
		return createStoreHouseDao.searchCreateStoreHouseGroupByStoreName();
	}
	
	//根据建库名称查找建库
	@Override
	public List<CreateStoreHouse> searchCreateStoreHouseByStoreName(String storeName) {
		return createStoreHouseDao.searchCreateStoreHouseByStoreName(storeName);
	}
	
	//仓库基本信息
	@Override
	public Map<String, Object> searchStoreBaseInfo(String createStoreHouseId) {
		return createStoreHouseDao.searchStoreBaseInfo(createStoreHouseId);
	}
	
	//根据建库id查找建库
	@Override
	public CreateStoreHouse selectCreateStoreHouseById(String createStoreId) {
		return createStoreHouseDao.selectCreateStoreHouseById(createStoreId);
	}
	
	//删除区位
	@Override
	@Transactional
	public Map<String, Object> deleteCreateStoreAreaById(String createstoreareaId) {
		Map<String, Object> result=new HashMap<String, Object>();
		try {
			//区位  例如（A1）
			CreateStoreArea createStoreArea = createStoreAreaDao.selectCreateStoreAreaById(createstoreareaId);
			//区位下的仓位
			List<StoreHouse> storeHouseList = StoreHouseDao.searchStoreHouseByCreateStoreAreaId(createstoreareaId);
			//判断仓位里是否有货
			for (int i = 0; i < storeHouseList.size(); i++) {
				if(storeHouseList.get(i).getStoreStatue() != 0){
					//警告：仓库有货！！如果仓库不是空闲状态，说明存在入库，出库，移库，调库订单，只要仓库为空闲，就可以删除 入出调移 库的相关记录
					result.put("stus", 300);
					return result;
				}
			}
			
			for (int i = 0; i < storeHouseList.size(); i++) {
				String storeId = storeHouseList.get(i).getStoreId();
				//当前仓库的入库单的单号
				List<String> inputStoreNoList = inputStoreDao.searchInputStoreNoByStoreId(storeId);
				if(inputStoreNoList.size() != 0){
					workStepDao.deleteWorkStepByOrderNo(inputStoreNoList);//删除入库作业
				}
				
				//当前仓库的出库单号
				List<String> outPutStoreNoList = outputStoreDao.searchOutPutStoreNoByStoreId(storeId);
				if(outPutStoreNoList.size() != 0){
					workStepDao.deleteWorkStepByOrderNo(outPutStoreNoList);//删除出库作业
				}
				
				//当前仓库的移库单号
				List<String> moveStoreNoList = moveStoreDao.searchMoveStoreNoByStoreId(storeId);
				if(moveStoreNoList.size() != 0){
					workStepDao.deleteWorkStepByOrderNo(moveStoreNoList);//删除移库作业
				}
				
				//当前仓库的调库单号
				List<String> changeStoreNoList = changeStoreDao.searchChangeStoreNoByStoreId(storeId);
				if(changeStoreNoList.size() != 0){
					workStepDao.deleteWorkStepByOrderNo(changeStoreNoList);//删除调库作业
				}
				
				//当前仓库的存储记录
				List<StoreHouseRecord> storeHouseRecordList = storeHouseRecordDao.searchStoreHouseRecordByStoreId(storeId);
				
				//删除 运行步骤记录
				for (int j = 0; j < storeHouseRecordList.size(); j++) {
					String orderNo = storeHouseRecordList.get(j).getOrderNo();//单号
					runStepRecordDao.deleteRunStepRecordByOrderNo(orderNo);
				}
				
				inputStoreDao.deleteInputStoreByStoreId(storeId);//删除入库订单
				
				outputStoreDao.deleteOutPutStoreByStoreId(storeId);//删除出库订单
				
				moveStoreDao.deleteMoveStoreByStoreIdFrom(storeId);//删除移库订单
				
				changeStoreDao.deleteChangeStoreByStoreIdFrom(storeId);//删除调库订单
				
				storeHouseRecordDao.deleteStoreHouseRecordByStoreId(storeId);//删除 仓库存储记录
			}
			createStoreAreaDao.deleteCreateStoreAreaById(createstoreareaId);//删除区位
			StoreHouseDao.deleteStoreByCreateStoreAreaId(createstoreareaId);//通过区位id删除所有对应的仓库
			
			result.put("stus", 200);
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}
	
	//编辑区位
	@Override
	@Transactional
	public Map<String, Object> editCreateStoreHouse2(CreateStoreArea createStoreArea) {
		Map<String, Object> result=new HashMap<String, Object>();
		try {
			CreateStoreArea csa = createStoreAreaDao.selectCreateStoreAreaById(createStoreArea.getCreatestoreareaId()); //当前区位
			List<StoreHouse> storeHouseList = StoreHouseDao.searchStoreHouseByCreateStoreAreaId(createStoreArea.getCreatestoreareaId());//当前区位对应的所有仓位
			
			//分析编辑后要创建的仓位
			String cshId = UUID.randomUUID().toString().replaceAll("-", "");
			Integer columnsStart = createStoreArea.getColumnsStart()!=null?createStoreArea.getColumnsStart():createStoreArea.getRowsStart();//列起始值
			Integer columnsCount = createStoreArea.getColumnsCount(); //列数
			Integer rowsCount = createStoreArea.getRowsCount();//行数
			Integer rowsStart = createStoreArea.getRowsStart()==null?createStoreArea.getColumnsStart():createStoreArea.getRowsStart();//行起始值
			Integer sequence = createStoreArea.getSequence();	//行列初始顺序
			Integer startLength=(sequence==0? rowsCount:columnsCount);//外层循环的次数
			Integer nextLength=(sequence==0? columnsCount:rowsCount);//内层循环的次数
			Integer startStart=(sequence==0? rowsStart:columnsStart);//外层开始值
			Integer nextStart=(sequence==0? columnsStart:rowsStart);//内层开始值
			String areaName = createStoreArea.getAreaName();//区位名称
			List<StoreHouse> list=new ArrayList<StoreHouse>();
			for(int m=0;m<startLength;m++){
				Integer nextStart2=nextStart;
				for(int n=0;n<nextLength;n++){
					//nextStart2=nextStart2+j;
					String storeNo=areaName+"1"+(startStart>9?startStart:"0"+startStart)+(nextStart2>9?nextStart2:"0"+nextStart2);
					StoreHouse sh = initStoreHouse(cshId,storeNo);
					list.add(sh);
					nextStart2++;
				}
				startStart++;
			}
			
			
			//当前仓库的仓位号
			List<String> storeNo1 = new ArrayList<String>();
			for (int i = 0; i < storeHouseList.size(); i++) {
				String storeNo = storeHouseList.get(i).getStoreNo();
				
				//根据当前仓位号查找仓位
				StoreHouse sh = StoreHouseDao.selectByStoreNo(storeNo,csa.getCreatestoreareaId());
				
				//修改区位名首字母
				StringBuffer newStoreNo = new StringBuffer(storeNo);
				newStoreNo.setCharAt(0, createStoreArea.getAreaName().toCharArray()[0]);
				sh.setStoreNo(newStoreNo.toString());
				StoreHouseDao.updateStoreHouse(sh);
				storeNo1.add(newStoreNo.toString());
			}
			//预计编辑后仓库的仓位号
			List<String> storeNo2 = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				storeNo2.add(list.get(i).getStoreNo());
			}
			
			//如果行列顺序发生改变 当前仓位号要改变
			List<StoreHouse> saveStoreHouse = new ArrayList<StoreHouse>();
			if(createStoreArea.getSequence() != csa.getSequence()){		
				for (int i = 0; i < storeNo1.size(); i++) {
					String two = storeNo1.get(i).substring(2,4);
					String three = storeNo1.get(i).substring(4,6);
					//根据当前仓位号查找仓位，改变成变化后的仓位号
					StoreHouse sh = StoreHouseDao.selectByStoreNo(storeNo1.get(i),csa.getCreatestoreareaId());
					sh.setStoreNo(areaName+"1" + three + two);
					saveStoreHouse.add(sh);
					storeNo1.set(i, areaName+"1" + three + two);//更新storeNo1List的仓位号
					
				}
				for (int i = 0; i < saveStoreHouse.size(); i++) {
					StoreHouseDao.updateStoreHouse(saveStoreHouse.get(i));
				}
			}
			
			//行列起始值不一样，当前仓库所有的仓位都要改变
			List<StoreHouse> shList = new ArrayList<StoreHouse>();
			if(createStoreArea.getColumnsStart() != csa.getColumnsStart()){
				if(createStoreArea.getColumnsStart() == 0){
					//起始值从0
					for (int i = 0; i < storeNo1.size(); i++) {
						int two = Integer.parseInt(storeNo1.get(i).substring(2,4)) - 1;
						int three = Integer.parseInt(storeNo1.get(i).substring(4,6)) - 1;
						String storeNo=areaName+"1"+(two>9?two:"0"+two)+(three>9?three:"0"+three);
						//根据当前仓位号查找仓位，改变成变化后的仓位号
						StoreHouse sh = StoreHouseDao.selectByStoreNo(storeNo1.get(i),csa.getCreatestoreareaId());
						sh.setStoreNo(storeNo);
						shList.add(sh);
						storeNo1.set(i, storeNo);
						
					}
					//放到list中来循环保存，是因为不能边改变查，否则会出现重复的单号导致查询错误。
					for (int i = 0; i < shList.size(); i++) {
						StoreHouseDao.updateStoreHouse(shList.get(i));
					}
				}else{
					//起始值从1
					for (int i = 0; i < storeNo1.size(); i++) {
						int two = Integer.parseInt(storeNo1.get(i).substring(2,4)) + 1;
						int three = Integer.parseInt(storeNo1.get(i).substring(4,6)) + 1;
						String storeNo=areaName+"1"+(two>9?two:"0"+two)+(three>9?three:"0"+three);
						//根据当前仓位号查找仓位，改变成变化后的仓位号
						StoreHouse sh = StoreHouseDao.selectByStoreNo(storeNo1.get(i),csa.getCreatestoreareaId());
						sh.setStoreNo(storeNo);
						shList.add(sh);
						storeNo1.set(i, storeNo);
					}
					//放到list中来循环保存，是因为不能边改变查，否则会出现重复的单号导致查询错误。
					for (int i = 0; i < shList.size(); i++) {
						StoreHouseDao.updateStoreHouse(shList.get(i));
					}
				}
			}
			
			
			int numNew = rowsCount *columnsCount;//修改后的仓位数目
			int numOld = csa.getRowsCount() * csa.getColumnsCount();//修改前的仓位数目
			
			//判断行列变化后，是否增大减少。
			if(numNew > numOld){
				//要新增仓位
				System.out.println("++++");
				storeNo2.removeAll(storeNo1);
				for (String s : storeNo2) {
					System.out.println(s);
					StoreHouse storeHouse = new StoreHouse();
					storeHouse.setStoreId(UUID.randomUUID().toString().replaceAll("-", ""));
					storeHouse.setStoreNo(s);//库位码
					storeHouse.setCreatestorehouseId(createStoreArea.getCreatestorehouseId());//建库id
					storeHouse.setStoreStatue(0);//仓库状态
					storeHouse.setCreatestoreareaId(csa.getCreatestoreareaId());
					StoreHouseDao.insertStoreHouse(storeHouse);
				}
			}else if(numNew < numOld){
				//要删除仓位
				System.out.println("----");
				storeNo1.removeAll(storeNo2);
				for (String s : storeNo1) {
					System.out.println(s);
					
					//删除仓位对应的信息
					StoreHouse sh = StoreHouseDao.selectByStoreNo(s,csa.getCreatestoreareaId());
					//当前仓库的入库单的单号
					List<String> inputStoreNoList = inputStoreDao.searchInputStoreNoByStoreId(sh.getStoreId());
					if(inputStoreNoList.size() != 0){
						workStepDao.deleteWorkStepByOrderNo(inputStoreNoList);//删除入库作业
						}
					
					//当前仓库的出库单号
					List<String> outPutStoreNoList = outputStoreDao.searchOutPutStoreNoByStoreId(sh.getStoreId());
					if(outPutStoreNoList.size() != 0){
						workStepDao.deleteWorkStepByOrderNo(outPutStoreNoList);//删除出库作业
					}
					
					//当前仓库的移库单号
					List<String> moveStoreNoList = moveStoreDao.searchMoveStoreNoByStoreId(sh.getStoreId());
					if(moveStoreNoList.size() != 0){
						workStepDao.deleteWorkStepByOrderNo(moveStoreNoList);//删除移库作业
					}
					
					//当前仓库的调库单号
					List<String> changeStoreNoList = changeStoreDao.searchChangeStoreNoByStoreId(sh.getStoreId());
					if(changeStoreNoList.size() != 0){
						workStepDao.deleteWorkStepByOrderNo(changeStoreNoList);//删除调库作业
					}
					//当前仓库的存储记录
					List<StoreHouseRecord> storeHouseRecordList = storeHouseRecordDao.searchStoreHouseRecordByStoreId(sh.getStoreId());
					//删除 运行步骤记录
					for (int j = 0; j < storeHouseRecordList.size(); j++) {
						String orderNo = storeHouseRecordList.get(j).getOrderNo();//单号
						runStepRecordDao.deleteRunStepRecordByOrderNo(orderNo);
					}
					
					inputStoreDao.deleteInputStoreByStoreId(sh.getStoreId());//删除入库订单
					outputStoreDao.deleteOutPutStoreByStoreId(sh.getStoreId());//删除出库订单
					moveStoreDao.deleteMoveStoreByStoreIdFrom(sh.getStoreId());//删除移库订单
					changeStoreDao.deleteChangeStoreByStoreIdFrom(sh.getStoreId());//删除调库订单
					storeHouseRecordDao.deleteStoreHouseRecordByStoreId(sh.getStoreId());//删除 仓库存储记录
					StoreHouseDao.deleteStoreByStoreNoAndAreaId(s,csa.getCreatestoreareaId());  //删除当前区位下仓位号的仓库
				}
			}else{
				//不增不减，根据行列顺序变化和起始值变化，更改仓位号
				for (int i = 0; i < storeHouseList.size(); i++) {
					StoreHouse storeHouse = storeHouseList.get(i);
					storeHouse.setStoreNo(storeNo1.get(i));
					StoreHouseDao.updateStoreHouse(storeHouse);
				}
			}
			csa.setAreaName(areaName+"1");
			csa.setRowsCount(rowsCount);
			csa.setColumnsCount(columnsCount);
			csa.setColumnsStart(columnsStart);
			csa.setRowsStart(rowsStart);
			csa.setSequence(sequence);
			csa.setRobotNo(createStoreArea.getRobotNo());
//			createStoreHouseDao.updateCreateStoreHouse(csa);
			createStoreAreaDao.updateCreateStoreArea(csa);
			result.put("stus", 200);
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return result;
	}
	
	//新增仓库
	@Override
	@Transactional
	public Map<String, Object> insertCreateStoreHouse(CreateStoreHouse cStoreHouse, String userId) {
		Map<String, Object> result=new HashMap<String, Object>();
		try {
			CreateStoreHouse storeHouse = new CreateStoreHouse();
			storeHouse.setCreatestorehouseId(UUID.randomUUID().toString().replaceAll("-", ""));
			storeHouse.setStoreName(cStoreHouse.getStoreName());
			storeHouse.setStoreAddress(cStoreHouse.getStoreAddress());
			storeHouse.setStoreType(cStoreHouse.getStoreType());
			storeHouse.setStoreMaster(cStoreHouse.getStoreMaster());
			storeHouse.setMasterTel(cStoreHouse.getMasterTel());
			storeHouse.setCreateTime(new Date());
			storeHouse.setCreator(userId);
			createStoreHouseDao.insertCreateStoreHouse(storeHouse);
			result.put("stus", 200);
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}
	
	//删除仓库
	@Override
	@Transactional
	public Map<String, Object> deleteCreateStoreHouseById(String createstorehouseId) {
		Map<String, Object> result=new HashMap<String, Object>();
		try {
			List<CreateStoreArea> createStoreAreaList = createStoreAreaDao.selectCreateStoreAreaByCreateStoreHouseId(createstorehouseId);//建库下所有的库区
			for (CreateStoreArea createStoreArea : createStoreAreaList) {
				List<StoreHouse> houseList = StoreHouseDao.searchStoreHouseByCreateStoreAreaId(createStoreArea.getCreatestoreareaId());//当前库区下的所有区位
				for (StoreHouse storeHouse : houseList) {
					if(storeHouse.getStoreStatue() != 0){
						result.put("stus", 300);//存在有货，不能删除仓库
						return result;
					}
					String storeId = storeHouse.getStoreId();//仓库id
					
					//当前仓库的入库单的单号
					List<String> inputStoreNoList = inputStoreDao.searchInputStoreNoByStoreId(storeId);
					if(inputStoreNoList.size() != 0){
						workStepDao.deleteWorkStepByOrderNo(inputStoreNoList);//删除入库作业
					}
					
					//当前仓库的出库单号
					List<String> outPutStoreNoList = outputStoreDao.searchOutPutStoreNoByStoreId(storeId);
					if(outPutStoreNoList.size() != 0){
						workStepDao.deleteWorkStepByOrderNo(outPutStoreNoList);//删除出库作业
					}
					
					//当前仓库的移库单号
					List<String> moveStoreNoList = moveStoreDao.searchMoveStoreNoByStoreId(storeId);
					if(moveStoreNoList.size() != 0){
						workStepDao.deleteWorkStepByOrderNo(moveStoreNoList);//删除移库作业
					}
					
					//当前仓库的调库单号
					List<String> changeStoreNoList = changeStoreDao.searchChangeStoreNoByStoreId(storeId);
					if(changeStoreNoList.size() != 0){
						workStepDao.deleteWorkStepByOrderNo(changeStoreNoList);//删除调库作业
					}
					
					//当前仓库的存储记录
					List<StoreHouseRecord> storeHouseRecordList = storeHouseRecordDao.searchStoreHouseRecordByStoreId(storeId);
					
					//删除 运行步骤记录
					for (int j = 0; j < storeHouseRecordList.size(); j++) {
						String orderNo = storeHouseRecordList.get(j).getOrderNo();//单号
						runStepRecordDao.deleteRunStepRecordByOrderNo(orderNo);
					}
					
					inputStoreDao.deleteInputStoreByStoreId(storeId);//删除入库订单
					
					outputStoreDao.deleteOutPutStoreByStoreId(storeId);//删除出库订单
					
					moveStoreDao.deleteMoveStoreByStoreIdFrom(storeId);//删除移库订单
					
					changeStoreDao.deleteChangeStoreByStoreIdFrom(storeId);//删除调库订单
					
					storeHouseRecordDao.deleteStoreHouseRecordByStoreId(storeId);//删除 仓库存储记录
				}
				createStoreAreaDao.deleteCreateStoreAreaById(createStoreArea.getCreatestoreareaId()); //删除当前区位
//				StoreHouseDao.deleteStoreByCreateStoreHouseId(createStoreArea.getCreatestorehouseId());//通过建库id删除所有对应的仓库
			}
			StoreHouseDao.deleteStoreByCreateStoreHouseId(createstorehouseId);//删除当前建库id所有的仓库
			createStoreHouseDao.deleteCreateStoreHouseById(createstorehouseId);//删除仓库
			
			result.put("stus", 200);
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}
	
	//查询已建仓库数目
	@Override
	public int searchCreateStoreHouseAllCount() {
		return createStoreHouseDao.searchCreateStoreHouseAll().size();
	}

}

