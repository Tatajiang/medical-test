package com.jiang.medical.platform.system.condition;

import com.homolo.framework.dao.hibernate.HibernateCondition;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/* *
 * @Description: 登录日志条件查询类
 * @Param:
 * @return:
 * @Author: zhantuo.jiang
 * @date: 2019/12/24 14:13
 */
public class LoginLogCondition implements HibernateCondition {
	
	private String userId; 				 // 登入用户id
	
	private String ip; 					 // 登入IP
	
	 private List<String> userIds;		 // 用户ids


	@Override
	public void populateDetachedCriteria(DetachedCriteria criteria) {
		
		if(StringUtils.isNotBlank(userId)){
			criteria.add(Restrictions.like("userId", userId, MatchMode.ANYWHERE));
		}
		
		if(StringUtils.isNotBlank(ip)){
			criteria.add(Restrictions.like("ip", ip, MatchMode.ANYWHERE));
		}
		
		if(userIds != null ){
            if(userIds.size()==0){
                criteria.add(Restrictions.eq("userId", "search-nothing"));
            }
            if (userIds.size() == 1) {
                criteria.add(Restrictions.eq("userId", userIds.get(0)));
            }
            if (userIds.size() > 1){
                criteria.add(Restrictions.in("userId", userIds));
            }
        }
		
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}
	
}
