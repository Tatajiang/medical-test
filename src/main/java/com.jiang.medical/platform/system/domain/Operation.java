package com.jiang.medical.platform.system.domain;

import com.homolo.framework.annotation.PrimaryKey;
import com.homolo.framework.bean.DomainObject;
import com.jiang.medical.ProjectConfig;
import org.hibernate.annotations.Index;

import javax.persistence.*;


/** 
* @ClassName: Operation 
* @Description: 平台登录实体类
* @author Tata 
* @date 2019年5月4日 下午12:57:18 
*  
*/ 
@Entity
@Table(name = ProjectConfig.NAME + "_Operation")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class Operation implements DomainObject{

	private static final long serialVersionUID = -2285720242807163549L;
	
	@PrimaryKey
	@Id
	@Column(length=32)
	private String id;			//资源id

	@Column
	private String name;		//资源名称
	
	@Column
	@Index(name="Index_operation_parentId")
	private String parentId;	//资源父类id
	
	@Column
	private Integer level;		//资源的深度

	@Column
	private String description;	//资源描述
 
	@Column
	@Index(name="Index_operation_url")
	private String url;			//资源的url

	@Column
	private String tip;			//资源提示信息
	
	@Enumerated(EnumType.STRING)
	private Type type;			//资源类型
	
	@Column
	private Integer showIndex=0;//资源显示序号
		
	@Column
	private Boolean isDefault=false;//是否默认显示，跟角色无关,true是默认
	
	private String iconCls;		//图标样式
	
	public static enum Type{
		Memu,
		Button,
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(Integer showIndex) {
		this.showIndex = showIndex;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	
}
