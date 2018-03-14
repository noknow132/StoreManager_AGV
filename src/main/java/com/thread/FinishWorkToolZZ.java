package com.thread;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dao.IChangeStoreDao;
import com.dao.ICreateStoreAreaDao;
import com.dao.IInputStoreDao;
import com.dao.IMoveStoreDao;
import com.dao.IOutinPlaceDao;
import com.dao.IOutputStoreDao;
import com.dao.IRunStepDao;
import com.dao.IRunStepRecordDao;
import com.dao.IStoreHouseDao;
import com.dao.IStoreHouseRecordDao;
import com.dao.IWorkStepDao;
import com.entity.ChangeStore;
import com.entity.CreateStoreArea;
import com.entity.InputStore;
import com.entity.MoveStore;
import com.entity.OutinPlace;
import com.entity.OutputStore;
import com.entity.RunStep;
import com.entity.RunStepRecord;
import com.entity.StoreHouse;
import com.entity.StoreHouseRecord;
import com.entity.WorkStep;
import com.util.plcconn.ConnDataStr;
import com.util.plcconn.DataType;
import com.util.plcconn.PLCConfig;
import com.util.plcconn.PLCController;
import com.util.plcconn.PlcMemory;

public class FinishWorkToolZZ {

	//输送链操作
	public static boolean  chainPlc(byte chainData ){
		boolean falg=false;
		try{
			if(PLCConfig.CheckConnect2(1,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2)>=0){
				byte data[]=new byte[]{chainData};
				falg = PLCController.wirteData(1, PlcMemory.DR,DataType.BYTE8,(short)102,(short)1,data );
			}else{
				falg=false;
			}
		}catch(Exception e){
			e.printStackTrace();
			falg=false;
		}
		return falg;
	}
	
