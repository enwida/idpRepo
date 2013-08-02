package de.enwida.web.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.interfaces.IRightsDao;
import de.enwida.web.model.Right;
import de.enwida.web.service.interfaces.IAspectService;

@Service("AspectService")
@Transactional
public class AspectServiceImpl implements IAspectService {

	@Autowired
	private IRightsDao rightsDao;

    public List<Right> getAllAspects(long roleID) throws Exception {
        return rightsDao.getAllAspects(roleID);
    }
}
