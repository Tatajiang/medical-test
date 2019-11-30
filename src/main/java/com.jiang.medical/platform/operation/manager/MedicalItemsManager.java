package com.jiang.medical.platform.operation.manager;

import com.homolo.framework.annotation.DomainEngine;
import com.homolo.framework.dao.DomainObjectDao;
import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.jiang.medical.ProjectConfig;
import com.jiang.medical.platform.operation.condition.MedicalItemsCondition;
import com.jiang.medical.platform.operation.condition.RelevanceCondition;
import com.jiang.medical.platform.operation.domain.MedicalItems;
import com.jiang.medical.platform.operation.domain.Relevance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 体检套餐业务逻辑
 * @author: zhantuo.jiang
 * @create: 2019-11-20 20:11
 */
@DomainEngine(types = MedicalItems.class)
@Transactional(readOnly = false)
public class MedicalItemsManager {

    @Resource(name = ProjectConfig.PREFIX + "MedicalItemsDao")
    DomainObjectDao<MedicalItems> objDao;

    @Autowired
    private RelevanceManager relevanceManager;

    @Autowired
    private ItemManager itemManager;

    @DomainEngine.C
    @Transactional(rollbackFor = Exception.class)
    public String create(MedicalItems obj){
        return objDao.createObject(obj);
    }

    @DomainEngine.U
    @Transactional(rollbackFor = Exception.class)
    public void update(MedicalItems obj){
        objDao.updateObject(obj);
    }

    @DomainEngine.D
    @Transactional(rollbackFor = Exception.class)
    public void delete(MedicalItems obj){
        objDao.deleteObject(obj);
    }

    @DomainEngine.D
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id){
        objDao.deleteObject(id);
    }

    @DomainEngine.R
    public MedicalItems getObject(String id) {
        if(StringUtils.isNotBlank(id)){
            MedicalItems old = objDao.loadObject(id);
            if(null != old){
                MedicalItems newObj = new MedicalItems();
                BeanUtils.copyProperties(old, newObj);
                return newObj;
            }
        }
        return null;
    }

    public List<MedicalItems> list(MedicalItemsCondition condition) {
        return objDao.findAllByCondition(condition);
    }

    public List<MedicalItems> list(MedicalItemsCondition condition, Sorter sorter) {
        return objDao.findAllByCondition(condition, sorter);
    }

    public List<MedicalItems> list(MedicalItemsCondition condition, int size) {
        return objDao.findByCondition(condition, size);
    }

    public List<MedicalItems> listAll() {
        return objDao.findAll();
    }

    public PaginationSupport<MedicalItems> pageList(MedicalItemsCondition condition, Range range, Sorter sorter) {
        return objDao.findByCondition(condition, range, sorter);
    }


    /* *
     * @Description: 获取套餐所有指标信息
     * @Param: [medicalId]
     * @return: java.lang.String
     * @Author: zhantuo.jiang
     * @date: 2019/11/30 12:47
     */
    public String getItemNames(String medicalId){
        StringBuffer itemNames = new StringBuffer("");
        RelevanceCondition rCondition = new RelevanceCondition();
        rCondition.setMedicalId(medicalId);
        List<Relevance> rList = relevanceManager.list(rCondition);
        for (Relevance rObj : rList) {
            String name = itemManager.getObject(rObj.getItemId()).getName();
            if ("".equals(itemNames.toString())){
                itemNames.append(name);
            }else{
                itemNames.append("、"+name);
            }
        }
        return itemNames.toString();
    }
}


