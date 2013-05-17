package de.enwida.transport.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.enwida.chart.DataManager.EmptyResultException;
import de.enwida.transport.ChartType;
import de.enwida.transport.DataResolution;


public class AbhDataTest extends TransportTest {
	
	// Convenience class
	private static class AbhTestCase extends DataTestCase {
		public AbhTestCase(int product, String isoStartTime, String isoEndTime, DataResolution resolution) {
			super(ChartType.rl_abh1, product, isoStartTime, isoEndTime, resolution);
		}
	}
	
	private List<DataTestCase> testCases = new ArrayList<TransportTest.DataTestCase>();
	
	@Before
	public void createTestCases() {
		testCases.add(new AbhTestCase(211, "2010-12-20 00:00", "2010-12-31 00:00", DataResolution.DAILY));
		testCases.add(new AbhTestCase(311, "2010-12-20 00:00", "2010-12-31 00:00", DataResolution.DAILY));
		testCases.add(new AbhTestCase(314, "2010-12-20 00:00", "2010-12-31 00:00", DataResolution.DAILY));
		testCases.add(new AbhTestCase(314, "2010-01-01 00:00", "2011-01-01 00:00", DataResolution.MONTHLY));
	}
	
	@Test
	public void performTests() throws Exception {
		testXYCases(testCases);
	}
	
	@Test(expected=EmptyResultException.class)
	public void invalidTimeRange() throws Exception {
		testXY(new AbhTestCase(316, "2010-10-15 00:00", "2010-09-17 00:00", DataResolution.DAILY));
	}
	
	@Test(expected=EmptyResultException.class)
	public void invalidProduct() throws Exception {
		testXY(new AbhTestCase(295, "2010-12-30 00:00", "2010-12-31 00:00", DataResolution.QUATER_HOURLY));
	}
	
}
