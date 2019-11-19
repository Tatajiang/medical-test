package com.jiang.medical.platform.system.manager;

import com.homolo.framework.annotation.DomainEngine;
import com.homolo.framework.dao.DomainObjectDao;
import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.jiang.medical.ProjectConfig;
import com.jiang.medical.platform.system.condition.LoginLogCondition;
import com.jiang.medical.platform.system.domain.LoginLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Tata
 * 登录日志业务逻辑类
 */
@DomainEngine(types = LoginLog.class)
@Transactional(readOnly = false)
public class LoginLogManager {


	@Resource(name = ProjectConfig.PREFIX + "LoginLogDao")
	DomainObjectDao<LoginLog> objDao;
	
	@DomainEngine.C
	@Transactional(rollbackFor = Exception.class)
	public String create(LoginLog obj){
		return objDao.createObject(obj);
	}
	
	@DomainEngine.U
	@Transactional(rollbackFor = Exception.class)
	public void update(LoginLog obj){
		objDao.updateObject(obj);
	}
	
	@DomainEngine.D
	@Transactional(rollbackFor = Exception.class)
	public void delete(LoginLog obj){
		objDao.deleteObject(obj);
	}
	
	@DomainEngine.D
	@Transactional(rollbackFor = Exception.class)
	public void delete(String id){
		objDao.deleteObject(id);
	}
	
	@DomainEngine.R
	public LoginLog getObject(String id) {
		if(StringUtils.isNotBlank(id)){
			LoginLog old = objDao.loadObject(id);
			if(null != old){
				LoginLog  newObj = new LoginLog();
				BeanUtils.copyProperties(old, newObj);
				return newObj;
			}
		}
		return null;
	}
	
	public List<LoginLog> list(LoginLogCondition condition) {
		return objDao.findAllByCondition(condition);
	}
	
	public List<LoginLog> list(LoginLogCondition condition, Sorter sorter) {
		return objDao.findAllByCondition(condition, sorter);
	}
	
	public List<LoginLog> list(LoginLogCondition condition, int size) {
		return objDao.findByCondition(condition, size);
	}
	
	public List<LoginLog> listAll() {
		return objDao.findAll();
	}
	
	public PaginationSupport<LoginLog> pageList(LoginLogCondition condition, Range range, Sorter sorter) {
		return objDao.findByCondition(condition, range, sorter);
	}
	
}
