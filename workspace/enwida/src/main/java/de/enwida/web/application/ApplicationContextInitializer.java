package de.enwida.web.application;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationContextInitializer implements
		ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		ApplicationContext applicationContext = arg0.getApplicationContext();

		// Do what needs to be done with application context
	}

}
