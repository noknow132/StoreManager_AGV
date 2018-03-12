/**  
 * Project Name:StoreManager  
 * File Name:PLCModel.java  
 * Package Name:com.entity  
 * Date:2018年2月23日下午3:20:05  
 * Copyright (c) 2018, chenzhou1025@126.com All Rights Reserved.  
 *  
*/  
  
package com.entity;

import java.net.Socket;

/**  
 * ClassName:PLCModel 
 * Function: PLC模型类  
 * Date:     2018年2月23日 下午3:20:05 
 * @author   王奎
 * @version    
 * @since    JDK 1.8  
 * @see        
 */
public class PLCModel {
	/**
	 *当前设备的socket连接
	 */
	private Socket socket;
	
	private int handle;
	
	private int ushort_0;
	
	/**
	 *当前plc设备的ip地址
	 */
	private String rip;
	
	/**
	 *当前plc设备的端口号
	 */
	private int ports;
	
	/**
	 *当前plc设备的连接状态
	 *值为0时  是关闭状态
	 */
	private int linked;

	
	public Socket getSocket() {
		return this.socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public int getHandle() {
		return this.handle;
	}

	public void setHandle(int handle) {
		this.handle = handle;
	}

	public int getUshort_0() {
		return this.ushort_0;
	}

	public void setUshort_0(int ushort_0) {
		this.ushort_0 = ushort_0;
	}

	public String getRip() {
		return this.rip;
	}

	public void setRip(String rip) {
		this.rip = rip;
	}

	public int getPorts() {
		return this.ports;
	}

	public void setPorts(int ports) {
		this.ports = ports;
	}

	public int getLinked() {
		return this.linked;
	}

	public void setLinked(int linked) {
		this.linked = linked;
	}
}
  
