package com.jiang.medical.platform.operation.domain;

import com.homolo.framework.annotation.PrimaryKey;
import com.homolo.framework.bean.DomainObject;
import com.jiang.medical.ProjectConfig;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description: 体检记录
 * @author: zhantuo.jiang
 * @create: 2019-12-18 10:08
 */
@Entity
@Table(name = ProjectConfig.NAME + "_InspectRecord")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class InspectRecord implements DomainObject {

    @Id
    @PrimaryKey
    @Column(length = 32)
    private String id; 											//主键Id

    @Column(length = 32)
    private String userId; 									    //用户id

    @Column(length = 32)
    private String itemId; 									    //项目id

    @Column(length = 32)
    private String reservationId; 								//预约记录id

    @Column(length = 10)
    private Boolean isComplete = false;                        //是否体检完成

    @Column
    private Date completeTime;                                 //完成时间

    @Column
    private Date createTime = new Date();                      //创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }
}