	//写数据
	public static boolean  writeDataplc_200(int handle,int startAddr,byte chainData ){
		boolean falg=false;
		try{
			if(PLCConfig.CheckConnect2(1,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2)>=0){
				byte data[]=new byte[]{chainData};
				falg = PLCController.wirteData(handle, PlcMemory.DR,DataType.BYTE8,(short)startAddr,(short)1,data );
			}else{
				falg=false;
			}
		}catch(Exception e){
			e.printStackTrace();
			falg=false;
		}
		return falg;
	}
	//入货台是否有货
	public static int  ArrivalTableHasGood( ){
		try{
			while(true){
				if(PLCConfig.CheckConnect2(1,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2)>=0){
					Object readData = PLCController.readData_200(1, PlcMemory.DR,DataType.BYTE8,(short)100,(short)1 );
					if(readData instanceof Object[]){
						Object[] readData2By = (Object[])readData;	
						if(((byte)readData2By[0] & 0xFF)==1){
							return 1;
						}
					}
					Thread.sleep(500);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	//出货台是否有货
	public static int  OutputTableHasGood( ){
		try{
			while(true){
				if(PLCConfig.CheckConnect2(1,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2)>=0){
					Object readData = PLCController.readData_200(1, PlcMemory.DR,DataType.BYTE8,(short)101,(short)1 );
					if(readData instanceof Object[]){
						Object[] readData2By = (Object[])readData;	
						if(((byte)readData2By[0] & 0xFF)==0){
							return 1;
						}
					}
					Thread.sleep(500);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	
	//读取出货步骤
	public static Object[] readOutStoreStep(){
		Object[] readData2By =new Object[1] ;
		try{
				if(PLCConfig.CheckConnect2(1,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2)>=0){
					Object readData = PLCController.readData_200(0, PlcMemory.DR,DataType.BYTE8,(short)22,(short)1 );
					if(readData instanceof Object[]){
						 readData2By = (Object[])readData;	
						return readData2By;
					}
					//Thread.sleep(500);
				}
		}catch(Exception e){
			e.printStackTrace();
			readData2By[0]=-1;
			return readData2By;
		}
		return readData2By;
	}
	
	//取余
	public static int[] getMod(byte num){
		int[] numArr=new int[3];
		int numtemp=(byte)num & 0xFF;

		if(numtemp>99){//如果是调库
			numArr[0]=numtemp/100;//百位
			numArr[1]=numtemp%100/10;//十位
			numArr[2]=numtemp%10;//个位
		}else{//如果是其他
			numArr[0]=0;//百位
			numArr[1]=numtemp%100/10;//十位
			numArr[2]=numtemp%10;//个位
		}
		return numArr;
	}

	/**
	 * 向plc写入data
	 * @param data1
	 * @param data2
	 * @param putPlace
	 * @param getPlace
	 */

	//data[0] 0  偏移15
	//data[1] 取列
	//data[2] 0
	//data[3] 放列
	//data[4] 取行
	//data[5] 放行
	//data[6] 行列层计算完成信号
	//data[7] 出库的步骤
	public static byte[] setByteValue(byte[] data,String putPlaceId,String getPlaceId,int workType,int outType, ICreateStoreAreaDao createStoreAreaDao,IStoreHouseDao storeHouseDao, IOutinPlaceDao outinPlaceDao){
		byte[] tempData=new byte[4];//缓存放货台 取货台向输送链发启动信号
		data[0]=0;
		data[2]=0;
		data[5]=2;//放行
		if(workType==0){//入库
			  data[4]=1;
			//取货位是取货台
			if(getPlaceId!=null&&!getPlaceId.equals("")){
				   OutinPlace op = outinPlaceDao.selectOutinPlaceById(getPlaceId);
				   data[1]=op.getColumn().byteValue();
			}
			//放货位是仓库
			getOrPutGetValue( data, putPlaceId, true, createStoreAreaDao, storeHouseDao);
		}else if(workType==1){//出库 可能会出现三个步骤
			data[4]=2;
			if(outType==1){
				//取货位是仓库
				getOrPutGetValue( data, getPlaceId, false, createStoreAreaDao, storeHouseDao);
				//放货位是仓库
				getOrPutGetValue( data, putPlaceId, true, createStoreAreaDao, storeHouseDao);
			}else if(outType==2){
				//取货位是仓库
				getOrPutGetValue( data, getPlaceId, false, createStoreAreaDao, storeHouseDao);
				//放货位是出口
				if(putPlaceId!=null&&!putPlaceId.equals("")){
					   OutinPlace op = outinPlaceDao.selectOutinPlaceById(putPlaceId);
					   data[3]=op.getColumn().byteValue();//放列
				}				
			}								
			
		}else if(workType==4){//直接出库
			//取货位是取货台
			  data[4]=1;//取行
			if(getPlaceId!=null&&!getPlaceId.equals("")){
				   OutinPlace op = outinPlaceDao.selectOutinPlaceById(getPlaceId);
				   data[1]=op.getColumn().byteValue();
			}
			//放货位是出口  
			if(putPlaceId!=null&&!putPlaceId.equals("")){
				   OutinPlace op = outinPlaceDao.selectOutinPlaceById(putPlaceId);
				   data[3]=op.getColumn().byteValue();//放列
				 
			}
		}
		tempData[0]=data[1]; //取列
		tempData[1]=data[3]; //放列
		tempData[2]=data[4]; //取行
		tempData[3]=data[5]; //放行
		return tempData;
	}	
	
	//启动输送链工作
	public static int letChainStart(byte column,byte row){
		int tempcomn=0;
		int yksPlcWrite =-1;
		if(row==1){//在取货口这边
			int a=5-((column& 0xFF)-1)/2;//是plc读取的第几位
			int b=(int)Math.pow(2, a-1);//向plc写入该值
			int checkConnect = PLCConfig.CheckConnect2(1,ConnDataStr.plc_1200_1,ConnDataStr.plc_200Smart_2);
			if(checkConnect==0){
				while(!iscanStartChain(1,500,0,a-1,b)){
				}
				 yksPlcWrite = PLCConfig.YKSPlcWrite(1,PlcMemory.DR,DataType.BYTE8,(short)501,(short)0,(short)1,new byte[]{(byte)b});
			}
		}else if(row==2){//在另一边
			int a=(10-(column& 0xFF) );
			int moveStep=0;//plc偏移量 
			if(a>7){
				int b =(int)Math.pow(2, a-8)	;
				int checkConnect = PLCConfig.CheckConnect2(1,ConnDataStr.plc_1200_1,ConnDataStr.plc_200Smart_2);
				if(checkConnect==0){
					while(!iscanStartChain(1,500,2, a-8,b)){
					}
					yksPlcWrite=PLCConfig.YKSPlcWrite(1,PlcMemory.DR,DataType.BYTE8,(short)501,(short)2,(short)1,new byte[]{(byte)b});
					moveStep=2;
				}
			}else{
				int b=(int)Math.pow(2, a)	;
				int checkConnect = PLCConfig.CheckConnect2(1,ConnDataStr.plc_1200_1,ConnDataStr.plc_200Smart_2);
				if(checkConnect==0){
					while(!iscanStartChain(1,500,1,a,b)){
					}
					yksPlcWrite=PLCConfig.YKSPlcWrite(1,PlcMemory.DR,DataType.BYTE8,(short)501,(short)1,(short)1,new byte[]{(byte)b});
					moveStep=1;

				}
			}

			
		}
		return yksPlcWrite;

	}	
	//启动输送链工作
	public static int letChainStop(byte column,byte row){
			//int tempcomn=0;
			int yksPlcWrite =-1;
			if(row==1){//在取货口这边
				int a=5-((column& 0xFF)-1)/2;//是plc读取的第几位
				// int b=(int)Math.pow(2, a-1);//向plc写入该值
				int b=0;
				int checkConnect = PLCConfig.CheckConnect2(1,ConnDataStr.plc_1200_1,ConnDataStr.plc_200Smart_2);
				if(checkConnect==0){
					/*while(!iscanStartChain(1,500,0,a-1,b)){
					}*/
					 yksPlcWrite = PLCConfig.YKSPlcWrite(1,PlcMemory.DR,DataType.BYTE8,(short)501,(short)0,(short)1,new byte[]{(byte)b});
				}
			}else if(row==2){//在另一边
				int a=(10-(column& 0xFF));
				if(a>7){
					//int b=(int)Math.pow(2, 9-a);
					int b=0;
					int checkConnect = PLCConfig.CheckConnect2(1,ConnDataStr.plc_1200_1,ConnDataStr.plc_200Smart_2);
					if(checkConnect==0){
						/*while(!iscanStartChain(2,500,2,9-a,b)){
						}*/
						yksPlcWrite=PLCConfig.YKSPlcWrite(1,PlcMemory.DR,DataType.BYTE8,(short)501,(short)2,(short)1,new byte[]{(byte)b});
					}
				}else{
					//int b=(int)Math.pow(2, a-1)	;
					int b=0;
					int checkConnect = PLCConfig.CheckConnect2(1,ConnDataStr.plc_1200_1,ConnDataStr.plc_200Smart_2);
					if(checkConnect==0){
						/*while(!iscanStartChain(2,501,1,a-1,b)){
						}*/
						yksPlcWrite=PLCConfig.YKSPlcWrite(1,PlcMemory.DR,DataType.BYTE8,(short)501,(short)1,(short)1,new byte[]{(byte)b});
					}
				}
			}
			return yksPlcWrite;

		}	
		
		
	public static boolean iscanStartChain(int handle,int block,int startadd,int moveStep,int writeNum) {
		
			int checkConnect = PLCConfig.CheckConnect2(1,ConnDataStr.plc_1200_1,ConnDataStr.plc_200Smart_2);
			if(checkConnect==0){
				
				while(true){
				Object yksPlcRead = PLCConfig.YKSPlcRead(handle,PlcMemory.DR,DataType.BYTE8,(short)block,(short)startadd,(short)1,ConnDataStr.plc_1200_1,ConnDataStr.plc_200Smart_2);
				//PLCConfig.YKSPlcWrite(1,PlcMemory.DR,DataType.BYTE8,(short)501,(short)2,(short)1,new byte[]{(byte)0});

				if(yksPlcRead instanceof Object[]){
					Object[] readData2By = (Object[])yksPlcRead;	
					if((((byte)readData2By[0] & 0xFF)& writeNum)>>>moveStep ==1){
						return true;
					}
				}else{
					PLCConfig.CheckConnect2(1,ConnDataStr.plc_1200_1,ConnDataStr.plc_200Smart_2);
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}				
			}else{
				return false;
			}		
	}
	
	

   private static void getOrPutGetValue(byte[] data,String placeId,boolean isput,ICreateStoreAreaDao createStoreAreaDao,IStoreHouseDao storeHouseDao){
	   if(placeId!=null&&!placeId.equals("")){
		   StoreHouse sh = storeHouseDao.selectByStoreId(placeId);
			String	place=sh.getStoreNo();
		
			CreateStoreArea cs = createStoreAreaDao.selectCreateStoreAreaByStoreId(placeId);
			int sence = cs.getSequence();
			int getcolumnsStart = cs.getColumnsStart();
			if(isput){
				data[3] = (byte) (Integer.parseInt(sence==0?place.substring(4,6):place.substring(2,4))+(getcolumnsStart==0?1:0)+6);//放列	
			}else{
				data[1] = (byte) ( Integer.parseInt(sence==0?place.substring(4,6):place.substring(2,4))+(getcolumnsStart==0?1:0)+6);//取列
			}
		 
	}
   }
	//为数组赋值
	public static void copyByteArr(byte[] sourceData,byte[] targetData){
		for(int i=0;i<sourceData.length;i++){
			targetData[i]=sourceData[i];
		}
	}


	//入库(肖子凡)
	public static void inStore(String orderNo ,IInputStoreDao inputStoreDao, IStoreHouseRecordDao storeHouseRecordDao,IStoreHouseDao storeHouseDao){
		InputStore inputStore = inputStoreDao.selectInpuStoreByNo(orderNo);//根据单号查找 入库
		inputStore.setStatue(3);//单据状态 已完成
		inputStoreDao.updateInputStore(inputStore);

		//				//订单详情
		//				OrderDetail orderDetail = orderDetailDao.searchOrderDetailById(inputStore.getOrderDetailId());

		//查找当前入库的仓库
		StoreHouse storeHouse = storeHouseDao.selectByStoreId(inputStore.getStoreId());
		storeHouse.setStoreStatue(2);//入库的仓库为有货
		storeHouse.setCount(inputStore.getCount());
		storeHouse.setUnit(inputStore.getUnit());
		storeHouse.setOrderId(inputStore.getOrderId());
		storeHouse.setGoodNo(inputStore.getBarCode());
		storeHouseDao.updateStoreHouse(storeHouse);

		//添加仓库记录
		StoreHouseRecord storeHouseRecord = new StoreHouseRecord();
		storeHouseRecord.setRecordId(UUID.randomUUID().toString().replaceAll("-", ""));
		storeHouseRecord.setStoreId1(storeHouse.getStoreId());
		storeHouseRecord.setOperateType(0);
		storeHouseRecord.setBarCode(inputStore.getBarCode());
		storeHouseRecord.setOrderNo(inputStore.getInputStoreNo());
		storeHouseRecord.setOperateTimeStart(inputStore.getInputTime());//开始时间
		storeHouseRecord.setOperateTimeEnd(new Date());//结束时间
		storeHouseRecord.setCount(inputStore.getCount());
		storeHouseRecord.setOrderId(inputStore.getOrderId());
		storeHouseRecord.setOrderDetailId(inputStore.getOrderDetailId());
		storeHouseRecord.setUnit(inputStore.getUnit());
		storeHouseRecordDao.insertStoreHouseRecord(storeHouseRecord);
	}
	
	//出库
	public static void outStore(String orderNo,IOutputStoreDao outputStoreDao,IStoreHouseRecordDao storeHouseRecordDao,
			IStoreHouseDao storeHouseDao,Integer outputStep,StoreHouse shA,StoreHouse shB,IWorkStepDao workStepDao){
		if(outputStep==3){//这种情况  其实就是移库  把A库的东西移到B库
			StoreHouse	shTemp=new  StoreHouse();
			//修改仓库B的状态  仓库B放的时原仓库A放的物体
			copyStoreHouse(shB,shA);
			storeHouseDao.updateStoreHouse(shB);
           //缓存仓库A的状态
			copyStoreHouse(shTemp,shA);
			//修改仓库A的状态
			shA.setStoreStatue(0);
			shA.setCount(0);
			shA.setGoodNo(null);
			shA.setOrderId(null);
			shA.setOrderDetailId(null);
			shA.setUnit(null);			
			storeHouseDao.updateStoreHouse(shA);
			Integer storeStatue = shTemp.getStoreStatue();//等于11表示预占用有货  当是预占用有货时
			if(storeStatue==11){//很有可能是在作业表中有出库作业
				Map<String, Object> params=new   HashMap<String,Object>();
				params.put("workStatue", 0);
				params.put("getPlace", shTemp.getStoreId());
				List<WorkStep> wss = workStepDao.selectWorkStepByStoreIdAndStatus(params);
				if(wss!=null&&wss.size()>0){
					WorkStep workStep = wss.get(0);
					workStep.setGetPlace(shB.getStoreId());
					workStepDao.updateWorkStep(workStep);
				}
			}
		}else{
			OutputStore ous = outputStoreDao.selectOutputStoreByNo(orderNo);
			ous.setStatue(3);
			outputStoreDao.updateOutputStoreByIdSelective(ous);
			//查找当前出库的仓库
			StoreHouse shOus= storeHouseDao.selectByStoreId(ous.getStoreId());
			//添加仓库记录
			StoreHouseRecord shrOus = new StoreHouseRecord();
			shrOus.setRecordId(UUID.randomUUID().toString().replaceAll("-", ""));
			shrOus.setStoreId1(ous.getStoreId());
			shrOus.setOperateType(1);
			shrOus.setBarCode(ous.getBarCode());
			shrOus.setOrderNo(ous.getOutputStoreNo());
			shrOus.setOperateTimeStart(ous.getOutputTime());//开始时间
			shrOus.setOperateTimeEnd(new Date());//结束时间
			shrOus.setCount(ous.getCount());
			shrOus.setOrderId(ous.getOrderId());
			shrOus.setOrderDetailId(shOus.getOrderDetailId());
			shrOus.setUnit(shOus.getUnit());
			storeHouseRecordDao.insertStoreHouseRecord(shrOus);
			//修改仓库状态
			shOus.setStoreStatue(0);
			shOus.setCount(0);
			shOus.setGoodNo(null);
			shOus.setOrderId(null);
			shOus.setOrderDetailId(null);
			shOus.setUnit(null);			
			storeHouseDao.updateStoreHouse(shOus);
		}
	}
	
	/**
	 * 
	 * @param shTemp
	 * @param sh
	 */
	public static void copyStoreHouse(StoreHouse shTemp,StoreHouse sh){
		shTemp.setCount(sh.getCount());
		//shTemp.setCreatestoreareaId(sh.getCreatestoreareaId());
		//shTemp.setCreatestorehouseId(sh.getCreatestorehouseId());
		shTemp.setGoodNo(sh.getGoodNo());
		shTemp.setOrderDetailId(sh.getOrderDetailId());
		shTemp.setOrderId(sh.getOrderId());
		shTemp.setRemark(sh.getRemark());
		//shTemp.setStoreId(sh.getStoreId());
		//shTemp.setStoreNo(sh.getStoreNo());
		shTemp.setStoreStatue(sh.getStoreStatue());
		shTemp.setUnit(sh.getUnit());
	}
	
	
	//移库 (肖子凡)
	public static void moveStore(String orderNo,IMoveStoreDao moveStoreDao,IStoreHouseRecordDao storeHouseRecordDao,IStoreHouseDao storeHouseDao){
		MoveStore moveStore = moveStoreDao.selectMoveStoreByNo(orderNo);
		moveStore.setStatus(3);//单据状态 已完成
		moveStoreDao.updateMoveStore(moveStore);
		//订单详情
		//				OrderDetail orderDetail2 = orderDetailDao.searchOrderDetailById(moveStore.getOrderDetailId());
		//起始仓库
		StoreHouse storeHouseFrom = storeHouseDao.selectByStoreId(moveStore.getStoreIdFrom());
		//目标仓库
		StoreHouse storeHouseTo = storeHouseDao.selectByStoreId(moveStore.getStoreIdTo());

		int fromCount = storeHouseFrom.getCount();
		String fromUnit = storeHouseFrom.getUnit();
		String toUnit = storeHouseTo.getUnit();
		String orderId2 = storeHouseFrom.getOrderId();
		String goodNo2 = storeHouseFrom.getGoodNo();

		storeHouseFrom.setStoreStatue(0);//起始仓库更改为空闲
		storeHouseFrom.setCount(0);
		storeHouseFrom.setUnit(toUnit);
		storeHouseFrom.setOrderId("");
		storeHouseFrom.setGoodNo("");
		storeHouseDao.updateStoreHouse(storeHouseFrom);

		storeHouseTo.setStoreStatue(2);//目标仓库更改为有货
		storeHouseTo.setCount(fromCount);
		storeHouseTo.setUnit(fromUnit);
		storeHouseTo.setOrderId(orderId2);
		storeHouseTo.setGoodNo(goodNo2);
		storeHouseDao.updateStoreHouse(storeHouseTo);

		//添加仓库记录
		StoreHouseRecord storeHouseRecord2 = new StoreHouseRecord();
		storeHouseRecord2.setRecordId(UUID.randomUUID().toString().replaceAll("-", ""));
		storeHouseRecord2.setStoreId1(storeHouseFrom.getStoreId());//起始仓库
		storeHouseRecord2.setStoreId2(storeHouseTo.getStoreId());//目标仓库
		storeHouseRecord2.setOperateType(3);
		storeHouseRecord2.setBarCode(moveStore.getBarCode());
		storeHouseRecord2.setOrderNo(moveStore.getMoveNo());
		storeHouseRecord2.setOperateTimeStart(moveStore.getMoveTime());//开始时间
		storeHouseRecord2.setOperateTimeEnd(new Date());//结束时间
		//				storeHouseRecord2.setCount(orderDetail2.getCount());
		//				storeHouseRecord2.setOrderId(moveStore.getOrderId());
		//				storeHouseRecord2.setOrderDetailId(moveStore.getOrderDetailId());
		//				storeHouseRecord2.setUnit(orderDetail2.getUnit());
		storeHouseRecordDao.insertStoreHouseRecord(storeHouseRecord2);
	}

	//调库
	public static void changeStore(String orderNo,IChangeStoreDao changeStoreDao,IStoreHouseRecordDao storeHouseRecordDao,IStoreHouseDao storeHouseDao){
		ChangeStore chs = changeStoreDao.selectChangeStoreByNo(orderNo);
		chs.setStatue(3);//单据状态 已完成
		changeStoreDao.updateCSByCSIdSelective(chs);
		StoreHouse shFrom = storeHouseDao.selectByStoreId(chs.getStoreIdFrom());	//起始仓库
		StoreHouse shTo=storeHouseDao.selectByStoreId(chs.getStoreIdTo());   //目标仓库

		Integer count3 = shFrom.getCount();
		String goodNo3 = shFrom.getGoodNo();
		String orderDetailId3 = shFrom.getOrderDetailId();
		String orderId3 = shFrom.getOrderId();
		String unit3 = shFrom.getUnit();

		shFrom.setCount(shTo.getCount());
		shFrom.setGoodNo(shTo.getGoodNo());
		shFrom.setOrderDetailId(shTo.getOrderDetailId());
		shFrom.setOrderId(shTo.getOrderId());
		shFrom.setUnit(shTo.getUnit());
		shFrom.setStoreStatue(2);//有货状态
		storeHouseDao.updateStoreHouse(shFrom);
		//入库插入一条
		/*		InputStore insF=new InputStore();
			insF.setBarCode(shTo.getGoodNo());
			insF.setCount(shTo.getCount());
			insF.setCreatestorehouseId(shFrom.getCreatestorehouseId());
			insF.setInputStoreId(UUID.randomUUID().toString().replaceAll("-", ""));
			insF.setInputStoreNo(inputStoreNo);
			insF.setInputTime(new Date());
			insF.setOrderDetailId(shTo.getOrderDetailId());
			insF.setOrderId(shTo.getOrderId());
			insF.setStatue();
			insF.setStoreId(shFrom.getStoreId());
			inputStoreDao.insertGoods(insF);*/


		shTo.setCount(count3);
		shTo.setGoodNo(goodNo3);
		shTo.setOrderDetailId(orderDetailId3);
		shTo.setOrderId(orderId3);
		shTo.setUnit(unit3);
		shTo.setStoreStatue(2);//有货状态
		storeHouseDao.updateStoreHouse(shTo);	
		//入库插入一条
		/*	InputStore insT=new InputStore();
			insT.setBarCode(goodNo3);
			insT.setCount(shTo.getCount());
			insT.setCreatestorehouseId(shFrom.getCreatestorehouseId());
			insT.setInputStoreId(UUID.randomUUID().toString().replaceAll("-", ""));
			insT.setInputStoreNo(inputStoreNo);
			insT.setInputTime(new Date());
			insT.setOrderDetailId(shTo.getOrderDetailId());
			insT.setOrderId(shTo.getOrderId());
			insT.setStatue();
			insT.setStoreId(shFrom.getStoreId());
			inputStoreDao.insertGoods(insT);*/




		//添加仓库记录
		StoreHouseRecord storeHouseRecord3 = new StoreHouseRecord();
		//StoreHouseRecord shrTo = new StoreHouseRecord();

		storeHouseRecord3.setRecordId(UUID.randomUUID().toString().replaceAll("-", ""));
		storeHouseRecord3.setStoreId1(shFrom.getStoreId());
		storeHouseRecord3.setStoreId2(shTo.getStoreId());
		storeHouseRecord3.setOperateType(2);
		storeHouseRecord3.setBarCode(shFrom.getGoodNo());
		storeHouseRecord3.setOrderNo(chs.getChangeNo());
		storeHouseRecord3.setOperateTimeStart(chs.getChangeTime());//开始时间
		storeHouseRecord3.setOperateTimeEnd(new Date());//结束时间
		//storeHouseRecord3.setCount(shFrom.getCount());
		//storeHouseRecord3.setOrderId(shFrom.getOrderId());
		//storeHouseRecord3.setOrderDetailId(shFrom.getOrderDetailId());
		//storeHouseRecord3.setUnit(shrFrom.getUnit());
		storeHouseRecordDao.insertStoreHouseRecord(storeHouseRecord3);
	}


	/**
	 * 轮询plc里的状态  
	 */
	public  static void canWritePlc(){
		while(true){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int[] startState = PLCController.getStartState();

			if(startState[1]==0&&startState[0]==0){
				break;
			}
		}
	}


	//运行状态记录表的插入
	public static boolean addRunStepRecord(String barCode,String orderId,int workType,String workId,IRunStepDao runStepDao,IRunStepRecordDao runStepRecordDao ){
		boolean flag = false;
		int  runstep=0;
		insertRsr( barCode, orderId, workType, workId, runstep ,runStepDao,runStepRecordDao);
		while(!flag){
			try {
				Thread.sleep(300);
				//启动
				Object runstepObj=PLCConfig.YKSPlcRead(0,PlcMemory.DR,DataType.BYTE8,(short)0,(short)7,(short)1,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2);
				Object[] runstepArr=(Object[])runstepObj;
				int runstep2=((byte)runstepArr[0] & 0xFF);
				int[] startState = PLCController.getStartState();
				//启动
				if(startState[1]==0 && startState[0]==0){
					flag = true;
					break;
				}
				if(runstep2>0&&runstep2<=8){
					if(runstep!=runstep2){
						System.out.println("runstep::::"+runstep);
						System.out.println("runstep2::::"+runstep2);
						insertRsr( barCode, orderId, workType, workId, runstep2,runStepDao,runStepRecordDao);
					} 
					runstep=runstep2;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}


	public static void insertRsr(String barCode,String orderId,int workType,String workId,Integer runstep2 ,IRunStepDao runStepDao,IRunStepRecordDao runStepRecordDao){
		RunStep rs = runStepDao.selectRunStepByRunCode(runstep2);
		String runRecordId = UUID.randomUUID().toString().replaceAll("-", "");
		RunStepRecord rsr=new RunStepRecord();
		rsr.setRunRecordId(runRecordId);
		rsr.setBarCode(barCode);
		rsr.setOrderId(orderId);
		rsr.setRunStepId(rs.getRunStepId());
		rsr.setRunTime(new Date());
		rsr.setWorkType(workType);
		rsr.setWorkId(workId);
		runStepRecordDao.insertRunStepRecord(rsr);
	}

}
