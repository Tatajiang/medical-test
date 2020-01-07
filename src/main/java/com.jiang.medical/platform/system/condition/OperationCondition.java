package com.jiang.medical.platform.system.condition;

import com.homolo.framework.dao.hibernate.HibernateCondition;
import com.jiang.medical.platform.system.domain.Operation;
import com.jiang.medical.platform.system.domain.Operation.Type;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/* *
 * @Description: 平台资源管理条件查询类
 * @Param:
 * @return:
 * @Author: zhantuo.jiang
 * @date: 2019/12/24 14:13
 */
public class OperationCondition  implements HibernateCondition {
	
	private List<String> listId;	//资源id集合
	
	private Operation.Type type;	//资源类型
	
	private String parentId;	//上级id
	
	private String name;	//资源名字
	
	private String url;
	
	private Boolean isDefaultOperation = false;	//获得默认菜单资源
	
	private Boolean getDefaultOperations = false;	//获得默认菜单资源条件二
	
	private Boolean isDefault = false;	//得到所有默认显示的菜单，以及它的上级菜单
	
	@Override
	public void populateDetachedCriteria(DetachedCriteria criteria) {
		if(isDefaultOperation) {	//查找某个资源的直接子资源+默认显示的资源
			criteria.add(Restrictions.or(
					Restrictions.and(
					Restrictions.eq("parentId", parentId), 
					Restrictions.eq("isDefault", true)) , 
					Restrictions.eq("parentId", parentId)));
			this.parentId = null;
		}
		if(getDefaultOperations) {		//查找某个用户所有的资源list当中某个资源的直接子资源+默认显示的资源
			criteria.add(Restrictions.or(
					Restrictions.and(
					Restrictions.and(
					Restrictions.eq("parentId", parentId), 
					Restrictions.eq("isDefault", true)) , Restrictions.in("id", listId)),
					Restrictions.eq("parentId", parentId)));
			this.parentId = null;
			this.listId = null;
		}
		if(null != listId) {
			if(listId.size() > 0) {
				criteria.add(Restrictions.in("id", listId));
			}
		}
		if(null != type) {
			criteria.add(Restrictions.eq("type", type));
		}
		if(StringUtils.isNotBlank(parentId)) {
			criteria.add(Restrictions.eq("parentId", parentId));
		}
		if(StringUtils.isNotBlank(name)) {
			criteria.add(Restrictions.eq("name", name));
		}
		if(StringUtils.isNotBlank(url)) {
			criteria.add(Restrictions.like("url", url , MatchMode.ANYWHERE));
		}
		if(isDefault) {
			criteria.add(Restrictions.eq("isDefault", true));
		}
	}

	public void setListId(List<String> listId) {
		this.listId = listId;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setIsDefaultOperation(Boolean isDefaultOperation) {
		this.isDefaultOperation = isDefaultOperation;
	}

	public void setGetDefaultOperations(Boolean getDefaultOperations) {
		this.getDefaultOperations = getDefaultOperations;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

}
