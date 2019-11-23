package com.jiang.medical.platform.operation.manager;

import com.homolo.framework.annotation.DomainEngine;
import com.homolo.framework.dao.DomainObjectDao;
import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.jiang.medical.ProjectConfig;
import com.jiang.medical.platform.operation.condition.ItemCondition;
import com.jiang.medical.platform.operation.domain.Item;
import com.jiang.medical.platform.operation.domain.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 项目内容业务逻辑
 * @author: zhantuo.jiang
 * @create: 2019-11-23 13:24
 */
@DomainEngine(types = Item.class)
@Transactional(readOnly = false)
public class ItemManager {

    @Resource(name = ProjectConfig.PREFIX + "ItemDao")
    DomainObjectDao<Item> objDao;

    @DomainEngine.C
    @Transactional(rollbackFor = Exception.class)
    public String create(Item obj){
        return objDao.createObject(obj);
    }

    @DomainEngine.U
    @Transactional(rollbackFor = Exception.class)
    public void update(Item obj){
        objDao.updateObject(obj);
    }

    @DomainEngine.D
    @Transactional(rollbackFor = Exception.class)
    public void delete(Item obj){
        objDao.deleteObject(obj);
    }

    @DomainEngine.D
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id){
        objDao.deleteObject(id);
    }

    @DomainEngine.R
    public Item getObject(String id) {
        if(StringUtils.isNotBlank(id)){
            Item old = objDao.loadObject(id);
            if(null != old){
                Item  newObj = new Item();
                BeanUtils.copyProperties(old, newObj);
                return newObj;
            }
        }
        return null;
    }

    public List<Item> list(ItemCondition condition) {
        return objDao.findAllByCondition(condition);
    }

    public List<Item> list(ItemCondition condition, Sorter sorter) {
        return objDao.findAllByCondition(condition, sorter);
    }

    public List<Item> list(ItemCondition condition, int size) {
        return objDao.findByCondition(condition, size);
    }

    public List<Item> listAll() {
        return objDao.findAll();
    }

    public PaginationSupport<Item> pageList(ItemCondition condition, Range range, Sorter sorter) {
        return objDao.findByCondition(condition, range, sorter);
    }
}
