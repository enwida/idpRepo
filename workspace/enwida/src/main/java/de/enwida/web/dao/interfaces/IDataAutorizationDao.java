package de.enwida.web.dao.interfaces;

import de.enwida.web.model.DataAuthorization;

public interface IDataAutorizationDao {
	
	public boolean isAuthorizedByExample(DataAuthorization dataAuthorization);
	public DataAuthorization getByExample(DataAuthorization dataAuthorization);

}
