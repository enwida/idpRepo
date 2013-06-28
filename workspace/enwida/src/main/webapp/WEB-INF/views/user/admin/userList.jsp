<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>

<table id="tblUsers" class="tablesorter">
	<thead>
		<tr>
			<th>User</th>
			<th>Enable/Disabled</th>
			<th>Name LastName</th>
			<th>Last Login</th>
			<th>Company Name</th>
			<th>Tel</th>
			<th>Operations</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="user" items="${users}">
			<tr>
				<td><a href='user?userID=${user.userID}'>${user.userName}</a></td>
				<td><input type="checkbox"
					onclick="enableDisableUser(${user.userID},this.checked);"
					${user.enabled == 'true' ? 'checked' : ''}></td>
				<td>${user.firstName} ${user.lastName}</td>
				<td>${user.loginCount+user.lastLogin}</td>
				<td>${user.telephone}</td>
				<td>${user.companyName}</td>
				<td><a href='editGroup?userID=${user.userID}'> Edit Group</a>
					<a href='user?userID=${user.userID}'> Details</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>