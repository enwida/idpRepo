package de.enwida.web.service.implementation;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
    
    @Autowired
    private MessageSource messageSource;

    public IDataLine getLine(LineRequest request, User user, Locale locale) throws Exception {
        if (!securityService.isAllowed(request, user)) {
            throw new SecurityException("Access to line denied");
        }
        if (!availibilityService.isAvailable(request)) {
            throw new IllegalAccessError("Data not available");
        }
        final IDataLine result = lineManager.getLine(request);
        applyLocalizations(result, locale);
        return result;
    }
    
    private void applyLocalizations(IDataLine line, Locale locale) {
    	final String title = messageSource.getMessage("de.enwida.chart.aspect." + line.getLineRequest().getAspect().name().toLowerCase() + ".title", null, "", locale);
    	final String unit = messageSource.getMessage("de.enwida.chart.aspect." + line.getLineRequest().getAspect().name().toLowerCase() + ".unit", null, "-", locale);
    	line.setTitle(title);
    	line.setUnit(unit);
    }

}
