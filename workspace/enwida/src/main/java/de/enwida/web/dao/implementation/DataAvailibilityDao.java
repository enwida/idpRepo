package de.enwida.web.dao.implementation;

import org.springframework.stereotype.Repository;

import de.enwida.web.dao.interfaces.BaseDao;
import de.enwida.web.dao.interfaces.IDataAvailibilityDao;
import de.enwida.web.model.DataAvailibility;

@Repository
public class DataAvailibilityDao extends BaseDao<DataAvailibility> implements IDataAvailibilityDao {

	public boolean isAvailableByExample(DataAvailibility dataAvailibility) {
		
		String SELECT_QUERY = "SELECT COUNT(*) FROM availibility WHERE product = ? AND timefrom <= ? AND timeto >= ? AND tablename LIKE ";
		
		return false;
	}

}
