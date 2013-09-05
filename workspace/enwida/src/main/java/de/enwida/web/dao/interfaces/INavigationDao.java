package de.enwida.web.dao.interfaces;

import java.util.Set;

import de.enwida.web.db.model.NavigationSettings;

public interface INavigationDao extends IDao<NavigationSettings> {
    
	Set<NavigationSettings> getNavigationSettingsByUserID(int userId);
	Set<NavigationSettings> getNavigationSettingsByClientID(String clientId);

	void saveUserNavigationSettings(NavigationSettings navigationSettings);

}
