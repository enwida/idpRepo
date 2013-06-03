package de.enwida.web.service.interfaces;

import de.enwida.chart.Aspect;
import de.enwida.web.model.DataAuthorization;
import de.enwida.web.utils.ProductRestriction;

public interface ISecurityService {
	
	public boolean isAllowed(DataAuthorization dataAuthorization);
	public ProductRestriction getProductRestriction(int productId, Aspect aspect);

}
