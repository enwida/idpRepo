package de.enwida.transport.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.enwida.chart.DataManager.EmptyResultException;
import de.enwida.transport.ChartType;
import de.enwida.transport.DataResolution;


public class AbgDataTest extends TransportTest {
	
	// Convenience class
	private static class AbgTestCase extends DataTestCase {
		public AbgTestCase(int product, String isoStartTime, String isoEndTime, DataResolution resolution) {
			super(ChartType.rl_abg1, product, isoStartTime, isoEndTime, resolution);
		}
	}
	
	private List<DataTestCase> testCases = new ArrayList<TransportTest.DataTestCase>();
	
	@Before
	public void createTestCases() {
		testCases.add(new AbgTestCase(211, "2010-12-20 00:00", "2010-12-31 00:00", DataResolution.DAILY));
		testCases.add(new AbgTestCase(311, "2010-12-20 00:00", "2010-12-31 00:00", DataResolution.DAILY));
		testCases.add(new AbgTestCase(314, "2010-12-20 00:00", "2010-12-31 00:00", DataResolution.DAILY));
		testCases.add(new AbgTestCase(314, "2010-01-01 00:00", "2011-01-01 00:00", DataResolution.MONTHLY));
	}
	
	@Test
	public void performTests() throws Exception {
		testXYCases(testCases);
	}
	
	@Test(expected=EmptyResultException.class)
	public void invalidTimeRange() throws Exception {
		testXY(new AbgTestCase(316, "2010-10-15 00:00", "2010-09-17 00:00", DataResolution.DAILY));
	}
	
	@Test(expected=EmptyResultException.class)
	public void invalidProduct() throws Exception {
		testXY(new AbgTestCase(295, "2010-12-30 00:00", "2010-12-31 00:00", DataResolution.QUATER_HOURLY));
	}
	
}
