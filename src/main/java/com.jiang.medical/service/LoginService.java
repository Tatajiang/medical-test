package com.jiang.medical.service;

import com.homolo.framework.rest.ActionMethod;
import com.homolo.framework.rest.RequestParameters;
import com.homolo.framework.rest.RestService;
import com.homolo.framework.util.MD5Util;
import com.jiang.medical.Constant;
import com.jiang.medical.platform.operation.domain.MedicalItems;
import com.jiang.medical.platform.operation.domain.ReservationRecord;
import com.jiang.medical.platform.operation.manager.MedicalItemsManager;
import com.jiang.medical.platform.operation.manager.ReservationRecordManager;
import com.jiang.medical.platform.system.domain.User;
import com.jiang.medical.platform.system.manager.UserManager;
import com.jiang.medical.util.RetInfo;
import com.jiang.medical.util.SessionUtil;
import com.jiang.medical.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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


}
