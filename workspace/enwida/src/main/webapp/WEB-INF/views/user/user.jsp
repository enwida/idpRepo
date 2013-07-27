<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>

<h1>User : ${user.userName}</h1>
<div>
	<form method='POST'>
		<table>
			<tr>
				<td>Name:</td>
				<td><input type="text" name="firstName"
					value="${user.firstName}"></td>
			</tr>
			<tr>
				<td>Last Name:</td>
				<td><input type="text" name="lastName" value="${user.lastName}"></td>
			</tr>
			<tr>
				<td>Join date:</td>
				<td>${user.joinDate}</td>
			</tr>
			<tr>
				<td>Email:</td>
				<td>${user.email}</td>
			</tr>
			<tr>
				<td>company:</td>
				<td>${user.company}</td>
			</tr>
			<tr>
				<td>Tel:</td>
				<td><input type="text" name="tel" value="${user.tel}"></td>
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
		<a href="">ResetPassword</a> <a href="">Edit Group</a> <a href="">Reset
			Password</a> <input type="submit" value="Save Changes" />
	</form>
</div>
