package com.quickplay.restbot;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import com.quickplay.restbot.annotations.Config;

public class AppContext {
	static final Logger logger = LoggerFactory.getLogger(AppContext.class);
	
	private ClassPathXmlApplicationContext applicationContext;
	private static AppContext instance;
	private static Set<String> configSet = new HashSet<String>();
	
	public static synchronized AppContext instance() {
		if (instance == null) {
			instance = new AppContext();
		}
		
		return instance;
	}

	public static void addConfig(Config config) {
		if (config != null) {
			for(String c : config.value()) {
				addConfig(c);
			}
		}
	}
	
	public static void addConfig(String configLocation) {
		configSet.add(configLocation);
	}
	
	private AppContext() {
			logger.debug("Initializing Spring context ...");
			configSet.add("/restbot-app-context.xml");
			String[] configs = new String[configSet.size()];
			configSet.toArray(configs);
			this.applicationContext = new ClassPathXmlApplicationContext(configs);
			logger.debug("Spring context initialized.");
	}
	

	
	public ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}
	

	public static void autowireBean(Object existingBean) {
		instance().getApplicationContext().getAutowireCapableBeanFactory().autowireBean(existingBean);
	}
	
	public static Object getBean(String beanName) {
		return instance().getApplicationContext().getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> requiredType) {
		return instance().getApplicationContext().getBean(requiredType);
	}
	
	public static Resource getResource(String path) {
		return instance().getApplicationContext().getResource(path);
	}
}
