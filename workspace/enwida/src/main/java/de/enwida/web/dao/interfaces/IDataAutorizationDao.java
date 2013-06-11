package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.model.DataAuthorization;

public interface IDataAutorizationDao {
	
	public boolean isAuthorizedByExample(DataAuthorization dataAuthorization);
	public DataAuthorization getByExample(DataAuthorization dataAuthorization);
	public List<DataAuthorization> getListByExample(DataAuthorization dataAuthorization);

}
