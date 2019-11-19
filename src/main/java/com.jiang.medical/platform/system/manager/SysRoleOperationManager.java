package com.jiang.medical.platform.system.manager;

import com.homolo.framework.annotation.DomainEngine;
import com.homolo.framework.dao.DomainObjectDao;
import com.homolo.framework.dao.util.Sorter;
import com.homolo.framework.util.UUID;
import com.jiang.medical.Constant;
import com.jiang.medical.ProjectConfig;
import com.jiang.medical.platform.system.condition.SysRoleOperationCondition;
import com.jiang.medical.platform.system.domain.Operation;
import com.jiang.medical.platform.system.domain.SysRoleOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 平台角色资源关联业务处理类
 * @author li
 * @date 2014-03-17
 * @version 1.0
 */
@DomainEngine(types = SysRoleOperation.class)
@Transactional(readOnly = false)
public class SysRoleOperationManager {
	
	@Resource(name = ProjectConfig.PREFIX + "SysRoleOperationDao")
	private DomainObjectDao<SysRoleOperation> objDao;
	
	@Autowired
	private OperationManager operationManager;
	
	/**
	 * 创建角色资源关联 
	 * @param roleId
	 * @param operationIds
	 * @throws Exception
	 */
	public void createObjects(String roleId , String operationIds) throws Exception {
		String[] operationId = operationIds.split(",");
		for(String str : operationId) {
			createObject(roleId, str);
		}
	}
	
	/**
	 * 修改角色资源关联
	 * @param roleId
	 * @param operationIds
	 * @throws Exception
	 */
	public void resetObjects(String roleId , String operationIds) throws Exception {
		//删除原有的角色资源关联
		SysRoleOperationCondition condition = new SysRoleOperationCondition();
		condition.setRoleId(roleId);
		List<SysRoleOperation> list = list(condition);
		if(!list.isEmpty()) {
			objDao.deleteObjects(list);
		}
		String[] operationId = operationIds.split(",");
		for(String str : operationId) {
			if(StringUtils.isNotBlank(str)) {
				this.createObject(roleId, str);
			}
		}
	}
	
	/**
	 * 删除角色资源关联 
	 * @param roleId
	 * @param operationIds
	 * @throws Exception
	 */
	public void deleteObjects(String roleId , String operationIds) throws Exception {
		String[] operationId = operationIds.split(",");
		for(String str : operationId) {
			deleteObject(roleId, str);
		}
	}
	/**
	 * 创建某个角色资源关联
	 * @param roleId 角色id
	 * @param operationId 资源id
	 * @return
	 * @throws Exception
	 */
	@DomainEngine.C
	@Transactional(rollbackFor = Exception.class)
	public String createObject(String roleId , String operationId) throws Exception {
		if(StringUtils.equals(operationId, Constant.HOME_CHARTS_ID)){
			SysRoleOperation obj = new SysRoleOperation();
			obj.setId(UUID.generateUUID());
			obj.setRoleId(roleId);
			obj.setOperationId(operationId);
			return objDao.createObject(obj);
		}
		//判断直接父项菜单是否存在，不存在则创建直接父项菜单
		Operation operation = operationManager.getObject(operationId);
		String parentId = operation.getParentId();
		SysRoleOperation r = getObjectByRoleIdOperationId(roleId, parentId);
		if(r == null) {
			SysRoleOperation obj = new SysRoleOperation();
			obj.setRoleId(roleId);
			obj.setOperationId(parentId);
			objDao.createObject(obj);
		}
		//判断子项菜单是否存在，若不存在，则创建子项菜单
		SysRoleOperation obj = getObjectByRoleIdOperationId(roleId, operationId);
		if(obj == null) {			
			obj = new SysRoleOperation();
			obj.setId(UUID.generateUUID());
			obj.setRoleId(roleId);
			obj.setOperationId(operationId);
			return objDao.createObject(obj);
		}
		return null;
	}

