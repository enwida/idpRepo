package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.model.AspectRight;

public interface IAspectsDao {
    
    public List<AspectRight> getAllAspects(long roleID);

}
