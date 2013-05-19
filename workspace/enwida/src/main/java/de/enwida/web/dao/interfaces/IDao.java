package de.enwida.web.dao.interfaces;

import java.util.List;

import javax.sql.DataSource;

public interface IDao <T> {

	void setDataSource(DataSource ds);
	
	T findById(Long id);
	List<T> findByExample(T obj);
	List<T> findAll();
	
	void delete(T obj);	
	void deleteAll();
	
	T save(T obj);	
	T update(T obj);	
}
