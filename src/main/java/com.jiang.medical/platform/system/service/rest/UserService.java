package com.jiang.medical.platform.system.service.rest;

import com.homolo.framework.bean.DomainObject;
import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.homolo.framework.rest.*;
import com.homolo.framework.service.ServiceResult;
import com.homolo.framework.util.MD5Util;
import com.jiang.medical.platform.enums.LevelEnum;
import com.jiang.medical.platform.system.condition.UserCondition;
import com.jiang.medical.platform.system.domain.LoginLog;
import com.jiang.medical.platform.system.domain.SysRoles;
import com.jiang.medical.platform.system.domain.User;
import com.jiang.medical.platform.system.manager.LoginLogManager;
import com.jiang.medical.platform.system.manager.SysRolesManager;
import com.jiang.medical.platform.system.manager.UserManager;
import com.jiang.medical.util.AutoEvaluationUtil;
import com.jiang.medical.util.DateUtil;
import com.jiang.medical.util.SessionUtil;
import com.jiang.medical.util.StringUtil;
import com.jiang.medical.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @Description 
 * @author yuanguo.huang
 * @date 2018-10-17 上午10:38:07 
 */
@RestService(name = "user.UserService")
public class UserService extends BaseDomainObjectServiceSupport<DomainObject> {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserManager userManager;
	
	@Autowired
	private SysRolesManager sysRolesManager;
	
	@Autowired
	private LoginLogManager loginLogManager;
	
	/**
	 * 
	 * @Description 用户分页查询
	 * @param reqParams
	 * @param request
	 * @return 
	 * @return Object  
	 * @author yuanguo.huang
	 * @date 2018-10-17 上午10:58:56
	 */
	@ActionMethod(response = "json")
	public Object query(RequestParameters reqParams, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Sorter sorter = AutoEvaluationUtil.genSorter(reqParams);	//排序
			Range range = AutoEvaluationUtil.genRange(reqParams);		//页码
			
			UserCondition cn = new UserCondition();
			AutoEvaluationUtil.evaluationObject(reqParams, cn);
			
			PaginationSupport<User> pg = userManager.pageList(cn, range, sorter);
			//查询所有管理员
			cn.setLevle(LevelEnum.Admin);
			
			List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
			for(User obj : pg.getItems()){
				Map<String,Object> item = AutoEvaluationUtil.domainToMap(obj);
				item.put("createTime", DateUtil.formatDateTime(obj.getCreateTime()));
				item.put("genderName",obj.getGender().getName());
				item.put("isDelete", obj.getIsDelete() ? "已禁用" : "已启用");
				item.put("roleNames", sysRolesManager.getNameByIds(obj.getRoleIds()));
				items.add(item);
			}
			
			map.put("rows", items);
			map.put("total", pg.getTotalCount());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return map;
	}
	
