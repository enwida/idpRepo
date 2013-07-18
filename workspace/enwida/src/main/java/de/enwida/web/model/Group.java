package de.enwida.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "groups", schema = "users")
public class Group {
    
    @Id
    @Column(name="group_id")
    private Long groupID;
    
    @Column(name="group_name")
    private String groupName;
    
    @Column(name="user_id")
    private boolean autoPass;
    @Transient
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
