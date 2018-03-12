package com.dao;

import com.entity.OrderDetail;

public interface IOrderDetailDao {
	/**
	 * 根据id查找订单详情
	 * @param orderDetailId 订单详情id
	 * @return
	 */
	OrderDetail searchOrderDetailById(String orderDetailId);
}