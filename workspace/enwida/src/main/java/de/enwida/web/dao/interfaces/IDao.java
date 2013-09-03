package de.enwida.web.dao.interfaces;

import java.util.List;

import javax.sql.DataSource;

public interface IDao <T> {

	public void setDataSource(DataSource ds);

	public List<T> findByExample(T obj);

    public T fetchById(long id);

    public T fetchByName(String name);
	
	public void create(T entity) throws Exception;
	
	public void create(T entity,boolean flushImmediate) throws Exception;

	public T update(T entity) throws Exception;
	
	public T update(T entity, boolean flushImmediate) throws Exception;

	public void delete(T entity) throws Exception;
	
	public void delete(T entity, boolean flushImmediate) throws Exception;

	public void refresh(T entity) throws Exception;

	public void deleteById(long entityId) throws Exception;

	public Long getNextSequenceNumber(String schema, String sequenceName) throws Exception;

	public List<T> fetchAll();
	
	public void flush();
}
