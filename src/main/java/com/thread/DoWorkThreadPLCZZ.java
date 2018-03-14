package com.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dao.IChangeStoreDao;
import com.dao.IConfigParamDao;
import com.dao.ICreateStoreAreaDao;
import com.dao.ICreateStoreHouseDao;
import com.dao.IInputStoreDao;
import com.dao.IMoveStoreDao;
import com.dao.IOutLetDao;

import com.dao.IOutinPlaceDao;
import com.dao.IOutputStoreDao;
import com.dao.IRunStepDao;
import com.dao.IRunStepRecordDao;
import com.dao.IStoreHouseDao;
import com.dao.IStoreHouseRecordDao;
import com.dao.IWorkStepDao;
import com.entity.ConfigParam;
import com.entity.OutLet;
import com.entity.RunStep;
import com.entity.RunStepRecord;
import com.entity.StoreHouse;
import com.entity.WorkStep;
import com.util.plcconn.ConnDataStr;
import com.util.plcconn.DataType;
import com.util.plcconn.PLCConfig;
import com.util.plcconn.PLCController;
import com.util.plcconn.PlcMemory;

@Component("doWorkThreadPLCZZ")
@Scope("prototype")
public class DoWorkThreadPLCZZ extends Thread{
	volatile private int preWorkType=-1;//前一次的作业类型
	private int isScan=0;//前一次的作业类型

	private int chainPlc=0;  //入库开启输送链plc
	
	private int outputStep=0;  //出库进行的步骤
	
	private String tempPlaceId; //进行出库的临时位置id

	private byte[] tempPutGetRowCol;//取列  放列  取行  放行
	
	
	private WorkStep workStep;
	
	private StoreHouse shA;//当A B相应位置都有货时  出库作业为B区  仓库A

	private StoreHouse shB;//当A B相应位置都有货时  出库作业为B区  仓库B

	@Resource
	private IOutputStoreDao outputStoreDao;
	@Resource
	private IInputStoreDao inputStoreDao;
	@Resource
	private IChangeStoreDao changeStoreDao;
	@Resource
	private IMoveStoreDao moveStoreDao;
	@Resource
	private IWorkStepDao workStepDao;
	@Resource
	private IStoreHouseRecordDao storeHouseRecordDao;
	@Resource
	private IStoreHouseDao storeHouseDao;
	@Resource
	private IConfigParamDao configParamDao;
	@Resource
	private IRunStepRecordDao runStepRecordDao;
	@Resource
	private IRunStepDao runStepDao;
	@Resource
	private ICreateStoreHouseDao createStoreHouseDao;
	@Resource
	private ICreateStoreAreaDao createStoreAreaDao;
	@Resource
	private IOutLetDao outLetDao;
	
	@Resource
	private IOutinPlaceDao outinPlaceDao;
	
	public DoWorkThreadPLCZZ() {
		super();
	}

	public WorkStep getWorkStep() {
		return workStep;
	}
	public void setWorkStep(WorkStep workStep) {
		this.workStep = workStep;
	}

