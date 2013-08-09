package de.enwida.web.service.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.web.dao.interfaces.IDataAvailibilityDao;
import de.enwida.web.dao.interfaces.IRightDao;
import de.enwida.web.model.Right;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.ISecurityService;
import de.enwida.web.utils.CalendarRange;
import de.enwida.web.utils.ProductRestriction;

@TransactionConfiguration(defaultRollback = true)
@Transactional
@Service("securityService")
public class SecurityServiceImpl implements ISecurityService {

	@Autowired
	private IDataAvailibilityDao dataAvailibilityDao;
	
	@Autowired
	private IRightDao rightDao;

	public boolean isAllowed(Right dataAuthorization) throws Exception {
		return rightDao.isAuthorizedByExample(dataAuthorization);
	}

    public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect, int role) throws Exception {
    	
        final Right dataAuthorization = new Right();
		dataAuthorization.setRoleID(role);
		dataAuthorization.setProduct(productId);
		dataAuthorization.setAspect(aspect.name());
		dataAuthorization.setTso(tso);
		dataAuthorization.setEnabled(true);
		
		final ProductRestriction pR = new ProductRestriction();
		final List<Right> dataAuthorizationResult = rightDao.getListByExample(dataAuthorization); 
		for (Right dA : dataAuthorizationResult) {
			pR.getResolutions().add(DataResolution.valueOf(dA.getResolution().trim()));

			final CalendarRange timeRange = new CalendarRange(dA.getTimeFrom(), dA.getTimeTo());
			if (pR.getTimeRange() == null) {
				pR.setTimeRange(timeRange);
			} else {
				final List<CalendarRange> ranges = Arrays.asList(new CalendarRange[] { pR.getTimeRange(), timeRange });

				// TODO: Or minimum or separate for each aspect
				pR.setTimeRange(CalendarRange.getMaximum(ranges));
			}
		}		
		return pR;
    }
    
    public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect, User user) throws Exception {
        final List<ProductRestriction> restrictions = new ArrayList<ProductRestriction>();
        for (final Role role : user.getRoles()) {
            final ProductRestriction restriction = getProductRestriction(productId, tso, aspect, (int) role.getRoleID());
            restrictions.add(restriction);
        }
        return ProductRestriction.combineMaximum(restrictions);
    }

	public void authorizeDataLine(int productId, int tso, Aspect aspect, int role, boolean enable) throws Exception {
		
	    Right dataAuthorization = new Right();
		dataAuthorization.setRoleID(role);
		dataAuthorization.setProduct(productId);
		dataAuthorization.setAspect(aspect.name());
		dataAuthorization.setTso(tso);
		dataAuthorization.setEnabled(enable);
		
		
		rightDao.enableLine(dataAuthorization); 
	}	
}
