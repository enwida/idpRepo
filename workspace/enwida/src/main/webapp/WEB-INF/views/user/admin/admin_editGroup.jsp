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
		<tfoot>
		<tr>
			<th colspan="7" class="pager form-horizontal">
				<button type="button" class="btn first"><i class="icon-step-backward"></i></button>
				<button type="button" class="btn prev"><i class="icon-arrow-left"></i></button>
				<span class="pagedisplay"></span> <!-- this can be any element, including an input -->
				<button type="button" class="btn next"><i class="icon-arrow-right"></i></button>
				<button type="button" class="btn last"><i class="icon-step-forward"></i></button>
				<select class="pagesize input-mini" title="Select page size">
					<option selected="selected" value="10">10</option>
					<option value="20">20</option>
					<option value="30">30</option>
					<option value="40">40</option>
				</select>
				<select class="pagenum input-mini" title="Select page number"></select>
			</th>
		</tr>
	</tfoot>
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
				<td><input type="submit" class="btn btn-primary"  name="addGroup" value=" <message:message code="de.enwida.userManagement.add" />"/></td>
			<tr>
		<tfoot>
	</table>
<script>
$(function() {
	$('#selectedUser option[value='+QueryString.userID+']').attr('selected','selected');
	$('#selectedGroup option[value='+QueryString.groupID+']').attr('selected','selected');
});
</script>
	<table  id="tblGroupMap">
		<thead>
			<tr>
				<th><message:message code="de.enwida.userManagement.groupName" /></th>
				<th><message:message code="de.enwida.userManagement.user" /></th>
			<tr>
		<thead>
		<tbody>
		<tr>
			<td>
				<select id="selectedGroup" name="selectedGroup">
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
				<input type="submit" class="btn btn-primary"  name="assign" value="<message:message code="de.enwida.userManagement.assign" />"/>
				<input type="submit" class="btn btn-primary"  name="deassign" value="<message:message code="de.enwida.userManagement.deAssign" />"/>
			</td>
		</tr>
	</table>
</form>

