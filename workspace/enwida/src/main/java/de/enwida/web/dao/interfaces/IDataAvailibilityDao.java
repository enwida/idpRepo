package de.enwida.web.dao.interfaces;

import de.enwida.web.model.DataAvailibility;

public interface IDataAvailibilityDao {

	public boolean isAvailableByExample(DataAvailibility dataAvailibility);
}
