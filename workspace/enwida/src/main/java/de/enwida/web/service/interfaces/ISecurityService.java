package de.enwida.web.service.interfaces;

import de.enwida.transport.Aspect;
import de.enwida.web.model.DataAuthorization;
import de.enwida.web.model.User;
import de.enwida.web.utils.ProductRestriction;

public interface ISecurityService {
	
	public boolean isAllowed(DataAuthorization dataAuthorization);
	public void authorizeDataLine(int productId, int tso, Aspect aspect, int role, boolean enable);
	
	public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect, User user);
}
