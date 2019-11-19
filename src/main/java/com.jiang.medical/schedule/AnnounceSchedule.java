package com.jiang.medical.schedule;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/** 
* @ClassName: AnnounceSchedule 
* @Description: 定时发送任务
* @author Tata 
* @date 2019年5月3日 下午4:01:29 
*  
*/ 
@Component
public class AnnounceSchedule {

	private Logger logger = LoggerFactory.getLogger(getClass());

	//@Scheduled(cron = "0 0 5 * * *") // 秒、分、时、日、月、年
	public void createAttendance() {
		try {
			logger.info("--------- 开始 ---------");
			
			//TODO
			
			logger.info("--------- 结束 ---------");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("失败:" + e.getMessage());
		}
	}
	
}
