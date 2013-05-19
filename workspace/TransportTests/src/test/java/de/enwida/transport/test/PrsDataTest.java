package de.enwida.transport.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.enwida.chart.DataManager.EmptyResultException;
import de.enwida.transport.ChartType;
import de.enwida.transport.DataResolution;


public class PrsDataTest extends TransportTest {
	
	// Some convenience classes
	private static class Prs1TestCase extends DataTestCase {
		public Prs1TestCase(int product, String isoStartTime, String isoEndTime, DataResolution resolution) {
			super(ChartType.rl_prs1, product, isoStartTime, isoEndTime, resolution);
		}
	}
	private static class Prs2TestCase extends DataTestCase {
		public Prs2TestCase(int product, String isoStartTime, String isoEndTime, DataResolution resolution) {
			super(ChartType.rl_prs2, product, isoStartTime, isoEndTime, resolution);
		}
	}
	private static class Prs3TestCase extends DataTestCase {
		public Prs3TestCase(int product, String isoStartTime, String isoEndTime, DataResolution resolution) {
			super(ChartType.rl_prs3, product, isoStartTime, isoEndTime, resolution);
		}
	}
	private static class Prs4TestCase extends DataTestCase {
		public Prs4TestCase(int product, String isoStartTime, String isoEndTime, DataResolution resolution) {
			super(ChartType.rl_prs4, product, isoStartTime, isoEndTime, resolution);
		}
	}
	
	private List<DataTestCase> testCases = new ArrayList<TransportTest.DataTestCase>();
	
	@Before
	public void createTestCases() {
		testCases.add(new Prs1TestCase(310, "2010-12-30 00:00", "2010-12-31 00:00", DataResolution.QUATER_HOURLY));
		testCases.add(new Prs1TestCase(320, "2010-12-20 00:00", "2010-12-21 00:00", DataResolution.QUATER_HOURLY));
		
		testCases.add(new Prs2TestCase(321, "2010-12-20 00:00", "2010-12-31 00:00", DataResolution.DAILY));
		testCases.add(new Prs2TestCase(325, "2010-10-10 00:00", "2010-12-28 00:00", DataResolution.WEEKLY));
		
		testCases.add(new Prs3TestCase(210, "2010-12-30 00:00", "2010-12-31 00:00", DataResolution.QUATER_HOURLY));
		testCases.add(new Prs3TestCase(210, "2010-10-21 00:00", "2010-12-21 00:00", DataResolution.WEEKLY));
		
		testCases.add(new Prs4TestCase(211, "2010-12-20 00:00", "2010-12-31 00:00", DataResolution.DAILY));
		testCases.add(new Prs4TestCase(312, "2010-01-20 00:00", "2010-12-31 00:00", DataResolution.MONTHLY));
	}
	
	@Test
	public void performTests() throws Exception {
		testMinMaxCases(testCases);
	}
	
	@Test(expected=EmptyResultException.class)
	public void invalidTimeRange() throws Exception {
		testXY(new Prs2TestCase(316, "2010-10-15 00:00", "2010-09-17 00:00", DataResolution.DAILY));
	}
	
	@Test(expected=EmptyResultException.class)
	public void invalidProduct() throws Exception {
		testXY(new Prs2TestCase(295, "2010-12-30 00:00", "2010-12-31 00:00", DataResolution.QUATER_HOURLY));
	}
	
	
}
