package de.enwida.web.application;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationContextInitializer implements
		ApplicationListener<ContextRefreshedEvent>, ServletContextListener {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		ApplicationContext applicationContext = arg0.getApplicationContext();

		// Do what needs to be done with application context
		logger.info("Enwida context loaded for : "
				+ applicationContext.getDisplayName());
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("Enwida web application context destroyed : "
				+ arg0.getServletContext().getServletContextName());

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("Enwida web application context initialised : "
				+ arg0.getServletContext().getServletContextName());

	}
}
