package de.enwida.web.dao.interfaces;

import java.util.Locale;

import de.enwida.web.model.ChartNavigationData;

public interface INavigationDao {
    
    ChartNavigationData getDefaultNavigation(int chartId, Locale locale);

}
