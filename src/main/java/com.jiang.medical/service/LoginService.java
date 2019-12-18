package com.jiang.medical.service;

import com.homolo.framework.dao.util.PaginationSupport;
import com.homolo.framework.dao.util.Range;
import com.homolo.framework.dao.util.Sorter;
import com.homolo.framework.rest.ActionMethod;
import com.homolo.framework.rest.RequestParameters;
import com.homolo.framework.rest.RestService;
import com.homolo.framework.util.MD5Util;
import com.jiang.medical.Constant;
import com.jiang.medical.platform.operation.condition.MedicalItemsCondition;
import com.jiang.medical.platform.operation.condition.ReservationRecordCondition;
import com.jiang.medical.platform.operation.domain.MedicalItems;
import com.jiang.medical.platform.operation.domain.ReservationRecord;
import com.jiang.medical.platform.operation.manager.MedicalItemsManager;
import com.jiang.medical.platform.operation.manager.ReservationRecordManager;
import com.jiang.medical.platform.system.domain.User;
import com.jiang.medical.platform.system.manager.UserManager;
import com.jiang.medical.util.AutoEvaluationUtil;
import com.jiang.medical.util.DateUtil;
import com.jiang.medical.util.JsonUtil;
import com.jiang.medical.util.RetInfo;
import com.jiang.medical.util.SessionUtil;
import com.jiang.medical.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description: 登录时拥有得接口
 * @author: zhantuo.jiang
 * @create: 2019-11-25 12:28
 */
