/**
 * Project : police-affairs-webapp
 * FileName: PropertyFileUtil.java
 * Copyright (c) 2018, www.narwell.com All Rights Reserved.
 */
package com.jiang.medical.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author lq
 * Email: xiaolei_java@163.com 2018年11月22日 上午10:21:48
 */
public class PropertyFileUtil {

	/***
	 * 获得properties对象
	 * @param propertiesFileName
	 * @return
	 */
	public static Properties getProperties(String propertiesFileName){
		Properties prop = new Properties(); 
        try { 
        	InputStream in = PropertyFileUtil.class.getResourceAsStream(propertiesFileName); 
        	if(in == null){
        		in = PropertyFileUtil.class.getClassLoader().getResourceAsStream(propertiesFileName);
        	}
        	if(in != null){
        		prop.load(in); 
        	}
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return prop;
	}
	
	/***
	 * 根据properties文件的key获取值
	 * @param propertiesFileName
	 * @param key
	 * @return
	 */
	public static String getPropertiesValueByKey(String propertiesFileName, String key){
		Properties prop = getProperties(propertiesFileName);
		if(prop != null){
			return prop.getProperty(key);
		}
		return "";
	}
	
	public static Map<String, String> getPropertiesValues(String fileName){
		Map<String, String> data = new HashMap<>();
		
		Properties prop = getProperties(fileName);
		if(prop != null){
			for(Object set:prop.keySet()){
				data.put(set.toString(), prop.getProperty(set.toString()));
			}
		}
		return data;
	}
	
	public static void main(String[] args) {
		Map<String, String> map = PropertyFileUtil.getPropertiesValues("app.properties");
		for(Map.Entry<String, String> entry:map.entrySet()){
			System.out.println(entry.getKey() + "---" + entry.getValue());
		}
	}
}
