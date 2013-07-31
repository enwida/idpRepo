package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.model.DataAuthorization;

public interface IRightsDao {

    boolean enableDisableAspect(long rightID, boolean enabled);
    public void enableLine(DataAuthorization dataAuthorization);
    public boolean isAuthorizedByExample(DataAuthorization dataAuthorization);
    public List<DataAuthorization> getListByExample(DataAuthorization dataAuthorization);

}
