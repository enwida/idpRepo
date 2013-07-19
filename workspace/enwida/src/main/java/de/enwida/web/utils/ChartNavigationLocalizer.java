package de.enwida.web.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import de.enwida.transport.DataResolution;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.ProductTree;

public class ChartNavigationLocalizer {
	
	private static List<String> infoKeys;
	
	static {
		infoKeys = Arrays.asList(new String[] {
			"title",
			"product",
			"timerange",
			"resolution",
			"tso"
		});
	}
	
	@Autowired
	private MessageSource messageSource;

	public ChartNavigationData localize(ChartNavigationData navigationData, int chartId, Locale locale) {
		return new Localizer(navigationData, chartId, locale).applyLocalizations();
	}
	
	class Localizer {
		private Locale locale;
		private int chartId;
		private ChartNavigationData navigationData;
	
		public Localizer(ChartNavigationData navigationData, int chartId, Locale locale) {
			this.navigationData = navigationData;
			this.chartId = chartId;
			this.locale = locale;
		}
		
		public ChartNavigationData applyLocalizations() {
			setTitles();
			setInfoKeys();
			setTsos();
			setTimeRanges();
			setResolutions();
			setProductParts();
	
			return navigationData;
		}
		
		private void setTitles() {
		    final String chartTitle = getChartSpecificMessage("title");
		    final String xAxisLabel = getChartSpecificMessage("xlabel");
		    final String yAxisLabel = getChartSpecificMessage("ylabel");
		    
	        navigationData.getDictionary().setTitle(chartTitle);
	        navigationData.getDictionary().setxAxisLabel(xAxisLabel);
	        navigationData.getDictionary().setyAxisLabel(yAxisLabel);
		}
		
		private void setInfoKeys() {
			for (final String infoKey : infoKeys) {
				final String infoKeyName = getChartMessage("infokey." + infoKey, infoKey);
				navigationData.getDictionary().getInfoKeys().put(infoKey, infoKeyName);
			}
		}
		    
		private void setTsos() {
		    for (final ProductTree tree : navigationData.getProductTrees()) {
		        final int tso = tree.getTso();
	    	    final String tsoName = getChartMessage("tso." + tso, "TSO " + tso);
	    	    navigationData.getDictionary().getTsos().put(tso, tsoName);
		    }
		}
		
		private void setTimeRanges() {
		    for (final String timeRange : navigationData.getTimeRanges()) {
		    	final String timeRangeName = getChartMessage("timerange." + timeRange, timeRange);
		    	navigationData.getDictionary().getTimeRanges().put(timeRange, timeRangeName);
		    }
		}

		private void setResolutions() {
			for (final DataResolution resolution : navigationData.getAllResolutions()) {
				final String resolutionName = getChartMessage("resolution." + resolution, resolution.toString().toLowerCase());
				navigationData.getDictionary().getResolutions().put(resolution, resolutionName);
			}
		}
		
		private void setProductParts() {
			for (final ProductTree tree : navigationData.getProductTrees()) {
				for (final ProductNode child : tree.getRoot().getChildren()) {
					setProductParts(child);
				}
			}
		}
		
		private void setProductParts(ProductNode node) {
			final String partName = getChartMessage("productpart." + node.getName(), node.getName());
			node.setName(partName);
			
			if (node.getChildren() != null) {
				for (final ProductNode child : node.getChildren()) {
					setProductParts(child);
				}
			}
		}
		
	    private String getChartSpecificMessage(String property, String defaultMessage) {
		    return messageSource.getMessage("de.enwida.chart." + chartId + "." + property, null, defaultMessage, locale);
	    }
	    
	    private String getChartSpecificMessage(String property) {
	    	return getChartSpecificMessage(property, "");
	    }
	    
	    private String getChartMessage(String property, String defaultMessage) {
		    return messageSource.getMessage("de.enwida.chart." + property, null, defaultMessage, locale);
	    }
	    
	}

}
