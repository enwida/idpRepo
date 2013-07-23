<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="message"%>
<%@ page session="false"%>

<table id="tblUsers" class="tablesorter">
	<thead>
		<tr>
			<th><message:message code="de.enwida.userManagement.users" /></th>
			<th><message:message code="de.enwida.userManagement.enabled" /></th>
			<th><message:message code="de.enwida.userManagement.firstName" /></th>
			<th><message:message code="de.enwida.userManagement.lastLogin" /></th>
			<th><message:message code="de.enwida.userManagement.company" /></th>
			<th><message:message code="de.enwida.userManagement.telephone" /></th>
			<th><message:message code="de.enwida.userManagement.operation" /></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="user" items="${users}">
			<tr>
				<td><a href='admin_user.html?userID=${user.userID}'>${user.userName}</a></td>
				<td><input type="checkbox"
					onclick="enableDisableUser(${user.userID},this.checked);"
					${user.enabled == 'true' ? 'checked' : ''}></td>
				<td>${user.firstName} ${user.lastName}</td>
				<td>${user.loginCount+user.lastLogin}</td>
				<td>${user.telephone}</td>
				<td>${user.companyName}</td>
				<td><a href='admin_editgroup.html?userID=${user.userID}'><message:message code="de.enwida.userManagement.userList.editGroup" /></a>
					<a href='admin_user.html?userID=${user.userID}'> <message:message code="de.enwida.userManagement.details" /></a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>