package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dao.IStoreHouseRecordDao;
import com.service.IStoreHouseRecordService;
@Service
public class StoreHouseRecordServiceImpl implements IStoreHouseRecordService{
	@Resource
	private IStoreHouseRecordDao storeHouseRecordDao;
    //查询仓库的历史记录
	@Override
	public Map<String, Object> searchStoreHouseRecord(String storeId,String search,String operateType) {
		Map<String, Object> result =new HashMap<String, Object>();
		 Map<String, Object> params =new HashMap<String ,Object>();

		try {

	
			 if(search != "" && search != null) {
				 search+=";";
				 List<String> seachStr = new ArrayList<String>();
				 String[] str = search.replace("，", ";").replace(",", ";").replace("；", ";").split(";");
				 for(String s : str) {
					 if(s.trim() != "" && s!=null) {
						 seachStr.add(s); 
					 }
				 }
				 params.put("searchList", seachStr);
			 }
			 params.put("storeId", storeId);
			 params.put("operateType", operateType);
		List<Map<String, Object>> list = storeHouseRecordDao.selectStoreHouseRecordByStoreIdMap(params);
		if(list!=null&&list.size()>0){
			result.put("list", list);
			result.put("code", 200);
		}
		}catch (Exception e) {
			e.printStackTrace();
			result.put("code", 500);
		}
		return result;
	}
	
	//批量删除历史记录
	@Override
	public Map<String, Object> delStoreHouseRecord(String ids) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String,Object> params=new HashMap<String, Object>();
		if (ids != null) {
			try {
				List<String> idList = new ArrayList<String>();
				if (ids != null) {
					String[] strArr = ids.split(",");
					for (String string : strArr) {
						idList.add(string);
					} 
				}
				params.put("ids", idList);
				/*params.put("messageGroupId", messageGroupId);*/
				storeHouseRecordDao.deleteStoreHouseRecordByIds(params);
				result.put("code", 200);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("code", 500);
			}
		} else {
			result.put("code", 404);
		}
		return result;
	}

}
