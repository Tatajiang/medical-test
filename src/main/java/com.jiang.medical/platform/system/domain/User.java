/** 
 * @Package com.yunmi.game.system.domain 
 * @Description 
 * @author yuanguo.huang
 * @date 2018-10-17 上午9:29:16 
 * @version V1.0 
 */ 
package com.jiang.medical.platform.system.domain;

import com.homolo.framework.annotation.PrimaryKey;
import com.homolo.framework.bean.DomainObject;
import com.jiang.medical.ProjectConfig;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;


/**
* @ClassName: User 
* @Description: 平台用户实体类
* @author Tata 
* @date 2019年5月4日 下午12:59:44 
*  
*/ 
@Entity
@Table(name = ProjectConfig.NAME + "_User")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class User implements DomainObject {

	/** 
	 * serialVersionUID long
	 */ 
	private static final long serialVersionUID = 6777167920680872463L;

	@Id
	@PrimaryKey
	@Column(length = 32)
	private String id; 											// 主键Id
	
	@Column
	@Index(name="Index_user_loginId")
	private String loginId; 									//登入用户名
	
	@Column
	private String password; 									//登入密码
	
	@Column
	private String nickname; 									//昵称
	
	@Column(length = 32)
	private String pictrueId;									//用户头像Id

	@Column(length = 255)
	private String card;										//身份证号码
	
	@Column
	@Enumerated(value = EnumType.STRING)
	private Gender gender = Gender.Male; 						//性别

	@Column
	private Date createTime = new Date();						//创建时间
	
	@Column
	@Index(name="Index_user_phone")
	private String phone; 										//电话

	@Column(length = 32)
	@Index(name = "Index_user_jsessionid")
	private String jsessionid;									//sessionId（用户登录内容）

	private String roleIds;										//所拥有的角色id集合

	private Boolean isDelete = false;                           //是否删除  true为删除，false未删除

	public static enum Gender {
		Male ("男"), 									 	//男
		FeMale("女");									 	//女

		private String name;
		private Gender(String name) {
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

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPictrueId() {
		return pictrueId;
	}

	public void setPictrueId(String pictrueId) {
		this.pictrueId = pictrueId;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Boolean getDelete() {
		return isDelete;
	}

	public void setDelete(Boolean delete) {
		isDelete = delete;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getJsessionid() {
		return jsessionid;
	}

	public void setJsessionid(String jsessionid) {
		this.jsessionid = jsessionid;
	}
}