	/**
	 * 创建某个角色资源关联
	 * @param obj 关联对象
	 * @return
	 * @throws Exception
	 */
	@DomainEngine.C
	@Transactional(rollbackFor = Exception.class)
	public String createObject(SysRoleOperation obj) throws Exception {
		return objDao.createObject(obj);
	}

	/**
	 * 删除某个角色资源关联
	 * @param roleId 角色id
	 * @param operationId	资源id
	 * @throws Exception
	 */
	@DomainEngine.D
	@Transactional(rollbackFor = Exception.class)
	public void deleteObject(String roleId , String operationId)  throws Exception {
		SysRoleOperation obj = getObjectByRoleIdOperationId(roleId, operationId);
		if(obj == null) {			
		}else{			
			objDao.deleteObject(obj);
		}
		//判断其父项菜单下是否不存在子项菜单，若不存在删除父项菜单
		Operation operation = operationManager.getObject(operationId);
		String parentId = operation.getParentId();
		List<SysRoleOperation> list = getByRoleIdParentId(roleId, parentId);
		if(null == list || list.size() == 0) {	//不存在子项菜单
			objDao.deleteObject(getObjectByRoleIdOperationId(roleId, parentId));
		}
	}

	/**
	 * 删除某个角色下的所有角色资源关联
	 * @param roleId 角色id
	 */
	@DomainEngine.D
	@Transactional(rollbackFor = Exception.class)
	public void deletedByRoleId(String roleId) {
		SysRoleOperationCondition condition = new SysRoleOperationCondition();
		condition.setRoleId(roleId);
		List<SysRoleOperation> list = list(condition);
		if(list.size() != 0) {
			for(SysRoleOperation o : list) {
				objDao.deleteObject(o);
			}
		}
	}
	
	@DomainEngine.R
	public SysRoleOperation getObject(String id) {
		if (StringUtils.isNotBlank(id)) {
			SysRoleOperation src = objDao.loadObject(id);
			if (src != null) {
				SysRoleOperation obj = new SysRoleOperation();
				BeanUtils.copyProperties(src, obj);
				return obj;
			}
		}
		return null;
	}

	/**
	 * 根据角色获得该角色的所有资源 （不包括平台超级管理员）
	 * @param roleId 角色id
	 * @return 当该角色下找不到资源或roleId为空时，返回空list
	 */
	public List<Operation> getOperationByRole(String roleId) {
/*		if(Constants.SYSTEMCONSTANT_ADMIN_ROLE_ID.equals(roleId)) {
			return operationManager.getAllOperations();
		}*/
		//获得所有资源id--获得所有资源
		SysRoleOperationCondition condition = new SysRoleOperationCondition();
		if(StringUtils.isNotBlank(roleId)) {			
			condition.setRoleId(roleId);
		}
		List<SysRoleOperation> list = list(condition);
		List<String>  listOperationId=new ArrayList<String>();
		for (SysRoleOperation obj:list){
			listOperationId.add(obj.getOperationId());
		}
		if(!listOperationId.isEmpty()) {
			List<Operation> operations=operationManager.getByTypeAndIds(listOperationId, Operation.Type.Memu);
			if(listOperationId.indexOf(Constant.HOME_CHARTS_ID) != -1){
				Operation obj = new Operation();
				obj.setId(Constant.HOME_CHARTS_ID);
				obj.setIconCls("fa fa-home");
				obj.setLevel(1);
				obj.setParentId(Constant.TREE_ROOT_ID);
				obj.setIsDefault(false);
				obj.setName("图形报表");
				obj.setShowIndex(999);
				obj.setType(Operation.Type.Memu);
				operations.add(obj);
			}
			return operations;
		}else{
			return null;
		}
	}

