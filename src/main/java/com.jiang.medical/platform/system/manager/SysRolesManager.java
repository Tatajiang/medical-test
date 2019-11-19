package com.jiang.medical.platform.system.manager;

import com.homolo.framework.annotation.DomainEngine;
import com.homolo.framework.dao.DomainObjectDao;
import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.jiang.medical.Constant;
import com.jiang.medical.ProjectConfig;
import com.jiang.medical.platform.system.condition.SysRolesCondition;
import com.jiang.medical.platform.system.condition.UserCondition;
import com.jiang.medical.platform.system.domain.SysRoles;
import com.jiang.medical.platform.system.domain.User;
import com.jiang.medical.util.AutoEvaluationUtil;
import com.jiang.medical.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台角色业务处理类
 * @author li
 * @date 2014-03-17
 * @version 1.0
 */
@DomainEngine(types = SysRoles.class)
@Transactional(readOnly = false)
public class SysRolesManager {
	
	@Resource(name = ProjectConfig.PREFIX + "SysRolesDao")
	private DomainObjectDao<SysRoles> objDao;
	
	@Resource(name = ProjectConfig.PREFIX + "UserDao")
	private DomainObjectDao<User> userDao;

	@Autowired
	private SysRoleOperationManager sysRoleOperationManager;
	
	@DomainEngine.C
	@Transactional(rollbackFor = Exception.class)
	public String createObject(SysRoles obj) throws Exception {
		if(isNameExsit(obj.getName() , null)) {	//如果名称重复
			throw new Exception("角色名称已经存在！");
		}else {	//向数据库中插入新的记录
			return objDao.createObject(obj);
		}
	}

	@DomainEngine.U
	@Transactional(rollbackFor = Exception.class)
	public void updateObject(SysRoles obj) throws Exception {
		if(isNameExsit(obj.getName(), obj.getId())){
			throw new Exception("角色名称已经存在！");
		}else {	//更新角色数据
			objDao.updateObject(obj);
		}
	}

	@DomainEngine.D
	@Transactional(rollbackFor = Exception.class)
	public void deleteObject(String id) throws Exception {
		//删除对应的角色菜单
		sysRoleOperationManager.deletedByRoleId(id);
		//删除角色自身
		objDao.deleteObject(id);
	}
	
	@DomainEngine.R
	public SysRoles getObject(String id) {
		if (StringUtils.isNotBlank(id)) {
			SysRoles src = objDao.loadObject(id);
			if (src != null) {
				SysRoles obj = new SysRoles();
				BeanUtils.copyProperties(src, obj);
				return obj;
			}
		}
		return null;
	}
	
	/**
	 * 根据角色id返回该角色是否被启用
	 * @param roleId 角色id
	 * @return
	 */
	@DomainEngine.R
	public Boolean isEnable(String roleId) {
		return getObject(roleId).getFlage();
	}
	
	/**
	 * 获得用于给下拉框动态赋值格式的角色列表 ,排除超级管理员
	 * @return
	 */
	public List<Map<String, Object>> listQuery(){
		List<SysRoles> list = this.list(new SysRolesCondition());
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String , Object>> listMap = new ArrayList<Map<String , Object>>();
		for(SysRoles obj: list){
			if(obj.getId().equals(Constant.SYSTEMCONSTANT_ADMIN_ROLE_ID)){
				continue;
			}
			map = AutoEvaluationUtil.domainToMap(obj);
			listMap.add(map);
		}
		return listMap;
	}

	/**
	 * 判断角色名是否重复 
	 * @param name 角色名称
	 * @param neName 排除本身 可为空
	 * @return true：重复 false：不重复
	 */
	public Boolean isNameExsit(String name , String neId) {
		SysRolesCondition condition = new SysRolesCondition();
		condition.setName(name);
		condition.setNeId(neId);
		List<SysRoles> list = list(condition);
		if(list.size() == 0) {
			return false;
		}else {
			return true;
		}
	}
	
	//根据id得到角色名称
	public String getNameById(String id) {
		SysRoles role = getObject(id);
		if(role == null) {
			return "";
		}else {
			return role.getName();
		}
	}
	
	/**
	 * @Description 根据id集合获取对应的名称集合
	 * @param ids
	 * @return 
	 * @return String  
	 * @author leijing
	 * @date 2018年10月25日 上午10:43:47
	 */
	public String getNameByIds(String ids) {
		String names = "";
		for(String id:StringUtil.split(ids, ",", true)){
			names += this.getNameById(id) + ",";
		}
		if (StringUtils.isNotBlank(names)) {
			names = names.substring(0, names.length()-1);
		}
		return names;
	}
	
	/**
	 * 根据角色名称获得角色 
	 * @param name
	 * @return 当没有找到时返回null
	 */
	public SysRoles getObjectByName(String name) {
		SysRolesCondition condition = new SysRolesCondition();
		if(StringUtils.isNotBlank(name)) {
			condition.setName(name);
			List<SysRoles> list = list(condition);
			if(list.size() != 0) {
				return list.get(0);
			}
		}
		return null;
	}
	
	/**
	 * 判断角色是否被关联
	 * @param inIds 角色id
	 * @return true：被关联 false：没被关联
	 */
	public Boolean isInIds(String rolesId) {
		UserCondition condition = new UserCondition();
		condition.setRolesId(rolesId);
		List<User> list = userList(condition);
		if(list.size() == 0) {
			return false;
		}else {
			return true;
		}
	}
	
	public List<User> userList(UserCondition condition) {
		return userDao.findAllByCondition(condition);
	}
	
	public List<SysRoles> list(SysRolesCondition condition) {
		return objDao.findAllByCondition(condition);
	}
	
	public List<SysRoles> listAll() {
		return objDao.findAll();
	}

	public List<SysRoles> list(SysRolesCondition condition,int size){
		return objDao.findByCondition(condition, size);
	}
	
	public List<SysRoles> list(SysRolesCondition condition, Sorter sorter) {
		return objDao.findAllByCondition(condition, sorter);
	}
	
	public List<SysRoles> list(SysRolesCondition condition, Sorter sorter, int size) {
		return objDao.findByCondition(condition, sorter, size);
	}
	
	public PaginationSupport<SysRoles> pageList(SysRolesCondition condition,Range range,Sorter sorter){
		return objDao.findByCondition(condition, range, sorter);
	}
	
	public List<SysRoles> list(Boolean flage) {
		SysRolesCondition condition = new SysRolesCondition();
		condition.setFlage(flage);
		return objDao.findAllByCondition(condition);
	}
}
