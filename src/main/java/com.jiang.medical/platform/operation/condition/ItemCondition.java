package com.jiang.medical.platform.operation.condition;

import com.homolo.framework.dao.hibernate.HibernateCondition;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

/**
 * @description: ${description}
 * @author: zhantuo.jiang
 * @create: 2019-11-23 13:21
 */
public class ItemCondition implements HibernateCondition {

    private String name; 										//项目名称

    private Date createTime;                                    //创建时间

    private List<String> neItemIds;							    //体检项目ids

    @Override
    public void populateDetachedCriteria(DetachedCriteria criteria) {

        if(StringUtils.isNotBlank(name)){
            criteria.add(Restrictions.like("name", name, MatchMode.ANYWHERE));
        }

        if(neItemIds != null ){
            if(neItemIds.size()==0){
                criteria.add(Restrictions.eq("id", "search-nothing"));
            }
            if (neItemIds.size() == 1) {
                criteria.add(Restrictions.eq("id", neItemIds.get(0)));
            }
            if (neItemIds.size() > 1){
                criteria.add(Restrictions.in("id", neItemIds));
            }
        }

        if(null != createTime){
            criteria.add(Restrictions.ge("createTime", createTime));
        }

        if(null != createTime){
            criteria.add(Restrictions.le("createTime", createTime));
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setItemIds(List<String> neItemIds) {
        this.neItemIds = neItemIds;
    }
}
