package com.jiang.medical.platform.operation.service.rest;

import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.homolo.framework.rest.ActionMethod;
import com.homolo.framework.rest.RequestParameters;
import com.homolo.framework.rest.RestService;
import com.homolo.framework.rest.ReturnResult;
import com.homolo.framework.service.ServiceResult;
import com.jiang.medical.platform.operation.condition.MedicalItemCondition;
import com.jiang.medical.platform.operation.domain.MedicalItem;
import com.jiang.medical.platform.operation.manager.MedicalItemManager;
import com.jiang.medical.util.AutoEvaluationUtil;
import com.jiang.medical.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 体检项目服务类
 * @author: zhantuo.jiang
 * @create: 2019-11-20 20:15
 */
@RestService(name = "platform.operation.MedicalItemService")
public class MedicalItemService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private MedicalItemManager medicalItemManager;


    /* *
     * @Description: 分页查询体检项目
     * @Param: [reqParams, request]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/11/20 20:26
     */
    @ActionMethod(response = "json")
    public Object query(RequestParameters reqParams, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Sorter sorter = AutoEvaluationUtil.genSorter(reqParams);	//排序
            Range range = AutoEvaluationUtil.genRange(reqParams);		//页码

            MedicalItemCondition cn = new MedicalItemCondition();
            AutoEvaluationUtil.evaluationObject(reqParams, cn);
 
            PaginationSupport<MedicalItem> pg = medicalItemManager.pageList(cn, range, sorter);

            List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
            for(MedicalItem obj : pg.getItems()){
                Map<String,Object> item = AutoEvaluationUtil.domainToMap(obj);
                item.put("suitableSexName", obj.getSuitableSex().getName());
                item.put("createTime", DateUtil.formatDateToDateTime(obj.getCreateTime()));
                items.add(item);
            }
            map.put("rows", items);
            map.put("total", pg.getTotalCount());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return map;
    }


    /* *
     * @Description: 创建体检项目
     * @Param: [reqParams]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/11/20 20:25
     */
    @ActionMethod(response = "json")
    public Object create(RequestParameters reqParams) {
        try {
            MedicalItem obj = new MedicalItem();
            AutoEvaluationUtil.evaluationObject(reqParams, obj);
            //TODO 校验参数正确性
            medicalItemManager.create(obj);
            return new ServiceResult(ReturnResult.SUCCESS, "添加成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ServiceResult(ReturnResult.FAILURE, e.getMessage());
        }
    }

    /* *
     * @Description: 修改体检项目
     * @Param: [reqParams]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/11/20 20:25
     */
    @ActionMethod(response = "json")
    public Object update(RequestParameters reqParams) {
        String id = reqParams.getParameter("id", String.class);
        try {
            MedicalItem obj = new MedicalItem();
            AutoEvaluationUtil.evaluationObject(reqParams, obj);
            //TODO 校验参数正确性
            medicalItemManager.update(obj);
            return new ServiceResult(ReturnResult.SUCCESS, "修改成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ServiceResult(ReturnResult.FAILURE, e.getMessage());
        }
    }


    /* *
     * @Description: 批量删除体检项目
     * @Param: [reqParams]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/11/20 20:25
     */
    @ActionMethod(response = "json")
    public Object deletes(RequestParameters reqParams) {
        try {
            String ids = reqParams.getParameter("ids", String.class);
            if (StringUtils.isNotBlank(ids)) {
                String[] id = ids.split(",");
                for (String s : id) {
                    MedicalItem obj = medicalItemManager.getObject(s);
                    if (obj == null) continue;
                    medicalItemManager.delete(obj.getId());
                }
            }
            return new ServiceResult(ReturnResult.SUCCESS, "批量删除成功");
        } catch (Exception e) {
            return new ServiceResult(ReturnResult.FAILURE, e.getMessage());
        }
    }


}
