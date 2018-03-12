package com.service.impl;

import java.io.File;
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
import com.dao.IInputStoreDao;
import com.dao.IMoveStoreDao;
import com.dao.IOutputStoreDao;
import com.dao.IStoreHouseDao;
import com.dao.IWarnRecordDao;
import com.dao.IWorkStepDao;
import com.entity.ChangeStore;
import com.entity.ConfigParam;
import com.entity.InputStore;
import com.entity.MoveStore;
import com.entity.OutputStore;
import com.entity.StoreHouse;
import com.entity.WarnRecord;
import com.entity.WorkStep;
import com.service.IConfigParamService;
import com.service.IWorkStepService;
import com.util.AuthorizationUtils;
import com.util.plcconn.PLCController;

@Service
public class ConfigParamServiceImpl implements IConfigParamService{
	@Autowired
	private IConfigParamDao configParamDao;
	@Autowired
	private IWorkStepService workStepService;
	@Resource
	private IOutputStoreDao outputStoreDao;
	@Resource
	private IChangeStoreDao changeStoreDao;
	@Autowired
	private IInputStoreDao inputStoreDao;
	@Autowired
	private IMoveStoreDao moveStoreDao;
	@Autowired
	private IWarnRecordDao warnRecordDao;
	@Autowired
	private IWorkStepDao workStepDao;
	@Autowired
	private IStoreHouseDao storeHouseDao;
	//查询参数配置
		@Override
		public Map<String, Object> initByOnekey() {
			Map<String,Object> result = new HashMap<String,Object>();
			try {
				configParamDao.initByOnekey();
				result.put("stus", 200);//名称已被修改
			} catch (Exception e) {
				result.put("stus", 500);
				e.printStackTrace();
			}
			return result;
		}
		
