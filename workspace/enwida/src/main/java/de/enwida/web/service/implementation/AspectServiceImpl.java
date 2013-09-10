package de.enwida.web.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.interfaces.IRightDao;
import de.enwida.web.model.Right;
import de.enwida.web.service.interfaces.IAspectService;

@TransactionConfiguration(defaultRollback = true)
@Service("aspectService")
@Transactional(rollbackFor = Exception.class)
public class AspectServiceImpl implements IAspectService {

	@Autowired
	private IRightDao rightDao;

    public List<Right> getAllAspects(int startPosition,int maxResult) throws Exception {
        return rightDao.getAllAspects(startPosition,maxResult);
    }

    @Override
    public List<Right> getAllAspects() {
        return rightDao.fetchAll();
    }
}
