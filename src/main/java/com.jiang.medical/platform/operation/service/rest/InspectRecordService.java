package com.jiang.medical.platform.operation.service.rest;

import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.homolo.framework.rest.ActionMethod;
import com.homolo.framework.rest.RequestParameters;
import com.homolo.framework.rest.RestService;
import com.homolo.framework.rest.ReturnResult;
import com.homolo.framework.service.ServiceResult;
import com.jiang.medical.platform.operation.condition.InspectRecordCondition;
import com.jiang.medical.platform.operation.condition.ItemCondition;
import com.jiang.medical.platform.operation.condition.MedicalItemsCondition;
import com.jiang.medical.platform.operation.domain.Item;
import com.jiang.medical.platform.operation.domain.InspectRecord;
import com.jiang.medical.platform.operation.domain.MedicalItems;
import com.jiang.medical.platform.operation.domain.ReservationRecord;
import com.jiang.medical.platform.operation.manager.ItemManager;
import com.jiang.medical.platform.operation.manager.InspectRecordManager;
import com.jiang.medical.platform.operation.manager.MedicalItemsManager;
import com.jiang.medical.platform.operation.manager.ReservationRecordManager;
import com.jiang.medical.platform.system.condition.UserCondition;
import com.jiang.medical.platform.system.domain.User;
import com.jiang.medical.platform.system.manager.UserManager;
import com.jiang.medical.util.AutoEvaluationUtil;
import com.jiang.medical.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @description: ${description}
 * @author: zhantuo.jiang
 * @create: 2019-12-18 10:24
 */
@RestService(name = "platform.operation.InspectRecordService")
public class InspectRecordService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private InspectRecordManager inspectRecordManager;

    @Autowired
    private ItemManager itemManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ReservationRecordManager reservationRecordManager;

    @Autowired
    private MedicalItemsManager medicalItemsManager;

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

            String userName = reqParams.getParameter("userName", String.class);
            String itemName = reqParams.getParameter("itemName", String.class);

            InspectRecordCondition cn = new InspectRecordCondition();
            AutoEvaluationUtil.evaluationObject(reqParams, cn);

            //获取用户ids
            if (StringUtils.isNotBlank(userName)) {
                ArrayList<String> userIds = new ArrayList<>();
                UserCondition userCondition = new UserCondition();
                userCondition.setLikeNickName(userName);
                List<User> list = userManager.list(userCondition);
                for (User user : list) {
                    userIds.add(user.getId());
                }
                cn.setNeUserIds(userIds);
            }

            //获取项目id
            if (StringUtils.isNotBlank(itemName)) {
                ArrayList<String> itemIds = new ArrayList<>();
                ItemCondition itemCondition = new ItemCondition();
                itemCondition.setName(itemName);
                List<Item> itemList = itemManager.list(itemCondition);
                for (Item Item : itemList) {
                    itemIds.add(Item.getId());
                }
                cn.setNeItemIds(itemIds);
            }


            PaginationSupport<InspectRecord> pg = inspectRecordManager.pageList(cn, range, sorter);

            List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
            for(InspectRecord obj : pg.getItems()){
                Map<String,Object> item = AutoEvaluationUtil.domainToMap(obj);
                item.put("isComplete",obj.getComplete());
                User userObj = userManager.getObject(obj.getUserId());
                Item itemObj = itemManager.getObject(obj.getItemId());
                ReservationRecord reservationObj = reservationRecordManager.getObject(obj.getReservationId());
                if (userObj == null || itemObj == null || reservationObj == null) continue;
                item.put("userName", userObj.getNickname());
                item.put("itemName", itemObj.getName());
                item.put("medicalName",medicalItemsManager.getObject(reservationObj.getMedicalId()).getMedicalName());
                item.put("isCompleteName", obj.getComplete() ? "已完成" : "未完成");
                item.put("completeTime", obj.getCompleteTime() == null ? "未完成" :DateUtil.formatDateToDateTime(obj.getCompleteTime()));
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
            InspectRecord obj = new InspectRecord();
            AutoEvaluationUtil.evaluationObject(reqParams, obj);
            //TODO 校验参数正确性
            inspectRecordManager.create(obj);
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
            InspectRecord obj = new InspectRecord();
            AutoEvaluationUtil.evaluationObject(reqParams, obj);
            //TODO 校验参数正确性
            inspectRecordManager.update(obj);
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
                    InspectRecord obj = inspectRecordManager.getObject(s);
                    if (obj == null) continue;
                    inspectRecordManager.delete(obj.getId());
                }
            }
            return new ServiceResult(ReturnResult.SUCCESS, "批量删除成功");
        } catch (Exception e) {
            return new ServiceResult(ReturnResult.FAILURE, e.getMessage());
        }
    }

    /* *
     * @Description: 完成体检项目
     * @Param: [reqParams]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/12/18 20:33
     */
    @ActionMethod(response = "json")
    public Object completeInsperct(RequestParameters reqParams) {
        String id = reqParams.getParameter("id", String.class);
        try {
            InspectRecord obj = inspectRecordManager.getObject(id);
            obj.setComplete(true);
            obj.setCompleteTime(new Date());
            inspectRecordManager.update(obj);
            return new ServiceResult(ReturnResult.SUCCESS, "修改成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ServiceResult(ReturnResult.FAILURE, e.getMessage());
        }
    }

}
