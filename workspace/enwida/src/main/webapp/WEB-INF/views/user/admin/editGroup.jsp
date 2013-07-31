<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="message"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>

<br>
<form name='f' method='POST'>
	<table id="tblGroups" class="tablesorter">
		<thead>
			<tr>
				<th><message:message code="de.enwida.userManagement.groupName" /></th>
				<th><message:message code="de.enwida.userManagement.users" /></th>
				<th><message:message code="de.enwida.userManagement.autoPass" /></th>
				<th><message:message code="de.enwida.userManagement.operation" /></th>
			<tr>
		<thead>
		<tbody>
			<c:forEach var="group" items="${groupsWithUsers}">
				<tr>
					<td>${group.groupName}</td>
					<td><c:forEach var="user" items="${group.assignedUsers}"><a href='admin_user.html?userID=${user.userID}'>${user.userName}</a>,</c:forEach>
					</td>
					<td><input type="checkbox" checked="${group.autoPass}"/></td>
					<td>
						<a href='admin_editgroup.html?groupID=${group.groupID}&action=delete'> <message:message code="de.enwida.userManagement.delete" /></a>
					</td>
				<tr>
			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td><input name="newGroup" type='text' placeholder=" <message:message code="de.enwida.userManagement.addGroup" />" /></td>
				<td></td>
				<td><input name="autoPass" type='checkbox' /></td>
				<td><input type="submit" name="addGroup" value=" <message:message code="de.enwida.userManagement.add" />"/></td>
			<tr>
		<tfoot>
	</table>
<script>
$(function() {
	$('#selectedUser option[value='+QueryString.userID+']').attr('selected','selected');
});
</script>
	<table  id="tblGroupMap" class="tablesorter">
		<thead>
			<tr>
				<th><message:message code="de.enwida.userManagement.groupName" /></th>
				<th><message:message code="de.enwida.userManagement.user" /></th>
			<tr>
		<thead>
		<tbody>
		<tr>
			<td>
				<select name="selectedGroup">
						<c:forEach var="group" items="${groupsWithUsers}">
							<option value="${group.groupID}">${group.groupName}</option>
						</c:forEach>
				</select>
			</td>
			<td>
				<select id="selectedUser" name="selectedUser">
						<c:forEach var="user" items="${users}">
							<option value="${user.userID}">${user.userName}</option>
						</c:forEach>
				</select>
			</td>
		</tr>
		<tbody>
		<tr>
			<td></td>
			<td>
				<input type="submit" name="assign" value="<message:message code="de.enwida.userManagement.assign" />"/>
				<input type="submit" name="deassign" value="<message:message code="de.enwida.userManagement.deAssign" />"/>
			</td>
		</tr>
	</table>
</form>

