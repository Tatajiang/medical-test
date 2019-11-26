package com.jiang.medical.platform.system.service.rest;

import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.homolo.framework.rest.ActionMethod;
import com.homolo.framework.rest.RequestParameters;
import com.homolo.framework.rest.RestService;
import com.jiang.medical.platform.system.condition.LoginLogCondition;
import com.jiang.medical.platform.system.condition.UserCondition;
import com.jiang.medical.platform.system.domain.LoginLog;
import com.jiang.medical.platform.system.domain.User;
import com.jiang.medical.platform.system.manager.LoginLogManager;
import com.jiang.medical.platform.system.manager.UserManager;
import com.jiang.medical.util.AutoEvaluationUtil;
import com.jiang.medical.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestService(name = "user.LoginLogService")
public class LoginLogService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LoginLogManager loginLogManager;
	
	@Autowired
	private UserManager userManager;
	
	
	@ActionMethod(response = "json")
	public Object query(RequestParameters reqParams, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Sorter sorter = AutoEvaluationUtil.genSorter(reqParams);	//排序
			Range range = AutoEvaluationUtil.genRange(reqParams);		//页码
			
			String userName = reqParams.getParameter("userName", String.class);
			
			LoginLogCondition cn = new LoginLogCondition();
			AutoEvaluationUtil.evaluationObject(reqParams, cn);
			
			//通过用户昵称查询
			if (StringUtils.isNotBlank(userName)) {
				List<String> userIds = new ArrayList<String>();
				UserCondition userCondition = new UserCondition();
				userCondition.setLikeNickName(userName);
				List<User>  userList = userManager.list(userCondition);
				for (User user : userList) {
					userIds.add(user.getId());
				}
				cn.setUserIds(userIds);
			}


			PaginationSupport<LoginLog> pg = loginLogManager.pageList(cn, range, sorter);
			
			List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
			for(LoginLog obj : pg.getItems()){
				Map<String,Object> item = AutoEvaluationUtil.domainToMap(obj);
				User user = userManager.getObject(obj.getUserId());
				if (user != null) {
					item.put("userName", user.getNickname());
					item.put("genderName", user.getGender().getName());
					item.put("phone", user.getPhone());
				}
				item.put("createTime", DateUtil.formatDateToDateTime(obj.getCreateTime()));
				items.add(item);
			}
			map.put("rows", items);
			map.put("total", pg.getTotalCount());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return map;
	}
}
