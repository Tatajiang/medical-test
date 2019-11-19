/**
 * Project : police-affairs-webapp
 * FileName: OperationService.java
 * Copyright (c) 2018, www.narwell.com All Rights Reserved.
 */
package com.jiang.medical.platform.system.service.rest;

import com.homolo.framework.rest.ActionMethod;
import com.homolo.framework.rest.RequestParameters;
import com.homolo.framework.rest.RestService;
import com.homolo.framework.rest.ReturnResult;
import com.homolo.framework.rest.bind.annotation.ParameterVariable;
import com.homolo.framework.service.ServiceResult;
import com.jiang.medical.Constant;
import com.jiang.medical.platform.system.domain.Operation;
import com.jiang.medical.platform.system.domain.User;
import com.jiang.medical.platform.system.manager.OperationManager;
import com.jiang.medical.platform.system.manager.SysRoleOperationManager;
import com.jiang.medical.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作菜单管理
 * @author lq
 * Email: xiaolei_java@163.com 2018年10月23日 上午10:50:14
 */
@RestService(name = "system.OperationService")
public class OperationService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private OperationManager operationManager;
	
	@Autowired
	private SysRoleOperationManager sysRoleOperationManager;
	
	/**
	 * 
	 * @Description 获取树形菜单
	 * @param reqParams
	 * @return Object  
	 * @author leijing
	 * @date 2018年10月23日 上午11:14:15
	 */
	@ActionMethod(response = "json")
	public Object getTree(RequestParameters reqParams){
		try{
			String parentId = reqParams.getParameter("parentId", String.class);
			if(StringUtils.isBlank(parentId)){
				parentId = Constant.TREE_ROOT_ID;
			}
			return operationManager.getTree(parentId);
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new HashMap<>();
		}
	}
	
	/**
	 * 
	 * @Description 查询出所有的菜单
	 * @param reqParams
	 * @return 
	 * @return Object  
	 * @author leijing
	 * @date 2018年10月25日 下午2:52:02
	 */
	@ActionMethod(response = "json")
	public Object queryAll(RequestParameters reqParams) {
		try {
			List<Operation> list = operationManager.getAllOperations();
			return new ServiceResult(ReturnResult.SUCCESS , list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE,e.getMessage());
		}
	}
	
	/**
	 * 
	 * @Description 根据角色id查询出相关的菜单
	 * @param roleId
	 * @return 
	 * @return Object  
	 * @author leijing
	 * @date 2018年10月25日 下午2:52:42
	 */
	@ActionMethod(response = "json")
	public Object query(@ParameterVariable("roleId") String roleId) {
		try {
			List<Operation> list = sysRoleOperationManager.getOperationByRole(roleId);
			return new ServiceResult(ReturnResult.SUCCESS , list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE,e.getMessage());
		}
	}
	
	/**
	 * 修改角色权限
	 * @param roleId
	 * @param operationId
	 * @return
	 */
	@ActionMethod(response = "json")
	public Object setRoleOperation(@ParameterVariable("roleId") String roleId , @ParameterVariable("operationId") String operationId, HttpServletRequest request){
		try {
			sysRoleOperationManager.resetObjects(roleId, operationId);
			
			request.getSession().setAttribute("_user", SessionUtil.getCurrentUser(request));
			return new ServiceResult(ReturnResult.SUCCESS, "设置成功！");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE, e.getMessage());
		}
	}
	
	/**
	 * 得到所有菜单
	 * @param userId
	 * @return
	 */
	@ActionMethod(response = "json")
	public Object getMemus(RequestParameters reqParams, HttpServletRequest request){
		User user = SessionUtil.getCurrentUser(request);
		try {
			List<Map<String,Object>> list = operationManager.getMenueIdsByUser(user);
			return new ServiceResult(ReturnResult.SUCCESS , list);
		} catch (Exception e) {
			return new ServiceResult(ReturnResult.FAILURE,e.getMessage());
		}
	}
	
}
