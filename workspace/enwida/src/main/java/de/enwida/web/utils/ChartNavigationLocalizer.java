package de.enwida.web.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import de.enwida.transport.DataResolution;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.ProductTree;

public class ChartNavigationLocalizer {
	
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
		    
	        navigationData.setChartTitle(chartTitle);
	        navigationData.setxAxisLabel(xAxisLabel);
	        navigationData.setyAxisLabel(yAxisLabel);
		}
		    
		private void setTsos() {
		    for (final ProductTree tree : navigationData.getProductTrees()) {
		        final int tso = tree.getTso();
	    	    final String tsoName = getChartMessage("tso." + tso, "TSO " + tso);
	    	    tree.setTsoName(tsoName);
		    }
		}
		
		private void setTimeRanges() {
		    for (final String key : navigationData.getTimeRanges().keySet()) {
		    	final String timeRangeName = getChartMessage("timerange." + key, key);
	    	    navigationData.getTimeRanges().put(key, timeRangeName);
		    }
		}
		
		private void setProductParts() {
			for (final ProductTree tree : navigationData.getProductTrees()) {
				for (final ProductNode child : tree.getRoot().getChildren()) {
					setProductParts(child);
				}
			}
		}
		
		private void setResolutions() {
			for (final DataResolution resolution : navigationData.getAllResolutions()) {
				final String resolutionName = getChartMessage("resolution." + resolution, resolution.toString().toLowerCase());
				navigationData.getResolutionNames().put(resolution, resolutionName);
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
