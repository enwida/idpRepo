package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.model.Right;

public interface IAspectsDao {
    
    public List<Right> getAllAspects(long roleID) throws Exception;

}
