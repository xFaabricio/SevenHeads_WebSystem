package org.primefaces.paradise.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

	private static ApplicationContext ctx = null;

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}
	
	public static <T> T getBean(Class<T> clazz) {
		return ctx.getBean(clazz);
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name, Object... args) {
		return (T) ctx.getBean(name, args);
	}
	
	@Override
	@SuppressWarnings("all")
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextProvider.ctx = ctx;		
	}
	
}
