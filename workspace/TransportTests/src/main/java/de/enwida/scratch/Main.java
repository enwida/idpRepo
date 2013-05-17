package de.enwida.scratch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.enwida.chart.DataManager;
import de.enwida.transport.ChartType;
import de.enwida.transport.DataRequest;
import de.enwida.transport.DataResolution;
import de.enwida.transport.DataResponse;
import de.enwida.transport.MinMaxDataLine;
import de.enwida.transport.MinMaxDataPoint;
import de.enwida.transport.XYDataLine;
import de.enwida.transport.XYDataPoint;

public class Main {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final ApplicationContext context = new ClassPathXmlApplicationContext("app-context.xml");
	private static final DataManager manager = context.getBean(DataManager.class);

	public static void main(String[] args) throws Exception {
		ab1Test();
		ab2Test();
		geb1Test();
		prs1Test();
	}
	
	
	private static void ab1Test() throws Exception {
		final DataResponse res = getResponse(ChartType.rl_ab1, 210, "2010-12-30 00:00", "2010-12-31 23:00", DataResolution.QUATER_HOURLY);
		res.throwError();
		System.out.println(res.getMetaData().getChartTitle());
		printDateDataLine((XYDataLine) res.getAllDataLines().get(0));
	}
	
	private static void ab2Test() throws Exception {
		final DataResponse res = getResponse(ChartType.rl_ab2, 323, "2010-09-20 00:00", "2010-11-15 23:00", DataResolution.WEEKLY);
		res.throwError();
		printDateDataLine((XYDataLine) res.getAllDataLines().get(0));
	}
	
	private static void geb1Test() throws Exception {
		final DataResponse res = getResponse(ChartType.rl_geb1, 212, "2010-12-01 00:00", "2010-12-31 23:00", DataResolution.HOURLY);
		res.throwError();
		printDataLine((XYDataLine) res.getAllDataLines().get(0));
		printDataLine((XYDataLine) res.getAllDataLines().get(1));
	}	
	
	private static void prs1Test() throws Exception {
		final DataResponse res = getResponse(ChartType.rl_prs1, 210, "2010-12-30 00:00", "2010-12-30 23:00", DataResolution.QUATER_HOURLY);
		res.throwError();
		printDateMinMaxLine((MinMaxDataLine) res.getAllDataLines().get(0));
	}
	
	private static DataResponse getResponse(ChartType chartType, int product, String startTime, String endTime, DataResolution resolution) throws ParseException {
		final Date d1 = dateFormat.parse(startTime);
		final Date d2 = dateFormat.parse(endTime);
		final Calendar c1 = Calendar.getInstance();
		final Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		
		final DataRequest req = new DataRequest(chartType, product, c1, c2, resolution, Locale.GERMAN);
		return manager.getData(req);
	}
	
	private static void printDateDataLine(XYDataLine line) {
		System.out.println("----------------");
		for (final XYDataPoint dp : line.getDataPoints()) {
			final Calendar xTime = Calendar.getInstance();
			xTime.setTimeInMillis((long) dp.x);
			System.out.println(dateFormat.format(xTime.getTime()) + ": " + dp.y);
		}
	}
	
	private static void printDataLine(XYDataLine line) {
		System.out.println("----------------");
		for (final XYDataPoint dp : line.getDataPoints()) {
			System.out.println(dp.x + ": " + dp.y);
		}
	}
	
	private static void printDateMinMaxLine(MinMaxDataLine line) {
		System.out.println("----------------");
		for (final MinMaxDataPoint dp : line.getDataPoints()) {
			final Calendar xTime = Calendar.getInstance();
			xTime.setTimeInMillis((long) dp.x);
			System.out.println(dateFormat.format(xTime.getTime()) + ": " + dp.mean + "[" + dp.min + ", " + dp.max + "]");
		}
	}


}
