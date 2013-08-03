package de.enwida.web.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;

import de.enwida.chart.LineManager;
import de.enwida.transport.IDataLine;
import de.enwida.transport.LineRequest;
import de.enwida.web.model.DataAvailibility;
import de.enwida.web.model.Right;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IAvailibilityService;
import de.enwida.web.service.interfaces.ILineService;
import de.enwida.web.service.interfaces.ISecurityService;

public class LineServiceImpl implements ILineService {
    
    @Autowired
    private ISecurityService securityService;
    
    @Autowired
    private IAvailibilityService availibilityService;
    
    @Autowired
    private LineManager lineManager;

    public IDataLine getLine(LineRequest request, User user) throws Exception {
        if (!isAllowed(request, user)) {
            throw new SecurityException("Access to line denied");
        }
        if (!availibilityService.isAvailable(getDataAvailibility(request))) {
            throw new IllegalAccessError("Data not available");
        }
        return lineManager.getLine(request);
    }
    
    private boolean isAllowed(LineRequest lineRequest, User user) throws Exception {
        final Right authorization = getDataAuthorization(lineRequest);

        for (final Role role : user.getRoles()) {
            authorization.setRoleID(role.getRoleID());
            if (securityService.isAllowed(authorization)) {
                return true;
            }
        }
        return false;
    }
    
    private Right getDataAuthorization(LineRequest request) {
        final Right result = new Right();

        result.setAspect(request.getAspect().toString());
        result.setProduct(request.getProduct());
        result.setTso(request.getTso());
        result.setTimeFrom(request.getStartTime().getTime());
        result.setTimeTo(request.getEndTime().getTime());
        result.setResolution(request.getResolution().toString());
        result.setEnabled(true);

        return result;
    }
    
    private DataAvailibility getDataAvailibility(LineRequest request) {
        final DataAvailibility result = new DataAvailibility();
        
        result.setProduct(request.getProduct());
        result.setTimeFrom(request.getStartTime().getTime());
        result.setTimeTo(request.getEndTime().getTime());
        // FIXME: Get table name from aspect/resolution
        result.setTableName("");
        
        return result;
    }

}