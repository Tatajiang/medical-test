package com.jiang.medical.platform.operation.domain;

import com.homolo.framework.annotation.PrimaryKey;
import com.homolo.framework.bean.DomainObject;
import com.homolo.usersystem.domain.Domain;
import com.jiang.medical.ProjectConfig;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @description: 关联表(体检项目-体检套餐)
 * @author: zhantuo.jiang
 * @create: 2019-11-23 13:51
 */
@Entity
@Table(name = ProjectConfig.NAME + "_Relevance")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class Relevance implements DomainObject {

    @Id
    @PrimaryKey
    @Column(length = 32)
    private String id; 											//主键Id

    @Column(length = 32)
    private String itemId; 										//项目id

    @Column(length = 32)
    private String medicalId; 								    //套餐id

    @Column
    private Date createTime = new Date();                       //创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
