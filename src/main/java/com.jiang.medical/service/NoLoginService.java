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
import com.jiang.medical.platform.operation.domain.MedicalItems;
import com.jiang.medical.platform.operation.manager.ItemManager;
import com.jiang.medical.platform.operation.manager.MedicalItemsManager;
import com.jiang.medical.platform.system.domain.User;
import com.jiang.medical.platform.system.manager.UserManager;
import com.jiang.medical.util.AutoEvaluationUtil;
import com.jiang.medical.util.DateUtil;
import com.jiang.medical.util.JsonUtil;
import com.jiang.medical.util.RetInfo;
import com.jiang.medical.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @description: 未登录时拥有得接口
 * @author: zhantuo.jiang
 * @create: 2019-11-25 12:28
 */
@RestService(name = "service.NoLoginService")
public class NoLoginService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserManager userManager;

    @Autowired
    private MedicalItemsManager medicalItemsManager;

    @Autowired
    private ItemManager itemManager;

    /* *
     * @Description: 注册
     * @Param: [reqParams, request]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/11/26 15:43
     */
    @ActionMethod(response = "json")
    public Object register(RequestParameters reqParams ,HttpServletRequest request) {
        try {
            String phone = reqParams.getParameter("phone", String.class);				    // 手机号
            String password = reqParams.getParameter("password", String.class);			// 密码
            User.Gender gender = reqParams.getParameter("gender", User.Gender.class);    //  性别

            if (StringUtils.isBlank(phone) || StringUtils.isBlank(StringUtil.checkIsPhone(phone))) {
                return new RetInfo(RetInfo.FAILURE, "手机号码无效!");
            }
            if (StringUtils.isBlank(password)) {
                return new RetInfo(RetInfo.FAILURE, "密码不能为空!");
            }
            if (!StringUtil.isLetterDigit(password)) {
                return new RetInfo(RetInfo.FAILURE, "密码至少包含字母及数字且在6-11位");
            }
            User user = userManager.getObjectByMobile(phone);
            if (user != null) {
                return new RetInfo(RetInfo.FAILURE, "该手机号码已注册!");
            }
            String sessionId = request.getSession().getId();
            User obj = userManager.register(sessionId,phone,password,gender);

            //格式化返回会员信息
            Map<String, Object> result = userManager.packUserInfo(obj);
            request.getSession().setAttribute(Constant.SESSION_USER_KEY, obj);
            //TODO:返回用户信息
            return new RetInfo(RetInfo.SUCCESS, "注册成功！", JsonUtil.getJsonStrFromEntity(result));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new RetInfo(RetInfo.FAILURE, e.getMessage());
        }
    }


    /**
     * @Description: 登录接口
     * @param request
     * @param reqParams
     * @return Object
     * @author zhantuo.jiang
     * @date 2019年5月9日下午2:53:02
     */
    @ActionMethod(response = "json")
    public Object login(HttpServletRequest request, RequestParameters reqParams) {
        try {
            String loginId = reqParams.getParameter("phone", String.class);			// 手机号
            String password = reqParams.getParameter("password", String.class);		// 密码

            //校验参数
            if (StringUtils.isBlank(loginId) || StringUtils.isBlank(password)) {
                return new RetInfo(RetInfo.FAILURE, "参数错误");
            }

            //获取用户对象内容
            User obj = userManager.getObjectByLoginId(loginId);
            if(obj == null){
                return new RetInfo(RetInfo.FAILURE, "用户名错误！");
            }

            //判断密码信息
            if (!MD5Util.encryptPassword(password).equals(obj.getPassword())) {
                return new RetInfo(RetInfo.FAILURE, "密码错误！");
            }

            //修改jsessonid
            obj.setJsessionid(request.getSession().getId());
            userManager.update(obj);

            //格式化返回会员信息
            Map<String, Object> result = userManager.packUserInfo(obj);
            return new RetInfo(RetInfo.SUCCESS, "登录成功！", JsonUtil.getJsonStrFromEntity(result));
        }catch (Exception e) {
            logger.error(e.getMessage());
            return new RetInfo(RetInfo.FAILURE, e.getMessage());
        }
    }


    /* *
     * @Description: 获取所有套餐信息
     * @Param: [request, reqParams]
     * @return: java.lang.Object
     * @Author: zhantuo.jiang
     * @date: 2019/11/30 12:41
     */
    @ActionMethod(response = "json")
    public Object getAllMedicalItems(HttpServletRequest request, RequestParameters reqParams) {
        try {
            Sorter sorter = AutoEvaluationUtil.genSorter(reqParams); 					// 排序
            Range range = AutoEvaluationUtil.genRange(reqParams); 						// 页码

            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

            //分页查询
            MedicalItemsCondition condition = new MedicalItemsCondition();
            //condition.setShow(true);
            PaginationSupport<MedicalItems> ps = medicalItemsManager.pageList(condition,range,sorter.asc("createTime"));
            for (MedicalItems obj : ps.getItems()) {
                Map<String, Object> list = AutoEvaluationUtil.domainToMap(obj);
                list.put("createTime", DateUtil.formatDateToDateTime(obj.getCreateTime()));
                list.put("items",medicalItemsManager.getItemNames(obj.getId()));
                list.put("suitableSex",obj.getSuitableSex().getName());
                result.add(list);
            }

            return new RetInfo(RetInfo.SUCCESS,"获取体检套餐信息成功", JsonUtil.getJsonStrFromEntity(result),
                    ps.getCurrentPageNo(), ps.getTotalPage(), ps.getPageSize(),  ps.getTotalCount());
        }catch (Exception e) {
            logger.error(e.getMessage());
            return new RetInfo(RetInfo.FAILURE, e.getMessage());
        }
    } 
    
    
}
