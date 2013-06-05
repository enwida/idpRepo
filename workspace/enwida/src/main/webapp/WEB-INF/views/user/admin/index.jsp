<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<script>
$(function() {
    $( "#tabs" ).tabs();
    $("#tblGroups").tablesorter(); 
    $("#tblUsers").tablesorter(); 
    $("#tblRoles").tablesorter(); 
  });
</script>



	<div id="tabs">
		<ul>
			<li><a href="#tabs-1">User</a></li>
			<li><a href="#tabs-2">Group</a></li>
			<li><a href="#tabs-3">Roles</a></li>
		</ul>
		<div id="tabs-1">
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
						<td><a href='admin/editGroup?userID=${user.userID}'> Edit
								Groups</a></td>
					</tr>
				</c:forEach>

			</table>

		</div>
		<div id="tabs-2">
			<input id="newGroup" type='text' />
			<a href="#">Add</a>
			<br>
			<table id="tblGroups" class="tablesorter">
				<thead>
					<tr>
						<th>Group Name</th>
						<th>Users</th>
						<th>Operation</th>
					</tr>
				<thead>
				<tbody> 
						<tr>
							<td>enwida</td>
							<td>enwida users</td>
							<td><a href="editRole">Edit Role</a> | <a href="">delete</a></td>
						</tr>
				</tbody> 
				</table>
		</div>
		<div id="tabs-3">
			<input id="newRole" type='text' />
			<a href="#">Add</a>
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
						<td>enwida</td>
						<td>enwida users</td>
						<td><a href="editAspect">Edit Aspect</a> | <a href="">delete</a></td>
					</tr>
				</tbody> 
			</table>
		</div>
	</div>