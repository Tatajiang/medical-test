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
 * @description: 项目内容
 * @author: zhantuo.jiang
 * @create: 2019-11-23 13:17
 */
@Entity
@Table(name = ProjectConfig.NAME + "_Item")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class Item implements DomainObject {

    @Id
    @PrimaryKey
    @Column(length = 32)
    private String id; 											//主键Id

    @Column(length = 100)
    private String name; 										//项目名称

    @Column(length = 255)
    private String description;                                 //项目描述

    @Column
    private Date createTime = new Date();                       //创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
