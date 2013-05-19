package de.enwida.transport.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.enwida.chart.DataManager.EmptyResultException;
import de.enwida.transport.ChartType;
import de.enwida.transport.DataResolution;


public class GebDataTest extends TransportTest {
	
	// Some convenience classes
	private static class Geb1TestCase extends DataTestCase {
		public Geb1TestCase(int product, String isoStartTime, String isoEndTime, DataResolution resolution) {
			super(ChartType.rl_geb1, product, isoStartTime, isoEndTime, resolution);
		}
	}
	private static class Geb2TestCase extends DataTestCase {
		public Geb2TestCase(int product, String isoStartTime, String isoEndTime, DataResolution resolution) {
			super(ChartType.rl_geb2, product, isoStartTime, isoEndTime, resolution);
		}
	}
	
	private List<DataTestCase> testCases = new ArrayList<TransportTest.DataTestCase>();
	
	@Before
	public void createTestCases() {
		testCases.add(new Geb1TestCase(211, "2010-12-01 00:00", "2010-12-31 00:00", DataResolution.DAILY));
		testCases.add(new Geb1TestCase(222, "2010-12-01 00:00", "2010-12-31 00:00", DataResolution.DAILY));
		
		testCases.add(new Geb2TestCase(212, "2010-12-01 00:00", "2010-12-31 00:00", DataResolution.DAILY));
		testCases.add(new Geb2TestCase(321, "2010-11-01 00:00", "2010-11-30 00:00", DataResolution.DAILY));
	}
	
	@Test
	public void performTests() throws Exception {
		for (final DataTestCase testCase : testCases) {
			testSplitXY(testCase);
		}
	}
	
	@Test(expected=EmptyResultException.class)
	public void invalidTimeRange() throws Exception {
		testXY(new Geb1TestCase(316, "2010-10-15 00:00", "2010-09-17 00:00", DataResolution.DAILY));
	}
	
	@Test(expected=EmptyResultException.class)
	public void invalidProduct() throws Exception {
		testXY(new Geb1TestCase(295, "2010-12-30 00:00", "2010-12-31 00:00", DataResolution.QUATER_HOURLY));
	}
	
}
