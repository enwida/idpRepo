package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.db.model.CalendarRange;
import de.enwida.web.model.AuthorizationRequest;
import de.enwida.web.model.Right;

public interface IRightDao extends IDao<Right> {

	Right addRight(Right right) throws Exception;
    boolean enableDisableAspect(long rightID, long roleID, boolean enabled)throws Exception;
    public void enableLine(Right dataAuthorization)throws Exception;
    public boolean isAuthorizedByExample(Right dataAuthorization)throws Exception;
    public List<CalendarRange> getAllowedTimeRanges(AuthorizationRequest request) throws Exception;
    public List<Right> getListByExample(Right dataAuthorization)throws Exception;
    List<Right> getRoleAspects(long roleID);

}
