package de.enwida.web.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.enwida.transport.Aspect;
import de.enwida.web.dao.interfaces.IDataAutorizationDao;
import de.enwida.web.dao.interfaces.IDataAvailibilityDao;
import de.enwida.web.model.DataAuthorization;
import de.enwida.web.model.User;
import de.enwida.web.model.UserRole;
import de.enwida.web.service.interfaces.ISecurityService;
import de.enwida.web.utils.CalendarRange;
import de.enwida.web.utils.EnwidaUtils;
import de.enwida.web.utils.ProductRestriction;

@Service
public class SecurityServiceImpl implements ISecurityService {

	@Autowired
	private IDataAvailibilityDao dataAvailibilityDao;
	
	@Autowired
	private IDataAutorizationDao dataAuthorizationDao;

	public boolean isAllowed(DataAuthorization dataAuthorization) {
		return dataAuthorizationDao.isAuthorizedByExample(dataAuthorization);
	}

    public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect, int role) {
    	
    	DataAuthorization dataAuthorization = new DataAuthorization();
		dataAuthorization.setRole(role);
		dataAuthorization.setProductId(productId);
		dataAuthorization.setAspect(aspect.name());
		dataAuthorization.setTso(tso);
		dataAuthorization.setEnabled(true);
		
		ProductRestriction pR = new ProductRestriction();
		List<DataAuthorization> dataAuthorizationResult = dataAuthorizationDao.getListByExample(dataAuthorization); 
		for (DataAuthorization dA : dataAuthorizationResult) {
			pR.getResolutions().add(EnwidaUtils.getDataResolution(dA.getResolution()));
			pR.setTimeRange(new CalendarRange(dA.getTimeFrom(), dA.getTimeTo()));
		}		
		return pR;
    }
    
    public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect, User user) {
        final List<ProductRestriction> restrictions = new ArrayList<>();
        for (final UserRole role : user.getRoles()) {
            final ProductRestriction restriction = getProductRestriction(productId, tso, aspect, (int) role.getRoleID());
            restrictions.add(restriction);
        }
        return ProductRestriction.combineMaximum(restrictions);
    }

	public void authorizeDataLine(int productId, int tso, Aspect aspect, int role, boolean enable) {
		
		DataAuthorization dataAuthorization = new DataAuthorization();
		dataAuthorization.setRole(role);
		dataAuthorization.setProductId(productId);
		dataAuthorization.setAspect(aspect.name());
		dataAuthorization.setTso(tso);
		dataAuthorization.setEnabled(enable);
		
		dataAuthorizationDao.enableLine(dataAuthorization); 
	}	
}
