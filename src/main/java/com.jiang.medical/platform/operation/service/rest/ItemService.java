package com.jiang.medical.platform.operation.service.rest;

import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.homolo.framework.rest.ActionMethod;
import com.homolo.framework.rest.RequestParameters;
import com.homolo.framework.rest.RestService;
import com.homolo.framework.rest.ReturnResult;
import com.homolo.framework.service.ServiceResult;
import com.jiang.medical.platform.operation.condition.ItemCondition;
import com.jiang.medical.platform.operation.condition.RelevanceCondition;
import com.jiang.medical.platform.operation.domain.InspectRecord;
import com.jiang.medical.platform.operation.domain.Item;
import com.jiang.medical.platform.operation.domain.Relevance;
import com.jiang.medical.platform.operation.manager.ItemManager;
import com.jiang.medical.platform.operation.manager.RelevanceManager;
import com.jiang.medical.util.AutoEvaluationUtil;
import com.jiang.medical.util.DateUtil;
import com.jiang.medical.util.StringUtil;
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
 * @create: 2019-11-23 13:27
 */
@RestService(name = "platform.operation.ItemService")
public class ItemService {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private ItemManager itemManager;

    @Autowired
    private RelevanceManager relevanceManager;
    
    

    /* *
     * @Description: 分页查询
     * @Param: [reqParams, request]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/11/23 13:29
     */
    @ActionMethod(response = "json")
    public Object query(RequestParameters reqParams, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String absenceMedical = reqParams.getParameter("absenceMedical", String.class);
            Sorter sorter = AutoEvaluationUtil.genSorter(reqParams);	//排序
            Range range = AutoEvaluationUtil.genRange(reqParams);		//页码

            //不存在该套餐中的体检项目ids
            List<String> noExistItemIds = new ArrayList<>();
            //查询出已存在该套餐中的体检项目ids
            List<String> existItemIds = new ArrayList<>();
            if (StringUtils.isNotBlank(absenceMedical)){
                RelevanceCondition rCondition = new RelevanceCondition();
                rCondition.setMedicalId(absenceMedical);
                List<Relevance> rList = relevanceManager.list(rCondition);
                for (Relevance rObj : rList) {
                    existItemIds.add(rObj.getItemId());
                }
            }
            //查询所有得体检项目
            ItemCondition cn = new ItemCondition();
            AutoEvaluationUtil.evaluationObject(reqParams, cn);
            List<Item> itemList = itemManager.list(cn);
            //通过比较得出不存在改套餐中的体检项目ids(全部数据与已存在数据进行比较)
            for (Item iObj : itemList) {
                if (existItemIds.size() > 0 ){
                    if (existItemIds.contains(iObj.getId())) {
                        continue;
                    }
                }
                noExistItemIds.add(iObj.getId());
            }
            //进行最后得分页查询
            ItemCondition itemCondition = new ItemCondition();
            itemCondition.setItemIds(noExistItemIds);
            List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
            PaginationSupport<Item> pg = itemManager.pageList(itemCondition, range, sorter);
            for(Item obj : pg.getItems()){
                Map<String,Object> item = AutoEvaluationUtil.domainToMap(obj);
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
     * @date: 2019/11/23 13:29
     */
    @ActionMethod(response = "json")
    public Object create(RequestParameters reqParams) {
        try {
            Item obj = new Item();
            AutoEvaluationUtil.evaluationObject(reqParams, obj);
            //TODO 校验参数正确性
            itemManager.create(obj);
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
     * @date: 2019/11/23 13:29
     */
    @ActionMethod(response = "json")
    public Object update(RequestParameters reqParams) {
        String id = reqParams.getParameter("id", String.class);
        try {
            if (StringUtils.isBlank(id)) {
                return new ServiceResult(ReturnResult.FAILURE, "参数错误");
            }
            Item obj = itemManager.getObject(id);
            if (obj == null) {
                return new ServiceResult(ReturnResult.FAILURE, "对象不存在");
            }
            AutoEvaluationUtil.evaluationObject(reqParams, obj);
            itemManager.update(obj);
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
     * @date: 2019/11/23 13:29
     */
    @ActionMethod(response = "json")
    public Object deletes(RequestParameters reqParams) {
        try {
            String ids = reqParams.getParameter("ids", String.class);
            if (StringUtils.isNotBlank(ids)) {
                String[] id = ids.split(",");
                for (String s : id) {
                    Item obj = itemManager.getObject(s);
                    if (obj == null) continue;
                    itemManager.delete(obj.getId());
                }
            }
            return new ServiceResult(ReturnResult.SUCCESS, "批量删除成功");
        } catch (Exception e) {
            return new ServiceResult(ReturnResult.FAILURE, e.getMessage());
        }
    }
    
    
    
}
