package de.enwida.web.service.interfaces;

import java.util.List;
import java.util.Locale;

import de.enwida.transport.IDataLine;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.utils.numbers.INumberFormatter;
import de.enwida.web.utils.timestamp.ITimestampFormatter;

public interface ICSVService {
	
	public String createCSV(ChartNavigationData navigationData, List<? extends IDataLine> lines, Locale locale);
	public String createCSV(ChartNavigationData navigationData, List<? extends IDataLine> lines, Locale locale, ITimestampFormatter timestampFormatter, INumberFormatter numberFormatter);

}
