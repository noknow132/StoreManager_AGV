package com.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dao.IOutinPlaceDao;
import com.entity.OutinPlace;
import com.service.IOutinPlaceService;

/**
 * @author 罗欢欢
 * @date 2018-3-17
 * @remark 出入口配置对应的业务逻辑实现
 */
@Service
public class OutinPlaceImpl implements IOutinPlaceService {
	@Resource
	private IOutinPlaceDao outinPlaceDao;

	@Override
	public Map<String, Object> searchOutinPlaceByType(Integer type) {
		Map<String, Object>  result=new HashMap<String, Object>();
		List<OutinPlace> list = outinPlaceDao.selectOutinPlaceByType(type);
		if(list!=null&&list.size()>0){
			result.put("list", list);
		}else{
			result.put("code", 404);
		}
		return result;
	}
}