	/**
	 * 
	 * @Description 根据参数创建用户
	 * @param reqParams
	 * @return 
	 * @return Object  
	 * @author yuanguo.huang
	 * @date 2018-10-17 上午11:26:05
	 */
	@ActionMethod(response = "json")
	public Object create(RequestParameters reqParams){
		User obj = new User();
		try {
			AutoEvaluationUtil.evaluationObject(reqParams, obj);
			obj.setLevle(LevelEnum.Admin);
			//验证
			Map<String, String> erMap = validate(obj, RestSessionFactory.getCurrentContext());
			ValidatorUtil.JSR303Validate(erMap);
			userManager.create(obj);
			return new ServiceResult(ReturnResult.SUCCESS,"添加成功"); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE, e.getMessage()); 
		}
	}
	
	/**
	 * 
	 * @Description 修改用户信息
	 * @param reqParams
	 * @return 
	 * @return Object  
	 * @author yuanguo.huang
	 * @date 2018-10-17 下午12:13:16
	 */
	@ActionMethod(response = "json")
	public Object update(RequestParameters reqParams){
		String id = reqParams.getParameter("id", String.class);
		try{
			if (StringUtils.isBlank(id)){
				throw new Exception("参数不正确");
			}
			User obj = userManager.getObject(id);
			if (obj == null) {
				throw new Exception("查找不到用户");
			}
			
			AutoEvaluationUtil.evaluationObject(reqParams, obj);
			
			//验证
			Map<String, String> errorMap = validate(obj, RestSessionFactory.getCurrentContext());
			ValidatorUtil.JSR303Validate(errorMap);
			userManager.update(obj);
			return new ServiceResult(ReturnResult.SUCCESS,"修改成功");
		} catch(Exception e){
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE,e.getMessage());
		}
	}
	
	/**
	 * 添加或修改用户信息
	 * @param reqParams
	 * @return
	 * @author zhouzhiguang
	 * @date 2018-10-25 上午10:49:15
	 */
	@ActionMethod(response = "json")
	public Object addOrUpdate(RequestParameters reqParams, HttpServletRequest request){
		try{
			String id = reqParams.getParameter("id", String.class);
			String loginId = reqParams.getParameter("loginId", String.class);
			if(StringUtils.isBlank(id)){
				User obj = new User();
				AutoEvaluationUtil.evaluationObject(reqParams, obj);
				// 验证
				Map<String, String> erMap = validate(obj, RestSessionFactory.getCurrentContext());
				ValidatorUtil.JSR303Validate(erMap);
				if(obj.getPassword() != null){
					obj.setPassword(MD5Util.encryptPassword(obj.getPassword()));
				}else{
					obj.setPassword(MD5Util.encryptPassword("888888")); // 默认密码888888
				}
				if(userManager.getObjectByLoginId(loginId) != null){
					return new ServiceResult(ReturnResult.FAILURE, "登录名已存在！");
				}

				// 操作日志
				Map<String,String> map = new HashMap<String, String>();
				String service = (String)request.getSession().getAttribute("user_service"); //跳转地址
				
				if(StringUtils.isNotBlank(service) && !StringUtils.endsWith(service, "/admin/login.jsp")){
					map.put("service", service);
				}else{
					map.put("service", request.getContextPath() + "/admin/index.jsp");
				}
				request.getSession().setAttribute("_user", SessionUtil.getCurrentUser(request));
				userManager.create(obj);
				return new ServiceResult(ReturnResult.SUCCESS, "添加成功！");
			}
			
			User obj = userManager.getObject(id);
			if(obj == null){
				return new ServiceResult(ReturnResult.FAILURE, "对象不存在！");
			}
			// 验证
			Map<String, String> errorMap = validate(obj, RestSessionFactory.getCurrentContext());
			ValidatorUtil.JSR303Validate(errorMap);
			if(obj.getLoginId().equals(loginId)){
				AutoEvaluationUtil.evaluationObject(reqParams, obj);
				userManager.update(obj);
				return new ServiceResult(ReturnResult.SUCCESS, "修改成功！");
			}else if(userManager.getObjectByLoginId(loginId) == null){
				AutoEvaluationUtil.evaluationObject(reqParams, obj);
				userManager.update(obj);
				return new ServiceResult(ReturnResult.SUCCESS, "修改成功！");
			}else{
				return new ServiceResult(ReturnResult.FAILURE, "登录名已存在！");
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE, "操作失败！");
		}
	}
	
	/**
	 * 禁用用户
	 * @author zhouzhiguang
	 * @date 2018-10-25 上午10:43:15
	 */
	@ActionMethod(response = "json")
	public Object delete(RequestParameters reqParams,HttpServletRequest request){
		try{
			String id = reqParams.getParameter("id", String.class);
			User obj = userManager.getObject(id);
			if(obj == null){
				return new ServiceResult(ReturnResult.FAILURE, "要禁用的对象不存在！");
			}
			obj.setIsDelete(true);
			userManager.update(obj);
			return new ServiceResult(ReturnResult.SUCCESS, "禁用成功");
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE, "操作失败！");
		}
	}

	/**
	 * 启用用户
	 * @author zhouzhiguang
	 * @date 2018-10-31 上午10:44:15
	 */
	@ActionMethod(response = "json")
	public Object use(RequestParameters reqParams,HttpServletRequest request){
		try{
			String id = reqParams.getParameter("id", String.class);
			User obj = userManager.getObject(id);
			if(obj == null){
				return new ServiceResult(ReturnResult.FAILURE, "要启用的对象不存在！");
			}
			obj.setIsDelete(false);
			userManager.update(obj);
			return new ServiceResult(ReturnResult.SUCCESS, "启用成功");
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE, "操作失败！");
		}
	}
	
	/**
	 * 用户后台登录
	 * @param reqParams
	 * @param request
	 * @param response
	 * @return
	 */
	@ActionMethod(response = "json")
	public Object login(RequestParameters reqParams, HttpServletRequest request, HttpServletResponse response){
		try{
			Map<String,String> map = new HashMap<String, String>();
			
			String loginId = reqParams.getParameter("loginId", String.class);
			String password = reqParams.getParameter("password", String.class);
			String service = (String)request.getSession().getAttribute("user_service"); //跳转地址
			String error = "";
			
			loginId = StringUtils.trimToEmpty(loginId); //去掉前后空格
			User user = userManager.getObjectByLoginId(loginId);
			
			if(user == null){
				error = "登录名不存在！";
				map.put("error", error);
				return map;
			}
			if(!StringUtils.equals(StringUtil.passwordRemoveTrim(password), user.getPassword())){
				error = "密码错误！";
				map.put("error",error);
				return map;
			}
			if(user.getIsDelete()){
				error = "账号被禁用，请联系管理员！";
				map.put("error",error);
				return map;
			}
			if (user.getLevle() != LevelEnum.Admin) {
				error = "该账号不具备登录该页面权限！";
				map.put("error",error);
				return map;
			}
			
			if(StringUtils.isNotBlank(service) && !StringUtils.endsWith(service, "/admin/login.jsp")){
				map.put("service", service);
			}else{
				map.put("service", request.getContextPath() + "/admin/index.jsp");
			}
			request.getSession().setAttribute("_user", user);

			//如果是管理员才创建记录
			if (user.getLevle() == LevelEnum.Admin) {
				//开始创建登录日志
				LoginLog log = new LoginLog();
				log.setUserId(user.getId());
				log.setIp(SessionUtil.getRemoteIP(request));
				loginLogManager.create(log);
			}

			return map;
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("error", "服务器内部错误!");
			return map;
		}
	}
	
	/**
	 * 
	 * @Description 根据userId获取当前拥有的角色是否选中状态
	 * @param reqParams
	 * @return 
	 * @return Object  
	 * @author leijing
	 * @date 2018年10月26日 上午10:21:21
	 */
	@ActionMethod(response = "json")
	public Object getRolesByUser(RequestParameters reqParams){
		List<Map<String, Object>> result = new ArrayList<>();
		try{
			String userId = reqParams.getParameter("userId", String.class);
			User user = userManager.getObject(userId);
			if(user == null){
				return new ServiceResult(ReturnResult.FAILURE, "记录不存在！");
			}
			List<String> hasRoles = StringUtil.split(user.getRoleIds(), ",", true);
			
			List<SysRoles> list = sysRolesManager.list(true);
			for(SysRoles obj:list){
				Map<String, Object> map = AutoEvaluationUtil.domainToMap(obj);
				map.put("checked", hasRoles.contains(obj.getId()) ? "checked" : "");
				result.add(map);
			}
			return new ServiceResult(ReturnResult.SUCCESS, "数据获取成功！", result);
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE, "操作失败！");
		}
	}
	
	/**
	 * 
	 * @Description 保存设置好的角色
	 * @param reqParams
	 * @return 
	 * @return Object  
	 * @author leijing
	 * @date 2018年10月26日 上午10:58:05
	 */
	@ActionMethod(response = "json")
	public Object setRolesByUser(RequestParameters reqParams, HttpServletRequest request){
		try{
			String userId = reqParams.getParameter("userId", String.class);
			String roleIds = reqParams.getParameter("roleIds", String.class);
			
			User user = userManager.getObject(userId);
			if(user == null){
				return new ServiceResult(ReturnResult.FAILURE, "记录不存在！");
			}
			user.setRoleIds(roleIds);
			userManager.update(user);
			
			return new ServiceResult(ReturnResult.SUCCESS, "操作成功！");
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE, "操作失败！");
		}
	}
	
	/**
	 * 
	 * @Description 根据关键字模糊查询用户列表
	 * @param reqParams
	 * @return 
	 * @return Object  
	 * @author leijing
	 * @date 2018年11月4日 下午6:27:09
	 */
	@ActionMethod(response = "json")
	public Object searchByKeyword(RequestParameters reqParams){
		List<Map<String, String>> result = new ArrayList<>();
		try{
			String keyword = reqParams.getParameter("keyword", String.class);
			UserCondition condition = new UserCondition();
			condition.setIsDelete(false);
			condition.setKeyword(keyword);
			List<User> list = userManager.list(condition);
			for(User obj:list){
				Map<String, String> map = new HashMap<>();
				map.put("id", obj.getId());
				map.put("name", obj.getNickname());
				
				result.add(map);
			}
			return result;
		}catch (Exception e) {
			logger.error(e.getMessage());
			return result;
		}
	}
	
	/** 
	* @Description: 修改密码 
	* @param reqParams
	* @param request
	* @return Object
	* @author zhantuo.jiang
	* @date 2019年3月8日下午4:37:33
	*/ 
	@ActionMethod(response = "json")
	public Object updatePw(RequestParameters reqParams,HttpServletRequest request) {
		User obj = SessionUtil.getCurrentUser(request);
		String oldPass = reqParams.getParameter("oldPass", String.class);
		String newPassOne = reqParams.getParameter("newPassOne", String.class);
		String newPassTwo = reqParams.getParameter("newPassTwo", String.class);
		try {
			if (obj == null) {
				return new ServiceResult(ReturnResult.NOT_FOUND_OBJECT,"此账号已不存在！");
			}
			if(!obj.getPassword().equals(MD5Util.encryptPassword(StringUtil.passwordRemoveTrim(oldPass)))) {
				return new ServiceResult(ReturnResult.FAILURE, "旧密码不正确");
			}
			if (!StringUtils.equals(StringUtil.passwordRemoveTrim(newPassOne),newPassTwo)) {
				return new ServiceResult(ReturnResult.FAILURE, "两次输入的密码不一致！");
			}
			obj.setPassword(MD5Util.encryptPassword(StringUtil.passwordRemoveTrim(newPassOne))); // 加密
			userManager.update(obj);
			request.getSession().setAttribute("_user", obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ServiceResult(ReturnResult.SUCCESS, "密码修改成功！");
	}
}
