package de.enwida.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.junit.Assert;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.transport.LineRequest;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.ProductTree;
import de.enwida.web.model.User;
import de.enwida.web.utils.ProductLeaf;

public class ChartNavigationTestsRelaxed extends ChartNavigationTest {
	
	@Override
	protected void checkLines(User user, ChartNavigationData navigationData, Calendar startTime, Calendar endTime) throws Exception {
		// Relaxation settings
		final Random random = new Random();
		final int coveredProductSharePercent = 20;
		final int coveredResolutionSharePercent = 30;
		final int coveredAspectSharePercent = 50;
		
		final Integer[] allProducts = new Integer[] { 200, 300, 211, 212, 221, 222, 311, 321, 312, 322, 313, 323, 314, 324, 315, 325, 316, 326 };
		
		final List<Integer> shuffledProuducts = new ArrayList<>(Arrays.asList(allProducts));
		Collections.shuffle(shuffledProuducts, random);
		
		final List<DataResolution> shuffledResolutions = new ArrayList<>(Arrays.asList(DataResolution.values()));
		Collections.shuffle(shuffledResolutions, random);

		final List<Aspect> shuffledAspects = new ArrayList<>(Arrays.asList(Aspect.values()));
		Collections.shuffle(shuffledAspects, random);
		
		final int numProductChecks = shuffledProuducts.size() * coveredProductSharePercent / 100;
		final int numResolutionChecks = shuffledResolutions.size() * coveredResolutionSharePercent / 100;
		final int numAspectChecks = shuffledAspects.size() * coveredAspectSharePercent / 100;
		final int numTotalChecks = numProductChecks * numResolutionChecks * numAspectChecks;
		
		int i = 0;

		for (final ProductTree tree : navigationData.getProductTrees()) {
			for (int productCount = 0; productCount < numProductChecks; productCount++) {
				final int product = shuffledProuducts.get(productCount);

				for (int resolutionCount = 0; resolutionCount < numResolutionChecks;  resolutionCount++) {
					final DataResolution resolution = shuffledResolutions.get(resolutionCount);
					
					for (int aspectCount = 0; aspectCount < numAspectChecks; aspectCount++) {
						final Aspect aspect = shuffledAspects.get(aspectCount);

						i++;
						System.out.println("[" + i + " / " + numTotalChecks + "] Product: " + product + " | Aspect: " + aspect.toString());

						final ProductLeaf leaf = tree.getLeaf(product);
						if (leaf == null || !leaf.getResolution().contains(resolution)) {
							final Calendar savedStartTime = startTime;
							final Calendar savedEndTime = endTime;

							if (startTime == null) {
								startTime = Calendar.getInstance();
								startTime.setTime(dateFormat.parse("2011-01-01"));
							}
							if (endTime == null) {
								endTime = Calendar.getInstance();
								endTime.setTime(dateFormat.parse("2012-01-01"));
							}

							final LineRequest lineRequest = new LineRequest(aspect, product, tree.getTso(), startTime, endTime, resolution, Locale.ENGLISH);
							Assert.assertFalse(securityService.isAllowed(lineRequest, user));
							
							startTime = savedStartTime;
							endTime = savedEndTime;
						} else {
							if (startTime == null) {
								startTime = leaf.getTimeRange().getFrom();
							}
							if (endTime == null) {
								endTime = leaf.getTimeRange().getTo();
							}

							final LineRequest lineRequest = new LineRequest(aspect, product, tree.getTso(), startTime, endTime, resolution, Locale.ENGLISH);
							Assert.assertTrue(securityService.isAllowed(lineRequest, user));

							// Enlarge time range
							final Calendar t1 = (Calendar) startTime.clone();
							final Calendar t2 = (Calendar) endTime.clone();
							t1.add(Calendar.YEAR, -2);
							t2.add(Calendar.YEAR, 2);
							lineRequest.setStartTime(t1);
							lineRequest.setEndTime(t2);

							Assert.assertFalse(securityService.isAllowed(lineRequest, user));
						}
					}
				}
			}
		}	}

}
