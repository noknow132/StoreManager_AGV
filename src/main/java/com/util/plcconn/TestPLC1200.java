/**  
 * Project Name:StoreManager_AGV  
 * File Name:TestPLC1200.java  
 * Package Name:com.util.plcconn  
 * Date:2018年3月14日下午3:59:39  
 * Copyright (c) 2018, chenzhou1025@126.com All Rights Reserved.  
 *  
*/  
  
package com.util.plcconn;

import java.util.List;

/**  
 * ClassName:TestPLC1200 <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2018年3月14日 下午3:59:39 <br/>  
 * @author   Administrator  
 * @version    
 * @since    JDK 1.6  
 * @see        
 */
public class TestPLC1200 {

	public static void main(String[] args) {
		int handle = 1;
		//int aa = PLCConfig.YKSPlcLink(0, "192.168.0.200", 0, "192.168.0.1", 102, 0, 2, 1000,ConnDataStr.plc_1200_1,ConnDataStr.plc_200Smart_2);
//		boolean conn0 = PLCController.PlcConn_200(handle, "192.168.0.10", 102);
//		System.out.println(conn0);
//		boolean reset = PLCController.resetPlace();
//		System.out.println(reset);
		
		boolean conn = PLCController.PlcConn_1200(handle, "192.168.0.1", 102);
		System.out.println(conn);
		
//		byte[] data = new byte[]{1,0,1,0,2,0,1,2};
//		boolean inout = PLCController.InOrOurStore(data);
//		System.out.println(inout);
		
//		byte[] data1 = new byte[]{(byte)1};
//		int j =  PLCConfig.YKSPlcWrite(handle,PlcMemory.DR,DataType.BYTE8,(short)1,(short)25,(short)1,data1);
//		System.out.println(j);
		//int c = PLCConfig.YKSPlcLink(0, "192.168.0.200", 0, "192.168.0.10", 102, 1, 2, 1000,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2);
		
//			try {
//				for(int i=0;i<3;i++) {
//					Thread.sleep(500);
//					byte[] data1 = new byte[]{(byte)0};
//					int j =  PLCConfig.YKSPlcWrite(handle,PlcMemory.DR,DataType.BYTE8,(short)501,(short)i,(short)1,data1);
//					System.out.println(i+"-------"+j);
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();  
//			}
			
//		byte[] data1 = new byte[]{(byte)0};
//		int j =  PLCConfig.YKSPlcWrite(handle,PlcMemory.DR,DataType.BYTE8,(short)501,(short)0,(short)1,data1);
		
//		PLC1200Move(AGVPlace.Downleft9start);
		
		PLC1200Move(AGVPlace.Upleftstop);
		PLC1200Move(AGVPlace.Downleft1stop);
		PLC1200Move(AGVPlace.Downleft2stop);
		
//		System.out.println("----");
//		Object obj1 = PLCConfig.YKSPlcRead(handle,PlcMemory.DR,DataType.BYTE8,(short)501,(short)0,(short)3,ConnDataStr.plc_1200_1,ConnDataStr.plc_200Smart_2);
//		try{
//			Object[] a = (Object[])obj1;
//			for(Object k : a) {
//				System.out.println(k.toString());
//				List<Integer> str102 = PLCConfig.str10To2Arr(k.toString());
//				for(Integer str : str102) {
//					System.out.print(str+"   ");
//				}
//				System.out.println();
//			}
//			System.out.println("----");
//			
//		}catch(Exception e) {
//			System.out.println(obj1.toString());
//		}
		
	}
	
	public static void PLC1200Move(AGVPlace place) {
		//byte[] data1 = new byte[]{(byte)place.getNum()};
		int j =  PLCConfig.YKSPlcWrite(1,PlcMemory.DR,DataType.BYTE8,(short)501,(short)place.getArea(),(short)1,new byte[]{(byte)place.getNum()});

	}

}
  