	/**
	 * 根据角色和资源，获得角色和资源关联 （不包括平台超级管理员）
	 * @param roleId 角色id
	 * @param operationId 资源id
	 * @return true:存在该关联 false：不存在该关联 null：roleId或operationId为空
	 */
	public SysRoleOperation getObjectByRoleIdOperationId(String roleId , String operationId) {
		SysRoleOperationCondition condition = new SysRoleOperationCondition();
		if(StringUtils.isNotBlank(roleId) && StringUtils.isNotBlank(operationId)) {
			condition.setRoleId(roleId);
			condition.setOperationId(operationId);
			List<SysRoleOperation> list = list(condition);
			if(list.size() != 0){
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * 根据角色id，角色上级id，获得角色与资源关联
	 * @param roleId 角色id
	 * @param parentId 角色的直接上级id
	 * @return
	 */
	public List<SysRoleOperation> getByRoleIdParentId(String roleId , String parentId) {
		//找到该父项菜单下所有的子项菜单id
		List<String> listOperationIds = operationManager.getByParentId(parentId);
		SysRoleOperationCondition condition = new SysRoleOperationCondition();
		if(StringUtils.isNotBlank(roleId) && StringUtils.isNotBlank(parentId)) {			
			condition.setListOperationId(listOperationIds);
			condition.setRoleId(roleId);
			List<SysRoleOperation> list = list(condition);
			if(list.size() > 0) {
				return list;
			}
		}
		return new ArrayList<SysRoleOperation>();
	}
	/**
	 * 根据角色ids，获得资源id集合,如果id中包含平台超级管理员，返回所有资源
	 * @param roleId
	 * @return
	 */
	public List<String> getObjectByRoleId(List<String> listRoleId) {
		List<String>  listOperationId = new ArrayList<String>();
		if(listRoleId.isEmpty()) return listOperationId;
/*		if(listRoleId.contains(Constants.SYSTEMCONSTANT_ADMIN_ROLE_ID)) {
			List<Operation> list = operationManager.getAllOperations();
			for (Operation obj:list){
				listOperationId.add(obj.getId());
			}
		}*/
		SysRoleOperationCondition condition = new SysRoleOperationCondition();
		condition.setListRoleId(listRoleId);
		List<SysRoleOperation> list = list(condition);
		for (SysRoleOperation obj:list){
			listOperationId.add(obj.getOperationId());
		}
		return listOperationId;
	}
	
	public List<SysRoleOperation> list(SysRoleOperationCondition condition) {
		return objDao.findAllByCondition(condition);
	}

	public List<SysRoleOperation> list(SysRoleOperationCondition condition, Sorter sorter) {
		return objDao.findAllByCondition(condition, sorter);
	}
	
	public List<SysRoleOperation> listAll() {
		return objDao.findAll();
	}
	/**
	 * @auther luoquan
	 * @date 2016-7-22 上午9:32:39
	 * @Description 
	 * @param roleId
	 * @return
	 */
	public Boolean checkedSysRoleOperationByRoleId(String roleId){
		List<SysRoleOperation> list = new ArrayList<SysRoleOperation>();
		SysRoleOperationCondition condition = new SysRoleOperationCondition();
		if(StringUtils.isNotBlank(roleId)){			
			condition.setRoleId(roleId);
			list = objDao.findAllByCondition(condition);
			if(list.size()==0){
				return true;
			}else{
				return false;
			}
		}else {
			return false;
		}
	}
	
	/******************************** TODO 自定义角色权限(菜单) ****************************************/
	
	/**
	 * @Description 根据角色和关联id获取该角色菜单 
	 * @param roleId
	 * @param relevanceId
	 * @return
	 * @throws Exception 
	 * @return List<Operation>  
	 * @author yuanguo.huang
	 * @date 2017-6-26 下午3:26:53
	 */
	public List<Operation> getOperationByRoleV2(String roleId ,String relevanceId) throws Exception{
		//获得所有资源id--获得所有资源
		SysRoleOperationCondition condition = new SysRoleOperationCondition();
		if(StringUtils.isNotBlank(roleId)) {
			condition.setRoleId(roleId);
		}
		List<SysRoleOperation> list = list(condition);
		List<String>  listOperationId=new ArrayList<String>();
		for (SysRoleOperation obj:list){
			listOperationId.add(obj.getOperationId());
		}
		if(listOperationId.size() != 0) {
			List<Operation> operations=operationManager.getByTypeAndIds(listOperationId, Operation.Type.Memu);
			return operations;
		}else{
			return null;
		}
	}
	
	public void resetObjectsV2(String roleId , String operationIds, String relevanceId) throws Exception {
		//删除原有的角色资源关联
		SysRoleOperationCondition condition = new SysRoleOperationCondition();
		condition.setRoleId(roleId);
		condition.setRelevanceId(relevanceId);
		List<SysRoleOperation> list = list(condition);
		if(list.size() > 0) {
			objDao.deleteObjects(list);
		}
		String[] operationId = operationIds.split(",");
		for(String str : operationId) {
			if(StringUtils.isNotBlank(str)) {				
				createObjectV2(roleId, str);
			}
		}
	}
	
	/**
	 * @Description 创建某个角色资源关联
	 * @param roleId
	 * @param operationId
	 * @param dbLevel
	 * @param relevanceId
	 * @return
	 * @throws Exception 
	 * @return String  
	 * @author yuanguo.huang
	 * @date 2017-6-26 下午4:43:12
	 */
	@DomainEngine.C
	@Transactional(rollbackFor = Exception.class)
	public String createObjectV2(String roleId , String operationId) throws Exception {
		//判断直接父项菜单是否存在，不存在则创建直接父项菜单
		Operation operation = operationManager.getObject(operationId);
		String parentId = operation.getParentId();
		SysRoleOperation r = getObjectByRoleIdOperationIdV2(roleId, parentId);
		if(r == null) {
			SysRoleOperation obj = new SysRoleOperation();
			obj.setRoleId(roleId);
			obj.setOperationId(parentId);
			objDao.createObject(obj);
		}
		//判断子项菜单是否存在，若不存在，则创建子项菜单
		SysRoleOperation obj = getObjectByRoleIdOperationIdV2(roleId, operationId);
		if(obj == null) {
			obj = new SysRoleOperation();
			obj.setId(UUID.generateUUID());
			obj.setRoleId(roleId);
			obj.setOperationId(operationId);
			return objDao.createObject(obj);
		}
		return null;
	}
	
	/**
	 * @Description 根据角色,资源,数据级别和关联id，获得角色和资源关联 
	 * @param roleId
	 * @param operationId
	 * @param dbLevel
	 * @param relevanceId
	 * @return 
	 * @return SysRoleOperation  
	 * @author yuanguo.huang
	 * @date 2017-6-26 下午5:07:57
	 */
	public SysRoleOperation getObjectByRoleIdOperationIdV2(String roleId , String operationId) {
		SysRoleOperationCondition condition = new SysRoleOperationCondition();
		if(StringUtils.isNotBlank(roleId) && StringUtils.isNotBlank(operationId)) {
			condition.setRoleId(roleId);
			condition.setOperationId(operationId);
			List<SysRoleOperation> list = list(condition);
			if(list.size() != 0){
				return list.get(0);
			}
		}
		return null;
	}
	
	/**
	 * @Description 根据角色,资源,数据级别和关联id，获得菜单id集合
	 * @param listRoleId
	 * @param dbLevel
	 * @param relevanceId
	 * @return 
	 * @return List<String>  
	 * @author yuanguo.huang
	 * @date 2017-6-26 下午5:43:03
	 */
	public List<String> getObjectByRoleIdV2(List<String> listRoleId) {
		List<String>  listOperationId=new ArrayList<String>();
		for (String id : listRoleId) {
			SysRoleOperationCondition cn = new SysRoleOperationCondition();
			cn.setRoleId(id);
			List<SysRoleOperation> list = list(cn);
			for (SysRoleOperation obj:list){
				listOperationId.add(obj.getOperationId());
			}
		}
		return listOperationId;
	}
	
}
