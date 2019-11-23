package com.jiang.medical.platform.operation.domain;

import com.homolo.framework.annotation.PrimaryKey;
import com.homolo.framework.bean.DomainObject;
import com.jiang.medical.ProjectConfig;
import com.jiang.medical.platform.enums.GenderEnum;
import com.jiang.medical.platform.system.domain.SysRoles;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

/**
 * @description: 体检套餐
 * @author: zhantuo.jiang
 * @create: 2019-11-20 19:32
 */
@Entity
@Table(name = ProjectConfig.NAME + "_Medical_Item")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class MedicalItems implements DomainObject{

    @Id
    @PrimaryKey
    @Column(length = 32)
    private String id; 											// 主键Id


    @Column(length = 255)
    @Index(name="Index_Item_Name")
    private String medicalName; 								// 体检套餐名

    @Column(length = 255)
    private String meaning; 								    // 指标意义


    @Enumerated(EnumType.STRING)
    private GenderEnum suitableSex = GenderEnum.Both;	        //合适性别


    @Column(length = 10)
    private Integer ageMin;	                                    //合适年龄（最小）


    @Column(length = 10)
    private Integer ageMax;	                                    //合适年龄（最大）

    @Column(length = 1)
    private Boolean isShow = false;                             //用于是否展示

    @Column
    private String showImg;                                     //展示图片（先不限制长度）

    @Column
    private Date createTime = new Date();                       //创建时间


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicalName() {
        return medicalName;
    }

    public void setMedicalName(String medicalName) {
        this.medicalName = medicalName;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public GenderEnum getSuitableSex() {
        return suitableSex;
    }

    public void setSuitableSex(GenderEnum suitableSex) {
        this.suitableSex = suitableSex;
    }

    public Integer getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    public String getShowImg() {
        return showImg;
    }

    public void setShowImg(String showImg) {
        this.showImg = showImg;
    }
}
