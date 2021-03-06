package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.web.model.Right;

public interface IAspectService {
    public List<Right> getAllAspects() throws Exception;

    public List<Right> getRoleAspects(Long roleID);
}
