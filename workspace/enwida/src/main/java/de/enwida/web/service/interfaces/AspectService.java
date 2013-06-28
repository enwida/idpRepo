package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.web.model.AspectRight;

public interface AspectService {
    public List<AspectRight> getAllAspects(long roleID);
}
