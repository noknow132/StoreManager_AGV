package com.entity;

import java.util.List;
/**
 * 分页对象
 * @author 
 *
 * @param <T>
 */
@SuppressWarnings("all")
public class PageBean<T> {

	private int currentPage;// 当前页
	private int pageSize;// 每页显示条数
	private int totalPage;// 总页数
	private int totalRecord;// 总记录数
	private List<T> dataList;// 分页数据

	private PageBean() {
	}

	/**
	 * 初始化PageBean实例
	 */
	private PageBean(final int pageSize, final String page,
			final int totalRecord) {
		// 初始化每页显示条数
		this.pageSize = pageSize;
		// 设置总记录数
		this.totalRecord = totalRecord;
		// 初始化总页数
		setTotalPage();
		// 初始化当前页
		setCurrentPage(page);
	}

	/**
	 * 外界获得PageBean实例
	 */
	public static PageBean newPageBean(final int pageSize, final String page,
			final int totalRecord) {
		return new PageBean(pageSize, page, totalRecord);
	}

	// 设置当前请求页
	private void setCurrentPage(String page) {
		try {
			currentPage = Integer.parseInt(page);
		} catch (java.lang.NumberFormatException e) {
			// 这里异常不做处理，当前页默认为1
			currentPage = 1;
		}
		// 如果当前页小于第一页时，当前页指定到首页
		if (currentPage < 1) {
			currentPage = 1;
		}
		if (currentPage > totalPage) {
			currentPage = totalPage;
		}
	}

	/**
	 * 总页数
	 */
	private void setTotalPage() {

		totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize
				: totalRecord / pageSize + 1;
		
	}

	/**
	 * 获得当前页
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * 获得总页数
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * 获得开始行数
	 */
	public int getStartRow() {
		return (currentPage - 1) * pageSize;
	}

	/**
	 * 获得结束行
	 */
	public int getEndRow() {
		return  pageSize;
	}

	/**
	 * 获得翻页数据
	 */
	public List<T> getDataList() {
		return dataList;
	}

	/**
	 * 设置翻页数据
	 */
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	/**
	 * 首页
	 */
	public int getFirst() {
		return 1;
	}

	/**
	 * 上一页
	 */
	public int getPrevious() {
		return currentPage - 1;
	}

	/**
	 * 下一页
	 */

	public int getNext() {
		return currentPage + 1;
	}

	/**
	 * 尾页
	 */
	public int getLast() {
		return totalPage;
	}
	/**
	 * 总记录数
	 */
	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	
}
