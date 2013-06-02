package de.enwida.web.dao.implementation;

import org.springframework.stereotype.Repository;

import de.enwida.web.dao.interfaces.BaseDao;
import de.enwida.web.dao.interfaces.IDataAutorizationDao;
import de.enwida.web.model.DataAuthorization;

@Repository
public class DataAuthorizationDao extends BaseDao<DataAuthorization> implements IDataAutorizationDao {

	public boolean isAuthorizedByExample(DataAuthorization dataAuthorization) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
