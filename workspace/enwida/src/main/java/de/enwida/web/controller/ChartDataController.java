package de.enwida.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

import de.enwida.chart.LineManager;
import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.transport.IDataLine;
import de.enwida.transport.LineRequest;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.User;
import de.enwida.web.service.implementation.CookieSecurityService;
import de.enwida.web.service.implementation.LineService;
import de.enwida.web.service.implementation.NavigationService;
import de.enwida.web.service.interfaces.INavigationService;
import de.enwida.web.service.interfaces.UserService;
import de.enwida.web.utils.CalendarRange;
import de.enwida.web.utils.ChartDefaults;
import de.enwida.web.utils.Constants;
import de.enwida.web.utils.NavigationDefaults;

/**
 * Handles chart data requests
 */
@Controller
@RequestMapping("/data")
public class ChartDataController {

    @Autowired
    private LineService lineService;

    @Autowired
    private INavigationService navigationService;

    @Autowired
    private CookieSecurityService cookieSecurityService;
    
    @Autowired
    private UserService userService;


    @RequestMapping(value = "/chart", method = RequestMethod.GET)
    public String exampleChart(Principal principal) {
    	return "charts/index";
    }

    @RequestMapping(value = "/navigation", method = RequestMethod.GET)
    @ResponseBody
    public ChartNavigationData getNavigationData(@RequestParam int chartId,
	    HttpServletRequest request, Principal principal, Locale locale) {

    	final ChartNavigationData chartNavigationData = navigationService.getNavigationData(chartId, getUser(principal), locale);

    	// Try to set the defaults from the cookie
    	try {
        	final NavigationDefaults defaults = getNavigationDefaultsFromCookie(chartId, request, principal);
        	if (defaults != null) {
            	chartNavigationData.setDefaults(defaults);
        	}
    	} catch (Exception ignored) { }

    	return chartNavigationData;
    }

