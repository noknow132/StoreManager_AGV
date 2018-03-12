package com.util.plcconn;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.entity.PLCModel;

public class PLCConfig {
	private static int PLClength = 101;
	public static PLCModel[] plcModel = new PLCModel[PLClength];
	//	public static Socket[] socket = new Socket[length];
	//	public static int[] handle = new int[length];
	//	private static int[] ushort_0 = new int[length];
	//	private static int[] ports = new int[length];
	//	private static String[] rips = new String[length];
	//	private static int[] linked = new int[length]; //值为0时  是关闭状态

	private final static byte[] PDU = new byte[2048];
	private static int LastError;

	/**
	 * @param localip 本地ip
	 * @param ushort_1 0
	 * @param plc ip
	 * @param plcport plc 端口
	 * @param ushort_3 0
	 * @param ushort_4 2
	 * @param int_1    0
	 * @param ushort_5 1000
	 * @return
	 */
	public static int YKSPlcLink(int handle,String localip,int ushort_1, String plcip, int plcport, int ushort_3, int ushort_4, int ushort_5)
	{
		if (ushort_3 > 7 | ushort_4 > 15){
			return -3;
		}
		byte[] array1 = new byte[4];//本机ip
		byte[] array2 = new byte[4];//plc ip
		int socketIndex = 0;
		try{
			String[] array3 = localip.split("\\.");//本机ip
			String[] array4 = plcip.split("\\.");//plc ip

			for (int i = 0; i < array4.length; i++) {

				array1[i] = (byte)Integer.parseInt(array3[i]);
				array2[i] = (byte)Integer.parseInt(array4[i]);
			}
			int int_1 = smethod_19(new byte[]{array1[2],array1[3],array2[2],array2[3]});//计算地址对应的编号
			//判断编号是否已经存在于数组int_0中
			if(int_1>=PLClength || int_1<0){
				for (int i = 0; i <= PLClength; i += 1){
					if(plcModel[i] != null && plcModel[i].getHandle() == handle&&plcModel[i].getRip() .equals(plcip)&&plcModel[i].getPorts()==plcport){
						socketIndex = i;
						break;
					}
					if(plcModel[i]==null){
						plcModel[i] = new PLCModel();
						plcModel[i].setHandle(handle);
						plcModel[i].setPorts(plcport);
						plcModel[i].setRip(plcip);
						plcModel[i].setLinked(0);
						plcModel[i].setUshort_0(3);
						socketIndex = i;
						break;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return -3;
		}
		return CheckConnect((byte)socketIndex);
	}
	/**
	 * 
	 * @param int_1
	 * @param plcMemory_0  
	 * @param dataType_0
	 * @param ushort_1  起始位置
	 * @param ushort_2 个数
	 * @return 
	 */

	public synchronized static Object YKSPlcRead(int handle, PlcMemory plcMemory_0, DataType dataType_0, short ushort_1, short ushort_2)
	{
		Object[] object_0 = new Object[ushort_2];
		int num = (int)(ushort_1 * 8);

		byte num2 = HandleToIndex(handle);
		if (num2 < 0 || num2 >= PLClength)return -19;

		int re = CheckConnect(num2);
		if (re != 0) return re;

		short num4 = 1;
		switch (dataType_0){
		case INT16: num4 = 2; break;
		case UINT16: num4 = 2; break;
		case DINT32: num4 = 4; break;
		case HEX32: num4 = 4; break;
		case REAL32: num4 = 4; break;
		case BIN16: num4 = 2; break;
		case CHAR8: num4 = 1; break;
		case BYTE8: num4 = 1; break;
		}

		short num5 = (short) (plcMemory_0 == PlcMemory.DR?1:0);

		byte[] array = new byte[31];
		array[0] = 3;
		array[1] = 0;
		array[2] = 0;
		array[3] = 31;
		array[4] = 2;
		array[5] = (byte)240;
		array[6] = (byte)128;
		array[7] = 50;
		array[8] = 1;
		array[9] = 0;
		array[10] = 0;
		array[11] = (byte)(plcModel[num2].getUshort_0() / 256);
		array[12] = (byte)(plcModel[num2].getUshort_0() % 256);
		array[13] = 0;
		array[14] = 14;
		array[15] = 0;
		array[16] = 0;
		array[17] = 4;
		array[18] = 1;
		array[19] = 18;
		array[20] = 10;
		array[21] = 16;
		array[22] = 2;
		array[23] = (byte)((ushort_2 * num4)/ 256);
		array[24] = (byte)((ushort_2 * num4) % 256);
		array[25] = (byte)(num5 / 256);
		array[26] = (byte)(num5 % 256);
		array[27] = (byte)(128 + plcMemory_0.getIndex());
		array[28] = (byte)(num / 65536L);//首地址
		array[29] = (byte)(num % 65536L / 256L);
		array[30] = (byte)(num % 65536L % 256L);

		try{
			if (!SendToPlc(num2, array)) return -11;

			int st = 25;
			int num6 = (st + (ushort_2 * num4));
			byte[] array2 = ReceiveFromPlc(num2, num6);
			if (array2[0] != 3) return -14;

			if (dataType_0 == DataType.BYTE8 || dataType_0 == DataType.CHAR8){
				for (int i = st; i < array2.length; i++){
					byte b = array2[i];
					int num8 = i - st;
					if(dataType_0 == DataType.BYTE8){
						object_0[num8] = b;
					}

					if (dataType_0 == DataType.CHAR8){
						object_0[num8] = (char)b;
					}
				}
			}else if (dataType_0 == DataType.INT16 || dataType_0 == DataType.UINT16 || dataType_0 == DataType.BIN16){
				byte[] array3 = new byte[2];
				for (int i = st; i < array2.length; i += 2){
					array3[0] = array2[i];
					array3[1] = array2[i + 1];
					int num8 = (i - st) / 2;
					int a = bytesToInt(array3);
					if (dataType_0 == DataType.INT16 || dataType_0 == DataType.UINT16){
						object_0[num8] = a;
					}
					if (dataType_0 == DataType.BIN16){
						object_0[num8] = smethod_15(a);
					}
				}
			}else if (dataType_0 == DataType.DINT32 | dataType_0 == DataType.HEX32 | dataType_0 == DataType.REAL32){
				byte[] array4 = new byte[4];
				for (int i = st; i < array2.length; i += 4)
				{
					array4[0] = array2[i];
					array4[1] = array2[i + 1];
					array4[2] = array2[i + 2];
					array4[3] = array2[i + 3];
					int num8 = (i - st) / 4;
					if (dataType_0 == DataType.DINT32){
						object_0[num8] = smethod_19(array4);
					}
					if (dataType_0 == DataType.HEX32 || dataType_0 == DataType.REAL32){
						object_0[num8] = bytesToInt(array4);
					}
				}
			}
			return object_0;
		}catch(Exception e){
			e.printStackTrace();
		}
		return -11;
	}

	/**
	 * 向plc中写入数据
	 * @param int_1 句柄
	 * @param plcMemory_0  选择寄存器区域
	 * @param dataType_0    一次写入的数据长度  BYTE8
	 * @param address     起始地址    0
	 * @param count       写入数据的数量 1
	 * @param value       data实际要写入的数据
	 * @return
	 */
	public synchronized static int YKSPlcWrite(int int_1, PlcMemory plcMemory_0, DataType dataType_0, short address, short count, byte[] value)
	{
		System.out.println("YKSPlcWrite");
		try{
			int fnum = (int)(address * 8);//first Write address
			int xnum = HandleToIndex(int_1);
			if (xnum < 0 || xnum >= PLClength) return -29;
			short num5 = 1;//多少个字节
			switch (dataType_0){
			case INT16: num5 = 2; break;
			case UINT16: num5 = 2; break;
			case DINT32: num5 = 4; break;
			case HEX32: num5 = 4; break;
			case REAL32: num5 = 4; break;
			case BIN16: num5 = 2; break;
			case CHAR8: num5 = 1; break;
			case BYTE8: num5 = 1; break;
			}

			int num6 = count * num5 + 4; //这个项目中  为5
			int num7 = count * num5 + 35; //这个项目中是  36
			int num8 = plcMemory_0 == PlcMemory.DR ? 1 : 0; //1
			byte[] array = new byte[35 + count * num5];//36个字节
			array[0] = 3;
			array[1] = 0;
			array[2] = (byte)(num7 / 256);
			array[3] = (byte)(num7 % 256);
			array[4] = 2;
			array[5] = (byte)240;
			array[6] = (byte)128;
			array[7] = 50;
			array[8] = 1;
			array[9] = 0;
			array[10] = 0;
			array[11] = (byte)(plcModel[xnum].getUshort_0() / 256);
			array[12] = (byte)(plcModel[xnum].getUshort_0() % 256);
			array[13] = 0;
			array[14] = 14;
			array[15] = (byte)(num6 / 256);
			array[16] = (byte)(num6 % 256);
			array[17] = 5;
			array[18] = 1;
			array[19] = 18;
			array[20] = 10;
			array[21] = 16;
			array[22] = 2;
			array[23] = (byte)(count * num5 / 256);
			array[24] = (byte)(count * num5 % 256);
			array[25] = (byte)(num8 / 256);
			array[26] = (byte)(num8 % 256);
			array[27] = (byte)(128 + plcMemory_0.getIndex());
			array[28] = (byte)((long)fnum / 65536L);//首地址
			array[29] = (byte)((long)fnum % 65536L / 256L);
			array[30] = (byte)((long)fnum % 65536L % 256L);
			array[31] = 0;
			array[32] = 4;
			array[33] = (byte)(count * num5 * 8 / 256);
			array[34] = (byte)(count * num5 * 8 % 256);

			if (dataType_0 == DataType.CHAR8 | dataType_0 == DataType.BYTE8){
				int num9 = (int)(count * num5 - 1);
				for (int i = 0; i <= num9; i++){
					array[35 + i] = (byte)value[i];
				}
			}else if (dataType_0 == DataType.INT16 | dataType_0 == DataType.UINT16 | dataType_0 == DataType.BIN16){
				byte[] array3 = new byte[count];
				int num10 = count * num5 - 1;
				for (int i = 0; i < num10; i += 2){
					if (dataType_0 == DataType.INT16 || dataType_0 == DataType.UINT16){
						array3 = intToByte((int)value[i/2]);
					}

					if (dataType_0 == DataType.BIN16){
						array3 = intToByte(smethod_16(value[i / 2]+""));
					}
					array[35 + i] = array3[0];
					array[36 + i] = array3[1];
				}
			}else if (dataType_0 == DataType.DINT32 | dataType_0 == DataType.HEX32 | dataType_0 == DataType.REAL32){
				byte[] array4 = new byte[4];
				int num11 = count * num5 - 1;
				for (int i = 0; i <= num11; i += 4){
					if (dataType_0 == DataType.DINT32 || dataType_0 == DataType.HEX32){
						array4 = smethod_24(value[i / 4]);
					}

					if (dataType_0 == DataType.REAL32){
						array4 = smethod_26(value[i / 4]);
					}
					array[35 + i] = array4[0];
					array[36 + i] = array4[1];
					array[37 + i] = array4[2];
					array[38 + i] = array4[3];
				}
			}
			if (!SendToPlc((byte)xnum, array)) return -21;
			Thread.sleep(1);
			byte[] array2 = ReceiveFromPlc(xnum, 22);
			if (array2[0] != 3) return -24;
			return 0;              
		}catch(Exception e){
			e.printStackTrace();
		}
		return -21;
	}

	//返回0是连接正常
	public static int CheckConnect(byte i){
		if (plcModel[i] != null && plcModel[i].getLinked() == 0){
			if (CreateSocket(i)) return -2;
			return ConnectToPLC(i);
		}else{
			return 0;
		}
	}

	//返回0是连接正常
	public static int CheckConnect2(int handle){
		byte socketIndex = HandleToIndex( handle);
		if (plcModel[socketIndex].getLinked() == 0){
			if (CreateSocket(socketIndex)) return -2;
			return ConnectToPLC(socketIndex);
		}else{
			return 0;
		}
	}

	public static int ConnectToPLC(byte i){
		try{
			String str1 = "03 00 00 16 11 E0 00 00 00 DF 00 C1 02 02 01 C2 02 02 01 C0 01 0A";
			byte[] data = str16ToByteArr(str1);
			if (SendToPlc(i, data)){
				byte[] bufferReceive = ReceiveFromPlc(i, 22);
				if (bufferReceive[0] == 3 && bufferReceive[21] == 1){
					String str2 = "03 00 00 19 02 F0 80 32 01 00 00 CC C1 00 08 00 00 F0 00 00 01 00 01 03 C0";
					if (SendToPlc(i, str16ToByteArr(str2))){
						bufferReceive = ReceiveFromPlc(i, 27);
						if (bufferReceive[0] == 3){
							plcModel[i].setLinked(1);
							return 0;
						}
					}
				}
			}
		}catch (Exception ex){
			System.out.print("PLC握手异常：");
			ex.printStackTrace();
		}
		return -4;
	}

	private static boolean SendToPlc(byte i, byte[] send)
	{
		try{
			if(plcModel[i] != null && !plcModel[i].getSocket().isClosed()){
				OutputStream socketWriter = plcModel[i].getSocket().getOutputStream();
				socketWriter.write(send);
				socketWriter.flush();
				Thread.sleep(10);
				return true;
			}
		}catch (Exception ex){
			System.out.print("发送数据到PLC异常：");
			ex.printStackTrace();
			plcModel[i].setLinked(0);
			CloseSocket(i);
		}
		return false;
	}

	private static byte[] ReceiveFromPlc(int i, int recount)
	{
		byte[] array6 = new byte[recount];
		if (recount > 1024) return array6;
		try{

			DataInputStream socketReader = new DataInputStream(plcModel[i].getSocket().getInputStream());
			int LastError=WaitForData(recount,2000,socketReader); //等待回复的数据

			if (LastError==0){
				//				int cc=socketReader.read(array6,0,recount);
				socketReader.read(array6,0,recount);
				Thread.sleep(10);
			}
		}catch (Exception ex){
			System.out.print("接收PLC数据异常：" );
			ex.printStackTrace();
			plcModel[i].setLinked(0);
			CloseSocket(i);
		}
		return array6;
	}

	/**
	 * 等待数据回复
	 * @param Size 数据长度
	 * @param Timeout
	 * @param InStream 输入流
	 * @return
	 */
	private static int WaitForData(int Size, int Timeout,InputStream InStream) {
		int cnt = 0;
		LastError=0;
		int SizeAvail;
		boolean Expired = false;

		try {
			SizeAvail=InStream.available();
			while ((SizeAvail<Size) && (!Expired) && (LastError==0)){
				cnt++;
				try {
					Thread.sleep(1);
				} 
				catch (InterruptedException ex) {
					LastError=4;
				}
				SizeAvail=InStream.available();  //发送数据的长度            
				Expired=cnt>Timeout;
				if (Expired && (SizeAvail>0) && (LastError==0))
					InStream.read(PDU, 0, SizeAvail);//输入流方法
			}
		}catch (IOException ex){
			LastError=4;
		}
		if (cnt>=Timeout){
			LastError=4;
		}        
		return LastError;
	}

	private static boolean CreateSocket(int i)
	{
		try{
			if(plcModel[i].getSocket() == null){
				plcModel[i].setSocket(new Socket(plcModel[i].getRip(),plcModel[i].getPorts()));// 与服务端建立连接
			}
			if(plcModel[i].getSocket().isConnected()){
				return false;
			}
			plcModel[i].getSocket().close();
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private static boolean CloseSocket(int i)
	{
		boolean result = false;
		try{
			if(plcModel[i].getSocket().isClosed()==false){
				plcModel[i].getSocket().shutdownInput();
				plcModel[i].getSocket().shutdownOutput();
				Thread.sleep(10);
				plcModel[i].getSocket().close();
				result = true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据handle值找到对应socket索引值
	 * @param handle
	 * @return
	 */
	private static byte HandleToIndex(int i)
	{
		for (int num2 = 0; num2 < PLClength; num2 += 1){
			if (plcModel[num2] != null && plcModel[num2].getHandle() == i){
				return (byte)num2;
			}
		}
		return -1;
	}

	private static int smethod_10(long i)
	{
		if (i > 2147483647L){
			return (int)(i - 4294967296L);
		}
		return (int)i;
	}

	private static String smethod_15(int i)
	{
		String text = "";
		while (i > 0){
			text = (i % 2) + text;
			i /= 2;
		}
		text = "0000000000000000" + text;
		return text;
	}

	private static int smethod_16(String string_0)
	{
		int num = string_0.length()-1;
		int num2 = 1;
		int num3 = 0;
		int result = 0;
		for (int i = num; i >= 0; i-=1){
			int num4 = Integer.parseInt(string_0.substring(i, 1));
			if (num4 >= 2){
				return result;
			}
			num3 += num2 * num4;
			num2 += num2;
		}
		return (int)num3;
	}

	private static int smethod_19(byte[] byte_0)
	{
		long num = bytesToInt(byte_0);
		return smethod_10(num);
	}

	private static byte[] smethod_24(Object object_0)
	{
		byte[] array2 = new byte[4];
		String hex = String.format("{0:X8}", object_0);
		for (int i = 0; i < array2.length; i++) {
			array2[i] = (byte)Integer.parseInt(hex.substring(i*2, 2));
		}
		return array2;
	}

	private static byte[] smethod_26(Object object_0)
	{
		byte[] array = new byte[4];
		byte[] array2 = new byte[4];
		for (int i = 0; i < array2.length; i++) {
			array2[i] = array[array2.length-1-i];
		}
		return array2;
	}

	public static byte[] str16ToByteArr(String string_0)
	{
		String[] array1 = string_0.split(" ");
		byte[] array2 = new byte[array1.length];
		for (int i = 0; i < array1.length; i++){
			byte[] array3 = hexString2Bytes(array1[i]);
			array2[i] = array3[0];
		}
		return array2;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String bytesToHexString(byte[] data){   
		StringBuilder stringBuilder = new StringBuilder("");   
		if (data == null || data.length <= 0) {   
			return null;   
		}   
		for (int i = 0; i < data.length; i++) {   
			String hv = Integer.toHexString(data[i] & 0xFF);   
			if (hv.length() < 2) {   
				stringBuilder.append(0);   
			}   
			stringBuilder.append(hv);   
		}   
		return stringBuilder.toString();   
	}

	/**
	 * 十六进制转成byte 数组
	 * @param hex
	 * @return
	 */
	public static byte[] hexString2Bytes(String hex) {
		if ((hex == null) || (hex.equals(""))){
			return null;
		}else if(hex.length()%2 != 0){
			return null;
		}else{                
			hex = hex.toUpperCase();
			int len = hex.length()/2;
			byte[] b = new byte[len];
			char[] hc = hex.toCharArray();
			for (int i=0; i<len; i++){
				b[i] = (byte) (charToByte(hc[2*i]) << 4 | charToByte(hc[2*i+1]));
			}
			return b;
		}           
	}

	/* byte[]转Int */  
	public static int bytesToInt(byte[] bytes) {
		int addr = bytes[1] & 0xFF;
		addr |= ((bytes[0] & 0xFF) << 8);  
		if(bytes.length==4){
			addr |= ((bytes[3] & 0xFF000000)<< 24);  
			addr |= ((bytes[2] & 0xFF0000)<< 16);  
		}
		return addr;  
	}  
	/* Int转byte[] */  
	public static byte[] intToByte(int i) {  
		byte[] abyte0 = new byte[4];  
		abyte0[0] = (byte) (0xff & i);  
		abyte0[1] = (byte) ((0xff00 & i) >> 8); 
		if(abyte0.length==4){
			abyte0[2] = (byte)((0xff0000 & i) >> 16);  
			abyte0[3] = (byte)((0xff000000 & i) >> 24); 
		}
		return abyte0;  
	}
}




