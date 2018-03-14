/**  
 * Project Name:StoreManager_AGV  
 * File Name:ConnDataStr.java  
 * Package Name:com.util.plcconn  
 * Date:2018年3月14日上午11:52:17  
 * Copyright (c) 2018, chenzhou1025@126.com All Rights Reserved.  
 *  
*/  
  
package com.util.plcconn;  
/**  
 * ClassName:ConnDataStr  
 * Function: 连接不同型号的PLC需要的握手数据
 * Date:     2018年3月14日 上午11:52:17 
 * @author   wangkui  
 * @version    
 * @since    JDK 1.8  
 * @see        
 */
public class ConnDataStr {
	/**
	 * 西门子S7-200 Smart初始化连接指令字符串，第一次握手
	 */
	public static final String plc_200Smart_1 = "03 00 00 16 11 E0 00 00 00 DF 00 C1 02 02 01 C2 02 02 01 C0 01 0A";
	
	/**
	 * 西门子S7-200 Smart初始化连接指令字符串，第二次握手
	 */
	public static final String plc_200Smart_2 = "03 00 00 19 02 F0 80 32 01 00 00 CC C1 00 08 00 00 F0 00 00 01 00 01 03 C0";
	
	/**
	 * 西门子S7-1200/S7-1500 初始化连接指令字符串，其它与S7-200 Smart指令一样
	 */
	public static final String plc_1200_1 = "03 00 00 16 11 E0 00 00 00 01 00 C1 02 10 00 C2 02 03 01 C0 01 0A";
}
  
