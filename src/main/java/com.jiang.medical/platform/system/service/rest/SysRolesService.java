package com.jiang.medical.platform.system.service.rest;

import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.homolo.framework.rest.ActionMethod;
import com.homolo.framework.rest.RequestParameters;
import com.homolo.framework.rest.RestService;
import com.homolo.framework.rest.ReturnResult;
import com.homolo.framework.service.ServiceResult;
import com.homolo.framework.util.UUID;
import com.jiang.medical.platform.system.condition.SysRolesCondition;
import com.jiang.medical.platform.system.domain.SysRoles;
import com.jiang.medical.platform.system.manager.SysRolesManager;
import com.jiang.medical.util.AutoEvaluationUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统角色请求方法
 * @author zhouzhiguang
 *2018年10月20日 下午04:03:04
 */
@RestService(name = "system.SysRolesService")
public class SysRolesService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SysRolesManager sysRolesManager;
	
	/**
	 * @Description 系统角色分页查询
	 * @param reqParams
	 * @param request
	 * @return 
	 * @return Object  
	 * @author zhouzhiguang
	 * @date 2018-10-20 下午03:06:56
	 */
	@ActionMethod(response = "json")
	public Object query(RequestParameters reqParams, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Sorter sorter = AutoEvaluationUtil.genSorter(reqParams);	// 排序
			Range range = AutoEvaluationUtil.genRange(reqParams);		// 页码
			
			SysRolesCondition cn = new SysRolesCondition();
			AutoEvaluationUtil.evaluationObject(reqParams, cn);
			
			PaginationSupport<SysRoles> pg = sysRolesManager.pageList(cn, range, sorter);
			
			List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
			for(SysRoles obj : pg.getItems()){
				Map<String,Object> item = AutoEvaluationUtil.domainToMap(obj);
				item.put("level", obj.getLevel().getName());
				
				if(obj.getFlage() != null){
					if(obj.getFlage()){
						item.put("flage", "账户已启用");
					}else{
						item.put("flage", "账户已禁用");
					}
				}
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
	 * @Description 根据参数创建角色
	 * @param reqParams
	 * @return 
	 * @return Object  
	 * @author zhouzhiguang
	 * @date 2018-10-20 下午04:38:05
	 */
	@ActionMethod(response = "json")
	public Object create(RequestParameters reqParams){
		SysRoles obj = new SysRoles();
		try {
			AutoEvaluationUtil.evaluationObject(reqParams, obj);
			sysRolesManager.createObject(obj);
			return new ServiceResult(ReturnResult.SUCCESS,"添加成功"); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE, e.getMessage()); 
		}
	}
	
	/**
	 * 添加或更新对象
	 * @param reqParams
	 * @return
	 * @author zhouzhiguang
	 * @date 2018-10-23 上午09:25:15
	 */
	@ActionMethod(response = "json")
	public Object update(RequestParameters reqParams, HttpServletRequest request){
		try{
			String id = reqParams.getParameter("id", String.class);
			if(StringUtils.isBlank(id)){
				SysRoles obj = new SysRoles();
				AutoEvaluationUtil.evaluationObject(reqParams, obj);
				obj.setId(UUID.generateUUID());
				sysRolesManager.createObject(obj);
				return new ServiceResult(ReturnResult.SUCCESS, "添加成功！");
			}
			SysRoles obj = sysRolesManager.getObject(id);
			if(obj == null){
				return new ServiceResult(ReturnResult.FAILURE, "对象不存在！");
			}
			AutoEvaluationUtil.evaluationObject(reqParams, obj);
			sysRolesManager.updateObject(obj);
			return new ServiceResult(ReturnResult.SUCCESS, "修改成功！");
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE, "操作失败！");
		}
	}
	
	/**
	 * 禁用角色
	 * @author zhouzhiguang
	 * @date 2018-10-22 下午03:55:15
	 */
	@ActionMethod(response = "json")
	public Object delete(RequestParameters reqParams, HttpServletRequest request){
		try{
			String id = reqParams.getParameter("id", String.class);
			SysRoles obj = sysRolesManager.getObject(id);
			if(obj == null){
				return new ServiceResult(ReturnResult.FAILURE, "要禁用的对象不存在！");
			}
			if(sysRolesManager.isInIds(id)){
				return new ServiceResult(ReturnResult.FAILURE, "该角色已被其他用户关联，不能禁用！");
			}else{
				obj.setFlage(false);
				sysRolesManager.updateObject(obj);
				return new ServiceResult(ReturnResult.SUCCESS, "操作成功");
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE, "操作失败！");
		}
	}
	
	/**
	 * 启用角色
	 * @author zhouzhiguang
	 * @date 2018-10-26 上午11:15:05
	 */
	@ActionMethod(response = "json")
	public Object use(RequestParameters reqParams, HttpServletRequest request){
		try{
			String id = reqParams.getParameter("id", String.class);
			SysRoles obj = sysRolesManager.getObject(id);
			if(obj == null){
				return new ServiceResult(ReturnResult.FAILURE, "要启用的对象不存在！");
			}
			obj.setFlage(true);
			sysRolesManager.updateObject(obj);
			return new ServiceResult(ReturnResult.SUCCESS, "操作成功");
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE, "操作失败！");
		}
	}
	
	@ActionMethod(response = "json")
	public Object list(RequestParameters reqParams){
		try{
			return sysRolesManager.list(true);
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE, "操作失败！");
		}
	}
}
