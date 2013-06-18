package de.enwida.web.model;

import java.util.List;


public class Group {

	private long groupID;
	private String groupName;
	private List<User> assignedUsers;
	private boolean status;
	
	public long getGroupID() {
		return groupID;
	}
	public void setGroupID(long groupID) {
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
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
}
