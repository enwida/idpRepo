<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>

<h1>User : ${user.userName}</h1>
<div>
	<table>
		<tr>
			<td>Name:</td>
			<td>${user.firstName}</td>
		</tr>
		<tr>
			<td>Last Name:</td>
			<td>${user.lastName}</td>
		</tr>
		<tr>
			<td>Join date:</td>
			<td>${user.joinDate}</td>
		</tr>
		<tr>
			<td>Login Info</td>
			<td></td>
		</tr>
		<tr>
			<td>Cookie ID</td>
			<td></td>
		</tr>
		<tr>
			<td>Groups</td>
			<td></td>
		</tr>
		<tr>
			<td>Roles</td>
			<td></td>
		</tr>
	</table>
	<a href="">Delete User</a> <a href="">Edit Group</a> <a href="">Reset
		Password</a>
</div>
