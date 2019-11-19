package com.jiang.medical.platform.system.manager;

import com.homolo.framework.annotation.DomainEngine;
import com.homolo.framework.dao.DomainObjectDao;
import com.homolo.framework.dao.util.Sorter;
import com.jiang.medical.ProjectConfig;
import com.jiang.medical.platform.system.condition.OperationCondition;
import com.jiang.medical.platform.system.domain.Operation;
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
 * 平台资源业务处理类
 * @author li
 * @date 2014-03-17
 * @version 1.0
 */
@DomainEngine(types = Operation.class)
@Transactional(readOnly = false)
public class OperationManager {
	
	@Resource(name = ProjectConfig.PREFIX + "OperationDao")
	private DomainObjectDao<Operation> objDao;
	
	@Autowired
	private SysRolesManager sysRolesManager;
	
	@Autowired
	private SysRoleOperationManager sysRoleOperationManager;
	
	@DomainEngine.C
	@Transactional(rollbackFor = Exception.class)
	public String createObject(Operation obj) throws Exception {
		return objDao.createObject(obj);
	}

	@DomainEngine.U
	@Transactional(rollbackFor = Exception.class)
	public void updateObject(Operation obj) throws Exception {
		objDao.updateObject(obj);
	}
	
	
	@DomainEngine.D
	@Transactional(rollbackFor = Exception.class)
	public void deleteObject(String id) {
		objDao.deleteObject(id);
	}
	
	@DomainEngine.R
	public Operation getObject(String id) {
		if (StringUtils.isNotBlank(id)) {
			Operation src = objDao.loadObject(id);
			if (src != null) {
				Operation obj = new Operation();
				BeanUtils.copyProperties(src, obj);
				return obj;
			}
		}
		return null;
	}
	
	private void toMap(List<Map<String, Object>> listMap , List<Operation> list) {
		for(Operation o : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			//map = AutoEvaluationUtil.domainToMap(operation);
			String urls = o.getUrl();
			if(urls != null) {
				String[] url = urls.split(",");
				map.put("url", url[0]);
			}else{	
				map.put("url", null);
			}
			map.put("tips", o.getTip());
			map.put("name", o.getName());
			map.put("level", o.getLevel());
			map.put("id", o.getId());
			map.put("iconCls", o.getIconCls());
			listMap.add(map);
		}
	}
	
	/**
	 * 查找所有资源
	 * @return 
	 */
	public List<Operation> getAllOperations() {
		return list(new OperationCondition(), new Sorter().asc("showIndex"));
	}
	
	/**
	 * 根据资源id和资源类型,父id获得资源
	 * @param listOperationId
	 * @param type
	 * @return
	 */
	public List<Operation> getByTypeAndIds(List<String> listOperationId , Operation.Type type) {
		OperationCondition oCondition=new OperationCondition();
		oCondition.setListId(listOperationId);
		oCondition.setType(type);
		Sorter sorter=new Sorter().asc("showIndex");
		return list(oCondition, sorter);
	}

	/**
	 * 查找某个菜单的所有直接子菜单
	 * @param parentId 直接上级菜单id
	 * @return 所有直接子菜单id的集合
	 */
	public List<String> getByParentId(String parentId) {
		List<String> listOperationIds = new ArrayList<String>();
		OperationCondition oCondition=new OperationCondition();
		oCondition.setParentId(parentId);
		oCondition.setIsDefaultOperation(true);
		List<Operation> list = list(oCondition);
		for(Operation o : list) {
			listOperationIds.add(o.getId());
		}
		return listOperationIds;
	}
	
	/**
	 * 查找某个菜单的所有直接子菜单
	 * @param parentId 直接上级菜单id
	 * @param listId 用户所有的菜单，可为空
	 * @return 所有listId中parentId的直接子菜单id的集合
	 */
	public List<Operation> getByParentId(String parentId , List<String> listId) {
		
		OperationCondition oCondition = new OperationCondition();
		if(StringUtils.isNotBlank(parentId) && listId != null && listId.size() != 0) {			
			oCondition.setListId(listId);
			oCondition.setParentId(parentId);
			oCondition.setGetDefaultOperations(true);
			return list(oCondition , new Sorter().asc("showIndex"));
		}
		return new ArrayList<Operation>();
	}
	
	//得到平台所有的默认显示的菜单
	public List<Operation> getDefaultOperation() {
		OperationCondition operationCondition = new OperationCondition();
		operationCondition.setIsDefault(true);
		return list(operationCondition , new Sorter().asc("showIndex"));
	}
	
	public List<Operation> list(OperationCondition condition) {
		return objDao.findAllByCondition(condition);
	}
	public List<Operation> list(OperationCondition condition, Sorter sorter) {
		return objDao.findAllByCondition(condition, sorter);
	}
	public List<Operation> listAll() {
		return objDao.findAll();
	}

	/**
	 * @Description 根据上级id获取列表
	 * @param parentId
	 * @return List<Dept>  
	 * @author leijing
	 * @date 2018年10月23日 上午11:10:40
	 */
	public List<Operation> listByParentId(String parentId){
		if(StringUtils.isBlank(parentId)) return new ArrayList<>();
		OperationCondition condition = new OperationCondition();
		condition.setParentId(parentId);
		return list(condition);
	}
	
