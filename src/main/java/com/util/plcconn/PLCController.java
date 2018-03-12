package com.util.plcconn;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PLCController {
	//堆垛机连接是0  输送链是1
	public static boolean PlcConn(int handle,String PLCIP,int PLCPort){
		List<String> strIps = getLocalIp();
		String localIp = "";//获取本地Ip
		if(strIps!=null && strIps.size()>0){
			localIp = strIps.get(0);
			int start = PLCConfig.YKSPlcLink(handle,localIp, 0, PLCIP, PLCPort, 0, 2, 1000);
			return start==0?true:false;
		}else{
			return false;
		}
	}

	//向PLC中写入数据
	public static boolean wirteData(int handle,PlcMemory plcMemory,DataType dataType,short startAddress,short dataCount,byte[] data){
		//byte[] data = new byte[]{(byte)181,30,70};
//		handle = 0;//句柄
//		plcMemory = PlcMemory.DR;//选择寄存器区域
//		dataType = DataType.INT16;//一次写入的数据长度
//		startAddress = 0;//起始地址
//		dataCount = 3;//写入数据的数量
//		//byte[] data实际要写入的数据
		int num2 = PLCConfig.YKSPlcWrite(handle, plcMemory, dataType, startAddress, dataCount, data);
		return num2==0?true:false;
	}
	
	//获取PLC中读出的数据
	public static Object readData(int handle,PlcMemory plcMemory,DataType dataType,short startAddress,short dataCount) {
//		handle = 0;//句柄
//		plcMemory = PlcMemory.DR;//选择寄存器区域
//		dataType = DataType.INT16;//一次写入的数据长度
//		startAddress = 0;//起始地址
//		dataCount = 3;//写入数据的数量
		return PLCConfig.YKSPlcRead(handle, plcMemory, dataType, startAddress, dataCount);
	}
	
	//获取当前位置
	public static int[] getNowPlace(){
		int[] data = new int[11];
		Object obj1 = readData(0,PlcMemory.DR,DataType.BYTE8,(short)7,(short)10);
		try{
			Object[] temp = (Object[])obj1;
//			data[0] = ((byte)temp[0] & 0xFF);//启动标志
//			data[1] = ((byte)temp[1] & 0xFF);//当前层
//			data[2] = ((byte)temp[2] & 0xFF);//当前列
//			data[3] = ((byte)temp[3] & 0xFF);//作业类型及步骤
//			data[4] = ((byte)temp[4] & 0xFF);//取行
//			data[5] = ((byte)temp[5] & 0xFF);//放行
//			data[6] = ((byte)temp[6] & 0xFF);//取层
//          data[7] = ((byte)temp[7] & 0xFF);//取列
//			data[8] = ((byte)temp[8] & 0xFF);//放层
//			data[9] = ((byte)temp[9] & 0xFF);//放列
			for (int i = 0; i < temp.length; i++) {
				data[i] = ((byte)temp[i] & 0xFF);
			}
		}catch(Exception e){
			data[0] = (Integer)obj1;
		}
		return data;
	}
	
	//获取启动和状态
	public static int[] getStartState(){
		int[] data = new int[2];
		Object obj2 = readData(0,PlcMemory.DR,DataType.BYTE8,(short)7,(short)1);
		try{
		    Object[] isStart=(Object[])obj2;
			//启动
			data[0] = ((byte)isStart[1] & 0xFF);	//启动
		}catch(Exception e){
			data[0] = (Integer)obj2;
		}
		return data;
	}
	
	//复位
	public static boolean resetPlace(){
//		PlcConn();//连接plc
		byte[] data = new byte[]{1};
		return wirteData(0,PlcMemory.DR,DataType.BYTE8,(short)6,(short)1,data);
	}
	
	//出库入库移库
	public static boolean InOrOurStore(byte[] data){
		//写入取行，取列，取层，放行，放列，放层的值
		//data[0] 作业类型及步骤
		//data[1] 取行
		//data[2] 放行
		//data[3] 取层
		//data[4] 取列
		//data[5] 放层
		//data[6] 放列
		//data[7] 行列层计算完成信号			
		boolean flag = wirteData(0,PlcMemory.DR,DataType.BYTE8,(short)10,(short)8,data);
		if(!flag) return false;
		
		while(true){
			Object readDataObj = readData(0,PlcMemory.DR,DataType.BYTE8,(short)17,(short)1);
			Object[] temp = (Object[])readDataObj;
			 if((byte)temp[0]==2){
			    //启动
				boolean flag4 = wirteData(0,PlcMemory.DR,DataType.BYTE8,(short)7,(short)1,new byte[]{1});
				if(!flag4) return false;
				break;
			 }
		}
		return true;
	}
	
	//获取本地Ip
	public static List<String> getLocalIp(){
		List<String> strIp = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements())
			{
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				Enumeration addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()){
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address){
						if(!ip.getHostAddress().equals("127.0.0.1")){
							strIp.add(ip.getHostAddress());
						}
					} 
				}
			}
		} catch (Exception e) { 
		     e.printStackTrace();
		}
		return strIp;//.size()>0?strIp.get(1):null;
	}
	
	//判断网络状态
	public static boolean isNetStateConnect() { 
		Runtime runtime = Runtime.getRuntime(); 
	    try { 
	    	Process process = runtime.exec("ping www.baidu.com"); 
	        InputStream is = process.getInputStream(); 
	        InputStreamReader isr = new InputStreamReader(is); 
	        BufferedReader br = new BufferedReader(isr); 
	        String line = null; 
	        StringBuffer sb = new StringBuffer(); 
	        while ((line = br.readLine()) != null) { 
	            sb.append(line); 
	        } 
	        is.close(); 
	        isr.close(); 
	        br.close(); 
	 
	        if (null != sb && !sb.toString().equals("")) { 
	        	if (sb.toString().indexOf("TTL") > 0) {
	        		return true;
	        	}
	        } 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    return false;
	} 
}
  
