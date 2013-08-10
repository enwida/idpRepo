package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.model.DataAvailibility;

public interface IDataAvailibilityDao extends IDao<DataAvailibility> {

	public boolean isAvailableByExample(DataAvailibility dataAvailibility);
	public DataAvailibility getByExample(DataAvailibility dataAvailibility);
	public List<DataAvailibility> getListByExample(DataAvailibility dataAvailibility);
}
