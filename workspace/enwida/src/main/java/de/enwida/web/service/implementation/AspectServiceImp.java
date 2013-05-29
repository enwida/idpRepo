package de.enwida.web.service.implementation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import de.enwida.chart.DataLineRequestManager;
import de.enwida.chart.DataRequest;
import de.enwida.chart.DataRequestManager;
import de.enwida.chart.GoogleChartData;

public class AspectServiceImp {
	
	@Autowired
	public DataLineRequestManager dataRequestManager;

	public GoogleChartData getLine(String completeUrl,Map pMap) {

		// Create the data request
		DataRequest dr = new DataRequest("json",pMap);
		

		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		final Calendar calStart = Calendar.getInstance();
		final Calendar calEnd = Calendar.getInstance();
		try {
			calStart.setTime(dateFormat.parse(dr.getStime1()));
			calEnd.setTime(dateFormat.parse(dr.getStime2()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dr.setTime1(calStart.getTimeInMillis());
		dr.setTime2(calEnd.getTimeInMillis());
		dataRequestManager.setDatef(dateFormat);
		dataRequestManager.setDatef2(dateFormat);
		
		dr=dataRequestManager.setUsualValues(dr, 2);
		// Fetch and display chart data in JSON representation
		dr=dataRequestManager.getLineData(dr);
		
		return dr.getGcd();
	}

}
