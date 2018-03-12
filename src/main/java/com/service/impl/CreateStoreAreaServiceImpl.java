package com.service.impl;

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

import com.dao.ICreateStoreAreaDao;
import com.dao.ICreateStoreHouseDao;
import com.dao.IStoreHouseDao;
import com.entity.CreateStoreArea;
import com.entity.CreateStoreHouse;
import com.entity.StoreHouse;
import com.service.ICreateStoreAreaService;

@Service
public class CreateStoreAreaServiceImpl implements ICreateStoreAreaService{
	@Autowired
	private ICreateStoreAreaDao createStoreAreaDao;
	@Autowired
	private ICreateStoreHouseDao createStoreHouseDao;
	@Autowired
	private IStoreHouseDao storeHouseDao;

	//新增区位
	@Override
	@Transactional
	public Map<String, Object> addCreateStoreArea(CreateStoreArea createStoreArea, String userId) {
		Map<String, Object> result=new HashMap<String, Object>();
		String csaId = UUID.randomUUID().toString().replaceAll("-", "");
		createStoreArea.setCreatestoreareaId(csaId);
		createStoreArea.setCreatestorehouseId(createStoreArea.getCreatestorehouseId());
		createStoreArea.setCreateTime(new Date());
		createStoreArea.setCreator(userId);
		String areaName = createStoreArea.getAreaName().toUpperCase()+"1";//区位名称
		createStoreArea.setAreaName(areaName);
		Integer columnsStart = createStoreArea.getColumnsStart()!=null?createStoreArea.getColumnsStart():createStoreArea.getRowsStart();//列起始值
		Integer columnsCount = createStoreArea.getColumnsCount(); //列数
		Integer rowsCount = createStoreArea.getRowsCount();//行数
		Integer rowsStart = createStoreArea.getRowsStart()==null?createStoreArea.getColumnsStart():createStoreArea.getRowsStart();//行起始值
		Integer sequence = createStoreArea.getSequence();	//行列初始顺序
		Integer startLength=(sequence==0? rowsCount:columnsCount);//外层循环的次数
		Integer nextLength=(sequence==0? columnsCount:rowsCount);//内层循环的次数
		Integer startStart=(sequence==0? rowsStart:columnsStart);//外层开始值
		Integer nextStart=(sequence==0? columnsStart:rowsStart);//内层开始值
		createStoreArea.setColumnsStart(columnsStart);
		createStoreArea.setRowsStart(rowsStart);
		try{
//			 CreateStoreHouse csh = createStoreHouseDao.selectCreateStoreHouseByAreaName(areaName);
//             if(csh==null){
            createStoreAreaDao.insertCreateStoreArea(createStoreArea); //保存区位
			List<StoreHouse> list=new ArrayList<StoreHouse>();
			for(int i=0;i<startLength;i++){
				Integer nextStart2=nextStart;
				for(int j=0;j<nextLength;j++){
					//nextStart2=nextStart2+j;
					String storeNo=areaName+""+(startStart>9?startStart:"0"+startStart)+(nextStart2>9?nextStart2:"0"+nextStart2);
					StoreHouse sh = initStoreHouse(createStoreArea.getCreatestorehouseId(),storeNo,csaId);
					//StoreHouseDao.insertStoreHouse(sh);
					list.add(sh);
					nextStart2++;
				}
				startStart++;
			}
			storeHouseDao.insertStoreHouses(list);
//            }
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
	 * @param csaId 区位id
	 * @return 仓库
	 */
	public StoreHouse initStoreHouse(String cshId,String storeNo,String csaId ){
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
		sh.setCreatestoreareaId(csaId);
		return sh;
	}

	//检查区位是否存在
	@Override
	public Map<String, Boolean> checkAreaNameExist(String areaName, String createstorehouseId) {
		areaName=areaName.toUpperCase()+"1";
		Map<String,Boolean> map = new HashMap<String,Boolean>();
		if(createstorehouseId.equals("")){
			//新增区位时的验证
			CreateStoreArea createStoreArea = createStoreAreaDao.checkAreaNameExist(createstorehouseId, areaName);
			if(createStoreArea == null){
			map.put("valid", true);
		}else{
			map.put("valid", false);
			}
		}else{
			//编辑区位时的验证
			CreateStoreArea createStoreArea = createStoreAreaDao.checkAreaNameExist(createstorehouseId, areaName);
			if(createStoreArea == null || createStoreArea.getAreaName().equals(areaName)){
				map.put("valid", true);
			}else{
				map.put("valid", false);
			}
		}
		
		return map;
	}

	//根据建库 查找区位
	@Override
	public  List<Map<String,Object>> searchCreateStoreAreaByCreateStoreHouseId(String CreateStoreHouseId) {
		return createStoreAreaDao.searchCreateStoreAreaByCreateStoreHouseId(CreateStoreHouseId);
	}

	//根据区位id 查询区位信息
	@Override
	public CreateStoreArea selectCreateStoreAreaById(String createStoreAreaId) {
		return createStoreAreaDao.selectCreateStoreAreaById(createStoreAreaId);
	}

}
