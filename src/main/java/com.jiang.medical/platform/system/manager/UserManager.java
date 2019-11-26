/**
 * Project : game-webapp
 * FileName: UserManager.java
 * Copyright (c) 2019, www.narwell.com All Rights Reserved.
 */
package com.jiang.medical.platform.system.manager;

import com.homolo.framework.annotation.DomainEngine;
import com.homolo.framework.dao.DomainObjectDao;
import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.homolo.framework.util.MD5Util;
import com.homolo.framework.util.UUID;
import com.jiang.medical.Constant;
import com.jiang.medical.ProjectConfig;
import com.jiang.medical.platform.enums.LevelEnum;
import com.jiang.medical.platform.system.condition.UserCondition;
import com.jiang.medical.platform.system.domain.User;
import com.jiang.medical.util.AutoEvaluationUtil;
import com.jiang.medical.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/** 
* @ClassName: UserManager 
* @Description: 用户业务逻辑类
* @author Tata 
* @date 2019年5月4日 下午1:01:22 
*  
*/ 
@DomainEngine(types = User.class)
@Transactional(readOnly = false)
public class UserManager {

	@Resource(name = ProjectConfig.PREFIX + "UserDao")
	DomainObjectDao<User> objDao;
	
	@DomainEngine.C
	@Transactional(rollbackFor = Exception.class)
	public String create(User obj){
		return objDao.createObject(obj);
	}
	
	@DomainEngine.U
	@Transactional(rollbackFor = Exception.class)
	public void update(User obj){
		objDao.updateObject(obj);
	}
	
	@DomainEngine.D
	@Transactional(rollbackFor = Exception.class)
	public void delete(User obj){
		objDao.deleteObject(obj);
	}
	
	@DomainEngine.D
	@Transactional(rollbackFor = Exception.class)
	public void delete(String id){
		objDao.deleteObject(id);
	}
	
	@DomainEngine.R
	public User getObject(String id) {
		if(StringUtils.isNotBlank(id)){
			User old = objDao.loadObject(id);
			if(null != old){
				User  newObj = new User();
				BeanUtils.copyProperties(old, newObj);
				return newObj;
			}
		}
		return null;
	}
	
	public List<User> list(UserCondition condition) {
		return objDao.findAllByCondition(condition);
	}
	
	public List<User> list(UserCondition condition, Sorter sorter) {
		return objDao.findAllByCondition(condition, sorter);
	}
	
	public List<User> list(UserCondition condition, int size) {
		return objDao.findByCondition(condition, size);
	}
	
	public List<User> listAll() {
		return objDao.findAll();
	}
	
	public PaginationSupport<User> pageList(UserCondition condition, Range range, Sorter sorter) {
		return objDao.findByCondition(condition, range, sorter);
	}
	
	/***
	 * 根据登录名获得对象
	 * @param loginId
	 * @return
	 */
	public User getObjectByLoginId(String loginId){
		if(StringUtils.isBlank(loginId)){
			return null;
		}
		UserCondition condition = new UserCondition();
		condition.setLoginId(loginId);
		List<User> list = list(condition);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @Description 用户信息转化为map
	 * @param obj
	 * @return 
	 * @return Map<String,Object>  
	 */
	public Map<String,Object> userToMap(User obj){
		Map<String,Object> item= new HashMap<String,Object> ();
		item.put("id",obj.getId());	
		item.put("createTime", DateUtil.formatDateTime(obj.getCreateTime()));
		if("10".equals(obj.getGender())){
			item.put("gender", "男");
		}else if("20".equals(obj.getGender())){
			item.put("gender", "女");
		}else if("30".equals(obj.getGender())){
			item.put("gender", "保密");
		}else{
			item.put("gender", "保密");
		}
		item.put("nickname", obj.getNickname());
		item.put("phone", obj.getPhone());
		item.put("pictrueId", obj.getPictrueId());
		return item;
	}

	/** 
	 * @Description 根据id获取名字
	 * @param userId
	 * @return 
	 * @return String  
	 */
	public String getNameById(String userId) {
		User obj = getObject(userId);
		return obj == null ? "" : StringUtils.defaultString(obj.getNickname(), "");
	}
	
	/**
	 * 
	 * @Description 根据查询条件返回用户id集合
	 * @param cn
	 * @return 
	 * @return List<String>  
	 */
	public List<String> getListByCondition(UserCondition cn){
		List<User> listUser = objDao.findAllByCondition(cn);
		List<String> list = new ArrayList<String>();
		for(Object id:listUser){//获取分类id
			list.add((String)id);
		}
		return list;
	}

	/* *
	 * @Description: 注册
	 * @Param: [sessionId, password, phone, code, mac, topBizUserId]
	 * @return: com.jiang.medical.platform.system.domain.User
	 * @Author: zhantuo.jiang
	 * @date: 2019/11/26 14:20
	 */
	@DomainEngine.C
	@Transactional(rollbackFor = Exception.class)
	public User register(String sessionId,String phone,String password,User.Gender gender) throws Exception{
		//创建会员信息
		User obj = new User();
		obj.setId(UUID.generateUUID());
		obj.setPhone(phone);
		obj.setLoginId(phone);
		obj.setGender(gender);
		obj.setJsessionid(sessionId);
		obj.setNickname("用户_"+StringUtils.substring(phone,7,11));
		//使用MD5加密密码
		obj.setPassword(MD5Util.encryptPassword(password));
		obj.setPictrueId(Constant.DEFAULT_HEAD);
		//设置角色信息
		obj.setRoleIds(Constant.SYSTEMCONSTANT_USER_ROLE_ID);
		//设置用户级别
		obj.setLevle(LevelEnum.User);

		//TODO:是否添加默认的身份证号码等信息

		this.create(obj);
		return obj;
	}

	/* *
	 * @Description: 通过JSeesionId获取用户对象
	 * @Param: [jsessionid]
	 * @return: com.jiang.medical.platform.system.domain.User
	 * @Author: zhantuo.jiang
	 * @date: 2019/11/26 14:10
	 */
	public User getObjectBySessionId(String jsessionid){
		if(StringUtils.isBlank(jsessionid)){
			return null;
		}
		UserCondition condition = new UserCondition();
		condition.setJsessionid(jsessionid);
		List<User> list = this.list(condition);
		return list.isEmpty() ? null : list.get(0);
	}

	/* *
	 * @Description: 通过电话号码获取用户对象
	 * @Param: [mobile]
	 * @return: com.jiang.medical.platform.system.domain.User
	 * @Author: zhantuo.jiang
	 * @date: 2019/11/26 14:19
	 */
	public User getObjectByMobile(String mobile){
		if(StringUtils.isBlank(mobile)){
			return null;
		}
		UserCondition condition = new UserCondition();
		condition.setPhone(mobile);
		List<User> list = this.list(condition, 1);
		return list.isEmpty() ? null : list.get(0);
	}


	/* *
	 * @Description: 封装可返回会员信息
	 * @Param: [obj]
	 * @return: java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: zhantuo.jiang
	 * @date: 2019/11/26 14:33
	 */
	public Map<String, Object> packUserInfo(User obj){
		Map<String, Object> result = new HashMap<>();
		result.put("loginId",obj.getLoginId());
		result.put("pictrueId",obj.getPictrueId());
		result.put("nickname",obj.getNickname());
		result.put("gender",obj.getGender());
		result.put("card",obj.getCard());
		result.put("phone",obj.getPhone());
		result.put("jsessionid",obj.getJsessionid());
		return result;
	}
}
