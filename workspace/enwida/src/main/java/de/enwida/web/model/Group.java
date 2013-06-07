package de.enwida.web.model;

import java.util.List;


public class Group {

	private Long groupID;
	private String groupName;
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
}
