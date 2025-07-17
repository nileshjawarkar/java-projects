package com.nnj.learn.javaee8.control;

import java.io.InputStream;
import java.util.Properties;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

@ApplicationScoped
public class AppConfigurationExposer {
	
	private Properties configProperties = null;
	
	@PostConstruct
	public void init() {
		try( InputStream ios = AppConfigurationExposer.class.getResourceAsStream("/application.properties") ) {
			configProperties = new Properties();
			configProperties.load(ios);
		} catch (final Exception e) {
		}
	}
	
	@Produces
	@AppConfig("unused")
	public String exposeConfig(final InjectionPoint injectionPoint) {
		final String key = injectionPoint.getAnnotated().getAnnotation(AppConfig.class).value(); 
		String value = "";
		if(key != null && !"".equals(key)) {
			value = configProperties.getProperty(key);
		}
		return value;
	}

}
