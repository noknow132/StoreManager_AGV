/**  
 * Project Name:FaceDistinguish  
 * File Name:TempServiceImpl.java  
 * Package Name:com.service.impl  
 * Date:2017年11月30日上午10:06:10  
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.  
 *  
 */  

package com.service.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dao.IOutLetDao;
import com.entity.OutLet;
import com.service.IOutLetService;


/**
 * @author 罗欢欢
 * @date 2018-1-12
 * @remark 出口对应的业务逻辑实现
 */
@Service
public class OutLetServiceImpl implements IOutLetService {

	@Resource
	private IOutLetDao outLetDao;
	
	//查询建库信息
	@Override
	public Map<String, Object> searchOutLetByType(Integer type) {
		Map<String, Object> result=new HashMap<String, Object> ();
		try{
		List<OutLet> list= outLetDao.selectOutLetByType(type);
		if(list!=null){
			result.put("list", list);
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
	
	// 查询所有出口
	@Override
	public List<Map<String,Object>> searchOutLet(String outType) {
		return outLetDao.searchOutLet(outType);
	}

	// 操作出口
	@Override
	public Map<String, Object> saveOutLet(String outletName,Integer outNo,Integer outType,String outletId) {
		Map<String, Object> result = new HashMap<String, Object> ();
		try {
			if(outletId == null || outletId.equals("")){
				//添加
				int count = outLetDao.searchOutLetCountByOutNo(outNo,outType);
				if(count == 0){
				OutLet ol = new OutLet();
				ol.setOutletId(UUID.randomUUID().toString().replaceAll("-", ""));
				ol.setCreateTime(new Date());
				ol.setIsUesd(1);
				ol.setOutletName(outletName);
				ol.setOutNo(outNo);
				ol.setOutType(outType);
				outLetDao.insertOutLet(ol);
				}else{
					result.put("stus", 300);
					return result;
				}
			}else{
				//修改
				OutLet outLet = outLetDao.searchOutLetById(outletId);
				
				int count = outLetDao.searchOutLetCountByOutNo(outNo,outType);
				if((count>0 && outLet.getOutNo() == outNo) || count == 0){
					outLet.setOutletName(outletName);
					outLet.setOutNo(outNo);
					outLet.setOutType(outType);
					outLetDao.updateOutLet(outLet);
				}else{
					result.put("stus", 300);
					return result;
				}
				
			}
			
			result.put("stus", 200);
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
		}		
		return result;
	}

	//启用或禁用出口
	@Override
	public Map<String, Object> updateOutLetIsUsed(String isUsed,String outletId) {
		Map<String, Object> result = new HashMap<String, Object> ();
		try {
			OutLet outLet = outLetDao.searchOutLetById(outletId);
			outLet.setIsUesd(Integer.parseInt(isUsed));
			outLetDao.updateOutLet(outLet);
			result.put("stus", 200);
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
		}
		return result;
	}

	//根据id查找出口
	@Override
	public OutLet searchOutLetById(String outletId) {
		return outLetDao.searchOutLetById(outletId);
	}


	
	
	//编辑仓库


}

