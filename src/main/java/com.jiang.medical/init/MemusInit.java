package com.jiang.medical.init;

import com.jiang.medical.Constant;
import com.jiang.medical.platform.system.domain.Operation;
import com.jiang.medical.platform.system.domain.SysRoleOperation;
import com.jiang.medical.platform.system.domain.SysRoles;
import com.jiang.medical.platform.system.manager.OperationManager;
import com.jiang.medical.platform.system.manager.SysRoleOperationManager;
import com.jiang.medical.platform.system.manager.SysRolesManager;
import com.jiang.medical.platform.system.domain.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


/** 
* @ClassName: MemusInit 
* @Description: 初始化菜单
* @author Tata 
* @date 2019年5月3日 下午4:11:26 
*  
*/ 
@Component
public class MemusInit {
	
	//超级管理员角色权限资源列表
	List<String> listOperation = new ArrayList<String>();
	
	Operation obj = null;
	
	String id= null;

	@Autowired
	private OperationManager operationManager;
	
	@Autowired
	private SysRolesManager sysRolesManager;
	
	@Autowired
	private	SysRoleOperationManager sysRoleOperationManager;
	
	/**
	 * @Description 			创建菜单
	 * @param id				主键
	 * @param name				菜单名
	 * @param parentId			父类
	 * @param description		描述
	 * @param showIndex			排序
	 * @param url				菜单链接
	 * @param tip				提示
	 * @param level				菜单的深度	
	 * @param isDefault			是否默认
	 * @param iconCls
	 * @throws Exception 
	 */
	private void initMemu(String id,String name,String parentId,String description,int showIndex,String url,
			String tip, int level,boolean isDefault, String iconCls) throws Exception{
		Operation operation = new Operation();
		operation.setId(id);
		operation.setName(name);
		operation.setParentId(parentId);
		operation.setDescription(description);
		operation.setLevel(level);
		operation.setUrl(url);
		operation.setTip(tip);
		operation.setShowIndex(showIndex);
		operation.setType(Operation.Type.Memu);
		operation.setIsDefault(isDefault);
		operation.setIconCls(iconCls);
		operationManager.createObject(operation);
	}
	

	/** 
	* @Title: initPlatFormMenu 
	* @Description: 平台菜单
	* @param @throws Exception 
	* @return void
	* @author Tata 
	*/ 
	@PostConstruct
	public void initPlatFormMenu() throws Exception {
		Integer intShowIndex=1000;
		
		
		/* *
		 * @Description: 用于初始化后台管理页面得层次结构（其中分为一个一层，其他的为二层结构）
		 * @Param: []
		 * @return: void
		 * @Author: zhantuo.jiang
		 * @date: 2019/11/19 12:26
		 */
		String parentId = "74284a9aebe24bae85388073aa1b9dc1";
		id = parentId;
		intShowIndex += 5;
		obj = operationManager.getObject(id);
		//如果是超级管理员权限 则增加到list
		listOperation.add(id);
		if(obj == null) {
			initMemu(id , "系统管理", Constant.TREE_ROOT_ID, "系统管理", intShowIndex, null, "系统管理", 1, false, "fa fa-windows");
		}
		
		intShowIndex+=5;
		id="66b55520972b47c6af628a8e9de63466";
		obj = operationManager.getObject(id);
		//如果是超级管理员权限 则增加到list
		listOperation.add(id);
		if(obj == null) {
			initMemu(id , "用户信息", parentId, "用户信息", intShowIndex, "/admin/system/user_info.jsp", "用户信息", 2, false, "");
		}
		
		intShowIndex+=5;
		id="016e751bc4e0462eb3b481eaed8ab7ee";
		obj = operationManager.getObject(id);
		//如果是超级管理员权限 则增加到list
		listOperation.add(id);
		if(obj == null) {
			initMemu(id , "用户管理", parentId, "用户管理", intShowIndex, "/admin/system/user_list.jsp", "用户管理", 2, false, "");
		}
		
		intShowIndex+=5;
		id="e64a3d04ace04e73987355482f1bf2cf";
		obj = operationManager.getObject(id);
		//如果是超级管理员权限 则增加到list
		listOperation.add(id);
		if(obj == null) {
			initMemu(id , "角色管理", parentId, "角色管理", intShowIndex, "/admin/system/sys_roles.jsp", "角色管理", 2, false, "");
		}
		
		intShowIndex+=5;
		id="c44a3d04ace05e73987355472f1bf2cf";
		obj = operationManager.getObject(id);
		//如果是超级管理员权限 则增加到list
		listOperation.add(id);
		if(obj == null) {
			initMemu(id , "登录日志", parentId, "登录日志", intShowIndex, "/admin/system/login_log.jsp", "登录日志", 2, false, "");
		}
		
		
		//首页图表统计
		listOperation.add(Constant.HOME_CHARTS_ID);
	}

	
	/***
	 * 初始化角色
	 * @throws Exception
	 */
	private void initRole(String id,String description,String name) throws Exception {
		SysRoles role = sysRolesManager.getObject(id);
		if(role == null){
			role = new SysRoles();
			role.setId(id);
			role.setDescription(description);
			role.setName(name);
			role.setFlage(true);
			sysRolesManager.createObject(role);
		}
	}
	
	@PostConstruct
	public void setInitFlow() throws Exception {

		//初始化平台超级管理员角色
		initRole(Constant.SYSTEMCONSTANT_ADMIN_ROLE_ID, "系统管理员", "系统管理员");

		//初始化平台超级管理员角色_资源关联
		for(int i = 0 ; i < listOperation.size(); i++) {
			String oid = listOperation.get(i);
			SysRoleOperation r = sysRoleOperationManager.getObjectByRoleIdOperationId(Constant.SYSTEMCONSTANT_ADMIN_ROLE_ID, oid);
			if(r == null) {
				initSysRoleOperation(oid);
			}
		}
	}
	
	/***
	 * 初始化平台超级管理员，某个资源的关联 
	 * @param operationId 某个资源的id
	 * @throws Exception 
	 */
	private void initSysRoleOperation(String operationId) throws Exception {
		SysRoleOperation roleOperation = new SysRoleOperation();
		roleOperation.setOperationId(operationId);
		roleOperation.setRoleId(Constant.SYSTEMCONSTANT_ADMIN_ROLE_ID);
		sysRoleOperationManager.createObject(roleOperation);
	}
	
}
