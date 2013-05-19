package de.enwida.transport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataResponse {

	private Exception error;
	private ChartMetaData metaData;
	private List<IDataLine> dataLines;
	
	public DataResponse(Exception error) {
		this.error = error;
	}
	
	public DataResponse(ChartMetaData metaData, List<? extends IDataLine> lines) {
		this.error = null;;
		this.metaData = metaData;
		this.dataLines = new ArrayList<IDataLine>(lines);
	}
	
	public DataResponse(ChartMetaData metaData, IDataLine line) {
		this(metaData, Arrays.asList(new IDataLine[] { line }));
	}
	
	public void throwError() throws Exception {
		if (error != null) {
			throw error;
		}
	}
	
	public ChartMetaData getMetaData() {
		return metaData;
	}
	
	public List<IDataLine> getAllDataLines() { 
		return dataLines;
	}
	
	public void addDataLine(IDataLine dataLine) {
		dataLines.add(dataLine);
	}
}
