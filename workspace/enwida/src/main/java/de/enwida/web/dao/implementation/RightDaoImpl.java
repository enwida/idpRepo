package de.enwida.web.dao.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.transport.Aspect;
import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IRightDao;
import de.enwida.web.db.model.CalendarRange;
import de.enwida.web.model.AuthorizationRequest;
import de.enwida.web.model.Right;

@Repository
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
public class RightDaoImpl extends AbstractBaseDao<Right> implements IRightDao {
	
	/**
	 * Enables or disables the Aspects in the database.So that right won't see that aspect 
	 */
    @Override
    public boolean enableDisableAspect(long rightID, boolean enabled) throws Exception{
		Right right = fetchById(rightID);
		right.setEnabled(enabled);
		update(right);
        return true;
    }

    public boolean isAuthorizedByExample(Right dataAuthorization)
            throws Exception {

        TypedQuery<Right> typedQuery = em.createQuery("from " + Right.class.getName()
                                + "  WHERE role_id = ? AND tso = ? AND product = ? AND aspect_id = ? AND resolution = ? AND time1 <= ? AND time2 >= ? AND enabled = ?",
                        Right.class);
        typedQuery.setParameter("role_id", dataAuthorization.getRole());
        typedQuery.setParameter("tso", dataAuthorization.getTso());
        typedQuery.setParameter("product", dataAuthorization.getProduct());
        typedQuery.setParameter("aspect_id",
                Aspect.valueOf(dataAuthorization.getAspect()).ordinal());
        typedQuery
                .setParameter("resolution", dataAuthorization.getResolution());
        typedQuery.setParameter("time1", dataAuthorization.getTimeRange()
                .getFrom());
        typedQuery.setParameter("time2", dataAuthorization.getTimeRange()
                .getTo());
        typedQuery.setParameter("enabled", dataAuthorization.isEnabled());

        return typedQuery.getResultList().size() > 0 ? true : false;

    }
    
    @Override    public List<CalendarRange> getAllowedTimeRanges(AuthorizationRequest request) throws Exception {

        TypedQuery<Right> typedQuery = em.createQuery( "from "+ Right.class.getName()
                + "  WHERE role_id in ? AND tso = ? AND product = ? AND aspect_id = ? AND resolution = ? AND enabled = true",
        Right.class);
        typedQuery.setParameter("role_id", request.getUser().getRoles().toArray());
        typedQuery.setParameter("tso",request.getTso());
        typedQuery.setParameter("product", request.getProduct());
        typedQuery.setParameter("aspect_id", request.getAspect());
        typedQuery.setParameter("resolution", request.getResolution().name());

        final List<CalendarRange> ranges = new ArrayList<>();
        
        for (Right right : typedQuery.getResultList()) {
            ranges.add(right.getTimeRange());
        }

        return ranges;
    }
    
	@Override
	public Right addRight(Right right) {

		Right exist = fetchById(right.getRightID());
		if (exist == null) {
			// create or refresh
			create(right);
		} else {
			right.setRightID(exist.getRightID());
			right = update(right);
		}
		return right;
    }
    
    @Override
    public void removeRight(Right right) throws Exception {
        delete(right);
    }

    public List<Right> getListByExample(Right dataAuthorization)
            throws Exception {
        TypedQuery<Right> typedQuery = em.createQuery( "from "+ Right.class.getName()
                                + "  WHERE role_id = ? AND tso = ? AND product = ? AND aspect_id = ? AND enabled = ?;",
                        Right.class);
        typedQuery.setParameter("groupID", dataAuthorization.getRole());
        typedQuery.setParameter("tso", dataAuthorization.getTso());
        typedQuery.setParameter("product", dataAuthorization.getProduct());
        typedQuery.setParameter("aspect_id", dataAuthorization.getAspect());
        typedQuery.setParameter("enabled", dataAuthorization.isEnabled());

        return typedQuery.getResultList();
    }

    public void enableLine(Right dataAuthorization) throws Exception{
        update(dataAuthorization);
    }

    @Override
    public List<Right> getAllAspects(long roleID) {
        TypedQuery<Right> typedQuery = em.createQuery( "from "+ Right.class.getName()
                + "  WHERE role_id = ? AND enabled = TRUE;", Right.class);
            typedQuery.setParameter("roleID", roleID);
            return typedQuery.getResultList();
    }
}
