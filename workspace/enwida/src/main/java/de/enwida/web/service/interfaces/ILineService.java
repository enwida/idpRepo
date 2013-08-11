package de.enwida.web.service.interfaces;

import de.enwida.transport.IDataLine;
import de.enwida.transport.LineRequest;
import de.enwida.web.model.User;

public interface ILineService {

	public IDataLine getLine(LineRequest request, User user) throws Exception;

}