	/**
	 * 执行中的数据写入PLC
	 * @param workStep2
	 * data[0] 作业类型及步骤
	 * data[1] 取行
	 * data[2] 放行
	 * data[3] 取层
	 * data[4] 取列
	 * data[5] 放层
	 * data[6] 放列
	 * data[7] 行列层计算完成信号	
	 */
	public void run() {
		int preMod3=-1;
		int isStop=-1;//用于是否停止输送链的缓存
		while(true){
			ConfigParam cp = configParamDao.selectConfigParamOne();//plc启动还是关闭
			Object[] readData = isCanRead(21,1);
			if((byte)readData[0]==-1){
				System.out.println("读失败");
				continue;
			}
		int[] mod = FinishWorkToolZZ.getMod((byte)readData[0]);//获取作业类型及步骤
			//添加0到5运行步骤记录
			if(mod[2]!=5){
				if(preMod3!=mod[2]&&workStep!=null){
					preMod3=mod[2];//缓存上一个步骤
					String barCode=workStep.getFringeCode();//货物码
					String orderId=workStep.getOrderId();//订单id
					String workId=workStep.getWorkId();
					int workType=workStep.getWorkType();//作业类型
					FinishWorkToolZZ.insertRsr(barCode, orderId, workType, workId,mod[2], runStepDao, runStepRecordDao);//插入运行记录
				}
				if(mod[2]==1){//行走到取货位 目前不需要任何操作
				
				}else if(mod[2]==2){//从取货位取货  
					Object[] isGetAddr = isCanRead(23,1);//小车是否到达
					int isGetAddrint=((byte)isGetAddr[0])& 0xFF;
					if(isGetAddrint==1){
						while(!FinishWorkToolZZ.writeDataplc_200(0, 23, (byte)2)){
						}
						int letChainStart = FinishWorkToolZZ.letChainStart(tempPutGetRowCol[0],tempPutGetRowCol[2]);//让输送链启动
						
						while(letChainStart<0){
							letChainStart = FinishWorkToolZZ.letChainStart(tempPutGetRowCol[0],tempPutGetRowCol[2]);//让输送链启动
						}
						isCanRead(28,1);
					}
				}else if(mod[2]==3){//行走到放货位
				
				}else if(mod[2]==4){//放货
					Object[] isGetAddr = isCanRead(23,1);//小车是否到达
					int isGetAddrint=((byte)isGetAddr[0])& 0xFF;
					if(isGetAddrint==1){
						while(!FinishWorkToolZZ.writeDataplc_200(0, 23, (byte )2)){
						}
                        int letChainStart = FinishWorkToolZZ.letChainStart(tempPutGetRowCol[1],tempPutGetRowCol[3]);
						while(letChainStart<0){
							letChainStart =FinishWorkToolZZ.letChainStart(tempPutGetRowCol[1],tempPutGetRowCol[3]);
						}
						while(!FinishWorkToolZZ.writeDataplc_200(0, 28, (byte)1)){//
						}
						//暂时的处理办法
				/*		try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						PLCConfig.YKSPlcWrite(1,PlcMemory.DR,DataType.BYTE8,(short)501,(short)0,(short)1,new byte[]{(byte)0,(byte)0});*/
					}
				}else if(mod[2]==5){
				//	int letChainStop = FinishWorkToolZZ.letChainStop(tempPutGetRowCol[1],tempPutGetRowCol[3]);

				}
				if(mod[2]>=3&&isStop!=0){//再给输送链写0
					int letChainStop = FinishWorkToolZZ.letChainStop(tempPutGetRowCol[0],tempPutGetRowCol[2]);
					if(letChainStop>=0){
						isStop=0;
					}
				}
			}
			if(mod[2]!=5){

				continue;
			}else{
				PLCConfig.YKSPlcWrite(1,PlcMemory.DR,DataType.BYTE8,(short)501,(short)0,(short)3,new byte[]{(byte)0,(byte)0,(byte)0});

			}
			preMod3=-1;
			isStop=-1;
			Object[] outStoreStep = FinishWorkToolZZ.readOutStoreStep();
			if((byte)outStoreStep[0]==-1){
				System.out.println("读失败");
				continue;
			}
			outputStep = ((byte)outStoreStep[0])& 0xFF;//出库步骤
			//完成作业
		/*	if(outputStep==3||outputStep==0){
				if((workStep!=null)){
					finishCurrentWorkStep(workStep);	//完成上条的作业
				}
				if(cp.getIsRun()==0){
					System.out.println("手动暂停");
					break;
				}
			}*/
			WorkStep ws =null;
			if(outputStep!=0&&outputStep!=3){
				ws=workStep;
			}else{
				if(workStep!=null){
					finishCurrentWorkStep(workStep,outputStep);	//完成上条的作业
				}
				if(cp.getIsRun()==0){
					System.out.println("手动暂停");
					break;
				}
			ws = workStepDao.searchWorkStepByStatueAndTypeZZ();
			System.out.println("ws:"+ws);
			workStep=ws;//缓存上一条作业
			}							
			if (ws!=null) {
				int workType=ws.getWorkType();//作业类型
				preWorkType=workType;
				byte[] data=new byte[10];
				String putPlace = ws.getPutPlace();
				String getPlace = ws.getGetPlace();
				tempPutGetRowCol=FinishWorkToolZZ.setByteValue(data,putPlace,getPlace,workType,2,createStoreAreaDao,storeHouseDao, outinPlaceDao);//为byte数组赋值
		    	data[7]=0;
				if(ws.getWorkType()==1){//出库 
					if(outputStep==0||outputStep==3){
						//要根据要出的库判断是哪一种出库
						StoreHouse sh = storeHouseDao.selectByStoreId(getPlace);
						shB=sh;
						String storeNo = sh.getStoreNo();
						String createStoreHouseId = sh.getCreatestorehouseId();
					    if(storeNo.substring(0, 1).equals("B")){//出库是B区
					    	StoreHouse sh2 = storeHouseDao.selectByStoreNoAndCreateStoreHouseId(storeNo.replaceAll("B", "A"), createStoreHouseId);
					    	if(sh2!=null&&(sh2.getStoreStatue()==2||sh2.getStoreStatue()==11)){//出b区的货物  a区有货物
					    		shA=sh2;
					    		List<StoreHouse> shs = storeHouseDao.searchEmptyStoreHouseZZ(createStoreHouseId);
					    		StoreHouse emptySh = shs.get(0);
					    		getPlace=sh2.getStoreId();
					    		putPlace=emptySh.getStoreId();
					    		tempPlaceId=putPlace;
					    		tempPutGetRowCol=FinishWorkToolZZ.setByteValue(data,putPlace,getPlace,workType,2,createStoreAreaDao,storeHouseDao, outinPlaceDao);//为byte数组赋值
								data[7]=1;
								PLCController.InOrOurStoreZZ(data);	
								updateWorkStepStatus(ws);//更改状态为执行中
					    	}else{//出库B区  A区为空
								PLCController.InOrOurStoreZZ(data);
								updateWorkStepStatus(ws);//更改状态为执行中
					    	}
					    }else if(storeNo.substring(0, 1).equals("A")){//出库是A区  但A区的货物已经运送到B区了
					    	Integer storeStatue = sh.getStoreStatue();
					    	if(storeStatue==0){//A区空闲
						    	StoreHouse sh2 = storeHouseDao.selectByStoreNoAndCreateStoreHouseId(storeNo.replaceAll("A", "B"), createStoreHouseId);
                                ws.setGetPlace(sh2.getStoreId());
					    	}
					    	PLCController.InOrOurStoreZZ(data);
							updateWorkStepStatus(ws);//更改状态为执行中
					    }else{
							PLCController.InOrOurStoreZZ(data);
							updateWorkStepStatus(ws);//更改状态为执行中
					    }
					}
					if(outputStep==1){//要进行第二步   把A区的货物移回原位置
						//要根据要出的库判断是哪一种出库
					data[7]=2;
					PLCController.InOrOurStoreZZ(data);
					}
					if(outputStep==2){
						//要根据要出的库判断是哪一种出库
						tempPutGetRowCol=FinishWorkToolZZ.setByteValue(data,getPlace,tempPlaceId,workType,2,createStoreAreaDao,storeHouseDao, outinPlaceDao);//为byte数组赋值
						data[7]=3;
						PLCController.InOrOurStoreZZ(data);
					}
					
				}else  if(ws.getWorkType()==0){//入库
					PLCController.InOrOurStoreZZ(data);
					updateWorkStepStatus(ws);//更改状态为执行中

				}else if(ws.getWorkType()==4){//直接出库
					PLCController.InOrOurStoreZZ(data);
					updateWorkStepStatus(ws);//更改状态为执行中
				}
			}else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	//判断是否能读
	private Object[] isCanRead(int solution,int count){
		int checkNum = PLCConfig.CheckConnect2(0,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2);
		Object[] readDataArr=new Object[1];
		if(checkNum==0){
			Object readData = PLCController.readData_200(0,PlcMemory.DR,DataType.BYTE8,(short)solution,(short)count);
			if(readData instanceof Object[]){
				readDataArr=(Object[]) readData;				
			}else{
				readDataArr[0]=(byte)-1;//读失败
			}
			return readDataArr;
		}
		readDataArr[0]=(byte)-1;
		return readDataArr;
	}

	//完成当前执行中的作业
	public void finishCurrentWorkStep(WorkStep workStep,Integer outputStep) {	
		workStep.setWorkStatue(2);//设置作业为已完成
		workStep.setOutputStoreTime(new Date());//操作结束时间
		workStepDao.updateWorkStep(workStep);
		switch (workStep.getWorkType()) {
		case 0:
			//入库(肖子凡)
			FinishWorkToolZZ.inStore(workStep.getOrderNo(), inputStoreDao,  storeHouseRecordDao, storeHouseDao);
			break;
		case 1:
			//出库
			FinishWorkToolZZ.outStore(workStep.getOrderNo(), outputStoreDao,  storeHouseRecordDao, storeHouseDao,outputStep,shA,shB,workStepDao);
			break;
		case 2:
			//移库 (肖子凡)
			FinishWorkToolZZ.moveStore(workStep.getOrderNo(), moveStoreDao,  storeHouseRecordDao, storeHouseDao);
			break;
		case 3:
			//调库
			FinishWorkToolZZ.changeStore(workStep.getOrderNo(), changeStoreDao,  storeHouseRecordDao, storeHouseDao);
			break;
	/*	case 4:
			//直接出库
			FinishWorkToolZZ.outStoreDirect(workStep.getOrderNo(),  storeHouseDao);
			break;*/
		}

	}

	public void insertRsr(String barCode,String orderId,int workType,String workId,Integer runstep2 ){
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

	public  void  updateWorkStepStatus(WorkStep ws){
		/*	WorkStep wstemp=new WorkStep();
		wstemp.setCount(ws.getCount());
		wstemp.setFringeCode(ws.getFringeCode());
		wstemp.setGetPlace(ws.getGetPlace());
		wstemp.setInputStoreTime(new Date());
		wstemp.setInsertTime(ws.getInsertTime());
		wstemp.setOrderId(ws.getOrderId());
		wstemp.setOrderNo(ws.getOrderNo());
		wstemp.setOutputStoreTime(ws.getOutputStoreTime());
		wstemp.setPutPlace(ws.getPutPlace());
		wstemp.setRebotCode(ws.getRebotCode());
		wstemp.setScanTime(ws.getScanTime());
		wstemp.setWorkId(ws.getWorkId());
		wstemp.setWorkStatue(1);
		wstemp.setWorkType(ws.getWorkType());*/
		ws.setWorkStatue(1);// 设置为执行中
		ws.setInputStoreTime(new Date());//操作开始时间
		workStepDao.updateWorkStep(ws);	
	}

	public int getCount(int workType){
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("workType", workType);
		params.put("workStatue", 0);//不是完成状态
		int count = workStepDao.selectWorkStepCountByStatus(params);
		return count;
	}


}
