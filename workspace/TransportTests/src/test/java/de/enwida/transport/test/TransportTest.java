package de.enwida.transport.test;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.enwida.chart.DataRequestManager;
import de.enwida.chart.DataManager;
import de.enwida.chart.GoogleChartData;
import de.enwida.chart.GoogleChartData.Cell;
import de.enwida.chart.GoogleChartData.Row;
import de.enwida.chart.util.ResolutionConverter;
import de.enwida.transport.ChartType;
import de.enwida.transport.DataLine;
import de.enwida.transport.DataRequest;
import de.enwida.transport.DataResolution;
import de.enwida.transport.DataResponse;
import de.enwida.transport.MinMaxDataLine;
import de.enwida.transport.MinMaxDataPoint;
import de.enwida.transport.XYDataLine;
import de.enwida.transport.XYDataPoint;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/app-context.xml" })
public abstract class TransportTest {

	protected static final SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	protected static final SimpleDateFormat managerDateFormat = new SimpleDateFormat("yyyyMMdd");
	protected static final Logger logger = LoggerFactory.getLogger(TransportTest.class);
	
	@Autowired
	protected DataManager managerNew;
	
	@Autowired
	protected DataRequestManager managerOld;
	
	protected static class DataTestCase {
		public ChartType type;
		public int product;
		public String isoStartTime;
		public String isoEndTime;
		public DataResolution resolution;
		
		public DataTestCase(ChartType type, int product, String isoStartTime,
						  String isoEndTime, DataResolution resolution) {
			
			this.type = type;
			this.product = product;
			this.isoStartTime = isoStartTime;
			this.isoEndTime = isoEndTime;
			this.resolution = resolution;
		}
		
	}
	
	protected static class Responses {
		public DataResponse expected;
		public GoogleChartData testee;
		
		public Responses(DataResponse expected, GoogleChartData testee) {
			this.expected = expected;
			this.testee = testee;
		}
	}
	
	@Before
	public void init() {
		managerOld.setDatef(managerDateFormat);
		managerOld.setDatef2(managerDateFormat);
	}
	
	protected de.enwida.chart.DataRequest getOldDataRequest(DataRequest req) {
		final de.enwida.chart.DataRequest result = new de.enwida.chart.DataRequest();
		
		result.setDataFormat("json");
		result.setChartType(req.getChartType().toString());
		result.setTime1(req.getStartTime().getTimeInMillis());
		result.setTime2(req.getEndTime().getTimeInMillis());
		result.setProduct(req.getProduct());
		result.setProduct2(req.getProduct());
		result.setResolution(ResolutionConverter.getString(req.getResolution()));
		result.setLocale(req.getLocale());
		
		return result;
	}
	
	protected Responses getResponses(DataTestCase testCase) throws Exception {
		final Calendar startTime = parseIsoDate(testCase.isoStartTime);
		final Calendar endTime = parseIsoDate(testCase.isoEndTime);
		
		final DataRequest req = new DataRequest(testCase.type, testCase.product, startTime, endTime, testCase.resolution, Locale.ENGLISH);
		final DataResponse expected = managerNew.getData(req);
		expected.throwError();
		
		final de.enwida.chart.DataRequest testReq = getOldDataRequest(req);
		final GoogleChartData testee = managerOld.getData(testReq);
		
		return new Responses(expected, testee);
	}
	
	protected void testXY(DataTestCase testCase) throws Exception {
		final Responses responses = getResponses(testCase);
		for (int i = 0; i < responses.expected.getAllDataLines().size(); i++) {
			checkYData((XYDataLine) responses.expected.getAllDataLines().get(i), responses.testee, i + 1);
		}
	}
	
	protected void testSplitXY(DataTestCase testCase) throws Exception {
		final Responses responses = getResponses(testCase);
		
		logger.info("Testing " + responses.testee.getRows().size() + " data points...");
		
		int col1Counter = 0;
		int col2Counter = 0;
		
		for (int i = 0; i < responses.testee.getRows().size(); i++) {
			final Row row = responses.testee.getRows().get(i);
			final Cell col1 = row.getC().get(1);
			final Cell col2 = row.getC().get(2);
			
			if (col1 != null) {
				final Double col1Val = (Double) col1.getV();
				
				if (col1Val != null) {
					final XYDataLine line = (XYDataLine) responses.expected.getAllDataLines().get(0);
					Assert.assertEquals(line.getDataPoints().get(col1Counter++).y, (double) col1Val, 0.0001);
				}
			}
			if (col2 != null) {
				final Double col2Val = (Double) col2.getV();
				
				if (col2Val != null) {
					final XYDataLine line = (XYDataLine) responses.expected.getAllDataLines().get(1);
					Assert.assertEquals(line.getDataPoints().get(col2Counter++).y, (double) col2Val, 0.0001);
				}
			}
		}
	}
	
	protected void testMinMax(DataTestCase testCase) throws Exception {
		final Responses responses = getResponses(testCase);
		final MinMaxDataLine line = (MinMaxDataLine) responses.expected.getAllDataLines().get(0);
		
		Assert.assertEquals(line.getDataPoints().size(), responses.testee.getRows().size());
		logger.info("Testing " + line.getDataPoints().size() + " data points...");
		
		for (int i = 0; i< line.getDataPoints().size(); i++) {
			final MinMaxDataPoint expectedDp = line.getDataPoints().get(i);
			final Row testeeRow = responses.testee.getRows().get(i);
			
			Assert.assertEquals(expectedDp.min, (Double) testeeRow.getC().get(1).getV(), 0.0001);
			Assert.assertEquals(expectedDp.mean, (Double) testeeRow.getC().get(2).getV(), 0.0001);
			Assert.assertEquals(expectedDp.max, (Double) testeeRow.getC().get(3).getV(), 0.0001);
		}
	}

	protected void testXYCases(List<DataTestCase> cases) throws Exception {
		for (final DataTestCase testCase : cases) {
			testXY(testCase);
		}
	}
	
	protected void testMinMaxCases(List<DataTestCase> cases) throws Exception {
		for (final DataTestCase testCase : cases) {
			testMinMax(testCase);
		}
	}
		
	protected void checkYData(DataLine<XYDataPoint> expected, GoogleChartData testee, int column) {
		Assert.assertEquals(expected.getDataPoints().size(), testee.getRows().size());
		
		logger.info("Testing " + expected.getDataPoints().size() + " data points...");
		
		for (int i = 0; i < testee.getRows().size(); i++) {
			final XYDataPoint expectedDp = expected.getDataPoints().get(i);
			final double testeeY = (Double) testee.getRows().get(i).getC().get(column).getV();
			
			Assert.assertEquals(expectedDp.y, testeeY, 0.0001);
		}
	}
	
	protected Calendar parseIsoDate(String isoDate) throws ParseException {
		final Calendar result = Calendar.getInstance();
		result.setTime(isoDateFormat.parse(isoDate));
		return result;
	}

}
