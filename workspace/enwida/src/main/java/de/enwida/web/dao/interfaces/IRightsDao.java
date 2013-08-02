package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.model.DataAuthorization;

public interface IRightsDao {

    boolean enableDisableAspect(long rightID, boolean enabled)throws Exception;
    public void enableLine(DataAuthorization dataAuthorization)throws Exception;
    public boolean isAuthorizedByExample(DataAuthorization dataAuthorization)throws Exception;
    public List<DataAuthorization> getListByExample(DataAuthorization dataAuthorization)throws Exception;

}
