package de.enwida.web.dao.interfaces;

import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.management.Query;
import javax.sql.DataSource;
import javax.swing.tree.RowMapper;

import de.enwida.web.utils.Constants;

public abstract class AbstractBaseDao<T> implements RowMapper<T>, IDao<T> {

    private Class<T> modelClass;
    protected JdbcTemplate jdbcTemplate;
    private String dbTableName;

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

    public T findById(Long id) throws Exception{
        String sql = "SELECT * FROM " + this.getDbTableName() + " WHERE id = "
                + id;
        return this.jdbcTemplate.queryForObject(sql, this.modelClass);
    }
    
    public void save(String sql,T obj)throws Exception{
        this.jdbcTemplate.update(sql,obj);
    }
    
    public T deleteById(Long id)throws Exception {
        String sql = "DELETE FROM " + this.getDbTableName() + " WHERE id = "
                + id;
        return this.jdbcTemplate.queryForObject(sql, this.modelClass);
    }

    public List<T> findAll() throws Exception{
        String sql = "SELECT * FROM " + this.getDbTableName();
        return this.jdbcTemplate.query(sql,this);
    }
    
    public List<T> findByColumn(String columnName,int columnValue)throws Exception {
        String sql = "SELECT * FROM " + this.getDbTableName()+ " WHERE "+columnName+"=?";
        return this.jdbcTemplate.query(sql,new Object[]{columnValue},this);
    }
    
    public List<T> findByColumn(String columnName,String columnValue)throws Exception {
        String sql = "SELECT * FROM " + this.getDbTableName()+ " WHERE "+columnName+"=?";
        return this.jdbcTemplate.query(sql,new Object[]{columnValue},this);
    }
    

    public List<T> findByExample(T obj)throws Exception {
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



	public T findById(int id) {
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
		entity = em.merge(entity);
		return entity;
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
			logger.error("Unable to reset sequence value : ", e);
		}
		return nextCounter.longValue();
	}
}