/**  
 * Project Name:FaceDistinguish  
 * File Name:TempServiceImpl.java  
 * Package Name:com.service.impl  
 * Date:2017年11月30日上午10:06:10  
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.  
 *  
 */  

package com.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.dao.IChangeStoreDao;
import com.dao.IConfigParamDao;
import com.dao.ICreateStoreHouseDao;
import com.dao.IInputStoreDao;
import com.dao.IMoveStoreDao;
import com.dao.IOrderDetailDao;
import com.dao.IOutputStoreDao;
import com.dao.IStoreHouseDao;
import com.dao.IStoreHouseRecordDao;
import com.dao.IWorkStepDao;
import com.entity.ChangeStore;
import com.entity.ConfigParam;
import com.entity.InputStore;
import com.entity.MoveStore;
import com.entity.OutputStore;
import com.entity.StoreHouse;
import com.entity.StoreHouseRecord;
import com.entity.WorkStep;
import com.service.IWorkStepService;
import com.thread.ApplicationContextProvider;
import com.thread.DoWorkThreadPLC;
import com.thread.DoWorkThreadPLCZZ;
import com.thread.FinishWorkTool;

import com.thread.WarnThreadPLC;
import com.util.plcconn.ConnDataStr;
import com.util.plcconn.PLCConfig;
import com.util.plcconn.PLCController;


/**
 * @author 罗欢欢
 * @date 2018-1-15
 * @remark 当前作业对应的业务逻辑实现
 */
@Service
public class WorkStepServiceImpl implements IWorkStepService {

	@Resource
	private IOutputStoreDao outputStoreDao;
	@Autowired
	private IInputStoreDao inputStoreDao;
	@Resource
	private IChangeStoreDao changeStoreDao;
	@Autowired
	private IMoveStoreDao moveStoreDao;
	@Resource
	private IStoreHouseDao storeHouseDao;
	@Resource
	private IWorkStepDao workStepDao;
	@Resource
	private IConfigParamDao configParamDao;
	@Resource
	private ICreateStoreHouseDao createStoreHouseDao;
	@Resource
	private IStoreHouseRecordDao storeHouseRecordDao;
	@Resource
	private IOrderDetailDao orderDetailDao;

	//根据状态查找作业
	@Override
	public Map<String, Object> searchWorkStepsByWorkStatue( Integer workStatue,Integer workType,String no) {
		Map<String, Object> result=new HashMap<String, Object>();
		try{
			List<Map<String, Object>> list = workStepDao.selectWorkStepsByWorkStatue(workStatue,workType,no);
			if(list!=null && list.size()>0){
				result.put("list",list);
			}
			result.put("code",200);
		}catch(Exception e){
			e.printStackTrace();
			result.put("code",500);
		}
		return result;
	}

	//作业实体初始化
	public WorkStep getWorkStep(StoreHouse sh,String rebotCode,String orderNo,String  toStoreId
			){
		//System.out.println("作业实体初始化");
		WorkStep ws=new WorkStep();
		String workId = UUID.randomUUID().toString().replaceAll("-", "");
		ws.setWorkId(workId);
		ws.setCount(sh.getCount());
		ws.setFringeCode(sh.getGoodNo());
		//ws.setGetPlace(sh.getStoreNo());//取货位
		ws.setGetPlace(sh.getStoreId());
		ws.setOrderId(sh.getOrderId());
		ws.setPutPlace(null);//出货位
		ws.setWorkType(3);
		ws.setWorkStatue(0);
		ws.setRebotCode(rebotCode);
		ws.setOrderNo(orderNo);
		return ws;
	}

	public String getNo(){
		Date now =new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String no = sdf.format(now);
		return no;
	}

