package com.jiang.medical.platform.operation.condition;

import com.homolo.framework.dao.hibernate.HibernateCondition;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @description: 预约记录条件查询
 * @author: zhantuo.jiang
 * @create: 2019-12-03 14:08
 */
public class ReservationRecordCondition implements HibernateCondition {

    private String medicalId; 								    //套餐id

    private String userId; 								        //用户id

    private Boolean isDispose;                                  //是否处理

    private List<String> neMedicalIds;							//套餐ids

    private List<String> neUserIds;							    //用户ids

    @Override
    public void populateDetachedCriteria(DetachedCriteria criteria) {

        if(StringUtils.isNotBlank(medicalId)){
            criteria.add(Restrictions.eq("medicalId", medicalId));
        }

        if(StringUtils.isNotBlank(userId)){
            criteria.add(Restrictions.eq("userId", userId));
        }

        if(null != isDispose){
            criteria.add(Restrictions.eq("isDispose", isDispose));
        }

        if(neMedicalIds != null ){
            if(neMedicalIds.size()==0){
                criteria.add(Restrictions.eq("medicalId", "search-nothing"));
            }
            if (neMedicalIds.size() == 1) {
                criteria.add(Restrictions.eq("medicalId", neMedicalIds.get(0)));
            }
            if (neMedicalIds.size() > 1){
                criteria.add(Restrictions.in("medicalId", neMedicalIds));
            }
        }

        if(neUserIds != null ){
            if(neUserIds.size()==0){
                criteria.add(Restrictions.eq("userId", "search-nothing"));
            }
            if (neUserIds.size() == 1) {
                criteria.add(Restrictions.eq("userId", neUserIds.get(0)));
            }
            if (neUserIds.size() > 1){
                criteria.add(Restrictions.in("userId", neUserIds));
            }
        }

    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDispose(Boolean dispose) {
        isDispose = dispose;
    }

    public void setNeMedicalIds(List<String> neMedicalIds) {
        this.neMedicalIds = neMedicalIds;
    }

    public void setNeUserIds(List<String> neUserIds) {
        this.neUserIds = neUserIds;
    }
}
