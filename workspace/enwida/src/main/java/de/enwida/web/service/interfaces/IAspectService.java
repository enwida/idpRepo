package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.web.model.Right;

public interface IAspectService {
    public List<Right> getAllAspects(int startPosition,int maxResult) throws Exception;

    public List<Right> getAllAspects();
}
