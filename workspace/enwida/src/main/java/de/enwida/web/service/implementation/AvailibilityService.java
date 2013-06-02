package de.enwida.web.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.enwida.web.dao.interfaces.IDataAutorizationDao;
import de.enwida.web.dao.interfaces.IDataAvailibilityDao;
import de.enwida.web.model.DataAvailibility;
import de.enwida.web.service.interfaces.IAvailibilityService;

@Service
public class AvailibilityService implements IAvailibilityService {

	@Autowired
	private IDataAvailibilityDao dataAvailibilityDao;
	
	@Autowired
	private IDataAutorizationDao dataAuthorizationDao;

	public boolean isAvailable(DataAvailibility dataAvailibility) {
		return dataAvailibilityDao.isAvailableByExample(dataAvailibility);
	}
}
