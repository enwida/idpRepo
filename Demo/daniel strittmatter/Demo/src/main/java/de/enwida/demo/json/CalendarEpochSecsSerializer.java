package de.enwida.demo.json;

import java.io.IOException;
import java.util.Calendar;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class CalendarEpochSecsSerializer extends JsonSerializer<Calendar> {

	@Override
	public void serialize(Calendar value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeNumber(value.getTimeInMillis() / 1000l);
	}

}
