package com.jiang.medical.platform.operation.condition;

import com.homolo.framework.dao.hibernate.HibernateCondition;
import com.jiang.medical.platform.enums.GenderEnum;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * @description: 体检项目条件查询
 * @author: zhantuo.jiang
 * @create: 2019-11-20 20:06
 */
public class MedicalItemCondition implements HibernateCondition {

    private String medicalName; 								// 体检项目名

    private GenderEnum suitableSex;	                            // 合适性别

    private Integer ageMin;	                                    //合适年龄（最小）

    private Integer ageMax;	                                    //合适年龄（最大）

    @Override
    public void populateDetachedCriteria(DetachedCriteria criteria) {

        if(StringUtils.isNotBlank(medicalName)){
            criteria.add(Restrictions.like("medicalName", medicalName, MatchMode.ANYWHERE));
        }

        if(null != suitableSex){
            criteria.add(Restrictions.eq("suitableSex", suitableSex));
        }

        if(null != ageMin){
            criteria.add(Restrictions.ge("ageMin", ageMin));
        }

        if(null != ageMax){
            criteria.add(Restrictions.le("ageMax", ageMax));
        }

    }

    public void setMedicalName(String medicalName) {
        this.medicalName = medicalName;
    }

    public void setSuitableSex(GenderEnum suitableSex) {
        this.suitableSex = suitableSex;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }
}
