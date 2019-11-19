/**
 * Project : yitong-portals
 * FileName: HttpRequest.java
 * Copyright (c) 2015, www.narwell.com All Rights Reserved.
 */
package com.jiang.medical.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;

/**
 * http请求页面，返回json对象
 * @author lq
 * Email: xiaolei_java@163.com 2015-11-10 下午4:39:50
 */
@SuppressWarnings("deprecation")
public class HttpRequestUtil {
	
	 private static Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);    //日志记录
	 
	 /**
	  * 请求方式.
	  */
	 public static enum Method {
		 POST,
		 GET,
		 PUT,
		 DELETE;
	 }
	 
	/**
     * httpPost
     * @param url  路径
     * @param jsonParam 参数
     * @return
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam){
        return httpPost(url, jsonParam, false);
    }
 
    /**
     * post请求
     * @param url         url地址
     * @param jsonParam     参数
     * @param noNeedResponse    不需要返回结果
     * @return
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam, boolean noNeedResponse){
        //post请求返回结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url);
        try {
            if (null != jsonParam) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
                    if (noNeedResponse) {
                        return null;
                    }
                    /**把json字符串转换成json对象**/
                    jsonResult = JSONObject.fromObject(str);
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url, e);
        }
        return jsonResult;
    }

    
    public static JSONObject httpPost(String url,String param){
        //post请求返回结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        try {
        	url = URLDecoder.decode(url, "UTF-8");
            HttpPost method = new HttpPost(url + "?" + param);
            HttpResponse result = httpClient.execute(method);
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
                    /**把json字符串转换成json对象**/
                    jsonResult = JSONObject.fromObject(str);
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url, e);
        }
        return jsonResult;
    }
    
    public static JSONObject httpPostArray(String url,String param){
        //post请求返回结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        try {
        	url = URLDecoder.decode(url, "UTF-8");
            HttpPost method = new HttpPost(url + "?" + param);
            HttpResponse result = httpClient.execute(method);
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
                    /**把json字符串转换成json对象**/
                    JSONArray jsa = JSONArray.fromObject(str);
                    jsonResult =  jsa.getJSONObject(0);
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url, e);
        }
        return jsonResult;
    }
    
    /**
     * 
     * @Description 根据接出警编码或者案件编码查询出案件详情
     * @param url
     * @param param
     * @return 
     * @return JSONObject  
     * @author yuanguo.huang
     * @date 2018-10-26 下午4:56:58
     */
    public static JSONObject httpPostForJSONArray(String url,String param){
        //post请求返回结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url + "?" + param);
        try {
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
                    /**把json字符串转换成json对象**/
                    JSONArray json = JSONArray.fromObject(str);
                    JSONObject jso =  json.getJSONObject(0);
                    jsonResult = jso.getJSONObject("dataList");
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url, e);
        }
        return jsonResult;
    }
    
    /**
     * 发送get请求
     * @param url    路径
     * @return
     */
    public static JSONObject httpGet(String url){
        //get请求返回结果
        JSONObject jsonResult = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
 
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                /**把json字符串转换成json对象**/
                jsonResult = JSONObject.fromObject(strResult);
                url = URLDecoder.decode(url, "UTF-8");
            } else {
                logger.error("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            logger.error("get请求提交失败:" + url, e);
        }
        return jsonResult;
    }
    
    /**
     * @Description 获取电话号码信息 
     * @param url
     * @return JSONObject  
     * @author yuanguo.huang
     * @date 2016-12-28 下午2:35:28
     */
    public static JSONObject httpGetByPhoneUrl(String url){
        //get请求返回结果
        JSONObject jsonResult = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
 
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                String[] str = strResult.split("=");
                String strend = str[1];
                /**把json字符串转换成json对象**/
                jsonResult = JSONObject.fromObject(strend);
                url = URLDecoder.decode(url, "UTF-8");
            } else {
                logger.error("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            logger.error("get请求提交失败:" + url, e);
        }
        return jsonResult;
    }
    
    public static String http(Method method, String url, JSONObject jsonParam, Map<String, String> headers){
    	String result = "";
    	try{
    		if(method == null) method = Method.GET;
    		
    		if(method == Method.POST){
    			result = httpPOST(url, jsonParam, headers);
    		}else if(method == Method.GET){
    			result = httpGET(url, jsonParam, headers);
    		}
    	}catch (Exception e) {
			logger.error("请求服务器发生错误：" + e.getMessage());
		}
    	return result;
    }
    
    /**
     * http post
     * @Description 
     * @param url
     * @param jsonParam
     * @param headers
     * @return 
     * @return String  
     * @author leijing
     * @date 2018年11月21日 上午10:14:43
     */
    @SuppressWarnings("resource")
	public static String httpPOST(String url, JSONObject jsonParam, Map<String, String> headers){
    	String responseResult = "";
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	HttpPost method = new HttpPost(url);
    	try{
    		StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
            entity.setContentEncoding("UTF-8");
    		method.setEntity(entity);
    		
    		for(Map.Entry<String, String> entry:headers.entrySet()){
    			String name = entry.getKey();
    			String value = entry.getValue();
    			
    			method.setHeader(name, value);
    		}
    		
    		HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                responseResult = EntityUtils.toString(result.getEntity());
            }else{
            	logger.error("POST请求提交失败:" + url);
            }
    	}catch (Exception e) {
    		logger.error("POST请求提交失败:" + url, e);
		}
    	return responseResult;
    }
    
    /**
     * http GET
     * @Description 
     * @param url
     * @param jsonParam
     * @param headers
     * @return 
     * @return String  
     * @author leijing
     * @date 2018年11月21日 上午10:43:31
     */
    @SuppressWarnings({ "unchecked", "resource" })
	public static String httpGET(String url, JSONObject jsonParam, Map<String, String> headers){
    	String responseResult = "";
    	DefaultHttpClient client = new DefaultHttpClient();
    	HttpGet method = new HttpGet();
    	try{
    		HttpParams params = new BasicHttpParams();
    		
    		Iterator<String> it = jsonParam.keys();
    		while(it.hasNext()){
    			String key = it.next();
    			Object value = jsonParam.get(key);
    			params.setParameter(key, value);
    		}
    		method.setParams(params);
    		
    		for(Map.Entry<String, String> entry:headers.entrySet()){
    			String name = entry.getKey();
    			String value = entry.getValue();
    			
    			method.setHeader(name, value);
    		}
    		HttpResponse result = client.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                responseResult = EntityUtils.toString(result.getEntity());
            }else{
            	logger.error("GET请求提交失败:" + url);
            }
    	}catch (Exception e) {
    		logger.error("GET请求提交失败:" + url, e);
		}
    	return responseResult;
    }
    
    public static void main(String[] args) {
    	String url = "http://api.cellocation.com:81/cell/?mcc=460&mnc=1&lac=29463&ci=225555972&output=json";
		JSONObject result = HttpRequestUtil.httpGet(url);
		System.out.println(result.toString());
	}
}
