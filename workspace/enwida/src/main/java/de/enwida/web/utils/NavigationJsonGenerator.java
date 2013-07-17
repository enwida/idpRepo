package de.enwida.web.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.web.dao.implementation.NavigationDaoImpl;
import de.enwida.web.model.ChartNavigationData;

public class NavigationJsonGenerator {

    public static void main(String[] args) {
        // Manually construct the needed beans
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/resources/messages/messages");
        
        final NavigationDaoImpl navigationDao = new NavigationDaoImpl();
        navigationDao.setMessageSource(messageSource);

        final ChartNavigationData navigationData = navigationDao.getDefaultNavigation(0, Locale.ENGLISH);
        navigationData.setIsDateScale(true);
        navigationData.getAspects().add(Aspect.CR_DEGREE_OF_ACTIVATION);

        navigationData.getTimeRanges().put("Day", "Day");
        navigationData.getTimeRanges().put("Week", "Week");
        navigationData.getTimeRanges().put("Month", "Month");
        navigationData.getTimeRanges().put("Year", "Year");

	    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    try {
            navigationData.setDefaults(new NavigationDefaults(99, DataResolution.MONTHLY, 211, new CalendarRange(dateFormat.parse("2010-11-01"), dateFormat.parse("2010-12-01"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
	    
        try {
            final ObjectMapper om = new ObjectMapperFactory(dateFormat).create();
            om.enable(SerializationFeature.INDENT_OUTPUT);
            System.out.println(om.writeValueAsString(navigationData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
