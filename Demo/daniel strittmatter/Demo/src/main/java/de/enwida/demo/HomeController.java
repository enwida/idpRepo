package de.enwida.demo;

import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.enwida.demo.data.OccupationDataPoint;
import de.enwida.demo.data.OccupationStats;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	private OccupationStats occupationStats;
	
	@Autowired
	private ObjectMapper jsonMapper;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "home";
	}
	
	@RequestMapping(value = "/data.json", method = RequestMethod.GET)
	@ResponseBody
	public String getOccupationDataJson() throws Exception {
		// Fetch all data points from database
		final List<OccupationDataPoint> counts = occupationStats.getAllDataPoints();
		
		// Convert to JSON
		return jsonMapper.writeValueAsString(counts);
	}
	
	@RequestMapping(value = "/rangedata.json", method = RequestMethod.GET)
	@ResponseBody
	public String getOccupationDataJson(@RequestParam int start, @RequestParam int end) throws Exception {
		// Fetch requested data points from database
		final List<OccupationDataPoint> counts = occupationStats.getDataPointsInDateRange(start, end);
		
		// Convert to JSON
		return jsonMapper.writeValueAsString(counts);
	}
	
}
