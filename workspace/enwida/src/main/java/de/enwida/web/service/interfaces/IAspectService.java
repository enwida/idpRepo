package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.web.model.AspectRight;

public interface IAspectService {
	public List<AspectRight> getAllAspects(long roleID);
}
