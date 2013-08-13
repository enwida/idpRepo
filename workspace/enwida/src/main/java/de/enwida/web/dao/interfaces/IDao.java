package de.enwida.web.dao.interfaces;

import java.util.List;

import javax.sql.DataSource;

public interface IDao <T> {

	void setDataSource(DataSource ds);

	public List<T> findByExample(T obj);

    public T fetchById(long id);

    public T fetchByName(String name);
	
	public void create(T entity);
	
	public void create(T entity,boolean flushImmediate);

	public T update(T entity);
	
	public T update(T entity, boolean flushImmediate);

	public void delete(T entity);
	
	public void refresh(T entity);

	public void deleteById(long entityId);

	public Long getNextSequenceNumber(String schema, String sequenceName);

	List<T> fetchAll();
	
	public void refresh(T entity);
}
