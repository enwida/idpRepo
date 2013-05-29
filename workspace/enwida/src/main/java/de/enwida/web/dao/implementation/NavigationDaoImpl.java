package de.enwida.web.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import de.enwida.web.dao.interfaces.BaseDao;
import de.enwida.web.dao.interfaces.INavigationDao;
import de.enwida.web.model.User;

@Repository
public class NavigationDaoImpl extends BaseDao<User> implements INavigationDao {
	@Autowired
	private DriverManagerDataSource datasource;
}
