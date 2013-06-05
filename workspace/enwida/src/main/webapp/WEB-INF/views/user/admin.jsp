<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Enwida Home Page</title>
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script src="../resources/js/admin/jquery.tablesorter.js"></script>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<link rel="stylesheet" href="../resources/css/table.css" />
</head>
<body>
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
					<c:forEach var="role" items="${roles}">
						<tr>
							<td>enwida</td>
							<td>enwida users</td>
							<td>delete</td>
						</tr>
					</c:forEach>
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
						<td><a href="">Aspects</a> <a href="">delete</a></td>
					</tr>
				</tbody> 
			</table>
		</div>
	</div>
</body>
</html>