	//修改系统名称
	@Override
	@Transactional
	public Map<String, Object> updateConfigParamName(String name) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			ConfigParam configParam = configParamDao.selectConfigParamOne();
			if(configParam.getProgramName() == null || configParam.getProgramName().equals("")) {
				configParam.setProgramName(name);
				configParamDao.updateConfigParam(configParam);
				result.put("systemName", name);
				result.put("stus", 200);
				return result;
			}
			result.put("stus", 201);//名称已被修改
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return result;
	}

	//查询参数配置
	@Override
	public ConfigParam selectConfigParamOne() {
		return configParamDao.selectConfigParamOne();
	}

	//报警系统
	@Override
	@Transactional
	public Map<String,Object> warn() {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			ConfigParam configParam = configParamDao.selectConfigParamOne();
			String warnCode = "0";//默认正常
			switch (configParam.getWarn()) {
			case 1:
				//取货位无货(取消作业，手动修改库位数据)
				warnCode = "1";
				break;
			case 2:
				//放货位有货(完成作业，堆垛机数据复位，手动修改库位数据)
				warnCode = "2";
				break;
			case 3:
				//启动时堆垛机有货(完成作业，堆垛机数据复位)
				warnCode = "3";
				break;
			case 4:
				//货物歪斜(完成作业，警告窗)
				warnCode = "4";
				break;
			case 5:
				//安全刹车报警(警告窗提示机器硬件故障)
				warnCode = "5";
				break;
			default:
				break;
			}
			//判断报警是否被记录
			if((configParam.getWarnTag() == null || configParam.getWarnTag() == 0) && !warnCode.equals("0")){
				//查找当前任务
				List<WorkStep> workStepList = workStepDao.searchWorkStepByStatue("1");
				if(workStepList.size() != 0 && workStepList.size() == 1){
					WorkStep workStep = workStepList.get(0);
					//添加报警记录
					WarnRecord warnRecord = new WarnRecord();
					warnRecord.setWarnRecordId(UUID.randomUUID().toString().replaceAll("-", ""));
					warnRecord.setWarnType(Integer.parseInt(warnCode));
					warnRecord.setWorkType(workStep.getWorkType());
					//根据作业类型不同，查找对应的仓库
					if(workStep.getWorkType() == 0 ){
						//入库订单
						InputStore inputStore = inputStoreDao.selectInpuStoreByNo(workStep.getOrderNo());
						warnRecord.setBarCode1(inputStore.getBarCode());//条形码
					}else if(workStep.getWorkType() == 1 ){
						//出库
						OutputStore outputStore = outputStoreDao.selectOutputStoreByNo(workStep.getOrderNo());
						warnRecord.setBarCode1(outputStore.getBarCode());//条形码
					}else if(workStep.getWorkType() == 2 ){
						//移库
						MoveStore moveStore = moveStoreDao.selectMoveStoreByNo(workStep.getOrderNo());
						StoreHouse storeHouse = storeHouseDao.selectByStoreId(moveStore.getStoreIdFrom());
						warnRecord.setBarCode1(storeHouse.getGoodNo());//条形码
					}else if(workStep.getWorkType() == 3 ){
						//调库
						ChangeStore changeStore = changeStoreDao.selectChangeStoreByNo(workStep.getOrderNo());
						StoreHouse storeHouse1 = storeHouseDao.selectByStoreId(changeStore.getStoreIdFrom());
						StoreHouse storeHouse2 = storeHouseDao.selectByStoreId(changeStore.getStoreIdTo());
						warnRecord.setBarCode1(storeHouse1.getGoodNo());//条形码
						warnRecord.setBarCode2(storeHouse2.getGoodNo());//条形码
					}
					warnRecord.setWarnTime(new Date());
					warnRecordDao.insertWarnRecord(warnRecord);
					
					configParam.setWarnTag(1);//标记为已记录报警
					configParamDao.updateConfigParam(configParam);
					
					result.put("warnRecordId", warnRecord.getWarnRecordId());
				}
			}
			result.put("warnCode", warnCode);
		} catch (Exception e) {
			result.put("warnCode", 500);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
		}
		return result;
	}

	//入口复位
	@Override
	@Transactional
	public Map<String, Object> reset() {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			ConfigParam configParam = configParamDao.selectConfigParamOne();
			int resetValue = configParam.getReset();//复位状态
			if(resetValue == 0){
				configParam.setReset(1);//正在复位
			}
			configParamDao.updateConfigParam(configParam);
			result.put("stus", 200);
			result.put("resetValue", resetValue);
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return result;
	}

	//解决报警
	@Override
	@Transactional
	public Map<String, Object> solveWarn(String warnCode,String warnRecordId) {
		Map<String,Object> result = new HashMap<String,Object>();
		ConfigParam configParam = configParamDao.selectConfigParamOne();
		WarnRecord warnRecord = warnRecordDao.searchLastWarnRecord();
		warnRecord.setResolveTime(new Date());//设置报警的解决时间
		warnRecordDao.updateWarnRecord(warnRecord);
		try {
			if(configParam.getWarn() != 0){
				configParam.setWarn(0);//解除报警 正常！！！
				configParam.setWarnTag(0);//初始化报警标识
				configParamDao.updateConfigParam(configParam);
				switch (Integer.parseInt(warnCode)) {
					case 1:
						//取货位无货(取消作业，手动修改库位数据)
						workStepService.cancleCurrentWorkStep(); 
						break;
					case 2:
						//放货位有货(完成作业，堆垛机数据复位，手动修改库位数据)
						workStepService.cancleCurrentWorkStep();
						PLCController.resetPlace();
						break;
					case 3:
						//启动时堆垛机有货(完成作业，堆垛机数据复位)
						workStepService.cancleCurrentWorkStep();
						PLCController.resetPlace();
						break;
					case 4:
						//货物歪斜(完成作业，警告窗)
						workStepService.cancleCurrentWorkStep();
						break;
					case 5:
						//安全刹车报警(警告窗提示机器硬件故障)
						workStepService.cancleCurrentWorkStep();
						break;
					default:
						break;
				}
			}
				
			result.put("stus", 200);
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}


	//手动判断注册码
	public Map<String, Object> createRegFile(List<String> info) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			ConfigParam configParam = configParamDao.selectConfigParamOne();
			if(configParam.getRegEdit()==null||configParam.getRegEdit().equals("")){
				//获取当前文件的所在路径
				File f2 = new File(this.getClass().getResource("").getPath());
		        String projectDir = f2.toString().substring(0,f2.toString().indexOf("WEB-INF"));
		        String[] value = AuthorizationUtils.createRegFile(projectDir, info);  //检查注册文件是否存在，返回文件内容
		        result.put("path", value[1].substring(value[1].lastIndexOf("\\")+1));
				configParam.setRegEdit(value[0]);//保存生成的注册码
				configParamDao.updateConfigParam(configParam);
			}
			result.put("stus", 200);//注册码正确
		} catch (Exception e) {
			e.printStackTrace();	
			result.put("stus", 500);
		}
		return result;
	}
	
	//自动判断注册码
	@Override
	public Map<String, Object> autoRegEdit() {
		Map<String,Object> result = new HashMap<String,Object>();
//		try {
			ConfigParam configParam = configParamDao.selectConfigParamOne();
			result.put("configParam", configParam);
			//获取当前文件的所在路径
			File f2 = new File(this.getClass().getResource("").getPath());
	        String projectDir = f2.toString().substring(0,f2.toString().indexOf("WEB-INF"));
	        List<String> regInfo = AuthorizationUtils.checkRegFile(projectDir); //检查注册文件是否存在，返回文件内容
	        if(regInfo != null && regInfo.size()>0){
//	        	if(!configParam.getMac().equals(regInfo.get(regInfo.size()-3).substring(4))
//	        			||!configParam.getMac().equals(AuthorizationUtils.getMac())){
//	        		return null;
//	        	}
				if(configParam.getRegEdit().equals(regInfo.get(regInfo.size()-2))){
				   configParam.setRegEditCopy(regInfo.get(regInfo.size()-1));
				   configParamDao.updateConfigParam(configParam);
				   result.put("stus", 200);//申请码正确
				}else{
					if(AuthorizationUtils.checkApplyFile(projectDir)){
			        	result.put("stus", 301);//已有申请码了 未有注册码
		        	}else{
			        	result.put("stus", 300);//尚未申请码
		        	}
		        }
	        }else{  
	        	if(AuthorizationUtils.checkApplyFile(projectDir)){
		        	result.put("stus", 301);//已有申请码了 未有注册码
	        	}else{
		        	result.put("stus", 300);//尚未申请注册码
	        	}
	        }
//		} catch (Exception e) {
//			result.put("stus", 500);
//			e.printStackTrace();
//		}
		return result;
	}

    //修改plc的启动暂停
	@Override
	public Map<String, Object> updateConfigParamIsRun(Integer isRun) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			//查找执行中的作业（结果仅有一条）
		/*	List<WorkStep> list = workStepDao.searchWorkStepByStatue("1");
			if(list.size() == 0){*/	
				ConfigParam configParam = configParamDao.selectConfigParamOne();
				configParam.setIsRun(isRun);
				configParamDao.updateConfigParam(configParam);
				result.put("stus", 200);
		/*	}else{
				result.put("stus", 301);
			}	*/	
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}


}
