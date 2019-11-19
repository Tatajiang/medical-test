package com.jiang.medical.platform.system.domain;

import com.homolo.framework.annotation.PrimaryKey;
import com.homolo.framework.bean.DomainObject;
import com.jiang.medical.ProjectConfig;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
* @ClassName: SysRoleOperation 
* @Description: 角色资源关联类
* @author Tata 
* @date 2019年5月4日 下午12:59:00 
*  
*/ 
@Entity
@Table(name = ProjectConfig.NAME + "_SysRoleOperation")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class SysRoleOperation implements DomainObject{

	private static final long serialVersionUID = -7426305650078845663L;

	@PrimaryKey
	@Id
	@Column(length = 32)
	private String id;		//方案id

	@Index(name="Index_roleOperation_roleId")
	private String roleId;	//角色id
	
	@Index(name="Index_roleOperation_operationId")
	private String operationId;	//权限id
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

}
