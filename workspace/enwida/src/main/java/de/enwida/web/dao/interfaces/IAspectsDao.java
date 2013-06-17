package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.transport.Aspect;
import de.enwida.web.model.AspectRight;

public interface IAspectsDao {
    
    public List<Aspect> getAspects(int chartId);
    
    public List<AspectRight> getAllAspects(long roleID);
}
