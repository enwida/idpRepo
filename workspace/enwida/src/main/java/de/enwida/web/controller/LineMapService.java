package de.enwida.web.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import de.enwida.chart.DataRequest;
import de.enwida.chart.DataRequestManager;
import de.enwida.chart.GoogleChartData;

public class LineMapService {
	

	public DataRequestManager dataRequestManager;

	public GoogleChartData getChart(HttpServletRequest request,String completeUrl,Map pMap) {
		// Define the start and end dates manually
		final Calendar calStart = Calendar.getInstance();
		calStart.set(Calendar.YEAR, 2010);
		calStart.set(Calendar.MONTH, Calendar.DECEMBER);
		calStart.set(Calendar.DAY_OF_MONTH, 30);
		calStart.set(Calendar.HOUR, 0);
		calStart.set(Calendar.MINUTE, 0);
		
		final Calendar calEnd = Calendar.getInstance();
		calEnd.set(Calendar.YEAR, 2010);
		calEnd.set(Calendar.MONTH, Calendar.DECEMBER);
		calEnd.set(Calendar.DAY_OF_MONTH, 31);	
		calStart.set(Calendar.HOUR, 23);
		calStart.set(Calendar.MINUTE, 59);
		// Create the data request
		DataRequest dr = new DataRequest("json",request.getLocale(),completeUrl, pMap);
		
		// Set these values manually because they are not converted automatically from the string
		// representation
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		dr.setTime1(calStart.getTimeInMillis());
		dr.setTime2(calEnd.getTimeInMillis());
		dr.setProduct(211);
		dataRequestManager.setDatef(dateFormat);
		dataRequestManager.setDatef2(dateFormat);
		
		dr=dataRequestManager.setUsualValues(dr, 3);
		// Fetch and display chart data in JSON representation
		dr=dataRequestManager.json_rl_vol_accepted(dr,1);
		dr=dataRequestManager.json_rl_vol_offered(dr,2);
		
		final GoogleChartData chartData = dr.getGcd();
		return chartData;
	}

}
