package de.enwida.web.dao.implementation;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.enwida.web.controller.AdminController;
import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IRightsDao;
import de.enwida.web.model.Role;

@Repository
public class RightsDaoImpl extends AbstractBaseDao<Role> implements IRightsDao {
	
	@Autowired
	private DataSource datasource;
	
	
    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);
	
	
	@Override
	public String getDbTableName() {
	    return "users.rights";
	}
	
    @Override
    public boolean enableDisableAspect(long rightID, boolean enabled) {
        String sql = "UPDATE users.rights SET enabled=? WHERE right_id=?";
        this.jdbcTemplate.update(sql,enabled,rightID);
        return true;
    }
}