	/** 
	 * @Description 根据id获取树结构
	 * @param parentId
	 * @return Object  
	 * @author leijing
	 * @date 2018年10月23日 上午10:56:12 
	 */ 
	public List<Map<String, Object>> getTree(String parentId){
		List<Map<String, Object>> tree = new ArrayList<>();
		if(StringUtils.isBlank(parentId)){
			return tree;
		}
		
		List<Operation> list = listByParentId(parentId);
		for(Operation obj:list){
			Map<String, Object> map = AutoEvaluationUtil.domainToMap(obj);
			map.put("text", obj.getName());
			
			if(listByParentId(obj.getId()).isEmpty()){
				map.put("icon", "fa fa-file");
			}
			
			Map<String, Boolean> state = new HashMap<>();
			state.put("checked", false);
			state.put("disabled", false);
			state.put("selected", false);
			state.put("expanded", false);
			
			map.put("state", state);
			map.put("nodes", getTree(obj.getId()));
			
			tree.add(map);
		}
		return tree;
	}

	/** 
	 * @Description 
	 * @param userId
	 * @param uri
	 * @return 
	 * @return boolean  
	 * @author leijing
	 * @date 2018年10月26日 下午4:16:31 
	 */ 
	public boolean notHasUrlOperation(User user, String uri) {
		//根据url获得资源id，当url不属于任何一个菜单资源时返回空
		OperationCondition operationCondition = new OperationCondition();
		operationCondition.setUrl(uri);
		List<Operation> list = list(operationCondition);
		if(list.size() != 0) {		//该资源属于一个菜单资源	
			//获得用户所属角色Id的list
			List<String> roleOfusers = StringUtil.split(user.getRoleIds(), ",", true);
			if(roleOfusers != null && !roleOfusers.isEmpty()){			
				//去掉其中未被启用的角色
				List<String> roleOfusersCopy = new ArrayList<String>();
				for(String roleId : roleOfusers) {
					if(sysRolesManager.isEnable(roleId)) {					
						roleOfusersCopy.add(roleId);
					}
				}
				if(roleOfusersCopy.size() != 0) {
					List<String> listOperationIds = sysRoleOperationManager.getObjectByRoleId(roleOfusersCopy);
					for(Operation o : list) {
						if(listOperationIds.contains(o.getId())){
							return true;
						}
						if(o.getIsDefault() != null && o.getIsDefault()) {
							return true;
						}
					}
					return false;
				}
			}
			for(Operation o : list) {
				if(!o.getIsDefault()) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 根据用户获得用户下的所有权限
	 * @param user
	 * @return
	 */
	public List<Map<String , Object>> getMenueIdsByUser(User user) {
		List<Map<String , Object>> listMap = new ArrayList<Map<String,Object>>();
		//获得用户所属角色Id的list
		List<String> roleOfusers = StringUtil.split(user.getRoleIds(), ",", true);
		if(roleOfusers != null){			
			//去掉其中未被启用的角色
			List<String> roleOfusersCopy = new ArrayList<String>();
			for(String roleId : roleOfusers) {
				if(sysRolesManager.isEnable(roleId)) {					
					roleOfusersCopy.add(roleId);
				}
			}
			if(roleOfusersCopy.size() != 0) {				
				//根据角色Id的list获得菜单id list（去掉重复的)，并根据资源id获得资源
				List<String> listOperationIds = sysRoleOperationManager.getObjectByRoleId(roleOfusersCopy);
				if(listOperationIds.size() != 0) {		
					List<Operation> listOperations = getByTypeAndIds(listOperationIds, Operation.Type.Memu);
					toMap(listMap, listOperations);
				}else {
					//得到默认菜单
					toMap(listMap, getDefaultOperation());
				}
			}else {
				//得到默认菜单
				toMap(listMap, getDefaultOperation());
			}
		}else {
			//得到默认菜单
			toMap(listMap, getDefaultOperation());
		}
		return listMap;
	}
	
	/**
	 * 根据用户获得用户下的所有权限
	 * @param user
	 * @return
	 */
	public List<Operation> getMenueByUser(User user) {
		List<Operation> listMap = new ArrayList<>();
		//获得用户所属角色Id的list
		List<String> roleOfusers = StringUtil.split(user.getRoleIds(), ",", true);
		if(roleOfusers == null){
			//得到默认菜单
			listMap.addAll(getDefaultOperation());
			return listMap;
		}
		//去掉其中未被启用的角色
		List<String> roleOfusersCopy = new ArrayList<String>();
		for(String roleId : roleOfusers) {
			if(sysRolesManager.isEnable(roleId)) {					
				roleOfusersCopy.add(roleId);
			}
		}
		if(roleOfusersCopy.isEmpty()) {
			//得到默认菜单
			listMap.addAll(getDefaultOperation());
			return listMap;
		}
		//根据角色Id的list获得菜单id list（去掉重复的)，并根据资源id获得资源
		List<String> listOperationIds = sysRoleOperationManager.getObjectByRoleId(roleOfusersCopy);
		if(listOperationIds.isEmpty()) {
			//得到默认菜单
			listMap.addAll(getDefaultOperation());
			return listMap;
		}
		List<Operation> listOperations = getByTypeAndIds(listOperationIds, Operation.Type.Memu);
		listMap.addAll(listOperations);
		return listMap;
	}
}
