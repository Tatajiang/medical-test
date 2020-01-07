package com.jiang.medical.platform.system.condition;

import com.homolo.framework.dao.hibernate.HibernateCondition;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/* *
 * @Description: 平台角色资源关联管理条件查询类
 * @Param:
 * @return:
 * @Author: zhantuo.jiang
 * @date: 2019/12/24 14:13
 */
public class SysRoleOperationCondition implements HibernateCondition {
	
	private String roleId;	//角色id
	
	private String operationId; //资源id
	
	private List<String> listRoleId; //角色id集合
	
	private List<String> listOperationId;	//资源id集合
	
	private String relevanceId;
	
	
	@Override
	public void populateDetachedCriteria(DetachedCriteria criteria) {
		if(StringUtils.isNotBlank(roleId)) {
			criteria.add(Restrictions.eq("roleId", roleId));
		}
		if(StringUtils.isNotBlank(operationId)) {
			criteria.add(Restrictions.eq("operationId", operationId));
		}
		if(null !=listRoleId && listRoleId.size() > 0) {
			criteria.add(Restrictions.in("roleId", listRoleId));
		}
		if(null != listOperationId && listOperationId.size() > 0) {
			criteria.add(Restrictions.in("operationId", listOperationId));
		}
		if(StringUtils.isNotBlank(relevanceId)){
			criteria.add(Restrictions.eq("relevanceId", relevanceId));
		}
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public void setListRoleId(List<String> listRoleId) {
		this.listRoleId = listRoleId;
	}

	public void setListOperationId(List<String> listOperationId) {
		this.listOperationId = listOperationId;
	}

	public void setRelevanceId(String relevanceId) {
		this.relevanceId = relevanceId;
	}
	
}
