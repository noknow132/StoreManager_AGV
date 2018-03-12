/**  
 * Project Name:FaceDistinguish  
 * File Name:TempServiceImpl.java  
 * Package Name:com.service.impl  
 * Date:2017年11月30日上午10:06:10  
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.  
 *  
*/  
  
package com.service.impl;


import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dao.ITempDao;
import com.entity.Temp;
import com.service.ITempService;




/**  
 * ClassName:TempServiceImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2017年11月30日 上午10:06:10 <br/>  
 * @author   Administrator  
 * @version    
 * @since    JDK 1.6  
 * @see        
 */
@Service
public class TempServiceImpl implements ITempService {
	
	@Resource
	private ITempDao tempDao;
	
	public boolean insert(Temp record) {
		record.setId(UUID.randomUUID().toString().replace("-", ""));
		return tempDao.insert(record)>0;
	}

	public boolean insertSelective(Temp record) {
		record.setId(UUID.randomUUID().toString().replace("-", ""));
		return tempDao.insertSelective(record)>0;
	}
	
	public List<Temp> getAllByDb() {
		return tempDao.getAllByDb();
	}
}
  
