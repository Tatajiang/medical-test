package com.jiang.medical.platform.system.condition;

import com.homolo.framework.dao.hibernate.HibernateCondition;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * 平台角色管理条件查询类
 * @author li
 * @date 2014-03-17
 * @version 1.0
 */
public class SysRolesCondition implements HibernateCondition {
	
	private String name;	//角色名称
	
	private String neId;	//排除本身
	
	private String id;		//本身id
	
	private String level;	//级别
	
	private Boolean flage;	//状态
	
	@Override
	public void populateDetachedCriteria(DetachedCriteria criteria) {
		
		//super.populateDetachedCriteria(criteria);
		
		if(StringUtils.isNotBlank(name)) {
			criteria.add(Restrictions.like("name", name, MatchMode.ANYWHERE));
		}
		
		if(StringUtils.isNotBlank(neId)) {
			criteria.add(Restrictions.ne("id", neId));
		}
		
		if(StringUtils.isNotBlank(id)) {
			criteria.add(Restrictions.eq("id", id));
		}
		
		if(null != level){
			criteria.add(Restrictions.eq("level", level));
		}
		
		if(null != flage){
			criteria.add(Restrictions.eq("flage", flage));
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNeId(String neId) {
		this.neId = neId;
	}
	
	public void setLevel(String level) {
		this.level = level;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setFlage(Boolean flage) {
		this.flage = flage;
	}

}
