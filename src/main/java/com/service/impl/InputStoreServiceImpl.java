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

import com.dao.ICreateStoreHouseDao;
import com.dao.IInputStoreDao;
import com.dao.IStoreHouseDao;
import com.dao.IWorkStepDao;
import com.entity.CreateStoreHouse;
import com.entity.InputStore;
import com.entity.StoreHouse;
import com.entity.WorkStep;
import com.service.IInputStoreService;

@Service
public class InputStoreServiceImpl implements IInputStoreService{
	@Autowired
	private IInputStoreDao inputStoreDao;
	@Autowired
	private IStoreHouseDao storeHouseDao;
	@Autowired
	private IWorkStepDao workStepDao;
	@Autowired
	private ICreateStoreHouseDao createStoreHouseDao;
	
	//入库添加
	@Override
	@Transactional
	public Map<String, Object> insertGoods(InputStore inputStore) {
		Map<String,Object> result = new HashMap<String,Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		StoreHouse sh = this.storeHouseDao.selectByStoreId(inputStore.getStoreId());
		CreateStoreHouse cs = createStoreHouseDao.selectCreateStoreHouseById(sh.getCreatestorehouseId());//根据仓库id查找建库
		try {			
			inputStore.setInputStoreId(UUID.randomUUID().toString().replaceAll("-", ""));
			inputStore.setOrderId(sdf.format(new Date()));
			inputStore.setInputStoreNo(sdf.format(new Date()));//入库单号
			inputStore.setCreatestorehouseId(cs.getCreatestorehouseId());//建库id
			inputStore.setStoreId(inputStore.getStoreId());//仓库id
			inputStore.setPlaceId(inputStore.getPlaceId());//取货台id
			inputStore.setBarCode(inputStore.getBarCode());//条形码，货物码
			inputStore.setCount(inputStore.getCount());//数量
			inputStore.setUnit("箱");//单位
			inputStore.setInputTime(new Date());//入库时间
			inputStore.setStatue(0);//单据状态
			inputStoreDao.insertGoods(inputStore);
			
			/**当前仓位改为预占用**/
			StoreHouse storeHouse = storeHouseDao.selectByStoreId(inputStore.getStoreId());
			storeHouse.setStoreStatue(12); //预占用无货
			storeHouseDao.updateStoreHouse(storeHouse);
			result.put("stus", "200");
		} catch (Exception e) {
			result.put("stus", "500");
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}		
		return result;
	}

	//批量删除入库作业
	@Override
	@Transactional
	public Map<String, Object> deleteManyInputStoreById(String[] ids) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			InputStore inputStore = null;
			StoreHouse storeHouse = null;
			//取消仓库预占用
			for (int i = 0; i < ids.length; i++) {
				inputStore = inputStoreDao.searchInpuStoreById(ids[i]);
				storeHouse = storeHouseDao.selectByStoreId(inputStore.getStoreId());
				storeHouse.setStoreStatue(0);//仓库修改为空
				storeHouseDao.updateStoreHouse(storeHouse);
				//修改入库为删除
				inputStore.setStatue(5);
				inputStoreDao.updateInputStore(inputStore);
			}
//			inputStoreDao.deleteManyInputStoreById(ids);//删除入库
			
			result.put("stus", "200");
		} catch (Exception e) {
			result.put("stus", "500");
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}	
		return result;
	}

	//查询入库作业
	@Override
	public Map<String, Object> searchInputStore(String status) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("list", inputStoreDao.searchInputStore(status));
		return result;
	}

	//执行入库作业
	@Override
	@Transactional
	public Map<String, Object> startInputStore(String[] ids) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			List<WorkStep> wsList=new ArrayList<WorkStep>();
			for (int i = 0; i < ids.length; i++) {
				InputStore inputStore = inputStoreDao.searchInpuStoreById(ids[i]);//根据id查询入库
				inputStore.setStatue(1);//入库确认待执行
				inputStoreDao.updateInputStore(inputStore);//修改入库
				//添加到作业表				
				StoreHouse storeHouse = storeHouseDao.selectByStoreId(inputStore.getStoreId());//查找当前入库商品的仓库
				WorkStep ws=new WorkStep();
				ws.setWorkId(UUID.randomUUID().toString().replaceAll("-", ""));
				ws.setOrderId(inputStore.getOrderId());
				ws.setWorkType(0);
				ws.setOrderNo(inputStore.getInputStoreNo());
				ws.setWorkStatue(0);
				//System.out.println("入库");

				ws.setFringeCode(inputStore.getBarCode());
				ws.setCount(inputStore.getCount());
				ws.setPutPlace(storeHouse.getStoreId());//放货位是仓库
				ws.setGetPlace(inputStore.getPlaceId());//放货位是仓库
				ws.setRebotCode("机器码");
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

	//检查条形码是否存在
	@Override
	public int checkBarCode(String barCode) {
		return inputStoreDao.checkBarCode(barCode);
	}

}
