package com.jiang.medical.platform.operation.manager;

import com.homolo.framework.annotation.DomainEngine;
import com.homolo.framework.dao.DomainObjectDao;
import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.jiang.medical.ProjectConfig;
import com.jiang.medical.platform.operation.condition.InspectRecordCondition;
import com.jiang.medical.platform.operation.domain.InspectRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 体检记录业务逻辑类
 * @author: zhantuo.jiang
 * @create: 2019-12-18 10:22
 */
@DomainEngine(types = InspectRecord.class)
@Transactional(readOnly = false)
public class InspectRecordManager {
    @Resource(name = ProjectConfig.PREFIX + "InspectRecordDao")
    DomainObjectDao<InspectRecord> objDao;

    @DomainEngine.C
    @Transactional(rollbackFor = Exception.class)
    public String create(InspectRecord obj){
        return objDao.createObject(obj);
    }

    @DomainEngine.U
    @Transactional(rollbackFor = Exception.class)
    public void update(InspectRecord obj){
        objDao.updateObject(obj);
    }

    @DomainEngine.D
    @Transactional(rollbackFor = Exception.class)
    public void delete(InspectRecord obj){
        objDao.deleteObject(obj);
    }

    @DomainEngine.D
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id){
        objDao.deleteObject(id);
    }

    @DomainEngine.R
    public InspectRecord getObject(String id) {
        if(StringUtils.isNotBlank(id)){
            InspectRecord old = objDao.loadObject(id);
            if(null != old){
                InspectRecord  newObj = new InspectRecord();
                BeanUtils.copyProperties(old, newObj);
                return newObj;
            }
        }
        return null;
    }

    public List<InspectRecord> list(InspectRecordCondition condition) {
        return objDao.findAllByCondition(condition);
    }

    public List<InspectRecord> list(InspectRecordCondition condition, Sorter sorter) {
        return objDao.findAllByCondition(condition, sorter);
    }

    public List<InspectRecord> list(InspectRecordCondition condition, int size) {
        return objDao.findByCondition(condition, size);
    }

    public List<InspectRecord> listAll() {
        return objDao.findAll();
    }

    public PaginationSupport<InspectRecord> pageList(InspectRecordCondition condition, Range range, Sorter sorter) {
        return objDao.findByCondition(condition, range, sorter);
    }

    @DomainEngine.C
    @Transactional(rollbackFor = Exception.class)
    public void createObjByInfo(String userId,String itemId,String reservationId){
        InspectRecord obj = new InspectRecord();
        obj.setUserId(userId);
        obj.setItemId(itemId);
        obj.setReservationId(reservationId);
        objDao.createObject(obj);
    }

}
