package de.enwida.web.dao.interfaces;

import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public abstract class AbstractBaseDao<T> implements RowMapper<T> {

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

}
