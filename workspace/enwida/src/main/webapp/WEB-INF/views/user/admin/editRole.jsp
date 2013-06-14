<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>

<br>
<table id="tblGroups" class="tablesorter">
	<thead>
		<tr>
			<th>Role Name</th>
			<th>Desciption</th>
			<th>Groups</th>
			<th>Operation</th>
		<tr>
	<thead>
	<tbody>
		<c:forEach var="role" items="${roles}">
			<tr>
				<td>${role.roleName}</td>
				<td>${role.description}</td>
				<td>${role.assignedGroups}</td>
				<td><a href='editAspect?roleID=${role.roleID}'> details</a></td>
			<tr>
		</c:forEach>
	</tbody>
</table>


<table>
	<tr>
		<td>
			<select>
			  <option value="enwida">enwida</option>
			</select>
		</td>
		<td>
			<select>
			  <option value="enwida">admin</option>
			</select>
		</td>
	</tr>
	<tr>
		<td></td>
		<td><a href="">Assign</a></td>
	</tr>
</table>



<table>
	<tr>
		<td>
			<select>
			  <option value="enwida">enwida</option>
			</select>
		</td>
		<td>
			<select>
			  <option value="enwida">admin</option>
			</select>
		</td>
	</tr>
	<tr>
		<td></td>
		<td><a href="">DeAssign</a></td>
	</tr>
</table>
