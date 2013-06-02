package de.enwida.web.service.interfaces;

import de.enwida.web.model.DataAuthorization;

public interface ISecurityService {
	
	public boolean isAllowed(DataAuthorization dataAuthorization);

}
