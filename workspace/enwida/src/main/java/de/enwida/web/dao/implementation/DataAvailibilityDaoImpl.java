package de.enwida.web.dao.implementation;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IDataAvailibilityDao;
import de.enwida.web.model.DataAvailibility;

@Repository
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
public class DataAvailibilityDaoImpl extends AbstractBaseDao<DataAvailibility> implements IDataAvailibilityDao {

	public boolean isAvailableByExample(DataAvailibility dataAvailibility) {
		
		String SELECT_QUERY = "SELECT COUNT(*) FROM availability WHERE product = ? AND timefrom <= ? AND timeto >= ? AND tablename SIMILAR TO ?;";
		
		Object[] param = new Object[4];
		param[0] = dataAvailibility.getProduct();
		param[1] = new java.sql.Timestamp(dataAvailibility.getTimeFrom().getTime());
		param[2] = new java.sql.Timestamp(dataAvailibility.getTimeTo().getTime());
		param[3] = "%" + dataAvailibility.getTableName() + "%";
		
		int count = jdbcTemplate.queryForInt(SELECT_QUERY, param);
		return count > 0 ? true : false;
	}

	public DataAvailibility getByExample(DataAvailibility dataAvailibility) {
        TypedQuery<DataAvailibility> typedQuery = em.createQuery( "FROM availability WHERE product = ? AND timefrom <= ? AND timeto >= ? AND tablename SIMILAR TO ?",
                DataAvailibility.class);
        typedQuery.setParameter("product", dataAvailibility.getProduct());
        typedQuery.setParameter("timefrom", dataAvailibility.getTimeFrom());
        typedQuery.setParameter("timeto", dataAvailibility.getTimeTo());
        typedQuery.setParameter("tablename", dataAvailibility.getTableName());
		
		return typedQuery.getSingleResult();
	}


	public List<DataAvailibility> getListByExample(DataAvailibility dataAvailibility) {
        TypedQuery<DataAvailibility> typedQuery = em.createQuery( " FROM availability WHERE product = ? AND tso = ? AND tablename SIMILAR TO ?",
                DataAvailibility.class);
        typedQuery.setParameter("product", dataAvailibility.getProduct());
        typedQuery.setParameter("tso", dataAvailibility.getTso());
        typedQuery.setParameter("tablename", dataAvailibility.getTableName());
		return typedQuery.getResultList();	
	}

}
