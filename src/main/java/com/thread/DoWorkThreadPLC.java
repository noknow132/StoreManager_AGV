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
import com.entity.WorkStep;
import com.util.plcconn.ConnDataStr;
import com.util.plcconn.DataType;
import com.util.plcconn.PLCConfig;
import com.util.plcconn.PLCController;
import com.util.plcconn.PlcMemory;

@Component("doWorkThreadPLC")
@Scope("prototype")
public class DoWorkThreadPLC extends Thread{

	volatile private int preWorkType=-1;//前一次的作业类型
    private int isScan=0;//前一次的作业类型
    
    private int chainPlc=0;  //入库开启输送链plc

	private WorkStep workStep;
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

	public DoWorkThreadPLC() {
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
		int  count=0;//当前类型作业条数  主要是在入库时使用
		while(true){
			//System.out.println("dowork");
			ConfigParam cp = configParamDao.selectConfigParamOne();//plc启动还是关闭
			Object[] readData = isCanRead();
			if((byte)readData[0]==-2){
				System.out.println("报警break");
				if(preWorkType==0) {//入库
					FinishWorkTool.chainPlc((byte)0);//输送链停止
					chainPlc=0;
				}
				break;//出现报警
			}
			if((byte)readData[0]==-1){
				System.out.println("读失败");
				continue;
			}
			int[] mod = FinishWorkTool.getMod((byte)readData[0]);//获取作业类型及步骤
			//添加0到8运行步骤记录
			if(preMod3!=mod[2]&&mod[2]!=9){
				preMod3=mod[2];
				if(workStep!=null){
					String barCode=workStep.getFringeCode();//货物码
					String orderId=workStep.getOrderId();//订单id
					String workId=workStep.getWorkId();
					int workType=workStep.getWorkType();//作业类型
					FinishWorkTool.insertRsr(barCode, orderId, workType, workId,mod[2], runStepDao, runStepRecordDao);
				}
			}
			if(mod[2]!=9){
				continue;
			}
			

			if(mod[1]!=0){
				count=(count>1?count-1:count);
			}
			preMod3=-1;
			if((mod[0]==1&&mod[1]==3)||(mod[0]!=1)){
				if((workStep!=null&&workStep.getWorkType()!=0)||(workStep!=null&&workStep.getWorkType()==0&&workStep.getScanTime()!=null)){
					finishCurrentWorkStep(workStep);	//完成上条的作业
				}
				if(cp.getIsRun()==0){
					System.out.println("手动暂停");
					break;
				}
			}			
			WorkStep ws =null;
			if(mod[0]==1&&mod[1]!=3){
				ws=workStep;
			}else{
			Map<String, Object> params=new HashMap<String, Object>();
			List <Integer> listParmas=new ArrayList<Integer>();
		   if(preWorkType==-1||preWorkType==0){//(刚开始启动  或一种作业类型完成了的时候 )或是入库的时候
				listParmas.add(0);
				listParmas.add(3);
				//count=workStepDao.selectWorkStepCountByStatus(count);
			}else{
				listParmas.add(0);
			}
			System.out.println("preWorkType:"+preWorkType);
						
			params.put("workType", preWorkType);
			params.put("list", listParmas);
			ws = workStepDao.searchWorkStepByStatueAndType(params);
		
			System.out.println("ws:"+ws);
			workStep=ws;//缓存上一条作业
			}
			
			if (ws!=null) {
				int workType=ws.getWorkType();//作业类型
				if(preWorkType==-1){//刚开始启动  或一种作业类型完成了的时候
					count=getCount(workType);
				}
				if(chainPlc!=50&&workType==0){//上一次不是入库作业   这次是入库作业  ????暂时没考虑报警情况
					while(!FinishWorkTool.chainPlc((byte)50)){ 
						chainPlc=50;//可能要更改  不知道
						continue;
					}
				}
			/*	if(preWorkType==0&&workType!=0){//上一次是入库作业   这次不是入库作业  ????暂时没考虑报警情况
					if(isScan!=0){
						isScan=0;
						cp.setIsScan(isScan);
						configParamDao.updateConfigParam(cp);
					}
					while(!FinishWorkTool.chainPlc((byte)0)){
						continue;
					}
				}*/
				preWorkType=workType;//缓存当前状态	
				byte[] data=new byte[10];
				System.out.println("ws.getWorkStatue()---:"+ws.getWorkStatue());
				String putPlace = ws.getPutPlace();
				String getPlace = ws.getGetPlace();
				FinishWorkTool.setByteValue(data,putPlace,getPlace,createStoreAreaDao,storeHouseDao);//为byte数组赋值
				if(ws.getWorkType()==1){//出库 
					String orderNo = ws.getOrderNo();
					OutLet outLet = outLetDao.selectOutLetByOutputStoreNo(orderNo);
					byte[] dataC=new byte[10];
					FinishWorkTool.copyByteArr(data,dataC);
					dataC[0]=30;
					dataC[5]=-2;//放货位 
					if(PLCConfig.CheckConnect2(0,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2)<0){
						continue;
					}
					if(FinishWorkTool.OutputTableHasGood()==1){
					PLCController.InOrOurStore(dataC);
					FinishWorkTool.chainPlc(outLet.getOutNo().byteValue());
					System.out.println("进行中");
					updateWorkStepStatus(ws);//更改状态为执行中
					}
				}else  if(ws.getWorkType()==0){//入库
					System.out.println("入库");
					if(isScan!=1){
						isScan=1;
						cp.setIsScan(isScan);
						configParamDao.updateConfigParam(cp);
					}
					if(ws.getScanTime()!=null){
						data[0]=20;
						if(PLCConfig.CheckConnect2(0,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2)<0||PLCConfig.CheckConnect2(1,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2)<0){//如果连接断了
							continue;
						}
						if(FinishWorkTool.ArrivalTableHasGood()<0){//读取入货口出现异常   这也应该会出现报警？？？？
							continue;
						}						
						PLCController.InOrOurStore(data);
						updateWorkStepStatus(ws);//更改状态为执行中
						count=(count==1?getCount(workType):count);//二次查询  确保这一条作业了
					  if(count==1){//入库作业只有当前这一条时  在写入堆垛机后
							if(isScan!=0){
								isScan=0;
								cp.setIsScan(isScan);
								configParamDao.updateConfigParam(cp);
							}
							FinishWorkTool.chainPlc((byte)0);//让输送链停止工作
							chainPlc=0;
							}
					}
				}else if(ws.getWorkType()==2){   //移库
					data[0]=40;
					if(PLCConfig.CheckConnect2(0,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2)<0){
						continue;
					}
					PLCController.InOrOurStore(data);
					updateWorkStepStatus(ws);//更改状态为执行中
				}else{//调库
					if(mod[0]==0||mod[1]==3){//第一步出库
					byte[] dataC=new byte[10];
					FinishWorkTool.copyByteArr(data,dataC);
					dataC[0]=110;
					dataC[2]= (byte) ((byte)"A".charAt(0)+((byte)"A".charAt(0)%2==0?128:0));
					dataC[5]=-1;//堆垛机放层
					dataC[6]=-1;//堆垛机放列
					if(PLCConfig.CheckConnect2(0,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2)<0){
						continue;
					}
					PLCController.InOrOurStore(dataC);
					updateWorkStepStatus(ws);//更改状态为执行中
					}else if(mod[1]==1){//第二步  出库入库
					byte[] dataCR=new byte[10];
					dataCR[0]=120;
					dataCR[1]=data[2];
					dataCR[2]=data[1];
					dataCR[3]=data[5];
					dataCR[4]=data[6];
					dataCR[5]=data[3];
					dataCR[6]=data[4];
					dataCR[7]=data[7];
					if(PLCConfig.CheckConnect2(0,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2)<0){
						continue;
					}
					PLCController.InOrOurStore(dataCR);
					}else if(mod[1]==2){//第三步 入库
						byte[] dataR=new byte[10];
						dataR[0]= (byte) 130;
						//dataR[1]=data[1];
						dataR[2]=data[2];
						dataR[1]=(byte) ((byte)"A".charAt(0)+((byte)"A".charAt(0)%2==0?128:0));

						dataR[3]=-1;
						dataR[4]=-1;
						dataR[5]=data[5];
						dataR[6]=data[6];
						dataR[7]=data[7];
						if(PLCConfig.CheckConnect2(0,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2)<0){
							continue;
						}
						PLCController.InOrOurStore(dataR);
					}
				}
			}else{
		/*		if(preWorkType==0){//上一次是入库作业   这次不是入库作业  ????暂时没考虑报警情况
					if(isScan!=0){
						isScan=0;
						cp.setIsScan(isScan);
						configParamDao.updateConfigParam(cp);
					}
					while(!FinishWorkTool.chainPlc((byte)0)){
						continue;
					}
				}*/
				preWorkType=-1;
				count=0;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
   //判断是否能读
	private Object[] isCanRead(){
		int checkNum = PLCConfig.CheckConnect2(0,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2);
		Object[] readDataArr=new Object[1];
		if(checkNum==0){
			Object readData2 = PLCController.readData_200(0,PlcMemory.DR,DataType.BYTE8,(short)40,(short)1);
			if(readData2 instanceof Object[]){
				Object[] readData2By = (Object[])readData2;	
				if(((byte)readData2By[0] & 0xFF)>0){
					readDataArr[0]=(byte)-2;//出现报警
					return readDataArr;
				}
			}else{
				readDataArr[0]=(byte)-1;//读失败
				return readDataArr;
			}
          
			Object readData = PLCController.readData_200(0,PlcMemory.DR,DataType.BYTE8,(short)10,(short)1);
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
	public void finishCurrentWorkStep(WorkStep workStep) {	
		workStep.setWorkStatue(2);//设置作业为已完成
		workStep.setOutputStoreTime(new Date());//操作结束时间
		workStepDao.updateWorkStep(workStep);
		switch (workStep.getWorkType()) {
		case 0:
			//入库(肖子凡)
			FinishWorkTool.inStore(workStep.getOrderNo(), inputStoreDao,  storeHouseRecordDao, storeHouseDao);
			break;
		case 1:
			//出库
			FinishWorkTool.outStore(workStep.getOrderNo(), outputStoreDao,  storeHouseRecordDao, storeHouseDao);
			break;
		case 2:
			//移库 (肖子凡)
			FinishWorkTool.moveStore(workStep.getOrderNo(), moveStoreDao,  storeHouseRecordDao, storeHouseDao);
			break;
		case 3:
			//调库
			FinishWorkTool.changeStore(workStep.getOrderNo(), changeStoreDao,  storeHouseRecordDao, storeHouseDao);
			break;
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
