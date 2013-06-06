package de.enwida.web.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.enwida.chart.LineManager;
import de.enwida.transport.Aspect;
import de.enwida.transport.IDataLine;
import de.enwida.transport.LineRequest;
import de.enwida.web.dao.interfaces.IAspectsDao;
import de.enwida.web.model.ChartLinesRequest;
import de.enwida.web.model.DataAuthorization;
import de.enwida.web.model.DataAvailibility;
import de.enwida.web.service.interfaces.IAvailibilityService;
import de.enwida.web.service.interfaces.ILineService;
import de.enwida.web.service.interfaces.ISecurityService;

public class LineService implements ILineService {
    
    @Autowired
    private IAspectsDao aspectDao;
    
    @Autowired
    private ISecurityService securityService;
    
    @Autowired
    private IAvailibilityService availibilityService;
    
    @Autowired
    private LineManager lineManager;

    public List<IDataLine> getLines(ChartLinesRequest request) {
        final List<IDataLine> result = new ArrayList<IDataLine>();
        
        for (final LineRequest lineRequest : getLineRequests(request)) {
            if (!securityService.isAllowed(getDataAuthorization(lineRequest))) {
                continue;
            }
            if (!availibilityService.isAvailable(getDataAvailibility(lineRequest))) {
                continue;
            }

            try {
                result.add(lineManager.getLine(lineRequest));
            } catch (Exception e) {
                // Don't add line to list
                e.printStackTrace();
            }
        }
        return result;
    }
    
    private List<LineRequest> getLineRequests(ChartLinesRequest request) {
        final List<LineRequest> result = new ArrayList<LineRequest>();
        final List<Aspect> aspects = aspectDao.getAspects(request.getChartId());
        
        for (final Aspect aspect : aspects) {
            result.add(new LineRequest(
                    aspect,
                    request.getProduct(),
                    request.getTso(),
                    request.getTimeRange().getFrom(),
                    request.getTimeRange().getTo(),
                    request.getResolution(),
                    request.getLocale()
                    ));
        }
        return result;
    }
    
    private DataAuthorization getDataAuthorization(LineRequest request) {
        final DataAuthorization result = new DataAuthorization();

        result.setAspect(request.getAspect().toString());
        result.setProductId(request.getProduct());
        result.setTso(request.getTso());
        result.setTimeFrom(request.getStartTime().getTime());
        result.setTimeTo(request.getEndTime().getTime());
        result.setResolution(request.getResolution().toString());

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
