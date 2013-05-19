package de.enwida.transport.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.enwida.chart.DataManager.EmptyResultException;
import de.enwida.transport.ChartType;
import de.enwida.transport.DataResolution;


public class AbDataTest extends TransportTest {
	
	// Some convenience classes
	private static class Ab1TestCase extends DataTestCase {
		public Ab1TestCase(int product, String isoStartTime, String isoEndTime, DataResolution resolution) {
			super(ChartType.rl_ab1, product, isoStartTime, isoEndTime, resolution);
		}
	}
	private static class Ab2TestCase extends DataTestCase {
		public Ab2TestCase(int product, String isoStartTime, String isoEndTime, DataResolution resolution) {
			super(ChartType.rl_ab2, product, isoStartTime, isoEndTime, resolution);
		}
	}
	
	private List<DataTestCase> testCases = new ArrayList<TransportTest.DataTestCase>();
	
	@Before
	public void createTestCases() {
		testCases.add(new Ab1TestCase(210, "2010-12-30 00:00", "2010-12-31 00:00", DataResolution.QUATER_HOURLY));
		testCases.add(new Ab1TestCase(310, "2010-12-30 00:00", "2010-12-31 00:00", DataResolution.HOURLY));
		
		testCases.add(new Ab2TestCase(210, "2010-12-30 00:00", "2010-12-31 00:00", DataResolution.QUATER_HOURLY));
		testCases.add(new Ab2TestCase(210, "2010-12-30 00:00", "2010-12-31 00:00", DataResolution.HOURLY));
		testCases.add(new Ab2TestCase(311, "2010-12-20 00:00", "2010-12-31 00:00", DataResolution.DAILY));
		testCases.add(new Ab2TestCase(321, "2010-01-01 00:00", "2010-11-01 00:00", DataResolution.MONTHLY));
		testCases.add(new Ab2TestCase(324, "2010-01-01 00:00", "2011-01-01 00:00", DataResolution.YEARLY));
		testCases.add(new Ab2TestCase(316, "2010-10-15 00:00", "2010-12-17 00:00", DataResolution.WEEKLY));
	}
	
	@Test
	public void performTests() throws Exception {
		testXYCases(testCases);
	}
	
	@Test(expected=EmptyResultException.class)
	public void invalidTimeRange() throws Exception {
		testXY(new Ab2TestCase(316, "2010-10-15 00:00", "2010-09-17 00:00", DataResolution.DAILY));
	}
	
	@Test(expected=EmptyResultException.class)
	public void invalidProduct() throws Exception {
		testXY(new Ab2TestCase(295, "2010-12-30 00:00", "2010-12-31 00:00", DataResolution.QUATER_HOURLY));
	}
	
	
}
