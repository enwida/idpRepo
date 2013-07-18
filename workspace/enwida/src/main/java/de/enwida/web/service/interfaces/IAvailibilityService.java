package de.enwida.web.service.interfaces;

import de.enwida.transport.Aspect;
import de.enwida.web.model.DataAvailibility;
import de.enwida.web.model.User;
import de.enwida.web.utils.ProductRestriction;

public interface IAvailibilityService {

	public boolean isAvailable(DataAvailibility dataAvailibility);

	public ProductRestriction getProductRestriction(int productId, int tso,
			Aspect aspect, User user);

}
