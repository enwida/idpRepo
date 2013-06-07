<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>

Group Name:
<input id="newGroup" type='text' />
<a href="#tab-2">Add</a>
<br>
<table id="tblGroups" class="tablesorter">
	<thead>
		<tr>
			<th>Group Name</th>
			<th>Users</th>
			<th>Operation</th>
		<tr>
	<thead>
	<tbody>
		<c:forEach var="group" items="${groups}">
			<tr>
				<td>${group.groupName}</td>
				<td>${group.assignedUsers}</td>
				<td><a href='editRole?groupID=${group.groupID}'> Edit Roles</a>
					| <a href="">delete</a></td>
			<tr>
		</c:forEach>
	</tbody>
</table>
</div>