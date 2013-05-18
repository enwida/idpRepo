package de.enwida.web.dao.implementation;

import org.springframework.stereotype.Repository;

import de.enwida.web.dao.interfaces.BaseDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.model.User;

@Repository
public class UserDao extends BaseDao<User> implements IUserDao {

}
