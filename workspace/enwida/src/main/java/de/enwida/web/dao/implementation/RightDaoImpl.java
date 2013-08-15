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
import de.enwida.web.model.Role;

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
                                + "  WHERE role_id = :role_id AND tso = :tso AND product = :product AND aspect = :aspect AND resolution = :resolution AND time1 <= :time1 AND time1 >= :time2 AND enabled = :enabled",
                        Right.class);

        typedQuery.setParameter("role_id", dataAuthorization.getRole().getRoleID());
        typedQuery.setParameter("tso", dataAuthorization.getTso());
        typedQuery.setParameter("product", dataAuthorization.getProduct());
        typedQuery.setParameter("aspect", dataAuthorization.getAspect());
        typedQuery.setParameter("resolution", dataAuthorization.getResolution());
        typedQuery.setParameter("time1", dataAuthorization.getTimeRange().getFrom());
        typedQuery.setParameter("time2", dataAuthorization.getTimeRange().getTo()); 
        typedQuery.setParameter("enabled", dataAuthorization.isEnabled());

        return typedQuery.getResultList().size() > 0 ? true : false;

    }
    
    @Override    public List<CalendarRange> getAllowedTimeRanges(AuthorizationRequest request) throws Exception {

    	final List<Long> allRoleIDs = new ArrayList<>();
    	for (final Role role : request.getUser().getAllRoles()) {
    		allRoleIDs.add(role.getRoleID());
    	}
    	
        TypedQuery<Right> typedQuery = em.createQuery( "from "+ Right.class.getName()
                + "  WHERE role_id in :role_id AND tso = :tso AND product = :product AND aspect = :aspect AND resolution = :resolution AND enabled = true",
        Right.class);
        typedQuery.setParameter("role_id", allRoleIDs);
        typedQuery.setParameter("tso",request.getTso());
        typedQuery.setParameter("product", request.getProduct());
        typedQuery.setParameter("aspect", request.getAspect().name());
        typedQuery.setParameter("resolution", request.getResolution().name());

        final List<CalendarRange> ranges = new ArrayList<>();
        for (Right right : typedQuery.getResultList()) {
            ranges.add(right.getTimeRange());
        }

        return ranges;
    }
    
	@Override
	public Right addRight(Right right) {
		Right exist = null;
		
		if (right.getRightID() != null) {
			exist = fetchById(right.getRightID());
		}
		if (exist != null) {
			right.setRightID(exist.getRightID());
			right = update(right);
		} else {
			create(right);
		}
		return right;
    }
    
    public List<Right> getListByExample(Right dataAuthorization)
            throws Exception {
        TypedQuery<Right> typedQuery = em.createQuery( "from "+ Right.class.getName()
                                + "  WHERE role_id = :role_id AND tso = :tso AND product = :product AND aspect = :aspect AND enabled = :enabled",
                        Right.class);
        typedQuery.setParameter("role_id", dataAuthorization.getRole().getRoleID());
        typedQuery.setParameter("tso", dataAuthorization.getTso());
        typedQuery.setParameter("product", dataAuthorization.getProduct());
        typedQuery.setParameter("aspect", dataAuthorization.getAspect());
        typedQuery.setParameter("enabled", dataAuthorization.isEnabled());

        return typedQuery.getResultList();
    }

    public void enableLine(Right dataAuthorization) throws Exception{
        update(dataAuthorization);
    }

    @Override
    public List<Right> getAllAspects(long roleID,int startPosition,int maxResult) {
        TypedQuery<Right> typedQuery = em.createQuery( "from "+ Right.class.getName()
                + "  WHERE role_id = :roleID AND enabled = TRUE", Right.class);
        typedQuery.setFirstResult(startPosition);
        typedQuery.setMaxResults(maxResult);
            typedQuery.setParameter("roleID", roleID);
            return typedQuery.getResultList();
    }
}
