package de.enwida.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.transport.IDataLine;
import de.enwida.transport.LineRequest;
import de.enwida.web.db.model.CalendarRange;
import de.enwida.web.db.model.NavigationDefaults;
import de.enwida.web.db.model.NavigationSettings;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.ILineService;
import de.enwida.web.service.interfaces.INavigationService;
import de.enwida.web.service.interfaces.ISVGService;
import de.enwida.web.service.interfaces.IUserService;

/**
 * Handles chart data requests
 */
@Controller
@RequestMapping("/data")
public class ChartDataController {

	private Logger logger = Logger.getLogger(getClass());

    @Autowired
	private ILineService lineService;

	@Autowired
    private INavigationService navigationService;

	@Autowired
    private IUserService userService;

	@Autowired
	private UserSessionManager userSession;
	
	@Autowired
	private ISVGService rasterizerService;

	@RequestMapping(value = "/chart", method = RequestMethod.GET)
    public String exampleChart(Principal principal) {
    	return "charts/index";
    }

    @RequestMapping(value = "/navigation", method = RequestMethod.GET)
    @ResponseBody
    public ChartNavigationData getNavigationData(@RequestParam int chartId,
	    HttpServletRequest request, Principal principal, Locale locale) throws Exception {

    	final User user = userService.getCurrentUser();
		final ChartNavigationData chartNavigationData = navigationService.getNavigationData(chartId, user, locale);

		// Apply defaults
    	try {
			final NavigationDefaults defaults = getNavigationDefaults(chartId, request);
        	if (defaults != null) {
            	chartNavigationData.setDefaults(defaults);
        	}
    	} catch (Exception ignored) {
    	    logger.info(ignored.getMessage());
    	}

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
	    HttpServletRequest request,
	    Locale locale) throws Exception {
        
        final List<IDataLine> result = new ArrayList<IDataLine>();
        final User user = userService.getCurrentUser();
        final List<Aspect> aspects = navigationService.getDefaultNavigationData(chartId).getAspects();
        
        for (final Aspect aspect : aspects) {
        	final LineRequest lineRequest = new LineRequest(aspect, product, tso, startTime, endTime, resolution, locale);

        	try {
                final IDataLine line = lineService.getLine(lineRequest, user, locale);
                result.add(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

		// Try to update the navigation settings data
        try {
			NavigationDefaults defaults = getNavigationDefaults(chartId, request);
			if (defaults == null) {
				defaults = new NavigationDefaults();
			}
			
			defaults.setTsoId(tso);
			defaults.setProduct(product);
			defaults.setResolution(resolution);
			defaults.setTimeRange(new CalendarRange(startTime, endTime));
			
			setNavigationSettings(defaults, chartId, request);
        } catch (Exception ignored) { 
            logger.info(ignored.getMessage());
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
	    Locale locale) throws Exception {
        
        final Aspect aspect = Aspect.valueOf(strAspect);
    	final LineRequest request = new LineRequest(aspect, product, tso, startTime, endTime, resolution, locale);
        return lineService.getLine(request, userService.getCurrentUser(), locale);
    }


    @RequestMapping(value = "/disabledLines", method = RequestMethod.POST)
    @ResponseBody
    public void setDisabledLines(
        @RequestParam int chartId,
        @RequestParam String lines,
        HttpServletRequest request,
        HttpServletResponse response,
        Principal principal) {
        
		// Try to update the navigation settings data
        try {
			final NavigationDefaults defaults = getNavigationDefaults(chartId,
					request);
            if (lines.isEmpty()) {
				defaults.setDisabledLines(new HashSet<Integer>());
            } else {
                final String[] lineIds = lines.split(",");
				final Set<Integer> disabledLines = new HashSet<Integer>();
    
                for (final String lineId : lineIds) {
                    disabledLines.add(Integer.parseInt(lineId));
                }
                defaults.setDisabledLines(disabledLines);
            }
			setNavigationSettings(defaults, chartId, request);
        } catch (Exception ignored) { 
            logger.info(ignored.getMessage());
        }
    }
    
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public String download(@RequestParam int chartId) {
    	return "charts/download";
    }
    
    @RequestMapping(value = "/svg", method = RequestMethod.POST)
    @ResponseBody
    public String downloadSvg(@RequestParam String svgData, HttpServletResponse response) {
    	try {
	    	response.setHeader("Content-Disposition", "attachment;filename=chart.svg");
	    	response.setContentType("image/svg");
	    	
	    	final InputStream in = new ByteArrayInputStream(svgData.getBytes("UTF-8"));
	    	final Document doc = rasterizerService.sanitizeSVG(in);
	    	
		    final DOMImplementationRegistry reg = DOMImplementationRegistry.newInstance();
		    final DOMImplementationLS lsImpl = (DOMImplementationLS) reg.getDOMImplementation("LS");
		    final LSSerializer serializer = lsImpl.createLSSerializer();

		    return serializer.writeToString(doc);
    	} catch (Exception e) {
    		logger.error(e);
    		response.setStatus(500);
    		return "Error";
    	}
    }
    
    @RequestMapping(value = "/png", method = RequestMethod.POST)
    public void rasterize(@RequestParam String svgData, HttpServletResponse response) {
    	try {
    		final InputStream in = new ByteArrayInputStream(svgData.getBytes("UTF-8"));
    		final OutputStream out = response.getOutputStream();

	    	response.setHeader("Content-Disposition", "attachment;filename=chart.png");
	    	response.setContentType("image/png");

    		rasterizerService.rasterize(in, out);

	    	in.close();
	    	out.close();
    	} catch (Exception e) {
    		logger.error(e);
    		response.setStatus(500);
    	}
    }
    
	private NavigationSettings getNavigationSetting(int chartId, HttpServletRequest request) throws IOException {
		final Set<NavigationSettings> chartSettings = getNavigationSettings(request);
		for (final NavigationSettings setting : chartSettings) {
			if (setting.getChartId() == chartId) {
				return setting;
			}
		}
	    return null;
    }

	private NavigationDefaults getNavigationDefaults(int chartId, HttpServletRequest request) throws IOException {
		final NavigationSettings setting = getNavigationSetting(chartId, request);
		if (setting == null) {
			return null;
		}
		return setting.getSettingsData();
    }

	private Set<NavigationSettings> getNavigationSettings(HttpServletRequest request) throws IOException {
		if (loggedIn()) {
			return navigationService.getNavigationSettingsByUserId(userSession.getUser().getUserId().intValue());
		} else {
			final String clientId = (String) request.getAttribute("clientId");
			return navigationService.getNavigationSettingsByClientId(clientId);
		}
	}

	private void setNavigationSettings(NavigationDefaults defaults, int chartId, HttpServletRequest request) throws Exception {
		final String clientId = (String) request.getAttribute("clientId");

		NavigationSettings newSetting = getNavigationSetting(chartId, request);
		if (newSetting == null) {
			if (loggedIn()) {
				// Don't save client ID if user is logged in.
				// This prevents changing the defaults after logout
				newSetting = new NavigationSettings(chartId, defaults, userSession.getUser(), "-");
			} else {
				newSetting = new NavigationSettings(chartId, defaults, userSession.getUser(), clientId);
			}
		} else {
			newSetting.setSettingsData(defaults);
		}

		navigationService.saveUserNavigationSettings(newSetting);
	}
	
	private boolean loggedIn() {
		return userSession.getUser() != null && !userSession.getUser().isAnonymous();
	}

}
