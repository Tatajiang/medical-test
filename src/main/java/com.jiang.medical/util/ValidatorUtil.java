/** 
 * @Package  com.narwell.yicalljifa.util 
 * @Description 系统工具包
 * @author jianbo.tan
 * @date 2015-2-9 下午5:39:00 
 * @version V1.0 
 */
package com.jiang.medical.util;

import com.homolo.framework.bean.DomainObject;
import com.homolo.framework.rest.BaseDomainObjectServiceSupport;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 验证工具包
 * @author jianbo.tan
 * @date 2015-2-9 下午5:39:00
 */
public class ValidatorUtil extends BaseDomainObjectServiceSupport<DomainObject> {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	/**
	 * 新身份证是18位的
	 */
	public static final int NEW_IDNUMBER_LENGTH = 18;

	/**
	 * 老身份证是15位的
	 */
	public static final int OLD_IDNUMBER_LENGTH = 15;

	private ValidatorUtil() {

	}

	/**
	 * @Description 验证邮箱
	 * @param email
	 * @return boolean true 有效 email, false 无效 email
	 * @author jianbo.tan
	 * @date 2015-2-9 下午5:41:12
	 */
	public static boolean validateEmail(final String email) {
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();

	}

	/**
	 * @Description 将15位的身份证转成18位的
	 * @param oldNumber
	 * @return
	 * @return String
	 * @author jianbo.tan
	 * @date 2015-2-9 下午6:10:37
	 */
	public static String convertToNewIdNumber(String oldNumber) {
		if (oldNumber == null) {
			return oldNumber;
		}
		oldNumber = oldNumber.trim();
		if (oldNumber.length() == 15) {
			int[] w = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
			char[] A = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
			String ID17 = oldNumber.substring(0, 6) + "19"
					+ oldNumber.substring(6, 15);
			String newID17 = "";
			int[] ID17Array;
			ID17Array = new int[17];
			for (int i = 0; i < 17; i++) {
				ID17Array[i] = Integer.parseInt(ID17.substring(i, i + 1));
				newID17 += String.valueOf(ID17Array[i]);
			}
			int s = 0;
			for (int i = 0; i < 17; i++) {
				s = s + ID17Array[i] * w[i];
			}
			s = s % 11;
			return newID17 + A[s];
		}
		return oldNumber;
	}

	/**
	 * @Description 根据18位身份证得到出生日期
	 * @param idNumber
	 * @return Date
	 * @author jianbo.tan
	 * @date 2015-2-9 下午6:10:19
	 */
	public static Date parseBirthday(String idNumber) {
		if (idNumber.length() == OLD_IDNUMBER_LENGTH) {
			idNumber = convertToNewIdNumber(idNumber);
		}
		if (idNumber.length() == 18) {
			String birthdayStr = idNumber.substring(6, 14);
			try {
				return DateUtils.parseDate(birthdayStr,
						new String[] { "yyyyMMdd" });
			} catch (ParseException e) {
				// do nothing
			}
		}
		return null;
	}

	/**
	 * @Description JSR303验证
	 * @param errorMap
	 * @throws BusinessException
	 * @return void
	 * @author jianbo.tan
	 * @date 2015-3-18 下午9:06:40
	 */
	public static void JSR303Validate(Map<String, String> errorMap)
			throws Exception {
		if (!errorMap.isEmpty()) {
			throw new Exception("无效参数");
		}
	}

	/**
	 * @Description 去掉错误信息Map里的字段值
	 * @param errorMap
	 * @return
	 * @return Map<String,String>
	 * @author jianbo.tan
	 * @date 2015-3-17 下午3:03:36
	 */
	public static Map<String, String> getErrorMessageMap(
			Map<String, String> errorMap) {
		if (!errorMap.isEmpty()) {
			for (String key : errorMap.keySet()) {
				errorMap.put(key, errorMap.get(key).replace(key, ""));
			}
		}
		return errorMap;
	}

	/**
	 * @Description 去掉错误信息Map里的字段值
	 * @param errorMap
	 * @return
	 * @return String
	 * @author jianbo.tan
	 * @date 2015-3-17 下午3:03:36
	 */
	public static String getErrorMessage(Map<String, String> errorMap) {
		if (!errorMap.isEmpty()) {
			for (String key : errorMap.keySet()) {
				return errorMap.get(key).replace(key, "");
			}
		}
		return "";
	}

	/**
	 * 
	 * @Description 生成随机数
	 * @param len 随机数长度
	 * @return String  
	 * @author yifang.huang
	 * @date 2015-7-14 下午5:53:22
	 */
	public static String generationVerificationCode(int len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < len) {
			// 生成随机数，取绝对值，防止生成负数，
			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}
	
}
