package de.enwida.web.utils;

import java.text.DateFormat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperFactory {
	
	private DateFormat dateFormat;
	private ObjectMapper cachedObjectMapper;
	
	public ObjectMapperFactory(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public synchronized ObjectMapper create() {
		if (cachedObjectMapper == null) {
			cachedObjectMapper = new ObjectMapper();
			cachedObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			cachedObjectMapper.setDateFormat(dateFormat);
		}
		return cachedObjectMapper;
	}
	
	public DateFormat getDateFormat() { 
		return dateFormat;
	}

}
