<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>

<br>
<table id="tblGroups" class="tablesorter">
	<thead>
		<tr>
			<th>Group Name</th>
			<th>Users</th>
			<th>AutoPass</th>
			<th>Operation</th>
		<tr>
	<thead>
	<tbody>
		<c:forEach var="group" items="${groupsWithUsers}">
			<tr>
				<td>${group.groupName}</td>
				<td><c:forEach var="user" items="${group.assignedUsers}">${user.userName},</c:forEach>
				</td>
				<td><input type="checkbox" checked="${group.autoPass}"/></td>
				<td><a href='editRole?groupID=${group.groupID}'> delete</a></td>
			<tr>
		</c:forEach>
	</tbody>
	<tfoot>
		<tr>
			<td><input name="newGroup" type='text' value="new Group" /></td>
			<td></td>
			<td><input name="autoPass" type='checkbox' /></td>
			<td><a href="">Add</a></td>
		<tr>
	<tfoot>
</table>

<script>
$(function() {
	$('#selectedUser option[value='+QueryString.userID+']').attr('selected','selected');
});
</script>

<form name='f' method='POST'>
	<table  id="tblGroupMap" class="tablesorter">
		<thead>
			<tr>
				<th>Group Name</th>
				<th>User</th>
			<tr>
		<thead>
		<tbody>
		<tr>
			<td>
				<select name="selectedGroup">
						<c:forEach var="group" items="${groups}">
							<option value="${group.groupID}">${group.groupName}</option>
						</c:forEach>
				</select>
			</td>
			<td>
				<select id="selectedUser" name="selectedUser">
						<c:forEach var="user" items="${users}">
							<option value="${user.userID}">${user.userName}</option>
						</c:forEach>
				</select>
			</td>
		</tr>
		<tbody>
		<tr>
			<td></td>
			<td>
				<input type="submit" name="assign" value="assign"/>
				<input type="submit" name="deassign" value="deassign"/>
			</td>
		</tr>
	</table>
</form>

