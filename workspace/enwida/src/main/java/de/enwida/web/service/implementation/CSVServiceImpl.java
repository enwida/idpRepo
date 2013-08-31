package de.enwida.web.service.implementation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import de.enwida.transport.DataResolution;
import de.enwida.transport.IDataLine;
import de.enwida.transport.XYDataLine;
import de.enwida.transport.XYDataPoint;
import de.enwida.web.db.model.CalendarRange;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.ProductTree;
import de.enwida.web.service.interfaces.ICSVService;
import de.enwida.web.utils.ProductNode;
import de.enwida.web.utils.numbers.DefaultNumberFormatter;
import de.enwida.web.utils.numbers.INumberFormatter;
import de.enwida.web.utils.timestamp.ITimestampFormatter;
import de.enwida.web.utils.timestamp.LocalTimestampFormatter;

@Service("csvService")
public class CSVServiceImpl implements ICSVService  {

	@Autowired
	private MessageSource messageSource;

	@Override
	public String createCSV(ChartNavigationData navigationData,
			List<? extends IDataLine> lines, Locale locale,
			ITimestampFormatter timestampFormatter, INumberFormatter numberFormatter) {

		if (lines.size() < 1) {
			throw new IllegalArgumentException("Provide at least one line");
		}
		final CSVCreator creator = new CSVCreator(lines, navigationData, locale, numberFormatter, timestampFormatter);
		return creator.create();
	}

	@Override
	public String createCSV(ChartNavigationData navigationData,
			List<? extends IDataLine> lines, Locale locale) {

		return createCSV(navigationData, lines, locale, new LocalTimestampFormatter(), new DefaultNumberFormatter());
	}
	
	class CSVCreator {
		
		private StringBuilder builder;
		private List<? extends IDataLine> lines;
		private ChartNavigationData navigationData;
		private Locale locale;
		
		private INumberFormatter numberFormatter;
		private ITimestampFormatter timestampFormatter;
	
	    public CSVCreator(List<? extends IDataLine> lines,
				ChartNavigationData navigationData, Locale locale,
				INumberFormatter numberFormatter,
				ITimestampFormatter timestampFormatter) {

			this.lines = lines;
			this.navigationData = navigationData;
			this.locale = locale;
			this.numberFormatter = numberFormatter;
			this.timestampFormatter = timestampFormatter;
		}
	    
	    public String create() {
	    	builder = new StringBuilder();
	    	
	    	try {
	    		final IDataLine line = lines.get(0);
		    	appendCSVInfo(line.getTso(), line.getProduct(), new CalendarRange(line.getStartTime(), line.getEndTime()), line.getResolution());
	    	} catch (Exception ignored) {}
	    	
	    	appendCSVHeader();
	    	appendCSVData();
	    	
	    	return builder.toString();
	    }

		private void appendCSVData() {
	    	final SortedMap<Double, List<Double>> csvDataLines = new TreeMap<>();
	    	
	    	for (final IDataLine line : lines) {
	    		if (!(line instanceof XYDataLine)) {
	    			throw new NotImplementedException("Only XY datalines are supported");
	    		}
	    		final XYDataLine xyLine = (XYDataLine) line;
	    		for (final XYDataPoint dp : xyLine.getDataPoints()) {
	    			List<Double> ys = csvDataLines.get(dp.x);
	    			if (ys == null) {
	    				ys = new ArrayList<>();
	    				csvDataLines.put(dp.x, ys);
	    			}
	    			ys.add(dp.y);
	    		}
	    	}
	
	    	for (final Double x : csvDataLines.keySet()) {
	    		if (navigationData.getIsDateScale()) {
	    			final Date date = new Date(x.longValue());
	    			builder.append(timestampFormatter.format(date));
	    		} else {
		    		builder.append(numberFormatter.formatNumber(x));
	    		}
	    		for (final Double y : csvDataLines.get(x)) {
	    			builder.append(";").append(numberFormatter.formatNumber(y));
	    		}
	    		builder.append(";\r\n");
	    	}
	    }
	    
	    private void appendCSVHeader() {
	    	// Heading
	    	builder.append(navigationData.getxAxisLabel());
	    	for (final IDataLine line : lines) {
	    		builder.append(";").append(line.getTitle() + " [" + line.getUnit() + "]");
	    	}
	    	builder.append(";\r\n");
	    }
	    
	    private void appendCSVInfo(int tso, int product, CalendarRange timeRange, DataResolution resolution) {
	    	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    	final String sTso = messageSource.getMessage("de.enwida.chart.tso." + tso, null, "-", locale);
	    	final String sProduct = getProductString(product, getTree(navigationData, tso));
	    	final String sResolution = messageSource.getMessage("de.enwida.chart.resolution." + resolution.name(), null, "-", locale);
	    	
	    	builder.append("# TSO: ").append(sTso).append("\r\n");
	    	builder.append("# Product: ").append(sProduct).append("\r\n");
	    	builder.append("# Time range: ").append(dateFormat.format(timeRange.getFrom().getTime()))
	    	                               .append(" - ")
	    	                               .append(dateFormat.format(timeRange.getTo().getTime()))
	    	                               .append("\r\n");
	    	builder.append("# Resolution: ").append(sResolution).append("\r\n");
	    }
	    
	}

    private static String getProductString(int product, ProductTree tree) {
    	final StringBuilder result = new StringBuilder();
    	final String sProduct = String.valueOf(product);
    	ProductNode node = tree.getRoot();
    	
    	for (int i = 0; i < sProduct.length(); i++) {
    		final int id = Integer.valueOf(sProduct.substring(i, i + 1));
    		
    		for (final ProductNode child : node.getChildren()) {
    			if (child.getId() == id) {
    				result.append(child.getName()).append(" ");
    				node = child;
    				break;
    			}
    		}
    	}
    	if (result.length() > 0) {
	    	return result.substring(0, result.length() - 1).toString();
    	} else {
    		return result.toString();
    	}
    }
    
    private static ProductTree getTree(ChartNavigationData navigationData, int tso) {
    	for (final ProductTree tree : navigationData.getProductTrees()) {
    		if (tree.getTso() == tso) {
    			return tree;
    		}
    	}
    	return null;
    }

}
