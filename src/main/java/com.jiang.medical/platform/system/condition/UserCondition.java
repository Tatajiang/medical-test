/** 
 * @Package com.yunmeng.admin.user.condition 
 * @Description 
 * @author yuanguo.huang
 * @date 2018-10-17 上午10:14:06 
 * @version V1.0 
 */ 
package com.jiang.medical.platform.system.condition;

import com.homolo.framework.dao.hibernate.HibernateCondition;
import com.jiang.medical.platform.enums.LevelEnum;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

/** 
 * @Description 用户查询条件
 * @author yuanguo.huang
 * @date 2018-10-17 上午10:14:06 
 */
public class UserCondition implements HibernateCondition {

	private String neId;									// 不等于此id	
	
	private Date geCreateTime;								// 创建时间大于等于设定时间
	
	private List<String> inIds;								// in用户IDS
	
	private String loginId; 								// 登入用户名
	
	private String likeLoginId;								// 模糊查询登录用户名
	
	private Date leCreateTime;								// 创建时间小于等于设定时间
	
	private String rolesId;                                 // 关联角色id
	
	private String keyword;									// 根据姓名、警员编号查询
	
	private String likeNickName;							// 模糊查询昵称
	
	private Boolean isRturnId;								// 是否只返回用户id
	
	private Boolean isDelete;								// 是否禁用(逻辑删除)

	private LevelEnum levle ;								// 用户级别

	private String card;									// 身份证号码

	private String phone; 									// 电话

	private String jsessionid;								//sessionId（用户登录内容）
	
	@Override
	public void populateDetachedCriteria(DetachedCriteria criteria) {
		
		if(isRturnId != null && isRturnId){
			criteria.setProjection(Projections.distinct(Projections.property("id")));
		}
		if(StringUtils.isNotBlank(keyword)){
			criteria.add(Restrictions.or(
				Restrictions.like("nickname", keyword, MatchMode.ANYWHERE),
				Restrictions.like("code", keyword, MatchMode.ANYWHERE)
			));
		}
		if(StringUtils.isNotBlank(neId)){
			criteria.add(Restrictions.ne("id", neId));
		}
		if(null != geCreateTime){
			criteria.add(Restrictions.ge("createTime", geCreateTime));
		}
		if(null != leCreateTime){
			criteria.add(Restrictions.le("createTime", leCreateTime));
		}
		if(inIds != null && inIds.size() > 0){
			criteria.add(Restrictions.in("id", inIds));
		}
		if(StringUtils.isNotBlank(loginId)){
			criteria.add(Restrictions.eq("loginId", loginId));
		}
		if(StringUtils.isNotBlank(likeLoginId)){
			criteria.add(Restrictions.like("loginId", likeLoginId, MatchMode.ANYWHERE));
		}
		if(StringUtils.isNoneBlank(rolesId)){
			criteria.add(Restrictions.like("roleIds", rolesId, MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(likeNickName)){
			criteria.add(Restrictions.like("nickname", likeNickName, MatchMode.ANYWHERE));
		}
		if(isDelete != null){
			criteria.add(Restrictions.eq("isDelete", isDelete));
		}
		if(levle != null){
			criteria.add(Restrictions.eq("levle", levle));
		}
		if(StringUtils.isNotBlank(card)){
			criteria.add(Restrictions.eq("card", card));
		}
		if(StringUtils.isNotBlank(phone)){
			criteria.add(Restrictions.eq("phone", phone));
		}
		if(StringUtils.isNotBlank(jsessionid)){
			criteria.add(Restrictions.eq("jsessionid", jsessionid));
		}
	}

	public void setNeId(String neId) {
		this.neId = neId;
	}

	public void setGeCreateTime(Date geCreateTime) {
		this.geCreateTime = geCreateTime;
	}

	public void setInIds(List<String> inIds) {
		this.inIds = inIds;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public void setLikeLoginId(String likeLoginId) {
		this.likeLoginId = likeLoginId;
	}

	public void setLeCreateTime(Date leCreateTime) {
		this.leCreateTime = leCreateTime;
	}

	public void setRolesId(String rolesId) {
		this.rolesId = rolesId;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setLikeNickName(String likeNickName) {
		this.likeNickName = likeNickName;
	}

	public void setIsRturnId(Boolean isRturnId) {
		this.isRturnId = isRturnId;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public void setRturnId(Boolean rturnId) {
		isRturnId = rturnId;
	}

	public void setDelete(Boolean delete) {
		isDelete = delete;
	}

	public void setLevle(LevelEnum levle) {
		this.levle = levle;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public void setJsessionid(String jsessionid) {
		this.jsessionid = jsessionid;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
