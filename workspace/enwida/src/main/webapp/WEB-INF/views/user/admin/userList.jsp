<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<script>
$(function() {
    $("#tblGroups").tablesorter(); 
  });
</script>



<table id="tblUsers" class="tablesorter">
	<thead>
		<tr>
			<th>User</th>
			<th>Enable/Disabled</th>
			<th>Name LastName</th>
			<th>Login Details</th>
			<th>Groups</th>
			<th>Operations</th>
		</tr>
	</thead>
	<c:forEach var="user" items="${users}">
		<tr>
			<td>${user.userName}</td>
			<td><input type="checkbox"
				onclick="updateRole(${user.userID},0,this);"
				${user.enabled == 'true' ? 'checked' : ''}></td>
			<td>${user.firstName} ${user.lastName}</td>
			<td>${user.loginCount+user.lastLogin}</td>
			<td>${user.groups} admin</td>
			<td><a href='editGroup?userID=${user.userID}'> Edit Groups</a></td>
		</tr>
	</c:forEach>
</table>