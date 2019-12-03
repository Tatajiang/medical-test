package com.jiang.medical.platform.operation.service.rest;

import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.homolo.framework.rest.ActionMethod;
import com.homolo.framework.rest.RequestParameters;
import com.homolo.framework.rest.RestService;
import com.jiang.medical.platform.operation.condition.MedicalItemsCondition;
import com.jiang.medical.platform.operation.condition.ReservationRecordCondition;
import com.jiang.medical.platform.operation.domain.Item;
import com.jiang.medical.platform.operation.domain.MedicalItems;
import com.jiang.medical.platform.operation.domain.ReservationRecord;
import com.jiang.medical.platform.operation.manager.MedicalItemsManager;
import com.jiang.medical.platform.operation.manager.ReservationRecordManager;
import com.jiang.medical.platform.system.condition.UserCondition;
import com.jiang.medical.platform.system.domain.User;
import com.jiang.medical.platform.system.manager.UserManager;
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
 * @description: 预约记录服务类
 * @author: zhantuo.jiang
 * @create: 2019-12-03 14:15
 */
@RestService(name = "platform.operation.ReservationRecordService")
public class ReservationRecordService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    
    @Autowired
    private ReservationRecordManager reservationRecordManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private MedicalItemsManager medicalItemsManager;
    /* *
     * @Description: 分页查询
     * @Param: [reqParams, request]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/12/3 14:18
     */
    @ActionMethod(response = "json")
    public Object query(RequestParameters reqParams, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Sorter sorter = AutoEvaluationUtil.genSorter(reqParams);	//排序
            Range range = AutoEvaluationUtil.genRange(reqParams);		//页码

            String userName = reqParams.getParameter("userName", String.class);
            String medicalName = reqParams.getParameter("medicalName", String.class);

            ReservationRecordCondition cn = new ReservationRecordCondition();
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
                if (userIds.size() > 0) {
                    cn.setNeUserIds(userIds);
                }
            }
            //获取套餐ids
            if (StringUtils.isNotBlank(medicalName)) {
                ArrayList<String> medicalIds = new ArrayList<>();
                MedicalItemsCondition medicalItemsCondition = new MedicalItemsCondition();
                medicalItemsCondition.setMedicalName(medicalName);
                List<MedicalItems> list = medicalItemsManager.list(medicalItemsCondition);
                for (MedicalItems medicalItems : list) {
                    medicalIds.add(medicalItems.getId());
                }
                if (medicalIds.size() > 0 ){
                    cn.setNeMedicalIds(medicalIds);
                }
            }

            PaginationSupport<ReservationRecord> pg = reservationRecordManager.pageList(cn, range, sorter);

            List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
            for(ReservationRecord obj : pg.getItems()){
                Map<String,Object> item = AutoEvaluationUtil.domainToMap(obj);
                //进行解析数据
                User user = userManager.getObject(obj.getUserId());
                MedicalItems medical = medicalItemsManager.getObject(obj.getMedicalId());
                if (null == user || null == medical)  continue;
                item.put("userName",user.getNickname());
                item.put("userCard", StringUtils.isNotBlank(user.getCard()) ? user.getCard() : "用户暂未填写该信息！");
                item.put("medicalName",medical.getMedicalName());
                //解析时间
                item.put("reservationTime", DateUtil.formatDateToDateTime(obj.getReservationTime()));
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
    
}
