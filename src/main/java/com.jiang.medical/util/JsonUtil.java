package com.jiang.medical.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.util.JSONTokener;

import java.lang.reflect.Field;
import java.util.*;

public class JsonUtil {
	
	private static JsonConfig configJson(String[] excludes) {   
		 
        JsonConfig jsonConfig = new JsonConfig();   

        jsonConfig.setExcludes(excludes);   

        jsonConfig.setIgnoreDefaultExcludes(false);   

        return jsonConfig;   

    }  
	
	/**
	 * 将字符串转换成JSON
	 * 
	 * @param str
	 * @return
	 */
	public static JSONObject getJsonStr(String str) {
		JSONTokener jsonParser = new JSONTokener(str);
		JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
		return jsonObject;
	}
	
	@SuppressWarnings("rawtypes")
	public static Object getBeanFromJson(String jsonStr, Class beanClass) {
		// 直接先将json字符串转换成JSONObject
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(jsonStr);
		Object obj = JSONObject.toBean(jsonObject, beanClass);
		return obj; // 转换成需要的实体返回
	}

	/**
	 * 将传入的json字符串对象转换成需要的实体对象
	 * 
	 * @param source
	 *            传入json中的字符串
	 * @param beanClass
	 *            实体类的类型
	 * @return object
	 */

	public static Object getBeanFromJsonStr(String jsonStr, @SuppressWarnings("rawtypes") Class beanClass) {
		// 直接先将json字符串转换成JSONObject
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(jsonStr);
		Object obj = JSONObject.toBean(jsonObject, beanClass);
		return obj; // 转换成需要的实体返回
	}

	/**
	 * 将JSONArray转换成listBean
	 * 
	 * @param JSONArray
	 *            需要反序列化的Json数组
	 * @param clazz
	 *            被反序列化之后的类
	 * @return 实体list
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List getListFromJsonArr(JSONArray jsonArr, Class<?> clazz) {
		List list = new ArrayList();
		for (int i = 0; i < jsonArr.size(); i++) {
			list.add(JSONObject.toBean(jsonArr.getJSONObject(i), clazz)); // 将jsonArray转换成我们需要的集合实体
		}
		return list;
	}

	
	/**  
     * 将java对象转换成json字符串  
     *
     * @param bean  
     * @return  
     */
    public static String beanToJson(Object bean) {
 
        JSONObject json = JSONObject.fromObject(bean);
         
        return json.toString();
 
    }
    
    /**  
     * 将java对象转换成json字符串  
     *
     * @param bean  
     * @return  
     */
    public static String beanToJson(Object bean, String[] _nory_changes, boolean nory) {
 
        JSONObject json = null;
         
        if(nory){//转换_nory_changes里的属性
             
            Field[] fields = bean.getClass().getDeclaredFields();
            String str = "";
            for(Field field : fields){

                str+=(":"+field.getName());
            }
            fields = bean.getClass().getSuperclass().getDeclaredFields();
            for(Field field : fields){
//              System.out.println(field.getName());
                str+=(":"+field.getName());
            }
            str+=":";
            for(String s : _nory_changes){
                str = str.replace(":"+s+":", ":");
            }
            json = JSONObject.fromObject(bean,configJson(str.split(":")));
             
        }else{//转换除了_nory_changes里的属性
            json = JSONObject.fromObject(bean,configJson(_nory_changes));
        }
         
        return json.toString();
    }
	/**
     * 
     * @param beans
     * @param _no_changes
     * @return
     */
   
    @SuppressWarnings("rawtypes")
	public static String beanListToJson(List beans, String[] _nory_changes, boolean nory) {
         
        StringBuffer rest = new StringBuffer();
         
        rest.append("[");
         
        int size = beans.size();
         
        for (int i = 0; i < size; i++) {
            try{
                rest.append(beanToJson(beans.get(i),_nory_changes,nory));
                if(i<size-1){
                    rest.append(",");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
         
        rest.append("]");
         
        return rest.toString();
 
    }
    /**  
     * 从json HASH表达式中获取一个map，改map支持嵌套功能  
     *
     * @param jsonString  
     * @return  
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Map jsonToMap(String jsonString) {
         
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        Iterator keyIter = jsonObject.keys();
        String key;
        Object value;
        Map valueMap = new HashMap();
 
        while (keyIter.hasNext()) {
             
            key = (String) keyIter.next();
            value = jsonObject.get(key).toString();
            valueMap.put(key, value);
             
        }
 
        return valueMap;
    }
     
    /**
     * map集合转换成json格式数据
     * @param map
     * @return
     */
    public static String mapToJson(Map<String, ?> map, String[] _nory_changes, boolean nory){
         
        String s_json = "{";
         
         Set<String> key = map.keySet();
         for (Iterator<?> it = key.iterator(); it.hasNext();) {
             String s = (String) it.next();
             if(map.get(s) == null){
                  
             }else if(map.get(s) instanceof List<?>){
                 s_json+=(s+":"+JsonUtil.beanListToJson((List<?>)map.get(s), _nory_changes, nory));
                 
             }else{
                 JSONObject json = JSONObject.fromObject(map);
                 s_json += (s+":"+json.toString());;
             }
              
             if(it.hasNext()){
                 s_json+=",";
             }
        }
 
         s_json+="}";
        return s_json; 
    }
    

	/**
	 * 序列化操作，无论是单个的对象，还是list，抑或是list中的属性仍包含list，都可以直接序列化成String类型
	 * 
	 * @param obj
	 *            需要被序列化的对象
	 * @return 序列化之后的字符串
	 */

	@SuppressWarnings("unchecked")
	public static String getJsonStrFromEntity(Object obj) {
		// 返回結果字符串
		String jsonStr = null;
		// 判断传入的对象是否为空
		if (obj == null) {
			return "{}";
		}

		// Json配置
		JsonConfig jsonCfg = new JsonConfig();

		// Integer默认值
		jsonCfg.registerDefaultValueProcessor(Integer.class,  
		    new DefaultValueProcessor() {
				@Override
				public Object getDefaultValue(Class type) {
					//return JSONNull.getInstance(); 
					return null;
				}
			}
		);

		// 判断是否是list
		if (obj instanceof Collection || obj instanceof Object[]) {
			jsonStr = JSONArray.fromObject(obj, jsonCfg).toString();
		} else {
			jsonStr = JSONObject.fromObject(obj, jsonCfg).toString();
		}
		return jsonStr;
	}
}