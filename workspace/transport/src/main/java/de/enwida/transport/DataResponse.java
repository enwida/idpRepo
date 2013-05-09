package de.enwida.transport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataResponse {

	private Exception error;
	private MetaData metaData;
	private List<IDataLine> dataLines;
	
	public DataResponse(Exception error) {
		this.error = error;
	}
	
	public DataResponse(MetaData metaData) {
		this.error = null;;
		this.metaData = metaData;
		this.dataLines = new ArrayList<IDataLine>();
	}
	
	public void throwError() throws Exception {
		if (error != null) {
			throw error;
		}
	}
	
	public MetaData getMetaData() {
		return metaData;
	}
	
	public List<IDataLine> getAllDataLines() { 
		return dataLines;
	}
	
	public IDataLine getDataLine(Product product) {
		final Iterator<IDataLine> iter = dataLines.iterator();
		
		while (iter.hasNext()) {
			final IDataLine dataLine = iter.next();
			if (dataLine.getProduct().equals(product)) {
				return dataLine;
			}
		}
		return null;
	}
	
	public void addDataLine(IDataLine dataLine) {
		dataLines.add(dataLine);
	}
}
