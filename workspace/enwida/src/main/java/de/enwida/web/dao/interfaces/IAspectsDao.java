package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.transport.Aspect;

public interface IAspectsDao {
    
    public List<Aspect> getAspects(int chartId);

}
