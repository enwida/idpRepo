package de.enwida.web.model;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private long groupID;
    private String groupName;
    private boolean autoPass;
    public List<User> assignedUsers;

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<User> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<User> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public void addAssignedUsers(User user) {
        if (assignedUsers == null) {
            assignedUsers = new ArrayList<User>();
        }
        this.assignedUsers.add(user);
    }

    public boolean isAutoPass() {
        return autoPass;
    }

    public void setAutoPass(boolean autoPass) {
        this.autoPass = autoPass;
    }
    
    @Override
    public String toString() {
        return getGroupName();
    }
}
