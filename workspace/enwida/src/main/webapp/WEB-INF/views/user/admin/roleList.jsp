<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>


<form name='f' method='POST'>
	<table>
		<tr>
			<td>Role Name:</td>
			<td><input name="newRole" type='text' /></td>
		</tr>
		<tr>
			<td>Description:</td>
			<td><input name=roleDescription type='text' /></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Add" /></td>
		</tr>
	</table>

</form>
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
		<c:forEach var="role" items="${roles}">
			<tr>
				<td>${role.name}</td>
				<td>${role.description}</td>
				<td></td>
				<td><a href='editRole?groupID=${role.roleID}'> Edit Roles</a>
					| <a href="">delete</a></td>
			<tr>
		</c:forEach>
	</tbody>
</table>