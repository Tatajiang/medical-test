package com.jiang.medical.platform.system.domain;

import com.homolo.framework.annotation.PrimaryKey;
import com.homolo.framework.bean.DomainObject;
import com.jiang.medical.ProjectConfig;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Tata
 * 用户登录日志实体类
 */
@Entity
@Table(name = ProjectConfig.NAME + "_Login_Log")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class LoginLog implements DomainObject{

	private static final long serialVersionUID = -9030964860784300034L;

	@Id
	@PrimaryKey
	@Column(length = 32)
	private String id; 											// 主键Id
	
	@Column(length = 32)
	@Index(name="Index_Login_userId")
	private String userId; 										// 登入用户id
	
	@Column
	private String ip; 											// 登入IP
	
	@Column
	private Date createTime = new Date();						// 创建时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "LoginLog [id=" + id + ", userId=" + userId + ", ip=" + ip + ", createTime=" + createTime + "]";
	}
	
}
