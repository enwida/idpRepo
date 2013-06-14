<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>


<form name='f' method='POST'>
<br>
<table id="tblRoles" class="tablesorter">
	<thead>
		<tr>
			<th>Role Name</th>
			<th>Description</th>
			<th>Groups</th>
			<th>Operation</th>
		<tr>
	<thead>
	<tbody>
		<c:forEach var="role" items="${rolesWithGroups}">
			<tr>
				<td>${role.roleName}</td>
				<td>${role.description}</td>
				<td><c:forEach var="group" items="${role.assignedGroups}">${group.groupName},</c:forEach></td>
				<td>
					<a href='editAspect?groupID=${role.roleID}'> Details</a>
				</td>
			<tr>
		</c:forEach>
	</tbody>
</table>

	<table  id="tblRoleMap" class="tablesorter">
		<thead>
			<tr>
				<th>Group Name</th>
				<th>Role Name</th>
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
					<select name="selectedRole">
						<c:forEach var="role" items="${roles}">
							<option value="${role.roleID}">${role.roleName}</option>
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