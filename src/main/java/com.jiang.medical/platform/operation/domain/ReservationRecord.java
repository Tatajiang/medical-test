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
 * @description: 预约记录实体类
 * @author: zhantuo.jiang
 * @create: 2019-12-03 14:03
 */
@Entity
@Table(name = ProjectConfig.NAME + "_Reservation_Record")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class ReservationRecord implements DomainObject {

    @Id
    @PrimaryKey
    @Column(length = 32)
    private String id; 											//主键Id

    @Column(length = 32)
    private String medicalId; 								    //套餐id

    @Column(length = 32)
    private String userId; 								        //用户id

    @Column(length = 100)
    private Date reservationTime;                               //预约时间

    @Column(length = 10)
    private Boolean isDispose = false;                          //是否处理

    @Column
    private Date createTime = new Date();                       //创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getDispose() {
        return isDispose;
    }

    public void setDispose(Boolean dispose) {
        isDispose = dispose;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Date reservationTime) {
        this.reservationTime = reservationTime;
    }
}
