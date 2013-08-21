package de.enwida.web.service.interfaces;

import java.util.Locale;

import de.enwida.transport.IDataLine;
import de.enwida.transport.LineRequest;
import de.enwida.web.model.User;

public interface ILineService {

	public IDataLine getLine(LineRequest request, User user, Locale locale) throws Exception;

}
