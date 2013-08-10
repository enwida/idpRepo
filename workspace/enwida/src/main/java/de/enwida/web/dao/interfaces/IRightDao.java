package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.model.Right;

public interface IRightDao extends IDao<Right> {

    void addRight(Right right) throws Exception;
    boolean enableDisableAspect(long rightID, boolean enabled)throws Exception;
    public void enableLine(Right dataAuthorization)throws Exception;
    public boolean isAuthorizedByExample(Right dataAuthorization)throws Exception;
    public List<Right> getListByExample(Right dataAuthorization)throws Exception;
    List<Right> getAllAspects(long roleID) throws Exception;

}
