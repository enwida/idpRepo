package de.enwida.web.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.enwida.transport.Aspect;
import de.enwida.web.dao.interfaces.IDataAutorizationDao;
import de.enwida.web.dao.interfaces.IDataAvailibilityDao;
import de.enwida.web.model.DataAuthorization;
import de.enwida.web.service.interfaces.ISecurityService;
import de.enwida.web.utils.ProductRestriction;

@Service
public class SecurityService implements ISecurityService {

	@Autowired
	private IDataAvailibilityDao dataAvailibilityDao;
	
	@Autowired
	private IDataAutorizationDao dataAuthorizationDao;

	public boolean isAllowed(DataAuthorization dataAuthorization) {
		return dataAuthorizationDao.isAuthorizedByExample(dataAuthorization);
	}

    public ProductRestriction getProductRestriction(int productId, Aspect aspect) {
        // TODO Please implement :)
        return null;
    }	
}
