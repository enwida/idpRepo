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
import de.enwida.transport.Aspect;
import de.enwida.web.service.interfaces.AspectService;

public class AspectServiceImp implements AspectService{
	
	@Autowired
	public DataLineRequestManager dataLineRequestManager;

	public GoogleChartData getLine(Aspect aspect,Map pMap) {

		// Create the data request
		DataRequest dr = new DataRequest("json",pMap);
		
		dr.setAspect(aspect);
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
		dataLineRequestManager.setDatef(dateFormat);
		dataLineRequestManager.setDatef2(dateFormat);
		
		dr=dataLineRequestManager.setUsualValues(dr, 2);
		// Fetch and display chart data in JSON representation
		dr=dataLineRequestManager.getLineData(dr);
		
		return dr.getGcd();
	}
	
	public GoogleChartData getChart(Map pMap){
		GoogleChartData gcd=null;
		String chartType=null;
		if (pMap.containsKey("type")){
			chartType=((String[]) pMap.get("type"))[0];
		}
		
		switch(chartType){
		case "rl_ab1":
			break;
		case "rl_ab2":
			break;
		case "rl_abg1":
			break;
		case "rl_abh1":
			break;
		case "rl_erl1":
			break;
		case "rl_geb1":
			break;
		case "rl_geb2":
			break;
		case "rl_prs1":
			break;
		case "rl_prs2":
			break;
		case "rl_prs3":
			break;
		case "rl_prs4":
			break;
		case "rl_prs5":
			break;
		case "rl_prs6":
			break;
		case "rl_vol1":
			gcd=getLine(Aspect.VOL_ACCEPTED,pMap);
			gcd=getLine(Aspect.VOL_OFFERED,pMap);
			break;
		}
		return gcd;
	}
}
