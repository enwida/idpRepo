package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.web.dto.UserDTO;
import de.enwida.web.model.AspectRight;
import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;

public interface AspectService {
    public List<AspectRight> getAllAspects(long roleID);
}
