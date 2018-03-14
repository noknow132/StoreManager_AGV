package com.service;

import java.util.Map;

public interface  IOutinPlaceService {
	
	/**
	 * 根据类型查找出入口
	 * @param type 类型
	 * @return
	 */
	Map<String, Object> searchOutinPlaceByType(Integer type);

}
