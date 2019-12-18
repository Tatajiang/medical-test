package com.jiang.medical.platform.operation.condition;

import com.homolo.framework.dao.hibernate.HibernateCondition;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * @description: 体检项目条件查询
 * @author: zhantuo.jiang
 * @create: 2019-12-18 10:14
 */
public class InspectRecordCondition implements HibernateCondition {

    private String userId; 									    //用户id

    private String itemId; 									    //项目id

    private String reservationId; 								//预约记录id

    private Boolean isComplete;                                 //是否体检完成

    @Override
    public void populateDetachedCriteria(DetachedCriteria criteria) {

        if(StringUtils.isNotBlank(userId)){
            criteria.add(Restrictions.eq("userId", userId));
        }

        if(StringUtils.isNotBlank(itemId)){
            criteria.add(Restrictions.eq("itemId", itemId));
        }

        if(StringUtils.isNotBlank(reservationId)){
            criteria.add(Restrictions.eq("reservationId", reservationId));
        }

        if(null != isComplete ){
            criteria.add(Restrictions.eq("isComplete", isComplete));
        }

    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }
}

