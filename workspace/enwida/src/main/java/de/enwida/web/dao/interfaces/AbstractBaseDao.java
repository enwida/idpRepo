package de.enwida.web.dao.interfaces;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public abstract class AbstractBaseDao<T> {

    private Class<T> modelClass;
    protected JdbcTemplate jdbcTemplate;
    private String dbTableName;

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

    public T findById(Long id) {
        String sql = "SELECT * FROM " + this.getDbTableName() + " WHERE id = "
                + id;
        return this.jdbcTemplate.queryForObject(sql, this.modelClass);
    }
    
    public void save(String sql,T obj){
        this.jdbcTemplate.update(sql,obj);
    }
    
    public T deleteById(Long id) {
        String sql = "DELETE FROM " + this.getDbTableName() + " WHERE id = "
                + id;
        return this.jdbcTemplate.queryForObject(sql, this.modelClass);
    }

    public List findAll() {
        String sql = "SELECT * FROM " + this.getDbTableName();
        return this.jdbcTemplate.queryForList(sql);
    }

    public List<T> findByExample(T obj) {
        String sql = "SELECT * FROM users."
                + this.modelClass.getSimpleName().toUpperCase()
                + " where first_name = :firstName and last_name = :lastName";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                obj);
        return (List<T>) this.jdbcTemplate.queryForList(sql, namedParameters);
    }

    public String getDbTableName() {
        return dbTableName;
    }

    public void setDbTableName(String dbTableName) {
        this.dbTableName = dbTableName;
    }

}
