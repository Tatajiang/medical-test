package com.jiang.medical.platform.operation.manager;

import com.homolo.framework.annotation.DomainEngine;
import com.homolo.framework.dao.DomainObjectDao;
import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.jiang.medical.ProjectConfig;
import com.jiang.medical.platform.operation.condition.ReservationRecordCondition;
import com.jiang.medical.platform.operation.domain.ReservationRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 预约记录业务逻辑类
 * @author: zhantuo.jiang
 * @create: 2019-12-03 14:12
 */
@DomainEngine(types = ReservationRecord.class)
@Transactional(readOnly = false)
public class ReservationRecordManager {

    @Resource(name = ProjectConfig.PREFIX + "ReservationRecordDao")
    DomainObjectDao<ReservationRecord> objDao;

    @DomainEngine.C
    @Transactional(rollbackFor = Exception.class)
    public String create(ReservationRecord obj){
        return objDao.createObject(obj);
    }

    @DomainEngine.U
    @Transactional(rollbackFor = Exception.class)
    public void update(ReservationRecord obj){
        objDao.updateObject(obj);
    }

    @DomainEngine.D
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReservationRecord obj){
        objDao.deleteObject(obj);
    }

    @DomainEngine.D
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id){
        objDao.deleteObject(id);
    }

    @DomainEngine.R
    public ReservationRecord getObject(String id) {
        if(StringUtils.isNotBlank(id)){
            ReservationRecord old = objDao.loadObject(id);
            if(null != old){
                ReservationRecord  newObj = new ReservationRecord();
                BeanUtils.copyProperties(old, newObj);
                return newObj;
            }
        }
        return null;
    }

    public List<ReservationRecord> list(ReservationRecordCondition condition) {
        return objDao.findAllByCondition(condition);
    }

    public List<ReservationRecord> list(ReservationRecordCondition condition, Sorter sorter) {
        return objDao.findAllByCondition(condition, sorter);
    }

    public List<ReservationRecord> list(ReservationRecordCondition condition, int size) {
        return objDao.findByCondition(condition, size);
    }

    public List<ReservationRecord> listAll() {
        return objDao.findAll();
    }

    public PaginationSupport<ReservationRecord> pageList(ReservationRecordCondition condition, Range range, Sorter sorter) {
        return objDao.findByCondition(condition, range, sorter);
    }
    
}
