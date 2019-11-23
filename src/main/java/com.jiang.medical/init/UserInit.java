package com.jiang.medical.init;

import com.homolo.framework.util.MD5Util;
import com.jiang.medical.Constant;
import com.jiang.medical.platform.system.domain.User;
import com.jiang.medical.platform.system.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class UserInit {
	
	@Autowired
	private UserManager userManager;
	

	/** 
	* @Title: initUser 
	* @Description: 初始化系统管理员
	* @param @throws Exception 
	* @return void
	* @author Tata 
	*/ 
	private void initUser() throws Exception {
		//初始化超级管理员
		String id = Constant.SYSTEMCONSTANT_USER_ADMIN_ID;
		User user = userManager.getObject(id);
		if(user == null){
			user = new User();
			user.setId(id);
			user.setLoginId("admin");
			user.setPassword(MD5Util.encryptPassword("888888"));
			user.setNickname("系统管理员");
			user.setPhone("15100000000");
			user.setRoleIds(Constant.SYSTEMCONSTANT_ADMIN_ROLE_ID);
			userManager.create(user);
		}
	}
	
	@PostConstruct
	public void setInitUser() {
		try {
			initUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