@RestService(name = "service.LoginService")
public class LoginService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserManager userManager;

    @Autowired
    private ReservationRecordManager reservationRecordManager;

    @Autowired
    private MedicalItemsManager medicalItemsManager;


    /* *
     * @Description: 获取个人信息
     * @Param: [reqParams, request]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/12/6 14:25
     */
    @ActionMethod(response = "json")
    public Object getPersonalInfo(RequestParameters reqParams, HttpServletRequest request) {
        try {
            User user = getUserBySessionId(reqParams, request);
            if (user == null) {
                return new RetInfo(RetInfo.AGAINLOGIN, "身份已过期，请重新登录");
            }
            User obj = userManager.getObject(user.getId());

            Map<String, Object> result = userManager.packUserInfo(obj);
            return new RetInfo(RetInfo.SUCCESS, "获取成功", JsonUtil.getJsonStrFromEntity(result));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new RetInfo(RetInfo.FAILURE, e.getMessage());
        }
    }
    
    /* *
     * @Description: 修改用户信息
     * @Param: [reqParams, request]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/12/6 14:19
     */
    @ActionMethod(response = "json")
    public Object updateUserInfo(RequestParameters reqParams, HttpServletRequest request) {
        try {
            User user = getUserBySessionId(reqParams, request);
            if (user == null) {
                return new RetInfo(RetInfo.AGAINLOGIN, "身份已过期，请重新登录");
            }
            User obj = userManager.getObject(user.getId());

            try {
                AutoEvaluationUtil.evaluationObject(reqParams, obj);
            } catch (Exception e) {
                return new RetInfo(RetInfo.FAILURE, "资料信息填写有误");
            }
            userManager.update(obj);
            //request.getSession().setAttribute(Constant.SESSION_USER_KEY, obj);
            Map<String, Object> result = userManager.packUserInfo(obj);
            return new RetInfo(RetInfo.SUCCESS, "个人资料修改成功", JsonUtil.getJsonStrFromEntity(result));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new RetInfo(RetInfo.FAILURE, e.getMessage());
        }
    }

    /* *
     * @Description: 修改密码
     * @Param: [reqParams, request]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/11/26 14:14
     */
    @ActionMethod(response = "json")
    public Object updatePassword(RequestParameters reqParams, HttpServletRequest request) {
        try {
            String oldPassword = reqParams.getParameter("oldPassword", String.class); // 旧密码
            String password = reqParams.getParameter("password", String.class); // 新密码
            String rePassword = reqParams.getParameter("rePassword", String.class); // 确认密码
            User user = getUserBySessionId(reqParams, request);
            if (user == null) {
                return new RetInfo(RetInfo.AGAINLOGIN, "身份已过期，请重新登录");
            }
            if (!password.equals(rePassword)) {
                return new RetInfo(RetInfo.FAILURE, "新密码与确认密码不相同");
            }
            if (!StringUtil.isLetterDigit(rePassword)) {
                return new RetInfo(RetInfo.FAILURE, "新密码无效,要求：至少包含字母及数字并且在6-12位");
            }
            if (!user.getPassword().equalsIgnoreCase(MD5Util.encryptPassword(oldPassword))) {
                return new RetInfo(RetInfo.FAILURE, "旧密码不正确");
            }
            user.setPassword(MD5Util.encryptPassword(password));
            userManager.update(user);
            // 刷新session
            request.getSession().setAttribute(Constant.SESSION_USER_KEY, user);
            return new RetInfo(RetInfo.SUCCESS, "密码修改成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new RetInfo(RetInfo.FAILURE, e.getMessage());
        }
    }


    /* *
     * @Description: 用户注销
     * @Param: [reqParams, request]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/11/26 14:14
     */
    @ActionMethod(response = "json")
    public Object userLogout(RequestParameters reqParams, HttpServletRequest request) {
        try {
            User user = getUserBySessionId(reqParams, request);
            if (user == null) {
                return new RetInfo(RetInfo.FAILURE, "您还未进行登录！");
            }
            // 清空session
            request.getSession().invalidate();
            return new RetInfo(RetInfo.SUCCESS, "注销成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new RetInfo(RetInfo.FAILURE, e.getLocalizedMessage());
        }
    }

    /* *
     * @Description: 通过Session获取最新的用户对象
     * @Param: [reqParams, request]
     * @return: com.jiang.medical.platform.system.domain.User
     * @Author: zhantuo.jiang
     * @date: 2019/11/26 14:10
     */
    private User getUserBySessionId(RequestParameters reqParams, HttpServletRequest request) throws Exception {
        String sessionId = reqParams.getParameter("jsessionid",String.class);
        // 获取登录会员的对象
        User sessionUser = SessionUtil.getCurrentUser(request);
        if (sessionUser == null) {
            if (StringUtils.isBlank(sessionId)) {
                return null;
            }
            //再通过sessionId 获取会员对象
            User obj = userManager.getObjectBySessionId(sessionId);
            if (obj == null) {
                return null;
            }
            //刷新session
            request.getSession().setAttribute(Constant.SESSION_USER_KEY, obj);
            obj.setJsessionid(request.getSession().getId());
            userManager.update(obj);
            return obj;
        }
        return sessionUser;
    }

    /* *
     * @Description: 创建预约记录
     * @Param: [reqParams, request]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/12/3 21:01
     */
    @ActionMethod(response = "json")
    public Object createReservationRecord(RequestParameters reqParams, HttpServletRequest request) {
        try {
            User user = getUserBySessionId(reqParams, request);
            if (user == null) {
                return new RetInfo(RetInfo.FAILURE, "请先进行登录！");
            }
            String medicalId = reqParams.getParameter("medicalId", String.class); // 套餐id
            Date reservationTime = reqParams.getParameter("reservationTime", Date.class); // 套餐id

            if (StringUtils.isBlank(medicalId) || null == reservationTime) {
                return new RetInfo(RetInfo.FAILURE, "参数错误");
            }
            MedicalItems medicalObj = medicalItemsManager.getObject(medicalId);
            if (null == medicalObj){
                return new RetInfo(RetInfo.FAILURE, "所预约的套餐出现错误");
            }
            ReservationRecord obj = new ReservationRecord();
            obj.setUserId(user.getId());
            obj.setMedicalId(medicalId);
            obj.setReservationTime(reservationTime);
            reservationRecordManager.create(obj);
            return new RetInfo(RetInfo.SUCCESS, "预约成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new RetInfo(RetInfo.FAILURE, e.getLocalizedMessage());
        }
    }


    /* *
     * @Description: 获取我的预约记录
     * @Param: [reqParams, request]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/12/6 13:55
     */
    @ActionMethod(response = "json")
    public Object queryReservationRecord(RequestParameters reqParams, HttpServletRequest request) {
        try {
            User user = getUserBySessionId(reqParams, request);
            if (user == null) {
                return new RetInfo(RetInfo.FAILURE, "请先进行登录！");
            }

            Sorter sorter = AutoEvaluationUtil.genSorter(reqParams); 					// 排序
            Range range = AutoEvaluationUtil.genRange(reqParams); 						// 页码
            String medicalName = reqParams.getParameter("medicalName", String.class); // 套餐名称

            ReservationRecordCondition cn = new ReservationRecordCondition();
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
            PaginationSupport<ReservationRecord> ps = reservationRecordManager.pageList(cn, range, sorter);
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            for(ReservationRecord obj : ps.getItems()){
                Map<String,Object> item = AutoEvaluationUtil.domainToMap(obj);
                MedicalItems medical = medicalItemsManager.getObject(obj.getMedicalId());
                if (null == medical)  continue;
                item.put("userName",user.getNickname());
                item.put("userCard", StringUtils.isNotBlank(user.getCard()) ? user.getCard() : "用户暂未填写该信息！");
                item.put("medicalName",medical.getMedicalName());
                //获取套餐内容
                item.put("meidicalContent",medicalItemsManager.getItemNames(medical.getId()));
                item.put("meaning",medical.getMeaning());
                //解析时间
                item.put("reservationTime", DateUtil.formatDateToDateTime(obj.getReservationTime()));
                item.put("createTime", DateUtil.formatDateToDateTime(obj.getCreateTime()));
                result.add(item);
            }
            return new RetInfo(RetInfo.SUCCESS,"获取成功", JsonUtil.getJsonStrFromEntity(result),
                    ps.getCurrentPageNo(), ps.getTotalPage(), ps.getPageSize(),  ps.getTotalCount());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new RetInfo(RetInfo.FAILURE, e.getLocalizedMessage());
        }
    }
    


}
