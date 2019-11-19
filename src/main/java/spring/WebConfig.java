/**
 * Project:police
 * File:WebConfig.java 
 * Copyright 2004-2012 Homolo Co., Ltd. All rights reserved.
 */
package spring;

import com.homolo.framework.bean.DynamicBeanFactory;
import com.homolo.framework.cache.CacheFactory;
import com.homolo.framework.cache.CacheFactoryFactory;
import com.homolo.framework.cache.MapCacheFactoryImpl;
import com.homolo.framework.cluster.Global;
import com.homolo.framework.cluster.Node;
import com.homolo.framework.cluster.Standalone;
import com.homolo.framework.dao.mongo.GlobalSynchronizer;
import com.homolo.framework.manager.BeanFieldWirer;
import com.homolo.framework.manager.DomainEngineFactory;
import com.homolo.framework.module.ModuleConfig;
import com.homolo.framework.module.ModuleRegistry;
import com.homolo.framework.util.BeanUtils;
import com.homolo.framework.util.EnvironmentHelper;
import com.homolo.framework.util.MessageUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ClassUtils;

import java.util.*;

@Configuration
@PropertySource(name = "web.env", value = "classpath:/env.properties")
@Import({
    /*** import framework configs ***/
    com.homolo.framework.protocol.BeanConfig.class,
    com.homolo.framework.rest.BeanConfig.class, 
    /*** import project configs ***/
    com.homolo.usersystem.HibernateProjectConfig.class,
    com.homolo.toolkit.HibernateProjectConfig.class,
    com.jiang.medical.ProjectConfig.class
})
public class WebConfig {
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	Environment environment;

	@Bean
    WebApplication webApplication() {
		return new WebApplication();
	}

	@Bean(name = "global.cacheFactory")
	CacheFactory globalCacheFactory() {
		return new MapCacheFactoryImpl();
	}

	@Bean(name = "local.cacheFactory")
	CacheFactory localCacheFactory() {
		return new MapCacheFactoryImpl();
	}
	
	@Bean
	CacheFactoryFactory cacheFactoryFactory() {
		return new CacheFactoryFactory();
	}
	
	@Bean(name = "us.passwordEncoder")
	public PasswordEncoder generatePasswordEncorder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	ModuleRegistry moduleRegistry() {
		return new ModuleRegistry();
	}
	
	@Bean
	DomainEngineFactory domainEngineFactory() {
		return new DomainEngineFactory();
	}
	
	@Bean
	BeanFieldWirer beanFieldWirer() {
		return new BeanFieldWirer();
	}
	
	@Bean
	GlobalSynchronizer globalSynchronizer() {
		return new GlobalSynchronizer();
	}

	@Bean
	DynamicBeanFactory dynamicBeanFactory() {
		return new DynamicBeanFactory();
	}

	@Bean
	Global global() {
		return new Standalone();
	}

	@Bean
	Node node() {
		return new Node();
	}

	
	@Bean(name = "hibernate.packagesToScan")
	List<String> hibernateScanPackages() {
		List<String> packageNames = new ArrayList<String>();
		Map<String, Object> moduleConfigBeans = applicationContext.getBeansWithAnnotation(ModuleConfig.class);
		for (Map.Entry<String, Object> entry : moduleConfigBeans.entrySet()) {
			Class<? extends Object> clz = AopUtils.getTargetClass(entry.getValue());
			if (ClassUtils.isCglibProxy(entry.getValue())) {
				clz = entry.getValue().getClass().getSuperclass();
			}
			ModuleConfig mc = clz.getAnnotation(ModuleConfig.class);
			if (mc!=null && mc.domainPackages() != null && mc.domainPackages().length > 0) {
				packageNames.addAll(Arrays.asList(mc.domainPackages()));
			}
		}
		packageNames.add("com.homolo.framework.hibernate");
		return packageNames;
	}
	
	@Bean
	MessageSource messageSource() {
		EnvironmentHelper helper = new EnvironmentHelper(environment);
		Map<String, Object> map = applicationContext.getBeansWithAnnotation(ModuleConfig.class);
		List<String> messages = new LinkedList<String>();
		for (Object obj : map.values()) {
			ModuleConfig mc = BeanUtils.getClass(obj).getAnnotation(ModuleConfig.class);
			if (mc == null) {
				continue;
			}
			String projName = BeanUtils.getClass(obj).getAnnotation(ModuleConfig.class).name();
			String messagePath = helper.getProperty("message.i18n." + projName);
			if (messagePath != null) {
				messages.add(messagePath);
			}
		}
		String messagePath = helper.getProperty("message.i18n.global");
		if (messagePath != null) {
			messages.add(messagePath);
		}
		String[] messageArray = messages.toArray(new String[messages.size()]);
		CollectionUtils.reverseArray(messageArray);
		MessageUtils.initDefaultMessageResources(messageArray);
		return MessageUtils.getDefaultMessageSource();
	}

	@Bean(name = "taskExecutor")
	TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
		te.setCorePoolSize(10);
		te.setMaxPoolSize(100);
		return te;
	}
	
}