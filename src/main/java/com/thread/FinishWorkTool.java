package com.thread;

import java.util.Date;
import java.util.UUID;

import com.dao.IChangeStoreDao;
import com.dao.ICreateStoreAreaDao;
import com.dao.IInputStoreDao;
import com.dao.IMoveStoreDao;
import com.dao.IOutputStoreDao;
import com.dao.IRunStepDao;
import com.dao.IRunStepRecordDao;
import com.dao.IStoreHouseDao;
import com.dao.IStoreHouseRecordDao;
import com.entity.ChangeStore;
import com.entity.CreateStoreArea;
import com.entity.InputStore;
import com.entity.MoveStore;
import com.entity.OutputStore;
import com.entity.RunStep;
import com.entity.RunStepRecord;
import com.entity.StoreHouse;
import com.entity.StoreHouseRecord;
import com.util.plcconn.ConnDataStr;
import com.util.plcconn.DataType;
import com.util.plcconn.PLCConfig;
import com.util.plcconn.PLCController;
import com.util.plcconn.PlcMemory;

public class FinishWorkTool {

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
	 * 向plc写入得
	 * @param data1
	 * @param data2
	 * @param putPlace
	 * @param getPlace
	 */

	//data[0] 作业类型及步骤
	//data[1] 取行
	//data[2] 放行
	//data[3] 取层
	//data[4] 取列
	//data[5] 放层
	//data[6] 放列
	//data[7] 行列层计算完成信号		
	public static void setByteValue(byte[] data,String putPlaceId,String getPlaceId,ICreateStoreAreaDao createStoreAreaDao,IStoreHouseDao storeHouseDao){
		StoreHouse shput=null; //放货位仓库
		StoreHouse shget=null; //取货位仓库
		String putPlace="";
		String getPlace="";
		if(putPlaceId!=null&&!putPlaceId.equals("")){
			shput = storeHouseDao.selectByStoreId(putPlaceId);
			putPlace=shput.getStoreNo();
		}
		if(getPlaceId!=null&&!getPlaceId.equals("")){
			shget = storeHouseDao.selectByStoreId(getPlaceId);
			getPlace=shget.getStoreNo();
		}
		if(putPlaceId!=null&&!putPlaceId.equals("")){//放货位
			CreateStoreArea cs = createStoreAreaDao.selectCreateStoreAreaByStoreId(putPlaceId);
			int putcolumnsStart = cs.getColumnsStart();
			int sence =cs.getSequence();
			data[2]= (byte) ((byte)putPlace.charAt(0)+((byte)putPlace.charAt(0)%2==0?128:0));//放行
			int ceng=(Integer.parseInt(sence==0?putPlace.substring(2,4):putPlace.substring(4,6))+(putcolumnsStart==0?1:0));
			data[5] = (byte) ( ceng>1?ceng+1:ceng);//放层??????如果运送台与仓位一层同高  >2  不用加一  否则加一
			data[6] = (byte) ( Integer.parseInt(sence==0?putPlace.substring(4,6):putPlace.substring(2,4))+(putcolumnsStart==0?1:0));//放列
		}else{
			//data[2]= (byte) ((byte)getPlace.charAt(0)+((byte)getPlace.charAt(0)%2==0?128:0));//暂时注释
			data[2]= (byte) ((byte)"A".charAt(0)+((byte)"A".charAt(0)%2==0?128:0));
			data[5] =-1;//堆垛机放层
			data[6] =-1;//堆垛机放列
		}
		if(getPlaceId!=null&& !getPlaceId.equals("")){
			CreateStoreArea cs = createStoreAreaDao.selectCreateStoreAreaByStoreId(getPlaceId);
			int sence = cs.getColumnsStart();
			int getcolumnsStart = cs.getColumnsStart();
			data[1]= (byte) ((byte)getPlace.charAt(0)+((byte)getPlace.charAt(0)%2==0?128:0));//取行
			int ceng=Integer.parseInt(sence==0?getPlace.substring(2,4):getPlace.substring(4,6))+(getcolumnsStart==0?1:0);
			data[3] = (byte) ( ceng>1?ceng+1:ceng);//取层     ??????如果运送台与仓位一层同高  >2  不用加一  否则加一
			data[4] = (byte) ( Integer.parseInt(sence==0?getPlace.substring(4,6):getPlace.substring(2,4))+(getcolumnsStart==0?1:0));//取列
		}else{
			//data[1]= (byte) ((byte)putPlace.charAt(0)+((byte)putPlace.charAt(0)%2==0?128:0));//暂时注释
			data[1]= (byte) ((byte)"A".charAt(0)+((byte)"A".charAt(0)%2==0?128:0));
			data[3] =-1;//堆垛机取层
			data[4] =-1;//堆垛机取列
		}
		data[7]=1;
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
	public static void outStore(String orderNo,IOutputStoreDao outputStoreDao,IStoreHouseRecordDao storeHouseRecordDao,IStoreHouseDao storeHouseDao){
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
