package de.enwida.web.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.web.dao.interfaces.IDataAvailibilityDao;
import de.enwida.web.dao.interfaces.IRightDao;
import de.enwida.web.db.model.CalendarRange;
import de.enwida.web.model.DataAvailibility;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IAvailibilityService;
import de.enwida.web.utils.EnwidaUtils;
import de.enwida.web.utils.ProductRestriction;

@TransactionConfiguration(defaultRollback = true)
@Service("availabilityService")
@Transactional
public class AvailibilityServiceImpl implements IAvailibilityService {

	@Autowired
	private IDataAvailibilityDao dataAvailibilityDao;
	
	@Autowired
	private IRightDao rightDao;

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
				// Add resolution
				final DataResolution resolution = EnwidaUtils.getDataResolution(dA.getTableName().split("_")[1]);
				if (resolution != null) {
					pR.getResolutions().add(resolution);
				}
				
				// Set time range there is none
				if (pR.getTimeRange() == null) {
					pR.setTimeRange(new CalendarRange(dA.getTimeFrom(), dA.getTimeTo()));
				}
				// Set the most accurate time range, if possible
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
