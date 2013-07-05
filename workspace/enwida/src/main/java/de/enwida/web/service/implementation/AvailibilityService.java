package de.enwida.web.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.web.dao.interfaces.IDataAutorizationDao;
import de.enwida.web.dao.interfaces.IDataAvailibilityDao;
import de.enwida.web.model.DataAvailibility;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IAvailibilityService;
import de.enwida.web.utils.CalendarRange;
import de.enwida.web.utils.EnwidaUtils;
import de.enwida.web.utils.ProductRestriction;

@Service
public class AvailibilityService implements IAvailibilityService {

	@Autowired
	private IDataAvailibilityDao dataAvailibilityDao;
	
	@Autowired
	private IDataAutorizationDao dataAuthorizationDao;

	public boolean isAvailable(DataAvailibility dataAvailibility) {
		return dataAvailibilityDao.isAvailableByExample(dataAvailibility);
	}

    public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect, User user) {
    	
    	DataAvailibility dataAvailibility = new DataAvailibility();
    	dataAvailibility.setProduct(productId);
    	dataAvailibility.setTableName(EnwidaUtils.getTableNameByAspect(aspect));
    	dataAvailibility.setTso(tso);
		
		ProductRestriction pR = new ProductRestriction();
		List<DataAvailibility> dataAvailibilityResult = dataAvailibilityDao.getListByExample(dataAvailibility); 
		for (DataAvailibility dA : dataAvailibilityResult) {
			if (dA.getTableName().contains("analysis")) {
				pR.getResolutions().add(EnwidaUtils.getDataResolution(dA.getTableName().split("_")[1]));
				if (EnwidaUtils.getDataResolution(dA.getTableName().split("_")[1]) == DataResolution.QUATER_HOURLY) {
					pR.setTimeRange(new CalendarRange(dA.getTimeFrom(), dA.getTimeTo()));
				}
			} else {
				pR.getResolutions().add(DataResolution.QUATER_HOURLY);
				pR.setTimeRange(new CalendarRange(dA.getTimeFrom(), dA.getTimeTo()));
			}
			
		}
		return pR;
    }
}
