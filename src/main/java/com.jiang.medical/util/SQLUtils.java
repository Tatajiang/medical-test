/**
 * Project : datamanage
 * FileName: SQLUtils.java
 * Copyright (c) 2015, www.narwell.com All Rights Reserved.
 */
package com.jiang.medical.util;

import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 公用的sql执行方法
 * @author tong.luo
 */
@Component
public class SQLUtils {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 获取当前session
	 * @return
	 */
	public Session getSession() {    
        return sessionFactory.getCurrentSession();    
    }
	
	/**
	 * 执行查询语句
	 * @param sql
	 * @return
	 */
	public List<?> querySql(String sql) throws JDBCException {
		Query query = getSession().createSQLQuery(sql);
        return query.list();
    }
	
	/**
	 * 执行其它增删改
	 * @param sql
	 * @return
	 */
	public int excuteSql(String sql) throws JDBCException{    
        SQLQuery query = this.getSession().createSQLQuery(sql);    
        return query.executeUpdate();  
    }
	
	/**
	 * 如果使用占位符就调用该方法
	 * @param sql
	 * @param bean
	 * @return
	 */
	public List<?> querySql(String sql, Map<String, Object> bean) throws JDBCException{
		Query query = getSession().createSQLQuery(sql);
		int i = 0;
        Set<Map.Entry<String, Object>> set = bean.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            query.setParameter(i, entry.getValue());
            i++;
        }
        return query.list();
	}
	
	/**
	 * 如果使用占位符就调用该方法
	 * @param sql
	 * @param bean
	 * @return
	 */
	public int excuteSql(String sql, Map<String, Object> bean) throws JDBCException{
		Query query = getSession().createSQLQuery(sql);
		int i = 0;
        Set<Map.Entry<String, Object>> set = bean.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            query.setParameter(i, entry.getValue());
            i++;
        }
        return query.executeUpdate();
	}
}
