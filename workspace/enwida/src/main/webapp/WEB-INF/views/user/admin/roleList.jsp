<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>

<table>
	<tr>
		<td>Role Name:</td>
		<td><input id="newRole" type='text' /></td>
	</tr>
	<tr>
		<td>Role Description:</td>
		<td><input id="newRole" type='text' /></td>
	</tr>
	<tr>
		<td></td>
		<td><a href="#tab-2">Add</a></td>
	</tr>
</table>
<br>
<table id="tblRoles" class="tablesorter">
	<thead>
		<tr>
			<th>Roles Name</th>
			<th>Description</th>
			<th>Operation</th>
		</tr>
	<thead>
	<tbody>
		<tr>
			<td>tum</td>
			<td>Only tum students</td>
			<td><a href="editAspect">Assign Aspect</a> | <a href="">delete</a></td>
		</tr>
	</tbody>
</table>