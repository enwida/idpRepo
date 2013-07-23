package de.enwida.web.dao.interfaces;

import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import de.enwida.web.utils.Constants;

public abstract class AbstractBaseDao<T> {

	private Logger logger = Logger.getLogger(getClass());

	private Class<T> modelClass;
	protected JdbcTemplate jdbcTemplate;

	@PersistenceContext(unitName = Constants.ENWIDA_USERS_JPA_CONTEXT)
	protected EntityManager em;

	@SuppressWarnings("unchecked")
	public AbstractBaseDao() {
		this.modelClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public Class<T> getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class<T> modelClass) {
		this.modelClass = modelClass;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public T findById(Long id) {
		String sql = "SELECT * FROM T_"
				+ this.modelClass.getSimpleName().toUpperCase()
				+ " WHERE id = " + id;
		return this.jdbcTemplate.queryForObject(sql, this.modelClass);
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(T obj) {
		String sql = "SELECT * FROM T_"
				+ this.modelClass.getSimpleName().toUpperCase()
				+ " where first_name = :firstName and last_name = :lastName";
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
				obj);
		return (List<T>) this.jdbcTemplate.queryForList(sql, namedParameters);
	}

	public T findById(long id) {
		return em.find(modelClass, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return em.createQuery("from " + modelClass.getName()).getResultList();
	}

	public void create(T entity) {
		em.persist(entity);
	}

	public T update(T entity) {
		return em.merge(entity);
	}

	public void delete(T entity) {
		em.remove(entity);
	}

	public void deleteById(long entityId) {
		T entity = findById(entityId);
		delete(entity);
	}

	/**
	 * This will help in getting the next id to be generated for a sequence
	 * 
	 * @param schema
	 * @param sequenceName
	 * @return
	 */
	public Long getNextSequenceNumber(String schema, String sequenceName) {
		BigInteger nextCounter = null;
		try {
			Query q = em.createNativeQuery("select nextval('" + schema + "."
					+ sequenceName + "')");
			nextCounter = (BigInteger) q.getSingleResult();

			Query q1 = null;

			if (nextCounter.intValue() == 1) {
				// resetting sequence to old value again
				q1 = em.createNativeQuery("select setval('" + schema + "."
						+ sequenceName + "'," + nextCounter.intValue()
						+ ",false)");
			} else {
				q1 = em.createNativeQuery("select setval('" + schema + "."
						+ sequenceName + "'," + (nextCounter.intValue() - 1)
						+ ",true)");
			}
			// dont do anything with result
			q1.getSingleResult();

		} catch (Exception e) {
			logger.error("Unable to update sequence value : ", e);
		}
		return nextCounter.longValue();
	}
}
