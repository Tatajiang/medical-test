package com.jiang.medical.platform.system.domain;

import com.homolo.framework.annotation.PrimaryKey;
import com.homolo.framework.bean.DomainObject;
import com.jiang.medical.ProjectConfig;

import javax.persistence.*;


/** 
* @ClassName: SysRoles 
* @Description: 平台角色信息实体类
* @author Tata 
* @date 2019年5月4日 下午12:59:27 
*  
*/ 
@Entity
@Table(name = ProjectConfig.NAME + "_SysRoles")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class SysRoles implements DomainObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2230864445520980880L;
	
	@PrimaryKey
	@Id
	@Column(length = 32)
	private String id;	//角色id

	@Column
	private String name;	//角色名称

	@Column
	private String description;	//角色描述

	@Column
	private Boolean flage = true;	//状态     true启用，false禁用
	
	@Enumerated(EnumType.STRING)
	private Level level = Level.admin;	// 用户级别
	
	public static enum Level {
		/**
		 * 系统管理员
		 */
		admin("系统管理员"),
		/**
		 * 其他用户
		 */
		others("其他用户");
		
		private String name;
		private Level(String name){
			this.name = name;
		}
		public String getName(){
			return this.name;
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getFlage() {
		return flage;
	}

	public void setFlage(Boolean flage) {
		this.flage = flage;
	}
	
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

}
