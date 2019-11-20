package com.jiang.medical.platform.operation.manager;

import com.homolo.framework.annotation.DomainEngine;
import com.homolo.framework.dao.DomainObjectDao;
import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.jiang.medical.ProjectConfig;
import com.jiang.medical.platform.operation.condition.MedicalItemCondition;
import com.jiang.medical.platform.operation.domain.MedicalItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 体检项目业务逻辑
 * @author: zhantuo.jiang
 * @create: 2019-11-20 20:11
 */
@DomainEngine(types = MedicalItem.class)
@Transactional(readOnly = false)
public class MedicalItemManager {

    @Resource(name = ProjectConfig.PREFIX + "MedicalItemDao")
    DomainObjectDao<MedicalItem> objDao;

    @DomainEngine.C
    @Transactional(rollbackFor = Exception.class)
    public String create(MedicalItem obj){
        return objDao.createObject(obj);
    }

    @DomainEngine.U
    @Transactional(rollbackFor = Exception.class)
    public void update(MedicalItem obj){
        objDao.updateObject(obj);
    }

    @DomainEngine.D
    @Transactional(rollbackFor = Exception.class)
    public void delete(MedicalItem obj){
        objDao.deleteObject(obj);
    }

    @DomainEngine.D
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id){
        objDao.deleteObject(id);
    }

    @DomainEngine.R
    public MedicalItem getObject(String id) {
        if(StringUtils.isNotBlank(id)){
            MedicalItem old = objDao.loadObject(id);
            if(null != old){
                MedicalItem  newObj = new MedicalItem();
                BeanUtils.copyProperties(old, newObj);
                return newObj;
            }
        }
        return null;
    }

    public List<MedicalItem> list(MedicalItemCondition condition) {
        return objDao.findAllByCondition(condition);
    }

    public List<MedicalItem> list(MedicalItemCondition condition, Sorter sorter) {
        return objDao.findAllByCondition(condition, sorter);
    }

    public List<MedicalItem> list(MedicalItemCondition condition, int size) {
        return objDao.findByCondition(condition, size);
    }

    public List<MedicalItem> listAll() {
        return objDao.findAll();
    }

    public PaginationSupport<MedicalItem> pageList(MedicalItemCondition condition, Range range, Sorter sorter) {
        return objDao.findByCondition(condition, range, sorter);
    }
    
}


