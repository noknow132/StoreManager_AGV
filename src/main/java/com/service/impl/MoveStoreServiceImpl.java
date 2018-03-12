package com.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.dao.IMoveStoreDao;
import com.dao.IStoreHouseDao;
import com.dao.IWorkStepDao;
import com.entity.MoveStore;
import com.entity.StoreHouse;
import com.entity.WorkStep;
import com.service.IMoveStoreService;

@Service
public class MoveStoreServiceImpl implements IMoveStoreService{
	@Autowired
	private IMoveStoreDao moveStoreDao;
	@Autowired
	private IStoreHouseDao storeHouseDao;
	@Autowired
	private IWorkStepDao workStepDao;
	
	//添加移库
	@Override
	@Transactional
	public Map<String, Object> insertMoveStore(String storeFromId, String storeToId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			/**当前仓位改为预占用**/
			StoreHouse storeHouseTo = storeHouseDao.selectByStoreId(storeToId);
			storeHouseTo.setStoreStatue(1);//仓库预占用
			storeHouseDao.updateStoreHouse(storeHouseTo);
			
			StoreHouse storeHouseFrom = storeHouseDao.selectByStoreId(storeFromId);
			storeHouseFrom.setStoreStatue(1);//仓库预占用
			storeHouseDao.updateStoreHouse(storeHouseFrom);
			
			MoveStore moveStore = new MoveStore();
			moveStore.setMoveId(UUID.randomUUID().toString().replaceAll("-", ""));
			moveStore.setMoveNo(sdf.format(new Date()));//移库单号
			moveStore.setOrderId(storeHouseFrom.getOrderId());//订单号
//			moveStore.setOrderDetailId("");//订单详情
			moveStore.setBarCode(storeHouseFrom.getGoodNo());//条形码/货物码
			moveStore.setStoreIdFrom(storeFromId);//起始仓库
			moveStore.setStoreIdTo(storeToId);//目标仓库
			moveStore.setMoveTime(new Date());//移库时间
			moveStore.setStatus(0);//单据状态 0为待确认
			moveStoreDao.insertMoveStore(moveStore);
			
			
			
			result.put("stus", "200");
		} catch (Exception e) {
			result.put("stus", "500");
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}	
		return result;
	}

	//执行移库作业
	@Override
	@Transactional
	public Map<String, Object> startMoveStore(String[] moveIds) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			List<WorkStep> wsList=new ArrayList<WorkStep>();
			StoreHouse fromStoreHouse = null;
			StoreHouse ToStoreHouse = null;
			for (int i = 0; i < moveIds.length; i++) {
				MoveStore moveStore = moveStoreDao.searchMoveStoreById(moveIds[i]);
				moveStore.setStatus(1);//已确认待执行
				moveStoreDao.updateMoveStore(moveStore);
					
				fromStoreHouse = storeHouseDao.selectByStoreId(moveStore.getStoreIdFrom());//起始仓库(有货)	
				ToStoreHouse = storeHouseDao.selectByStoreId(moveStore.getStoreIdTo());//目标仓库(无货)
				
				//添加到作业表
				WorkStep ws=new WorkStep();
				ws.setWorkId(UUID.randomUUID().toString().replaceAll("-", ""));
				ws.setOrderId(moveStore.getOrderId());
				ws.setWorkType(2);//作业类型 移库
				ws.setOrderNo(moveStore.getMoveNo());//单号
				ws.setWorkStatue(0);//未执行状态
				//System.out.println("移库");

				ws.setFringeCode(moveStore.getBarCode());//条形码
				ws.setCount(fromStoreHouse.getCount());
				
				ws.setPutPlace(ToStoreHouse.getStoreId());//放货位
				ws.setGetPlace(fromStoreHouse.getStoreId());//取货位
				
				ws.setRebotCode("机器码移库"+i);
				ws.setInsertTime(new Date());
				wsList.add(ws);
			}
			workStepDao.insertWorkSteps(wsList);//保存作业表
			result.put("stus", "200");
		} catch (Exception e) {
			result.put("stus", "500");
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}	
		return result;
	}

	//删除移库
	@Override
	@Transactional
	public Map<String, Object> deleteMoveStore(String moveId) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			MoveStore moveStore = moveStoreDao.searchMoveStoreById(moveId);
			moveStore.setStatus(5);//删除移库
			moveStoreDao.updateMoveStore(moveStore);
			
			StoreHouse storeHouseTo = storeHouseDao.selectByStoreId(moveStore.getStoreIdTo());
			storeHouseTo.setStoreStatue(0);//仓库改为空
			storeHouseDao.updateStoreHouse(storeHouseTo);
			
			StoreHouse storeHouseFrom = storeHouseDao.selectByStoreId(moveStore.getStoreIdFrom());
			storeHouseFrom.setStoreStatue(2);//仓库改为占用
			storeHouseDao.updateStoreHouse(storeHouseFrom);
			
			result.put("stus", "200");
		} catch (Exception e) {
			result.put("stus", "500");
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

	//查询预移库
	@Override
	public List<Map<String, Object>> searchtMoveStore(String status) {
		return moveStoreDao.searchMoveStore(status);
	}

}
