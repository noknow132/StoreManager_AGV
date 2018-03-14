package com.thread;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dao.IChangeStoreDao;
import com.dao.IConfigParamDao;
import com.dao.IOutputStoreDao;
import com.entity.ConfigParam;
import com.util.plcconn.ConnDataStr;
import com.util.plcconn.DataType;
import com.util.plcconn.PLCConfig;
import com.util.plcconn.PLCController;
import com.util.plcconn.PlcMemory;
@Component("warnThreadPLC")
@Scope("prototype")
public class WarnThreadPLC  extends Thread{
	public volatile boolean exit = false;
	@Resource
	private IConfigParamDao configParamDao;
	
	private int warn;//缓存数据库
	private int warnb;//缓存plc
	@Override
	public void run(){
		ConfigParam cfp = null;
		try{
			while(!exit){
				int checkNum = PLCConfig.CheckConnect2(0,ConnDataStr.plc_200Smart_1,ConnDataStr.plc_200Smart_2);
				if(checkNum<0){
					continue;
				}
				Object[] readData = (Object[])PLCController.readData_200(0,PlcMemory.DR,DataType.BYTE8,(short)40,(short)1);	
				cfp = configParamDao.selectConfigParamOne();
				int warnb2=(byte)readData[0] & 0xFF;
				if(warnb2!=0&&warnb2!=warnb){
					cfp.setWarn(warnb2);
					cfp.setIsRun(0);
					configParamDao.updateConfigParam(cfp);
					warnb=warnb2;
					warn=warnb2;
				}
				int warn2=cfp.getWarn();
				if(warn2==0&&warn2!=warn){
					PLCController. wirteData(0,PlcMemory.DR,DataType.BYTE8,(short)41,(short)1,new byte[]{1});
					warn=warn2;
					warnb=warn2;
					  cfp = configParamDao.selectConfigParamOne();
						if(cfp.getIsRun()==0){
							break;
						}
				}
				Thread.sleep(1000);
			}
		}catch(Exception e){
			
		}
	}

	public int getWarn() {
		return warn;
	}

	public void setWarn(int warn) {
		this.warn = warn;
	}
	
	public int getWarnb() {
		return this.warnb;
	}

	public void setWarnb(int warnb) {
		this.warnb = warnb;
	}
}
