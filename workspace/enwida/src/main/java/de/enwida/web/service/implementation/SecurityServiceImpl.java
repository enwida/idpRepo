package de.enwida.web.service.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.transport.LineRequest;
import de.enwida.web.dao.interfaces.IDataAvailibilityDao;
import de.enwida.web.dao.interfaces.IRightDao;
import de.enwida.web.db.model.CalendarRange;
import de.enwida.web.model.AuthorizationRequest;
import de.enwida.web.model.Right;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.ISecurityService;
import de.enwida.web.utils.ProductRestriction;

@TransactionConfiguration(defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
@Service("securityService")
public class SecurityServiceImpl implements ISecurityService {

	@Autowired
	private IDataAvailibilityDao dataAvailibilityDao;
	
	@Autowired
	private IRightDao rightDao;

	public boolean isAllowed(Right dataAuthorization) throws Exception {
		return rightDao.isAuthorizedByExample(dataAuthorization);
	}
	
	public boolean isAllowed(LineRequest request, User user) throws Exception {
		final AuthorizationRequest authRequest = new AuthorizationRequest();
		authRequest.setAspect(request.getAspect());
		authRequest.setProduct(request.getProduct());
		authRequest.setResolution(request.getResolution());
		authRequest.setTso(request.getTso());
		authRequest.setUser(user);
		authRequest.setTimeRange(new CalendarRange(request.getStartTime(), request.getEndTime()));
		
		final List<CalendarRange> ranges = rightDao.getAllowedTimeRanges(authRequest);
        final List<CalendarRange> expandedRanges = CalendarRange.getConnectedRanges(ranges);

        for (final CalendarRange range : expandedRanges) {
        	if (range.getFrom().compareTo(request.getStartTime()) <= 0 && range.getTo().compareTo(request.getEndTime()) >= 0) {
        		return true;
        	}
        }
        return false;
	}

    public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect, long role) throws Exception {
    	
        final Right dataAuthorization = new Right();
		dataAuthorization.setRole(new Role(role));
		dataAuthorization.setProduct(productId);
		dataAuthorization.setAspect(aspect.name());
		dataAuthorization.setTso(tso);
		dataAuthorization.setEnabled(true);
		
		final ProductRestriction pR = new ProductRestriction();
		final List<Right> dataAuthorizationResult = rightDao.getListByExample(dataAuthorization); 
		for (Right dA : dataAuthorizationResult) {
			pR.getResolutions().add(DataResolution.valueOf(dA.getResolution().trim()));

			if (pR.getTimeRange() == null) {
				pR.setTimeRange(dA.getTimeRange());
			} else {
				final List<CalendarRange> ranges = Arrays.asList(new CalendarRange[] { pR.getTimeRange(), dA.getTimeRange() });

				// TODO: Or minimum or separate for each aspect
				pR.setTimeRange(CalendarRange.getMaximum(ranges));
			}
		}		
		return pR;
    }
    
    public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect, User user) throws Exception {
        final List<ProductRestriction> restrictions = new ArrayList<ProductRestriction>();
        for (final Role role : user.getAllRoles()) {
            final ProductRestriction restriction = getProductRestriction(productId, tso, aspect, role.getRoleID());
            restrictions.add(restriction);
        }
        return ProductRestriction.combineMaximum(restrictions);
    }

	public void authorizeDataLine(int productId, int tso, Aspect aspect, int role, boolean enable) throws Exception {
		
	    Right dataAuthorization = new Right();
		dataAuthorization.setRole(new Role(role));
		dataAuthorization.setProduct(productId);
		dataAuthorization.setAspect(aspect.name());
		dataAuthorization.setTso(tso);
		dataAuthorization.setEnabled(enable);
		
		
		rightDao.enableLine(dataAuthorization); 
	}	
}
