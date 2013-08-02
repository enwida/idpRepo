package de.enwida.web.service.interfaces;

import de.enwida.transport.Aspect;
import de.enwida.web.model.Right;
import de.enwida.web.model.User;
import de.enwida.web.utils.ProductRestriction;

public interface ISecurityService {
	
	public boolean isAllowed(Right dataAuthorization) throws Exception;
	public void authorizeDataLine(int productId, int tso, Aspect aspect, int role, boolean enable) throws Exception;
	
	public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect, User user) throws Exception;
}
