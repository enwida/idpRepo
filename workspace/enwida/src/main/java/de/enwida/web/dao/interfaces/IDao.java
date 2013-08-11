package de.enwida.web.dao.interfaces;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public interface IDao <T> {
        
    Configuration cfg = new Configuration().configure();
    SessionFactory sessionFactory = cfg.buildSessionFactory();

	void setDataSource(DataSource ds);
	
	public T findById(Long id);

	public List<T> findByExample(T obj);

	public T findById(int id);

	public List<T> findAll();
	
	public void create(T entity);

	public T update(T entity);

	public void delete(T entity);

	public void deleteById(long entityId);

	public Long getNextSequenceNumber(String schema, String sequenceName);

	List<T> fetchAll();
}
