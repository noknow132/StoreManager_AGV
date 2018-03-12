/**  
 * Project Name:SocketChat  
 * File Name:AuthorizationUtils.java  
 * Package Name:com.regedit.demo  
 * Date:2018年1月27日下午2:45:32  
 * Copyright (c) 2018, chenzhou1025@126.com All Rights Reserved.  
 *  
*/  
  
package com.util;  

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** 
 * java获取mac和机器码，注册码的实现 
 *  
 */  
public class AuthorizationUtils {  
    public static void main1(String args[]) throws Exception {  
//    	System.out.println("本机物理地址:"+getMac());  
    	String code = getMachineCode();  
    	//code = code.substring(0, code.length()).replace('/', '2').replace('$', '1').replace('.', '0');
//        System.out.println("运行环境编码：" + code);  
        
//        String authCode = auth(code);
//        //authCode = authCode.substring(0, authCode.length()).replace('/', '2').replace('$', '1').replace('.', '0');
//        System.out.println("注册码：" + authCode);
    }  
    
//    public static String auth(String machineCode) {  
//        //String newCode = "(email)["+ machineCode.toUpperCase() + "]";
//    	String newCode = machineCode.toUpperCase();
//        String code = new BCryptPasswordEncoder().encode(newCode).toUpperCase() ;//+ machineCode.length();  
//        return changeUnicode(getSplitString(code, "-", 4));
//    }

  //检查注册文件是否存在，返回文件内容
    public static String[] createRegFile(String path,List<String> info){
//    	String ip = getServerIp();
    	String[] value = new String[2];
    	if(info.size()>0){
    		try {
    			path = path+"storeRegedit.txt"; 
	    		File f = new File(path);      
		        if (!f.exists()){       
		            f.createNewFile();      
		        }      
		        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"gbk");      
		        BufferedWriter out=new BufferedWriter(write);   
		        String envo = getMachineCode();
		        for (int i = 0; i < info.size(); i++) {
		        	out.write(info.get(i)+"\r\n"); 
				}
		        out.write("Mac：" + getMac()+"\r\n"); 
		        out.write("申请码：" + envo); 
		        out.close();
		        value[0] = envo;
		        value[1] = path;
		        return value;
	        } catch (Exception e) {
	        	e.printStackTrace();
			}  
    	}
    	return null;
    }
    
//    /**
//     * 获取服务器IP地址
//     * @return
//     */
//    public static String getServerIp(){
//        String SERVER_IP = null;
//        try {
//            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
//            InetAddress ip = null;
//            while (netInterfaces.hasMoreElements()) {
//                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
//                ip = (InetAddress) ni.getInetAddresses().nextElement();
//                SERVER_IP = ip.getHostAddress();
//                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
//                        && ip.getHostAddress().indexOf(":") == -1) {
//                    SERVER_IP = ip.getHostAddress();
//                    break;
//                }else{
//                    ip = null;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return SERVER_IP;
//    }
    
    //检查注册文件是否存在，返回文件内容
    public static List<String> checkRegFile(String path){
    	List<String> regInfo = new ArrayList<String>();
    	File[] f = new File(path).listFiles();
		File file = null;
		for (int i = 0; i < f.length; i++) {
			String filename = f[i].getName();
			if(filename.indexOf(".")>=0){
				String lastname = filename.substring(filename.lastIndexOf(".")+1);
				if(lastname.equals("eeclass")){
					file = f[i];
					break;
				}
			}
		}
		if(file == null){
			return null;
		}
		try {
	        InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"GBK"); 
	        BufferedReader br = new BufferedReader(reader);
	        String line = "";
	        while (br.ready()) {
	        	line = br.readLine();
	        	if(line.indexOf("：")>0){
	        		line = line.trim().substring(line.indexOf("：")+1);
	        		regInfo.add(line);
	        	}
	        }
	        br.close();
	        reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regInfo;
    }
    
    
    //检查申请码文件是否存在
    public static boolean checkApplyFile(String path){
    	//List<String> regInfo = new ArrayList<String>();
    	boolean flag=false;
    	File[] f = new File(path).listFiles();
		for (int i = 0; i < f.length; i++) {
			String filename = f[i].getName();
			if(filename.equals("storeRegedit.txt")){
				flag=true;
				break;
			}
		}
		return flag;
    }
    
    
    
    public static String getMac() {  
        try {  
            Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();  
            while (el.hasMoreElements()) {  
                byte[] mac = el.nextElement().getHardwareAddress();  
                if (mac == null)continue;  
                String hexstr = bytesToHexString(mac);  
                return hexstr.toUpperCase();//getSplitString(hexstr, "-", 2).toUpperCase();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }
    
    public static String getMachineCode() {  
        Set<String> result = new HashSet<>();  
        String mac = getMac();  
//        System.out.println("mac:" + getMac());  
        result.add(mac);  
        Properties props = System.getProperties();  
        String javaVersion = props.getProperty("java.version");  
        result.add(javaVersion);  
        // System.out.println("Java的运行环境版本：    " + javaVersion);  
        String javaVMVersion = props.getProperty("java.vm.version");  
        result.add(javaVMVersion);  
        // System.out.println("Java的虚拟机实现版本：" + props.getProperty("java.vm.version"));  
        String osVersion = props.getProperty("os.version");  
        result.add(osVersion);  
        // System.out.println("操作系统的版本：" + props.getProperty("os.version"));  
        
        String code = new BCryptPasswordEncoder().encode(result.toString());
        return changeUnicode(getSplitString(code, "-", 4));  
  
    }  
  
    private static String getSplitString(String str, String split, int length) {  
        int len = str.length();  
        StringBuilder temp = new StringBuilder();  
        for (int i = 0; i < len; i++) {  
            if (i % length == 0 && i > 0) {  
                temp.append(split);  
            }  
            temp.append(str.charAt(i));  
        }  
        String[] attrs = temp.toString().split(split);  
        StringBuilder finalMachineCode = new StringBuilder();  
        for (String attr : attrs) {  
            if (attr.length() == length) {
                finalMachineCode.append(attr).append(split);
            }  
        }  
        String result = finalMachineCode.toString().substring(0,  
                finalMachineCode.toString().length() - 1); 
        return result;  
    }  
  
    public static String bytesToHexString(byte[] src) {  
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {
                stringBuilder.append(0);  
            }
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString();  
    }  
  
    private static String changeUnicode(String str){
    	return str.substring(0, str.length()).replace('/', '2').replace('$', '1').replace('.', '0');
    }
}  
  
