package de.enwida.web.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

import de.enwida.chart.LineManager;
import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.transport.IDataLine;
import de.enwida.transport.LineRequest;
import de.enwida.web.dao.interfaces.INavigationDao;
import de.enwida.web.model.ChartLinesRequest;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.ProductTree;
import de.enwida.web.model.User;
import de.enwida.web.service.implementation.CookieSecurityService;
import de.enwida.web.service.implementation.LineService;
import de.enwida.web.service.interfaces.INavigationService;
import de.enwida.web.utils.CalendarRange;
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

	@RequestMapping(value="/lines", method = RequestMethod.GET)
	@ResponseBody
	public List<IDataLine> getLines (
								@RequestParam int chartId,
								@RequestParam int product,
								@RequestParam int tso,
								@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Calendar startTime,
								@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Calendar endTime,
								@RequestParam DataResolution resolution,
								Locale locale
							   )
	{
	    final ChartLinesRequest request = new ChartLinesRequest(
	            chartId,
	            product,
	            tso,
	            new CalendarRange(startTime, startTime),
	            resolution,
	            locale
	            );
	    
	    return lineService.getLines(request);
	}
	
	@RequestMapping(value="/chart", method=RequestMethod.GET)
	public String exampleChart(Principal principal) {
		if(principal!=null){
			System.out.println(principal.getName());
		}
		return "charts/index";
	}
	
	@RequestMapping(value = "/navigation", method = RequestMethod.GET)
	@ResponseBody
	public ChartNavigationData getNavigationData(@RequestParam int chartId, Principal principal, Locale locale) {
	    // FIXME: get user / submit correct role
	    return navigationService.getNavigationData(chartId, 0, locale);
	}
	
	/*
	 * =======================================================================================
	 * CAUTION:
	 * ! Never ever leave the methods below in production code !
	 * 
	 * These get navigation data and lines bypassing the security layer in order to test frontend
	 * JavaScript code
	 * =======================================================================================
	 */
	
	@Autowired
	private LineManager lineManager;
	
	@Autowired
	private INavigationDao navigationDao;
	
	@RequestMapping(value = "/navigation.test", method = RequestMethod.GET)
	@ResponseBody
	public ChartNavigationData getNavigationDataTest(@RequestParam int chartId, Principal principal, Locale locale) throws ParseException {
	    final ChartNavigationData result = navigationDao.getDefaultNavigation(chartId, locale);
	    result.addProductTree(new ProductTree(1));
	    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    final Date from = dateFormat.parse("2010-07-01");
	    final Date to = dateFormat.parse("2010-12-31");
	    final Calendar cFrom = Calendar.getInstance();
	    final Calendar cTo = Calendar.getInstance();
	    cFrom.setTime(from);
	    cTo.setTime(to);
	    result.setDefaults(new NavigationDefaults(99, DataResolution.MONTHLY, 211, new CalendarRange(cFrom, cTo)));
	    result.setIsDateScale(true);
	    result.addTso(99, "Standard");
//	    result.addTso(1, "Test");
	    return result;
	}
	
	@RequestMapping(value="/lines.test", method = RequestMethod.GET)
	@ResponseBody
	public List<IDataLine> getLinesTest (
								@RequestParam int chartId,
								@RequestParam int product,
								@RequestParam int tso,
								@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Calendar startTime,
								@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Calendar endTime,
								@RequestParam DataResolution resolution,
								Locale locale
							   )
	{
	    final List<IDataLine> result = new ArrayList<IDataLine>();

	    for (final Aspect aspect : Arrays.asList(new Aspect[] { Aspect.CR_POWERPRICE_MIN, Aspect.CR_POWERPRICE_MID, Aspect.CR_POWERPRICE_MAX })) {
	        final LineRequest req = new LineRequest(aspect, product, tso, startTime, endTime, resolution, locale);
	        try {
                result.add(lineManager.getLine(req));
            } catch (Exception e) {
                e.printStackTrace();
            }
	    }
	    return result;
	}
	
	/**
	 * This method is used to set the chart settings of user in a {@link Cookie}
	 * 
	 * @param user
	 *            User which is used for setting user name in cookie data
	 * @param response
	 *            {@link HttpServletResponse}
	 */
	public void setUserSettingsInCookie(final User user,
			final HttpServletResponse response) {

		final String usersettingsjson = "";
		final String encryptedData = cookieSecurityService
				.encryptJsonString(usersettingsjson);
		final Cookie chartcookie = new Cookie(Constants.ENWIDA_CHART_COOKIE,
				encryptedData);
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
	public String getUserSettingsFromCookie(HttpServletRequest request) {
		String decryptString = null;
		final Cookie[] cookies = request.getCookies();
		for (final Cookie cookie : cookies) {
			// System.out.println(cookie.getValue());
			if (((cookie.getName() != null) && cookie.getName().equals(
					Constants.ENWIDA_CHART_COOKIE))
					&& (cookie.getValue() != null)) {
				decryptString = cookieSecurityService.dycryptJsonString(cookie
						.getValue());
			}

		}
		return decryptString;

	}

}
