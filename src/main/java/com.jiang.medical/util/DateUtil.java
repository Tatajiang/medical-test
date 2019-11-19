package com.jiang.medical.util;

import com.homolo.framework.util.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
	/**
	 * 日期转换为字符串
	 * @param date	转换的对象
	 * @param format	字符串格式
	 * @param nullStr	当对象为空时返回的自定义字符串
	 * @return
	 */
	public static String formatDate(Date date,String format,String nullStr){
		if(date==null){
			return nullStr == null ? "" : nullStr;
		}
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	/**
	 * 
	 * @Description	根据时间戳获取时间 
	 * @param time
	 * @return 
	 * @return Date  
	 * @author shidong.zhao
	 * @date 2016-7-13 下午2:38:29
	 */
	public static Date getDateByTimeStamp(long time){
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = format.format(time); 
		Date date = null;
		try {
			date = format.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String formateDateMonth(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 	
		Calendar c = Calendar.getInstance();    
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		String first = format.format(c.getTime());
		return first;
	}
	
 	public static String formatDate(Date date,String format){
 		if(date==null){
 			return "";
 		}
 		SimpleDateFormat df = new SimpleDateFormat(format);
 		return df.format(date);
 	}
 	
 	public static String formatDateToDateTimeStr(Date date){
 		if (date == null)
 			return null;
 		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 		return dateFormat.format(date);
 	}
 	
 	public static String formatDateToDateTime(Date date){
 		if (date == null)
 			return null;
 		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 		return dateFormat.format(date);
 	}
 	
	public static String formatDateTime(Date date){
 		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 		return dateFormat.format(date);
 	}
 	
	public static String formatDateTime(Date date, Integer index){
 		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 		if(index != null){
 			if(index == 1){
 				dateFormat = new SimpleDateFormat("yyyyMMdd");
 			}
 		}
 		return dateFormat.format(date);
 	}
	
 	public static Date strToDate(String date,String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		try {
			return df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
 	
 	public static String formatDateToDateStr(Date date){
 		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 		return dateFormat.format(date);
 	}
 	
 	public static String formatDateToHourStr(Date date){
 		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
 		return dateFormat.format(date);
 	}

 	/**
 	 * 日期转字符串
 	 * @Description 
 	 * @param date
 	 * @param index
 	 * @return 
 	 * @return String  
 	 * @author shidong.zhao
 	 * @date 2016-10-22 下午3:27:20
 	 */
 	public static String dateToString(Date date, int index){
 		SimpleDateFormat simpleDateFormat = DateUtil.getSimpleDateFormatByIndes(index);
		try {
			return simpleDateFormat.format(date);
		} catch (Exception e) {
			return "";
		}
 	}
 	
 	public static SimpleDateFormat getSimpleDateFormatByIndes(Integer index) {
 		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 		if(index == 1){
 			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 		}else if(index == 2){
 			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 		}else if(index == 3){
 			dateFormat = new SimpleDateFormat("yyyyMMdd");
 		}else if(index == 4){
 			dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
 		}else if(index == 5){
 			dateFormat = new SimpleDateFormat("HH:mm");
 		}else if(index == 6){
 			dateFormat = new SimpleDateFormat("MM-dd");
 		}else if(index == 7){
 			dateFormat = new SimpleDateFormat("yyyyMM");
 		}else if(index == 8){
 			dateFormat = new SimpleDateFormat("MM-dd HH:mm");
 		}else if(index == 9){
 			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 		}
 		return dateFormat;
	}
 	
 	/**
 	 * 
 	 * @Description 默认格式为年月日
 	 * @param strDate
 	 * @param index
 	 * @return 
 	 * @return Date  
 	 * @author shidong.zhao
 	 * @date 2016-8-11 上午10:20:18
 	 */
 	public static Date strDateToDate(String strDate, int index){
 		SimpleDateFormat simpleDateFormat = DateUtil.getSimpleDateFormatByIndes(index);
		try {
			return simpleDateFormat.parse(strDate);
		} catch (Exception e) {
			return null;
		}
 	}
	
	/**  
	* 字符串转换成日期  
	* @param strDate  转换对象
	* @param strFormat	需要转换的格式，例：yyyy-MM-dd HH:mm:ss
	* @return date  
	*/  
	public static Date StrToDate(String strDate, String strFormat) {   
	   SimpleDateFormat format = new SimpleDateFormat(strFormat);   
	   try {   
		   return format.parse(strDate);   
	   } catch (ParseException e) {   
		   return null;   
	   }   
	}   

	/**
	 *java中对日期的加减操作
	 *gc.add(1,-1)表示年份减一.
	 *gc.add(2,-1)表示月份减一.
	 *gc.add(3.-1)表示周减一.
	 *gc.add(5,-1)表示天减一.
	 *以此类推应该可以精确的毫秒吧.没有再试.大家可以试试.
	 *GregorianCalendar类的add(int field,int amount)方法表示年月日加减.
	 *field参数表示年,月.日等.
	 *amount参数表示要加减的数量.
	 */
	public static Date computing(Date obj,int field,int amonut){
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(obj);
		gc.add( field,amonut);
		return gc.getTime();
	}
	
	public static String dateConvertStr(Date date) {
		if(date	== null){
			return "";
		}
		String now = formatDate(new Date(), "yyyy-MM-dd HH:mm:ss", null);
		Date today = StrToDate(now + " 00:00:00", "yyyy-MM-dd HH:mm:ss");

		String unnow = formatDate(date, "yyyy-MM-dd HH:mm:ss", null);
		Date untoday = StrToDate(unnow + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		long days = daysBetween(untoday, today);

		if (days == 0) {
			long _day = (long)(new Date().getTime()-date.getTime()) / 1000;
			if (_day < 60) {
				return "1分钟前";
			}
			_day = (long)_day / 60;
			if (_day < 60) {
				return _day + " 分钟前";
			}
			_day = (long)_day / 60;
			if (_day < 24) {
				return _day + " 小时前";
			}
		}
		if (days == 1) {

			return "昨天: " + formatDate(date, "HH:mm", null);
		}
		if(days<31){
			return days + " 天前";
		}
		return formatDate(date, "yyyy-MM-dd HH:mm:ss", null);
	}
	
	/**
	 * 计算2个时间直接的小时差毫秒数
	 * @param startTime  开始时间
	 * @param endTime	  结束时间
	 * @return
	 */
	public static long millisecondsBetween(Date startTime, Date endTime) {
		if ((startTime == null) || (endTime == null)) {
			return 0;
		}
		long ld1 = startTime.getTime();
		long ld2 = endTime.getTime();
		return ld2 - ld1;
	}
	
	/**
	 * 计算2个时间直接的小时数
	 * @param startTime  开始时间
	 * @param endTime	  结束时间
	 * @return
	 */
	public static long hoursBetween(Date startTime, Date endTime) {
		return millisecondsBetween(startTime, endTime)/(1*60*60*1000);
	}
	
	/**
	 * 计算2个时间直接的分钟数
	 * @param startTime  开始时间
	 * @param endTime	  结束时间
	 * @return
	 */
	public static long minutesBetween(Date startTime, Date endTime) {
		return millisecondsBetween(startTime, endTime)/(60*1000);
	}
	
	/**
	 * 计算2个时间直接的秒数
	 * @param startTime  开始时间
	 * @param endTime	  结束时间
	 * @return
	 */
	public static long secondBetween(Date startTime, Date endTime) {
		return millisecondsBetween(startTime, endTime)/(1000);
	}
	
	/**
	 * 计算2个时间相差多少时间，返回类型为：00：03：34
	 * @param startTime  开始时间
	 * @param endTime	  结束时间
	 * @return
	 */
	public static String getSubTime(Date startTime, Date endTime) {
		long subTime = millisecondsBetween(startTime, endTime);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。  
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		return formatter.format(subTime); 
	}
	
	/**
	 * 计算2个时间间隔天数
	 * @param startTime	开始时间
	 * @param endTime	 结束时间
	 * @return
	 */
	public static long daysBetween(Date startTime, Date endTime) {
		long days = millisecondsBetween(startTime, endTime) / (24*60*60*1000);
		return days;
	}
	
	/**
	 * 验证一个时间间隔是否在在2个时间间隔之间
	 * 失效返回true,否则返回false
	 * @param startTime
	 * @param endTime
	 * @param validate	小时（h）
	 * @return
	 */
	public static boolean validateTimeIsEffect(Date startTime, Date endTime, int validate){
		if ((startTime == null) || (endTime == null)) {
			return true;
		}
		long ld1 = startTime.getTime();
		long ld2 = endTime.getTime();
		
		long valiate_interval = ld2 - ld1;
		if(valiate_interval <= validate*60*60*1000){
			return false;
		}else{
			return true;
		}
	}
	
    /***
     * 取days天后的时间
     * @param days
     * @return
     */
    public static Date getAfterAFewDays(Date d, int days){
    	Calendar c = Calendar.getInstance();
    	c.setTime(d);
		c.add(Calendar.DAY_OF_MONTH,days);
		return c.getTime();
    }

    /**
     * 
     * add by cupai 
     * 获取几天前时间 
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) 
    {   
        Calendar now = Calendar.getInstance();   
        now.setTime(d);   
        now.add(Calendar.DAY_OF_MONTH,-day); 
        return now.getTime();   
    } 
    /**
     * add by xupai 
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBeforeMonth(Date d, int month) 
    {   
        Calendar now = Calendar.getInstance();   
        now.setTime(d);   
        now.add(Calendar.MONTH,-month);
        return now.getTime();   
    }  
    
    /***
	 * 前几天或后几天的时间列表
	 * @param sourceDate 参照时间
	 * @param calculate "prev" or "next"
	 * @param days
	 * @return
	 * @throws Exception 
	 */
	public static List<Date> getDateList(Date sourceDate, String calculate,int days) throws Exception{
		List<Date> dateList = new ArrayList<Date>();
		if(StringUtils.equals(calculate, "prev")){
			Calendar cl = Calendar.getInstance();
			cl.setTime(sourceDate);
			cl.set(Calendar.DATE, cl.get(Calendar.DATE) - days);
			return getDateList(cl.getTime(), sourceDate);
		}
		if(StringUtils.equals(calculate, "next")){
			Calendar cl = Calendar.getInstance();
			cl.setTime(sourceDate);
			cl.set(Calendar.DATE, cl.get(Calendar.DATE) + days);
			return getDateList(sourceDate, cl.getTime());
		}
		return dateList;
	}
	
	
	/***
	 * 获取两个时间段内的日期列表
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception 
	 */
	public static List<Date> getDateList(Date startDate, Date endDate) throws Exception{
		List<Date> dateList = new ArrayList<Date>();
		long start = startDate.getTime();
		long end = endDate.getTime();
		if(start > end){
			throw new Exception("开始时间不能大于结束时间！");
		}
		int days = DateUtils.getBetweenDays(startDate, endDate);
		
		dateList.add(startDate);
		for(int i = 1; i <= days; i ++){
			Calendar cl = Calendar.getInstance();
			cl.setTime(startDate);
			cl.set(Calendar.DATE, cl.get(Calendar.DATE) + i);
			dateList.add(cl.getTime());
		}
		return dateList;
	}
    
	public static String formatDateToEnglish(Date date, String format, String nullStr) {
		String dateStr = DateUtil.formatDate(new Date(), format, null);
		dateStr = dateStr.replace("一月", "Jan").replace("二月", "Feb").replace("三月", "Mar").replace("四月", "Apr");
		dateStr = dateStr.replace("五月", "May").replace("六月", "Jun").replace("七月", "Jul").replace("八月", "Aug");
		dateStr = dateStr.replace("九月", "Sep").replace("十月", "Oct").replace("十一月", "Nov").replace("十二月", "Dec");
		dateStr = dateStr.replace("星期一", "Mon").replace("星期二", "Tue").replace("星期三", "Wed").replace("星期四", "Thu");
		dateStr = dateStr.replace("星期五", "Fri").replace("星期六", "Sat").replace("星期日", "Sun");
		dateStr = dateStr.replace("上午", "AM").replace("下午", "PM");
		return dateStr;
	}
	
 	public static final int ADDYEAR = 1;
 	public static final int ADDMONTH = 2;
 	public static final int ADDWEEK = 3;
 	public static final int ADDDAY = 5;
 	public static final int ADDHOUR = 10;
 	public static final int ADDMINUTE = 12;
 	public static final int ADDSECOND = 13;
 	
 	/**
 	 * 增加或减少时间，返回新的date对象
 	 * @param date
 	 * @param addType
 	 * @param val
 	 * @return
 	 */
 	public static Date add(Date date,int addType,int val){
 		Calendar calendar=Calendar.getInstance();   
 		calendar.setTime(date); 
 		calendar.add(addType, val);
 		return calendar.getTime();
 	}
 	
	
 	
 	/**
 	 * 返回 输入时间的 某个整点时间
 	 * @Description 
 	 * @param date 
 	 * @param hour 整点
 	 * @return 
 	 * @return Date  
 	 * @author peng.wang
 	 * @date 2015-6-19 下午1:31:52
 	 */
	public static Date getHourOfDay(Date date,int hour){
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.set(Calendar.DATE, cl.get(Calendar.DATE));
		cl.set(Calendar.HOUR_OF_DAY, hour);
		cl.set(Calendar.MINUTE, 0);
		cl.set(Calendar.SECOND, 0);
		cl.set(Calendar.MILLISECOND, 0);
		Date rsDate = cl.getTime();
		return rsDate;
	}
	
	/**
	 * 返回 输入时间的 某时时间
	 * @Description 
	 * @param date 当前时间
	 * @param hour 小时
	 * @param minute 分钟
	 * @param second 秒
	 * @return 
	 * @return Date  
	 * @author peng.wang
	 * @date 2015-6-19 下午1:33:47
	 */
	public static Date getTimeOfDay(Date date,int hour,int minute,int second){
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.set(Calendar.DATE, cl.get(Calendar.DATE));
		cl.set(Calendar.HOUR_OF_DAY, hour);
		cl.set(Calendar.MINUTE, minute);
		cl.set(Calendar.SECOND, second);
		cl.set(Calendar.MILLISECOND, 0);
		Date rsDate = cl.getTime();
		return rsDate;
	}
 	
    /**
     * @Test
     * @param args
     */
    public static void main(String[] args)
	{
    	
    	
    	//System.out.println(prettySeconds(280213));
//    	System.out.println(formatDate(getDateBeforeMonth(new Date(),3) , "yyyy-MM-dd HH:mm:ss", null));
//		System.out.println(formatDate(getDateBefore(new Date(), 121) , "yyyy-MM-dd HH:mm:ss", null));
    	/*Date now = new Date();
    	Calendar ddd = Calendar.getInstance();
		ddd.setTime(now);
		ddd.set(Calendar.MONTH, 5);
		ddd.set(Calendar.DAY_OF_MONTH, 27);
		ddd.set(Calendar.HOUR_OF_DAY, 0);
		ddd.set(Calendar.MINUTE, 0);
		System.out.println(ddd.getTime());*/
		/*long reservedTime = ddd.getTimeInMillis();
    	try {
			int flag = 0;
			Calendar cl = Calendar.getInstance();
			cl.setTime(now);
			for(int i =0;i<3;i++){
				if(i!=0){
					cl.set(Calendar.DATE, cl.get(Calendar.DATE)+1);
				}
				//获取当天10点时间
				Date date10 = DateUtil.getHourOfDay(cl.getTime(),10);
				System.out.println(date10);
				//获取当天18：15时间,如果是立即下单 下单时间变成当前时间+15分钟，所以取18:15分的时间
				Date date18 = DateUtil.getTimeOfDay(cl.getTime(),18,15,0);
				System.out.println(date18);
				if(date10.getTime()<=reservedTime&&reservedTime<=date18.getTime()){
					flag = 1;break;
				}
			}
			if(flag ==0){
				System.out.println(0);
			}else {
				System.out.println(1);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	
    	
		/* System.out.println(formatDateToDateTime(getHourOfDay(new Date(), 9))); */
	}
    
    /**
	  * 根据日期字符串判断当月第几周
	  * @param str
	  * @return
	  * @throws Exception
	  */
	 public static String getWeek(String str) throws Exception{
		 SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		 Date date =sdf.parse(str);
		return getWeek(date);
	 }
	 
	 public static String getWeek(Date date) throws Exception{
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTime(date);
		 //第几周
	     int week = calendar.get(Calendar.WEEK_OF_YEAR);
	     int year=calendar.get(Calendar.YEAR);
	     String ret=year+"";
	     if(week<10){
	    	 ret+="0"+week;
	     }else{
	    	 ret+=week;
	     }
	     //第几天，从周日开始
	     return ret;
	 }
    
	/**
	 * 显示秒值为**年**月**天 **时**分**秒  如1年2个月3天 10小时
	 * @param totalSeconds
	 * @return
	 */
    public static String prettySeconds(int totalSeconds) {
        StringBuilder s = new StringBuilder();
        int second = totalSeconds % 60;
        if (totalSeconds > 0 && second > 0) {
            s.append("秒");
            s.append(StringUtils.reverse(String.valueOf(second)));
        }

        totalSeconds = totalSeconds / 60;
        int minute = totalSeconds % 60;
        if (totalSeconds > 0 && minute > 0) {
            s.append("分");
            s.append(StringUtils.reverse(String.valueOf(minute)));
        }

        totalSeconds = totalSeconds / 60;
        int hour = totalSeconds % 24;
        if (totalSeconds > 0 && hour > 0) {
            s.append(StringUtils.reverse("小时"));
            s.append(StringUtils.reverse(String.valueOf(hour)));
        }

        totalSeconds = totalSeconds / 24;
        int day = totalSeconds % 31;
        if (totalSeconds > 0 && day > 0) {
            s.append("天");
            s.append(StringUtils.reverse(String.valueOf(day)));
        }

        totalSeconds = totalSeconds / 31;
        int month = totalSeconds % 12;
        if (totalSeconds > 0 && month > 0) {
            s.append("月");
            s.append(StringUtils.reverse(String.valueOf(month)));
        }

        totalSeconds = totalSeconds / 12;
        int year = totalSeconds;
        if (totalSeconds > 0 && year > 0) {
            s.append("年");
            s.append(StringUtils.reverse(String.valueOf(year)));
        }
        return s.reverse().toString();
    }
    
    /**
     * 根据日期获得星期 
     * @Description 
     * @param date
     * @return 
     * @return String  
     * @author shidong.zhao
     * @date 2016-12-29 下午1:55:54
     */
    public static String getWeekOfDate(Date date) { 
	  String[] weekDaysName = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" }; 
	  Calendar calendar = Calendar.getInstance(); 
	  calendar.setTime(date); 
	  int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; 
	  return weekDaysName[intWeek]; 
	} 
    
   /**
    * @auther luoquan
    * @date 2017-7-14 上午9:20:21
    * @Description 获取当前的开始时间
    * @return
    */
    public static Date getDayBegin() {
      Calendar cal = new GregorianCalendar();
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      return cal.getTime();
    }
      /**
       * @auther luoquan
       * @date 2017-7-14 上午9:20:38
       * @Description 获取当天的结束时间
       * @return
       */
      public static Date getDayEnd() {
          Calendar cal = new GregorianCalendar();
          cal.set(Calendar.HOUR_OF_DAY, 23);
          cal.set(Calendar.MINUTE, 59);
          cal.set(Calendar.SECOND, 59);
          return cal.getTime();
      }
    
    /**
     * @auther luoquan
     * @date 2017-7-14 上午9:20:45
     * @Description 获取本周的开始时间
     * @return
     */
    public static Date getBeginDayOfWeek() {
         Date date = new Date();
         Calendar cal = Calendar.getInstance();
         cal.setTime(date);
         int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
         if (dayofweek == 1) {
             dayofweek += 7;
         }
         cal.add(Calendar.DATE, 2 - dayofweek);
         return getDayStartTime(cal.getTime());
    }
    
    /**
     * @auther luoquan
     * @date 2017-7-14 上午9:20:59
     * @Description 获取本周的开始时间
     * @return
     */
     public static Date getEndDayOfWeek(){
    	 Calendar cal = Calendar.getInstance();
         cal.setTime(getBeginDayOfWeek());  
         cal.add(Calendar.DAY_OF_WEEK, 6); 
         Date weekEndSta = cal.getTime();
         return getDayEndTime(weekEndSta);
	 }
    
     /**
      * @auther luoquan
      * @date 2017-7-14 上午9:21:04
      * @Description 获取本月的开始时间
      * @return
      */
	  public static Date getBeginDayOfMonth() {
         Calendar calendar = Calendar.getInstance();
         calendar.set(getNowYear(), getNowMonth() - 1, 1);
         return getDayStartTime(calendar.getTime());
	   }
	/**
	 * @auther luoquan
	 * @date 2017-7-14 上午9:21:16
	 * @Description 获取本月的结束时间
	 * @return
	 */
   public static Date getEndDayOfMonth() {
      Calendar calendar = Calendar.getInstance();
      calendar.set(getNowYear(), getNowMonth() - 1, 1);
      int day = calendar.getActualMaximum(5);
      calendar.set(getNowYear(), getNowMonth() - 1, day);
      return getDayEndTime(calendar.getTime());
   }
       
   /**
    * @auther luoquan
    * @date 2017-7-14 上午9:23:55
    * @Description 获取某个日期的开始时间
    * @param d
    * @return
    */
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
    /**
     * @auther luoquan
     * @date 2017-7-14 上午9:23:59
     * @Description 获取某个日期的结束时间
     * @param d
     * @return
     */
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    /**
     * 
     * @auther luoquan
     * @date 2017-7-14 上午9:25:36
     * @Description 获取今年是哪一年
     * @return
     */
      public static Integer getNowYear() {
         Date date = new Date();
         GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
         gc.setTime(date);
         return Integer.valueOf(gc.get(1));
     }
      /**
       * @auther luoquan
       * @date 2017-7-14 上午9:25:45
       * @Description 获取本月是哪一月
       * @return
       */
      public static int getNowMonth() {
         Date date = new Date();
         GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
         gc.setTime(date);
         return gc.get(2) + 1;
      }
      
      public static Date getBeforeByHourTime(int ihour){ 
    	     String returnstr = ""; 
    	     Calendar calendar = Calendar.getInstance(); 
    	     calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - ihour); 
    	     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	     returnstr = df.format(calendar.getTime());
    	     Date date=strToDate(returnstr, "yyyy-MM-dd HH:mm:ss");
	     	 return date;
    	     
    	  } 
      /**
       * 判断时间是不是今天
       * @param date
       * @return    是返回true，不是返回false
       */
      public static boolean isNow(Date date) {
          //当前时间
          Date now = new Date();
          SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
          //获取今天的日期
          String nowDay = sf.format(now);
           
           
          //对比的时间
          String day = sf.format(date);
           
          return day.equals(nowDay);
           
           
           
      }
    	 
}
