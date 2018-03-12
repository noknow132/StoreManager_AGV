package com.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.dao.IPlcConnConfigDao;
import com.entity.PlcConnConfig;
import com.service.IPlcConnConfigService;
import com.util.plcconn.PLCController;

@Service
public class PlcConnConfigServiceImpl implements  IPlcConnConfigService{
	@Autowired
	private IPlcConnConfigDao plcConnConfigDao;

	//手动测试连接(新增或修改IP地址到数据库)
	@Override
	@Transactional
	public Map<String, Object> saveIp(String localIp, String PLCIP, int PLCPort, String PLCId) {
		Map<String,Object> result = new HashMap<String,Object>();
		PlcConnConfig plcConfig = plcConnConfigDao.searchPLCById(PLCId);
		try {
			plcConfig.setLocalIp(localIp);
			plcConfig.setPlcIp(PLCIP);
			plcConfig.setPlcPort(PLCPort);
			plcConnConfigDao.updatePlcConnConfig(plcConfig);
			Object stus = plcConnById(PLCId).get("stus");//测试连接PLC
			result.put("stus", stus);
		} catch (Exception e) {
			result.put("stus", 500);//代码出错
			e.printStackTrace();	
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

	//启动 连接plc
	@Override
	public Map<String, Object> connectPLC() {
		Map<String,Object> result = new HashMap<String,Object>();
		List<PlcConnConfig> plcConfigList = plcConnConfigDao.searchPlcConnConfigAll();
		try {
			//首先判断配置参数是否正确
			for (int i = 0; i < plcConfigList.size(); i++) {
				//判断配置是否填写
				if(plcConfigList.get(i).getPlcIp() == null || plcConfigList.get(i).getPlcPort() == null){
					result.put("stus", 300);//请检查系统配置参数是否正确
					return result;
				}
				//再判断连接是否正常
				boolean flag = PLCController.PlcConn(Integer.parseInt(plcConfigList.get(i).getPicId()),plcConfigList.get(i).getPlcIp(), plcConfigList.get(i).getPlcPort());
				result.put("stus", flag);//true PLC连接成功，false PLC连接失败
				if(flag == false){
					return result;
				}
			}
		} catch (Exception e) {
			result.put("stus", 500);//代码出错
			e.printStackTrace();			
		}
		return result;
	}

	//通过plc名称连接plc
	@Override
	public Map<String, Object> plcConnById(String plcId) {
		Map<String,Object> result = new HashMap<String,Object>();
		PlcConnConfig plcConfig = plcConnConfigDao.searchPLCById(plcId);
		if (plcConfig.getPlcIp() == null || plcConfig.getPlcPort() == null) {
			result.put("stus", 300);// 请检查系统配置参数是否正确
		} else {
			boolean flag =false;
//		    if(plcId.equals("1")){
//		    	flag= PLCController.PlcConn(1,plcConfig.getPlcIp(), plcConfig.getPlcPort());
//		    }else{
//		    	flag= PLCController.PlcConn(0,plcConfig.getPlcIp(), plcConfig.getPlcPort());
//		    }
		    flag= PLCController.PlcConn(Integer.parseInt(plcId),plcConfig.getPlcIp(), plcConfig.getPlcPort());
			result.put("stus", flag);// true PLC连接成功，false PLC连接失败
		}
		return result;
	}

	//自动测试连接
	@Override
	public Map<String, Object> testConnect() {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			Object stus = connectPLC().get("stus");//测试连接PLC
			result.put("stus", stus);
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
		}
		return result;
	}

	//通过plc名称查找plc配置
	@Override
	public PlcConnConfig searchPlcConfigByName(String plcName) {
		return plcConnConfigDao.searchPLCByName(plcName);
	}

	//新增plc连接配置
	@Override
	@Transactional
	public Map<String, Object> insertPlcConfig(PlcConnConfig plcConnConfig) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			int m = 0;
			if(m == 0){
				PlcConnConfig plc = new PlcConnConfig();
				plc.setPicId(UUID.randomUUID().toString().replace("-", ""));
				plc.setPlcName(plcConnConfig.getPlcName());
				plc.setLocalIp(plcConnConfig.getLocalIp());
				plc.setPlcIp(plcConnConfig.getPlcIp());
				plc.setPlcPort(plcConnConfig.getPlcPort());
				plc.setRefresh(plcConnConfig.getRefresh());
				plcConnConfigDao.insertPlcConfig(plc);
			}else{
				PlcConnConfig plc = plcConnConfigDao.searchPLCById(plcConnConfig.getPicId());
				plc.setPlcName(plcConnConfig.getPlcName());
				plc.setLocalIp(plcConnConfig.getLocalIp());
				plc.setPlcIp(plcConnConfig.getPlcIp());
				plc.setPlcPort(plcConnConfig.getPlcPort());
				plc.setRefresh(plcConnConfig.getRefresh());
				plcConnConfigDao.updatePlcConnConfig(plc);
			}
			result.put("stus", 200);
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

	//删除plc连接配置
	@Override
	@Transactional
	public Map<String, Object> deletePlcConfigById(String id) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			plcConnConfigDao.deletePlcConfigById(id);
			result.put("stus", 200);
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

	//通过plc类型查找plc配置
	@Override
	public List<PlcConnConfig> searchPlcConnConfigByType(String plcType) {
		return plcConnConfigDao.searchPlcConnConfigByType(plcType);
	}

}
