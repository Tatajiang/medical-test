package com.jiang.medical.platform.operation.manager;

import com.homolo.framework.annotation.DomainEngine;
import com.homolo.framework.dao.DomainObjectDao;
import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.jiang.medical.ProjectConfig;
import com.jiang.medical.platform.operation.condition.RelevanceCondition;
import com.jiang.medical.platform.operation.domain.Relevance;
import com.jiang.medical.platform.operation.domain.Relevance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: ${description}
 * @author: zhantuo.jiang
 * @create: 2019-11-23 13:56
 */
@DomainEngine(types = Relevance.class)
@Transactional(readOnly = false)
public class RelevanceManager {

    @Resource(name = ProjectConfig.PREFIX + "RelevanceDao")
    DomainObjectDao<Relevance> objDao;

    @DomainEngine.C
    @Transactional(rollbackFor = Exception.class)
    public String create(Relevance obj){
        return objDao.createObject(obj);
    }

    @DomainEngine.U
    @Transactional(rollbackFor = Exception.class)
    public void update(Relevance obj){
        objDao.updateObject(obj);
    }

    @DomainEngine.D
    @Transactional(rollbackFor = Exception.class)
    public void delete(Relevance obj){
        objDao.deleteObject(obj);
    }

    @DomainEngine.D
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id){
        objDao.deleteObject(id);
    }

    @DomainEngine.R
    public Relevance getObject(String id) {
        if(StringUtils.isNotBlank(id)){
            Relevance old = objDao.loadObject(id);
            if(null != old){
                Relevance newObj = new Relevance();
                BeanUtils.copyProperties(old, newObj);
                return newObj;
            }
        }
        return null;
    }

    public List<Relevance> list(RelevanceCondition condition) {
        return objDao.findAllByCondition(condition);
    }

    public List<Relevance> list(RelevanceCondition condition, Sorter sorter) {
        return objDao.findAllByCondition(condition, sorter);
    }

    public List<Relevance> list(RelevanceCondition condition, int size) {
        return objDao.findByCondition(condition, size);
    }

    public List<Relevance> listAll() {
        return objDao.findAll();
    }

    public PaginationSupport<Relevance> pageList(RelevanceCondition condition, Range range, Sorter sorter) {
        return objDao.findByCondition(condition, range, sorter);
    }
}
