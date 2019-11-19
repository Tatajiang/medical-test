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
import com.jiang.medical.ProjectConfig;
import com.jiang.medical.platform.system.condition.UserCondition;
import com.jiang.medical.platform.system.domain.User;
import com.jiang.medical.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
	 * @author yuanguo.huang
	 * @date 2018-10-19 上午9:52:02
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
	 * @author leijing
	 * @date 2018年10月23日 下午4:25:12 
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
	 * @author yuanguo.huang
	 * @date 2018-10-31 下午3:28:33
	 */
	public List<String> getListByCondition(UserCondition cn){
		List<User> listUser = objDao.findAllByCondition(cn);
		List<String> list = new ArrayList<String>();
		for(Object id:listUser){//获取分类id
			list.add((String)id);
		}
		return list;
	}
}
