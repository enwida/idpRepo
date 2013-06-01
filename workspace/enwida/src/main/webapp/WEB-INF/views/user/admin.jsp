<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Enwida Home Page</title>
	<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
	<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
</head>
<body>
<script>
function updateRole(userID,permissionID,cb)
{
	$.ajax({
		  url: "updateRole?state="+cb.checked+"&userID="+userID+"&roleID="+permissionID
	});
}
</script>

	 <table id="tablePermissions">
	 	<tr>
	 		<td>User</td>
	 		<td>Enabled</td>
	 		<td>admin</td>
	 		<td>testuser</td>
	 		<td>export</td>
	 	</tr>
	 	<c:forEach var="user" items="${users}">
	 	<tr>
		    <td>${user.userName} </td>
	 		<td><input type="checkbox" onclick="updateRole(${user.userID},0,this);"  ${user.enabled == 'true' ? 'checked' : ''}></td>
	 		<td><input type="checkbox" onclick="updateRole(${user.userID},1,this);" ${user.hasPermission("Admin")  == 'true' ? 'checked' : ''}></td>
	 		<td><input type="checkbox" onclick="updateRole(${user.userID},2,this);"  ${user.hasPermission("Test")  == 'true' ? 'checked' : ''}></td>
	 		<td><input type="checkbox" onclick="updateRole(${user.userID},3,this);"  ${user.hasPermission("Export")  == 'true' ? 'checked' : ''}></td>
	 	</tr>
		</c:forEach>
	 	
	 </table>
</body>
</html>