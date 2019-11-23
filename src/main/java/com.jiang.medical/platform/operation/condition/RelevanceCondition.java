package com.jiang.medical.platform.operation.condition;

import com.homolo.framework.dao.hibernate.HibernateCondition;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * @description: 关联表条件查询
 * @author: zhantuo.jiang
 * @create: 2019-11-23 13:54
 */
public class RelevanceCondition implements HibernateCondition {

    private String itemId; 										//项目id

    private String medicalId; 								    //套餐id

    @Override
    public void populateDetachedCriteria(DetachedCriteria criteria) {

        if(StringUtils.isNotBlank(itemId)){
            criteria.add(Restrictions.eq("itemId", itemId));
        }

        if(StringUtils.isNotBlank(medicalId)){
            criteria.add(Restrictions.eq("medicalId", medicalId));
        }

    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }
}
