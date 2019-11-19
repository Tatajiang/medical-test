package com.jiang.medical.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ListUtil {

	/**
     * 将List<Object>转换为List<Map<String,Object>>
     * @param list
     * @return
     */
    private List<Map<String,Object>> convertListMap(List<Object> list){
        List<Map<String,Object>> maps=new ArrayList<Map<String,Object>>();
        for(Object obj:list){
            Class c = obj.getClass();
            Field[] f = c.getDeclaredFields();
            Map<String,Object> map=new HashMap<String, Object>();
            for(Field fie : f){
                try {
                    fie.setAccessible(true);//取消语言访问检查
                    map.put(fie.getName(), fie.get(obj));//获取私有变量值
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            //获取父类的私有属性
            for(Field fie : c.getSuperclass().getDeclaredFields()){
                try {
                    fie.setAccessible(true);//取消语言访问检查
                    map.put(fie.getName(), fie.get(obj));//获取私有变量值
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            maps.add(map);
        }
        return maps;
    }
    
    
    /**
	 * List<实体>转为list<Object>
	 *
	 * @param t
	 * @return
	 */
	public static List<Object> beanToObject(List<?> t) {
		List<Object> o = new ArrayList<Object>();
		if (t != null) {
			Iterator<?> it = t.iterator();
			while (it.hasNext()) {
				o.add(it.next());
			}
		}
		return o;
	}

	/**
	 * List<objcet> 转为List<Map>
	 *
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static List<Map> beanToMap(List<Object> object) throws Exception {
		List<Map> maps = new ArrayList<Map>();
		if (object != null) {
			for (Object o : object) {
				String s = JSON.toJSONString(o);
				Map map = JSONObject.parseObject(s, Map.class);

				maps.add(map);
			}
		}

		return maps;
	}

	/**
	 * bean 转Map
	 * 
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> objectToMap(Object obj) throws Exception {
		if (obj == null)
			return null;

		Map<String, Object> map = new HashMap<String, Object>();

		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			if (key.compareToIgnoreCase("class") == 0) {
				continue;
			}
			Method getter = property.getReadMethod();
			Object value = getter != null ? getter.invoke(obj) : null;
			map.put(key, value);
		}

		return map;
	}

	
}