	//删除未完成的作业
	@Transactional
	@Override
	public Map<String, Object> delWorkStepsUnfinished(String[] workIds) {
		Map<String, Object> result=new HashMap<String, Object>();
		try{
			for(String workId :workIds){
				WorkStep ws  = workStepDao.selectWorkStepByWorkId(workId);
				Integer workType = ws.getWorkType();
				String orderNo = ws.getOrderNo();
				switch(workType){
				case 0:recallInputStore(orderNo);break;
				case 1:recallOuputStore(orderNo);break;
				case 2:recallMoveStore(orderNo);break;
				case 3:recallChangeStore(orderNo);break;
				}
			}
			this.workStepDao.delectWorkStepsByWorkIds(workIds);
			result.put("code",200);
		}catch(Exception e){
			e.printStackTrace();
			result.put("code",500);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

	//入库撤回
	public  void  recallInputStore(String orderNo){		 
		InputStore ins = this.inputStoreDao.selectInpuStoreByNo(orderNo);
		StoreHouse storeHouse = storeHouseDao.selectByStoreId(ins.getStoreId());
		storeHouse.setStoreStatue(0);//仓库修改为空
		storeHouseDao.updateStoreHouse(storeHouse);
		//修改入库为删除
		ins.setStatue(5);
		inputStoreDao.updateInputStore(ins);
	}
	
	//出库撤回
	public  void  recallOuputStore(String orderNo){		 
		OutputStore ous = this.outputStoreDao.selectOutputStoreByNo(orderNo);
		ous.setStatue(5);//出库设置为删除
		StoreHouse storeHouse = storeHouseDao.selectByStoreId(ous.getStoreId());
		storeHouse.setStoreStatue(2);//仓库修改为有货
		storeHouseDao.updateStoreHouse(storeHouse);
	}
	
	//调库撤回
	public  void  recallChangeStore(String orderNo){		 
		ChangeStore chs = this.changeStoreDao.selectChangeStoreByNo(orderNo);
		chs.setStatue(5);//出库单状态更改为删除
		changeStoreDao.updateCSByCSIdSelective(chs);
		StoreHouse fromsh = this.storeHouseDao.selectByStoreId(chs.getStoreIdFrom());
		StoreHouse tosh = this.storeHouseDao.selectByStoreId(chs.getStoreIdTo());
		fromsh.setStoreStatue(2);//恢复仓位的状态
		tosh.setStoreStatue(2);
		storeHouseDao. updateStoreHouse(fromsh);
		storeHouseDao. updateStoreHouse(tosh);
	} 
	
	//移库撤回
	public  void  recallMoveStore(String orderNo){		 
		MoveStore mos = moveStoreDao.selectMoveStoreByNo(orderNo);
		mos.setStatus(5);//删除移库
		moveStoreDao.updateMoveStore(mos);
		/**当前仓位改为空**/
		StoreHouse storeHouse = storeHouseDao.selectByStoreId(mos.getStoreIdTo());
		storeHouse.setStoreStatue(0);//仓库改为空
		storeHouseDao.updateStoreHouse(storeHouse);

		StoreHouse storeHouseFrom = storeHouseDao.selectByStoreId(mos.getStoreIdFrom());
		storeHouseFrom.setStoreStatue(2);//仓库改为占用
		storeHouseDao.updateStoreHouse(storeHouseFrom);
	} 

	//删除已完成的作业
	@Transactional
	@Override
	public Map<String, Object> delWorkStepsFinished(String[] workIds) {
		Map<String, Object> result=new HashMap<String, Object>();
		try{
			workStepDao.delectWorkStepsByWorkIds(workIds);//批量删除作业
			result.put("code",200);
		}catch(Exception e){
			e.printStackTrace();
			result.put("code",500);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		}
		return result;
	}

	//完成当前执行中的作业
	@Override
	@Transactional
	public void finishCurrentWorkStep() {	
//		int[] data = PLCController.getStartState();
//		if(data[1] == 0){
			//查找执行中的作业（结果仅有一条）
			List<WorkStep> list = workStepDao.searchWorkStepByStatue("1");
			if(list.size() != 0){
				WorkStep workStep = list.get(0);
				workStep.setWorkStatue(2);//设置作业为已完成
				workStep.setOutputStoreTime(new Date());//操作结束时间
				workStepDao.updateWorkStep(workStep);
				switch (workStep.getWorkType()) {
				case 0:
					//入库(肖子凡)
					InputStore inputStore = inputStoreDao.selectInpuStoreByNo(workStep.getOrderNo());//根据单号查找 入库
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
					break;
				case 1:
					//出库
					OutputStore ous = outputStoreDao.selectOutputStoreByNo(workStep.getOrderNo());
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
					break;
				case 2:
					//移库 (肖子凡)
					MoveStore moveStore = moveStoreDao.selectMoveStoreByNo(workStep.getOrderNo());
					moveStore.setStatus(3);//单据状态 已完成
					moveStoreDao.updateMoveStore(moveStore);

					//起始仓库
					StoreHouse storeHouseFrom = storeHouseDao.selectByStoreId(moveStore.getStoreIdFrom());
					//目标仓库
					StoreHouse storeHouseTo = storeHouseDao.selectByStoreId(moveStore.getStoreIdTo());

					int fromCount = storeHouseFrom.getCount();
					String fromUnit = storeHouseFrom.getUnit();
					String toUnit = storeHouseTo.getUnit();
					String orderId2 = storeHouseFrom.getOrderId();
					String goodNo2 = storeHouseFrom.getGoodNo();

					storeHouseFrom.setStoreStatue(0);//起始仓库 更改为空闲
					storeHouseFrom.setCount(0);//起始仓库 数量为0
					storeHouseFrom.setUnit(toUnit);//起始仓库 单位为目标仓库单位
					storeHouseFrom.setOrderId("");//起始仓库 订单置空
					storeHouseFrom.setGoodNo("");//起始仓库 商品条码 置空
					storeHouseDao.updateStoreHouse(storeHouseFrom);


					storeHouseTo.setStoreStatue(2);//目标仓库更改为有货
					storeHouseTo.setCount(fromCount);//目标仓库 数量为起始仓库数量
					storeHouseTo.setUnit(fromUnit);//目标仓库 单位为起始仓库单位
					storeHouseTo.setOrderId(orderId2);//目标仓库 订单号为起始仓库订单号
					storeHouseTo.setGoodNo(goodNo2);//目标仓库 条形码为起始仓库条形码
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
					//storeHouseRecord2.setCount(orderDetail2.getCount());
					//storeHouseRecord2.setOrderId(moveStore.getOrderId());
					//storeHouseRecord2.setOrderDetailId(moveStore.getOrderDetailId());
					//storeHouseRecord2.setUnit(orderDetail2.getUnit());
					storeHouseRecordDao.insertStoreHouseRecord(storeHouseRecord2);
					break;
				case 3:
					//调库
					ChangeStore chs = this.changeStoreDao.selectChangeStoreByNo(workStep.getOrderNo());
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

					shTo.setCount(count3);
					shTo.setGoodNo(goodNo3);
					shTo.setOrderDetailId(orderDetailId3);
					shTo.setOrderId(orderId3);
					shTo.setUnit(unit3);
					shTo.setStoreStatue(2);//有货状态
					storeHouseDao.updateStoreHouse(shTo);			

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
					//				storeHouseRecord3.setCount(shFrom.getCount());
					//				storeHouseRecord3.setOrderId(shFrom.getOrderId());
					//				storeHouseRecord3.setOrderDetailId(shFrom.getOrderDetailId());
					//				storeHouseRecord3.setUnit(shrFrom.getUnit());
					storeHouseRecordDao.insertStoreHouseRecord(storeHouseRecord3);
					break;
				default:
					break;
				}
				//将最新的一条【待执行】作业变更为【执行中】
				/*		List<WorkStep> list2 = workStepDao.searchWorkStepByStatue("0");
			if(list2.size() != 0){
				WorkStep workStep2 = list2.get(0);
				operateStore( workStep2);
			}*/
			}
		
	}

	//取消当前作业
	@Override
	@Transactional
	public void cancleCurrentWorkStep() {
		// 查找执行中的作业（结果仅有一条）
		List<WorkStep> list = workStepDao.searchWorkStepByStatue("1");
		if(list.size() !=0){
			WorkStep workStep = list.get(0);
			workStepDao.delectWorkStepByWorkId(workStep.getWorkId());//删除作业


			switch (workStep.getWorkType()) {
			case 0:
				// 入库(肖子凡)
				InputStore inputStore = inputStoreDao.selectInpuStoreByNo(workStep.getOrderNo());// 根据单号查找 入库
				inputStore.setStatue(4);// 单据状态 异常关闭
				inputStoreDao.updateInputStore(inputStore);

				// 查找当前入库的仓库
				StoreHouse storeHouse = storeHouseDao.selectByStoreId(inputStore.getStoreId());
				storeHouse.setStoreStatue(0);// 入库的仓库为空闲
				storeHouseDao.updateStoreHouse(storeHouse);
				break;
			case 1:
				// 出库
				OutputStore ous = outputStoreDao.selectOutputStoreByNo(workStep.getOrderNo());
				ous.setStatue(4);// 单据状态 异常关闭
				outputStoreDao.updateOutputStoreByIdSelective(ous);
				//查找当前出库的仓库
				StoreHouse shOus= storeHouseDao.selectByStoreId(ous.getStoreId());
				shOus.setStoreStatue(2);// 入库的仓库为有货
				break;
			case 2:
				// 移库 (肖子凡)
				MoveStore moveStore = moveStoreDao.selectMoveStoreByNo(workStep.getOrderNo());
				moveStore.setStatus(4);// 单据状态 异常关闭
				moveStore.setMoveTime(new Date());
				moveStoreDao.updateMoveStore(moveStore);

				// 起始仓库(有)
				StoreHouse storeHouseFrom = storeHouseDao.selectByStoreId(moveStore.getStoreIdFrom());
				storeHouseFrom.setStoreStatue(2);// 起始仓库更改为有货
				storeHouseDao.updateStoreHouse(storeHouseFrom);

				// 目标仓库(无)
				StoreHouse storeHouseTo = storeHouseDao.selectByStoreId(moveStore.getStoreIdTo());
				storeHouseTo.setStoreStatue(0);// 目标仓库更改为空闲
				storeHouseDao.updateStoreHouse(storeHouseTo);

				break;
			case 3:
				// 调库
				ChangeStore chs = this.changeStoreDao.selectChangeStoreByNo(workStep.getOrderNo());
				chs.setStatue(4);// 单据状态 异常关闭
				changeStoreDao.updateCSByCSIdSelective(chs);		

				StoreHouse shFrom = storeHouseDao.selectByStoreId(chs.getStoreIdFrom());	//起始仓库
				StoreHouse shTo=storeHouseDao.selectByStoreId(chs.getStoreIdTo());   //目标仓库

				shFrom.setStoreStatue(2);//有货状态
				shTo.setStoreStatue(2);//有货状态

				storeHouseDao.updateStoreHouse(shFrom);
				storeHouseDao.updateStoreHouse(shTo);			

				break;
			default:
				break;
			}
			//		// 将最新的一条【待执行】作业变更为【执行中】
			//		List<WorkStep> list2 = workStepDao.searchWorkStepByStatue("0");
			//		WorkStep workStep2 = list2.get(0);
			//		workStep2.setWorkStatue(1);// 设置为执行中
			//		workStep2.setInputStoreTime(new Date());
			//		workStepDao.updateWorkStep(workStep2);
		}
	}
	
	//根据执行状态查找作业
	@Override
	public List<WorkStep> searchWorkStepByStatue(String status) {
		return workStepDao.searchWorkStepByStatue(status);
	}
	
    //获取plc中的当前信息
	@Override
	public Map<String, Object> getNowPlace() {
		Map<String ,Object> result=new HashMap<String,Object>();
		/*int isConnect = PLCConfig.CheckConnect2(0,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2);
		if(isConnect<0){
			//当连接失败的时 把数据库中作业中的作业改成等待中
			List<WorkStep> list2 = workStepDao.searchWorkStepByStatue("1");
			if(list2!=null&& list2.size()>0){
				for(int i=0;i<list2.size();i++){
					WorkStep workStep = list2.get(i);
					workStep.setWorkStatue(0);
					System.out.println("获取plc中的当前信息");
					workStepDao.updateWorkStep(workStep);
				}
			}
			result.put("code", 404);//连接失误
			return result;
		}
		try{
			if(isConnect>=0){
				int[] nowPlace = PLCController.getNowPlace();				
				if(nowPlace[0]>=0){
					result.put("currentL", nowPlace[2]>127?((256-nowPlace[2])):nowPlace[2]);//当前列
					result.put("currentC", nowPlace[1]>127?(nowPlace[1]==255?"取货口":"放货口"):nowPlace[1]);//当前层
					result.put("getH", (nowPlace[4]==0? "0" : (char)(nowPlace[4]>128?nowPlace[4]-128:nowPlace[4])));//取行
					result.put("getL", nowPlace[7]>127?(256-nowPlace[7]):nowPlace[7]);//取列
					result.put("getC", nowPlace[6]>127?(nowPlace[6]==255?"取货口":"放货口"):nowPlace[6]);//取层
					result.put("putH", (nowPlace[5]==0?"0":(char)(nowPlace[5]>128?nowPlace[5]-128:nowPlace[5])));//放行
					result.put("putL", nowPlace[9]>127?((256-nowPlace[9])):nowPlace[9]);//放列
					result.put("putC", nowPlace[8]>127?(nowPlace[8]==255?"取货口":"放货口"):nowPlace[8]);//放层
					result.put("currentStart", nowPlace[0]);//启动
					int[] mod = FinishWorkTool.getMod((byte)nowPlace[3]);
					result.put("workStatus1", mod[1]);//启动
					result.put("workStatus2", mod[2]);//启动
//					System.out.println("getNowPlace#########:"+mod[1]);
//					System.out.println("getNowPlace#########:"+mod[2]);

					result.put("code", 200);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result.put("code", 404);//连接失误

		}*/
		return result;
	}

	//启动机器，执行任务(PLC)
	public Map<String, Object> startWorkStep(String workId ) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			//查找执行中的作业（结果仅有一条）
			List<WorkStep> list = workStepDao.searchWorkStepByStatue("1");
			if(list.size() == 0){				
				Map<String,Object> params=new HashMap<String,Object>() ;
				List list3=new ArrayList();
				list3.add(0);//等待中的
				list3.add(3);//
				params.put("list", list3);
				List<WorkStep> list2 = workStepDao.searchWorkStepByStatues(params);
				if (list2.size() != 0) {
					ConfigParam configParam = configParamDao.selectConfigParamOne();
					configParam.setIsRun(1);
					configParamDao.updateConfigParam(configParam);
					//WarnThreadPLC watp= ApplicationContextProvider.getBean("warnThreadPLC", WarnThreadPLC.class);
					DoWorkThreadPLCZZ dwtp= ApplicationContextProvider.getBean("doWorkThreadPLCZZ", DoWorkThreadPLCZZ.class);
					ConfigParam cfg = this.configParamDao.selectConfigParamOne();
				/*	watp.setWarn(cfg.getWarn());
					watp.setWarnb(0);
					watp.start();*/
					dwtp.start();
					result.put("stus", 200);//操作成功
				}else{
					result.put("stus", 300);//没有可操作的任务
				}
			}else{
				result.put("stus", 301);//当前存在执行的任务
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("stus", 500);
		}
		return result;
	}

	//根据扫描的入库作业  改变作业状态
	@Override
	public Map<String, Object> changeWorkStatus(String fringeCode, int workStatue) {
		Map<String ,Object> params =new HashMap<String ,Object>();//查询参数集合
		Map<String ,Object> result =new HashMap<String ,Object>();//返回的执行结果
		params.put("fringeCode", fringeCode);
		params.put("workType", 0);

		WorkStep ws = workStepDao.selectWorkStepByFringeCode(params);
		try{
			if(ws!=null){
				ConfigParam cf = configParamDao.selectConfigParamOne();
				if(cf.getIsScan()==1){
					ws.setWorkStatue(3);
					ws.setScanTime(new Date());
					workStepDao.updateWorkStep(ws);
					result.put("code", 202);
				}			
				result.put("code", 200);
			}else{
				result.put("code", 404);//找不到此货物码的入库单
			}

		}catch(Exception e){
			e.printStackTrace();
			result.put("code", 500);
		}
		return result;
	}
}

