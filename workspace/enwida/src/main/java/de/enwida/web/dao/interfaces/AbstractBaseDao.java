package de.enwida.web.dao.interfaces;

import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import de.enwida.web.utils.Constants;

public abstract class AbstractBaseDao<T> implements IDao<T>, RowMapper<T> {

    private Class<T> modelClass;
    protected JdbcTemplate jdbcTemplate;
    private String dbTableName;

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
	public T findById(Long id) {
        String sql = "SELECT * FROM " + this.getDbTableName() + " WHERE id = "
                + id;
        return this.jdbcTemplate.queryForObject(sql, this.modelClass);
    }
    
	@Deprecated
	public void save(String sql, T obj) {
        this.jdbcTemplate.update(sql,obj);
    }
    
	@Deprecated
	public T deleteById(Long id) {
        String sql = "DELETE FROM " + this.getDbTableName() + " WHERE id = "
                + id;
        return this.jdbcTemplate.queryForObject(sql, this.modelClass);
    }

	@Deprecated
	public List<T> findAll() {
        String sql = "SELECT * FROM " + this.getDbTableName();
        return this.jdbcTemplate.query(sql,this);
    }
    
	@Deprecated
	public List<T> findByColumn(String columnName, int columnValue) {
        String sql = "SELECT * FROM " + this.getDbTableName()+ " WHERE "+columnName+"=?";
        return this.jdbcTemplate.query(sql,new Object[]{columnValue},this);
    }
    
	@Deprecated
	public List<T> findByColumn(String columnName, String columnValue) {
        String sql = "SELECT * FROM " + this.getDbTableName()+ " WHERE "+columnName+"=?";
        return this.jdbcTemplate.query(sql,new Object[]{columnValue},this);
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
    
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }

    public String getDbTableName() {
        return dbTableName;
    }

    public void setDbTableName(String dbTableName) {
        this.dbTableName = dbTableName;
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