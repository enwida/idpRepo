package de.enwida.web.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.chart.LineManager;
import de.enwida.transport.IDataLine;
import de.enwida.transport.LineRequest;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IAvailibilityService;
import de.enwida.web.service.interfaces.ILineService;
import de.enwida.web.service.interfaces.ISecurityService;

@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
@Service("lineService")
public class LineServiceImpl implements ILineService {
    
    @Autowired
    private ISecurityService securityService;
    
    @Autowired
    private IAvailibilityService availibilityService;
    
    @Autowired
    private LineManager lineManager;

    public IDataLine getLine(LineRequest request, User user) throws Exception {
        if (!securityService.isAllowed(request, user)) {
            throw new SecurityException("Access to line denied");
        }
        if (!availibilityService.isAvailable(request)) {
            throw new IllegalAccessError("Data not available");
        }
        return lineManager.getLine(request);
    }

}
