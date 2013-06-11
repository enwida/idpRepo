<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>


<form name='f' method='POST'>
	<table>
		<tr>
			<td>Group Name:</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Add" /></td>
		</tr>
	</table>
	
</form>
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
	<tfoot>
		<tr>
			<tr><input name="newGroup" type='text' value="new Group"/></tr>
			<tr></th>
			<tr><a href="">Add</a></tr>
		<tr>
	<tfoot>
</table>