package com.jiang.medical.platform.operation.service.rest;

import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.homolo.framework.rest.ActionMethod;
import com.homolo.framework.rest.RequestParameters;
import com.homolo.framework.rest.RestService;
import com.homolo.framework.rest.ReturnResult;
import com.homolo.framework.service.ServiceResult;
import com.jiang.medical.platform.operation.condition.RelevanceCondition;
import com.jiang.medical.platform.operation.domain.Item;
import com.jiang.medical.platform.operation.domain.Relevance;
import com.jiang.medical.platform.operation.manager.ItemManager;
import com.jiang.medical.platform.operation.manager.RelevanceManager;
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
 * @description: 关联表服务类
 * @author: zhantuo.jiang
 * @create: 2019-11-23 13:58
 */
@RestService(name = "platform.operation.RelevanceService")
public class RelevanceService {
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    
    @Autowired
    private RelevanceManager relevanceManager;

    @Autowired
    private ItemManager itemManager;

    /* *
     * @Description: 分页查询
     * @Param: [reqParams, request]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/11/23 14:01
     */
    @ActionMethod(response = "json")
    public Object query(RequestParameters reqParams, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Sorter sorter = AutoEvaluationUtil.genSorter(reqParams);	//排序
            Range range = AutoEvaluationUtil.genRange(reqParams);		//页码

            RelevanceCondition cn = new RelevanceCondition();
            AutoEvaluationUtil.evaluationObject(reqParams, cn);

            PaginationSupport<Relevance> pg = relevanceManager.pageList(cn, range, sorter);

            List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
            for(Relevance obj : pg.getItems()){
                Map<String,Object> item = AutoEvaluationUtil.domainToMap(obj);
                //在这里将体检项目内容解析
                Item itemObj = itemManager.getObject(obj.getItemId());
                if (itemObj == null) continue;
                item.put("name",itemObj.getName());
                item.put("description",itemObj.getDescription());
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
     * @Description: 创建
     * @Param: [reqParams]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/11/23 14:01
     */
    @ActionMethod(response = "json")
    public Object create(RequestParameters reqParams) {
        try {
            Relevance obj = new Relevance();
            AutoEvaluationUtil.evaluationObject(reqParams, obj);
            //TODO 校验参数正确性
            relevanceManager.create(obj);
            return new ServiceResult(ReturnResult.SUCCESS, "添加成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ServiceResult(ReturnResult.FAILURE, e.getMessage());
        }
    }


    /* *
     * @Description: 修改
     * @Param: [reqParams]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/11/23 14:01
     */
    @ActionMethod(response = "json")
    public Object update(RequestParameters reqParams) {
        String id = reqParams.getParameter("id", String.class);
        try {
            Relevance obj = new Relevance();
            AutoEvaluationUtil.evaluationObject(reqParams, obj);
            //TODO 校验参数正确性
            relevanceManager.update(obj);
            return new ServiceResult(ReturnResult.SUCCESS, "修改成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ServiceResult(ReturnResult.FAILURE, e.getMessage());
        }
    }


    /* *
     * @Description: 批量删除
     * @Param: [reqParams]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/11/23 14:01
     */
    @ActionMethod(response = "json")
    public Object deletes(RequestParameters reqParams) {
        try {
            String ids = reqParams.getParameter("ids", String.class);
            if (StringUtils.isNotBlank(ids)) {
                String[] id = ids.split(",");
                for (String s : id) {
                    Relevance obj = relevanceManager.getObject(s);
                    if (obj == null) continue;
                    relevanceManager.delete(obj.getId());
                }
            }
            return new ServiceResult(ReturnResult.SUCCESS, "批量删除成功");
        } catch (Exception e) {
            return new ServiceResult(ReturnResult.FAILURE, e.getMessage());
        }
    }
}
