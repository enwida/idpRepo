package de.enwida.web.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.interfaces.IAspectsDao;
import de.enwida.web.model.AspectRight;
import de.enwida.web.service.interfaces.AspectService;

@Service("AspectService")
@Transactional
public class AspectServiceImpl implements AspectService {

	@Autowired
	private IAspectsDao aspectDao;

    public List<AspectRight> getAllAspects(long roleID) {
        return aspectDao.getAllAspects(roleID);
    }
}