    @RequestMapping(value = "/lines", method = RequestMethod.GET)
    @ResponseBody
    public List<IDataLine> getLines(
	    @RequestParam int chartId,
	    @RequestParam int product,
	    @RequestParam int tso,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar startTime,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar endTime,
	    @RequestParam DataResolution resolution,
	    Locale locale,
	    Principal principal) {
        
        final List<IDataLine> result = new ArrayList<>();
        final List<Aspect> aspects = navigationService.getDefaultNavigationData(chartId).getAspects();
        
        for (final Aspect aspect : aspects) {
        	final LineRequest request = new LineRequest(aspect, product, tso, startTime, endTime, resolution, locale);

        	try {
                final IDataLine line = lineService.getLine(request, getUser(principal));
                result.add(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @RequestMapping(value = "/line", method = RequestMethod.GET)
    @ResponseBody
    public IDataLine getLine(
	    @RequestParam String strAspect,
	    @RequestParam int product,
	    @RequestParam int tso,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar startTime,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar endTime,
	    @RequestParam DataResolution resolution,
	    Locale locale,
	    Principal principal) throws Exception {
        
        final Aspect aspect = Aspect.valueOf(strAspect);
    	final LineRequest request = new LineRequest(aspect, product, tso, startTime, endTime, resolution, locale);
        return lineService.getLine(request, getUser(principal));
    }


    @RequestMapping(value = "/disabledLines", method = RequestMethod.POST)
    @ResponseBody
    public void setDisabledLines(
        @RequestParam int chartId,
        @RequestParam String lines,
        HttpServletRequest request,
        HttpServletResponse response,
        Principal principal) {
        
        // Try to update the cookie data
        try {
            final NavigationDefaults defaults = getNavigationDefaultsFromCookie(chartId, request, principal);
            if (lines.isEmpty()) {
                defaults.setDisabledLines(new ArrayList<Integer>());
            } else {
                final String[] lineIds = lines.split(",");
                final List<Integer> disabledLines = new ArrayList<>();
    
                for (final String lineId : lineIds) {
                    disabledLines.add(Integer.parseInt(lineId));
                }
                defaults.setDisabledLines(disabledLines);
            }
            updateChartDefaultsCookie(chartId, defaults, request, response, principal);
        } catch (Exception ignored) { }
    }
    
    private User getUser(Principal principal) {
        try {
            return userService.getUser(principal.getName());
        } catch (Exception e) {
            return null;
        }
    }
    

    /*
     * ==========================================================================
     * ============= CAUTION: ! Never ever leave the methods below in production
     * code !
     * 
     * These get navigation data and lines bypassing the security layer in order
     * to test frontend JavaScript code
     * ==========================================
     * =============================================
     */

    @Autowired
    private LineManager lineManager;

    @RequestMapping(value = "/navigation.test", method = RequestMethod.GET)
    @ResponseBody
    public ChartNavigationData getNavigationDataTest(@RequestParam int chartId,
	    Principal principal, Locale locale, HttpServletRequest request,
	    HttpServletResponse response) throws ParseException {

        final ChartNavigationData result = ((NavigationService) navigationService).getNavigationDataUNSECURE(chartId, getUser(principal), locale);

    	// Try to get the navigation defaults from the cookie
    	try {
    	    final String cookieValue = getChartDefaultsCookie(request, principal);
    	    final JsonReader jsonReader = new JsonReader(new ByteArrayInputStream(cookieValue.getBytes()));
    	    final ChartDefaults chartDefaults = (ChartDefaults) jsonReader.readObject();
    	    jsonReader.close();

    	    final NavigationDefaults defaults = chartDefaults.get(chartId);
    	    if (defaults != null) {
    	        result.setDefaults(defaults);
    	    }
    	} catch (Exception ignored) { }
    	
    	return result;
    }

    @RequestMapping(value = "/lines.test", method = RequestMethod.GET)
    @ResponseBody
    public List<IDataLine> getLinesTest(
	    @RequestParam int chartId,
	    @RequestParam int product,
	    @RequestParam int tso,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar startTime,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar endTime,
	    @RequestParam DataResolution resolution,
	    Locale locale,
	    HttpServletRequest request,
	    HttpServletResponse response,
	    Principal principal) {

    	final List<IDataLine> result = new ArrayList<IDataLine>();
    	final ChartNavigationData navigationData = navigationService.getDefaultNavigationData(chartId);

    	for (final Aspect aspect : navigationData.getAspects()) {

    	    final LineRequest req = new LineRequest(aspect, product, tso,
    		    startTime, endTime, resolution, locale);
    	    try {
        		result.add(lineManager.getLine(req));
    	    } catch (Exception e) {
        		e.printStackTrace();
    	    }
    	}
    	
    	// Update cookie data
    	final NavigationDefaults defaults = new NavigationDefaults(
    	        tso,
    	        resolution,
    	        product,
    	        new CalendarRange(startTime, endTime));

    	try {
            updateChartDefaultsCookie(chartId, defaults, request, response, principal);
        } catch (Exception e) {
            // Don't reply with an error if saving the defaults failed
            e.printStackTrace();
        }
    
    	return result;
    }
    
    private NavigationDefaults getNavigationDefaultsFromCookie(int chartId, HttpServletRequest request, Principal principal) throws IOException {
	    final String cookieValue = getChartDefaultsCookie(request, principal);
	    final JsonReader jsonReader = new JsonReader(new ByteArrayInputStream(cookieValue.getBytes()));
	    final ChartDefaults chartDefaults = (ChartDefaults) jsonReader.readObject();
	    jsonReader.close();
	    return chartDefaults.get(chartId);
    }

    private void updateChartDefaultsCookie(int chartId, NavigationDefaults defaults, HttpServletRequest request,
	    HttpServletResponse response, Principal principal) throws IOException {

        // Get the current chart defaults
        final String cookieValue = getChartDefaultsCookie(request, principal);
        ChartDefaults chartDefaults;
        try {
            final JsonReader jsonReader = new JsonReader(new ByteArrayInputStream(cookieValue.getBytes()));
            chartDefaults = (ChartDefaults) jsonReader.readObject();
            jsonReader.close();
        } catch (Exception e) {
            chartDefaults = new ChartDefaults();
        }
        
        // Set the defaults for our chart ID
        chartDefaults.set(chartId, defaults);
        final String defaultsJson = JsonWriter.objectToJson(chartDefaults);
        
    	// Create/update cookie
    	if (principal != null) {
    	    setUserSettingsInCookie(defaultsJson, response,
    		    Constants.ENWIDA_CHART_COOKIE_USER);
    	} else {
    	    setUserSettingsInCookie(defaultsJson, response,
    		    Constants.ENWIDA_CHART_COOKIE_ANONYMOUS);
    	}
    }
    
    private String getChartDefaultsCookie(HttpServletRequest request, Principal principal) {
        if (principal == null) {
            return getUserSettingsFromCookie(request, Constants.ENWIDA_CHART_COOKIE_ANONYMOUS);
        }
        return getUserSettingsFromCookie(request, Constants.ENWIDA_CHART_COOKIE_USER);
    }

    /**
     * This method is used to set the chart settings of user in a {@link Cookie}
     * 
     * @param user
     *            User which is used for setting user name in cookie data
     * @param response
     *            {@link HttpServletResponse}
     */
    public void setUserSettingsInCookie(final String userName,
	    final HttpServletResponse response, String cookieName) {

    	final String usersettingsjson = userName;
    	final String encryptedData = cookieSecurityService
    		.encryptJsonString(usersettingsjson);
    	final Cookie chartcookie = new Cookie(cookieName, encryptedData);
    	// works only for SSL connection
    	// chartcookie.setSecure(true);
    	chartcookie.setMaxAge(Constants.ENWIDA_CHART_COOKIE_EXPIRY_TIME);
    	response.addCookie(chartcookie);
    }

    /**
     * This method is used to get the chart settings of user in a {@link Cookie}
     * 
     * @param user
     *            User which is used for setting user name in cookie data
     * @param response
     *            {@link HttpServletResponse}
     */
    public String getUserSettingsFromCookie(HttpServletRequest request,
	    String cookieName) {

    	String decryptString = null;
    	final Cookie[] cookies = request.getCookies();
    	for (final Cookie cookie : cookies) {
    	    // System.out.println(cookie.getValue());
    	    if (((cookie.getName() != null) && cookie.getName().equals(
    		    cookieName))
    		    && (cookie.getValue() != null)) {
    		decryptString = cookieSecurityService.dycryptJsonString(cookie
    			.getValue());
    	    }
    
    	}
    	return decryptString;
    }

}
