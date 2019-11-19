/**
 * Project:police
 * File:WebApplication.java 
 * Copyright 2004-2012 Homolo Co., Ltd. All rights reserved.
 */
package spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * @author Homolo Generator
 * @since Mar 14, 2011 10:41:16 AM
 * @version $Id: WebApplication.java 21216 2012-08-19 15:34:19Z rory $
 */
public class WebApplication {

	private final Logger logger = LoggerFactory.getLogger(WebApplication.class);
	
	@PostConstruct
	public void init() {
		logger.info("Set web application default locale: zh_CN.");
		Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
	}
	
	
}
