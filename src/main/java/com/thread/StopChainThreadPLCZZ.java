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

@Component("stopChainThreadPLCZZ")
@Scope("prototype")
public class StopChainThreadPLCZZ extends Thread{
	
	@Resource
	private IOutinPlaceDao outinPlaceDao;
	
	public StopChainThreadPLCZZ() {
		super();
	}
	public void run() {
		
	}		

}
