package de.enwida.web.model;

import java.util.ArrayList;
import java.util.List;

public class Role {
    private long roleID;
    private String roleName;
    private String description;
    private  List<Group> assignedGroups;
    
    public void addAssignedGroups(Group group) {
        if (assignedGroups == null) {
            assignedGroups = new ArrayList<Group>();
        }
        this.assignedGroups.add(group);
    }
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public long getRoleID() {
        return roleID;
    }
    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Group> getAssignedGroups() {
        return assignedGroups;
    }

    public void setAssignedGroups(List<Group> assignedGroups) {
        this.assignedGroups = assignedGroups;
    }


}
