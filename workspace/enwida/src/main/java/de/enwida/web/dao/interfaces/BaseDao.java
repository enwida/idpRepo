package de.enwida.web.dao.interfaces;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public abstract class BaseDao<T> implements IDao<T> {

	private Class<T> modelClass;
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	public BaseDao() {
		this.modelClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public Class<T> getModelClass() {
		return modelClass;
	}
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
	}
	
	public T findById(Long id) {
		String sql = "SELECT * FROM T_" + this.modelClass.getSimpleName().toUpperCase() + " WHERE id = :id";
		Map namedParameters = Collections.singletonMap("id", id);
		return (T) this.jdbcTemplate.queryForObject(sql, namedParameters, this.modelClass);
	}

	public List<T> findByExample(T obj) {
		String sql = "SELECT * FROM T_" + this.modelClass.getSimpleName().toUpperCase() + " where first_name = :firstName and last_name = :lastName";
	    SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(obj);
	    return (List<T>) this.jdbcTemplate.queryForList(sql, namedParameters);
	}

	public List<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(T obj) {
		// TODO Auto-generated method stub
		
	}

	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	public T save(T obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public T update(T obj) {
		// TODO Auto-generated method stub
		return null;
	}
		
}
