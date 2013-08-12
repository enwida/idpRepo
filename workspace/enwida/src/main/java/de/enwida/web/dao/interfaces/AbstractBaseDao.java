package de.enwida.web.dao.interfaces;

import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import de.enwida.web.utils.Constants;

public abstract class AbstractBaseDao<T> implements IDao<T> {

    private Class<T> modelClass;
    protected JdbcTemplate jdbcTemplate;

	private Logger logger = Logger.getLogger(getClass());

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

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<T> findByExample(T obj) {
        String sql = "SELECT * FROM users."
                + this.modelClass.getSimpleName().toUpperCase()
                + " where first_name = :firstName and last_name = :lastName";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                obj);
        return (List<T>) this.jdbcTemplate.queryForList(sql, namedParameters);
    }

	@Override
	public T fetchById(long id) {
		return em.find(modelClass, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> fetchAll() {
		return em.createQuery("from " + modelClass.getName()).getResultList();
	}

	public void create(T entity) {
		em.persist(entity);
	}

	public T update(T entity) {
		entity = em.merge(entity);
		return entity;
	}

	public void delete(T entity) {
		em.remove(entity);
	}

	public void deleteById(long entityId) {
		T entity = fetchById(entityId);
		delete(entity);
	}
	
	 /**
     * If we can use same structure in every model classes, we can query by their name  like this
     *  
     * @param name Name
     * @return
     */
    public T fetchByName(String name) {
        T entity = null;
        TypedQuery<T> typedQuery = em.createQuery( "from " + this.modelClass.getSimpleName()+" WHERE name= :name", modelClass);
		try {
			entity = typedQuery.setParameter("name", name).getSingleResult();
		} catch (NoResultException noresult) {
			// if there is no result
			logger.error("No data found for " + modelClass.getSimpleName()
					+ " with name : " + name);
		} catch (NonUniqueResultException notUnique) {
			// if more than one result
			logger.info("More than one record found for "
					+ modelClass.getSimpleName() + " with name : " + name);
		}
        return entity;
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
			logger.error("Unable to reset sequence value : ", e);
		}
		return nextCounter.longValue();
	}
}