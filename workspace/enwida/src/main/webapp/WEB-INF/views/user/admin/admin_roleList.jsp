<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="message"%>
<%@ page session="false"%>


<form name='f' method='POST'>
<br>
<table id="tblRoles" class="tablesorter">
	<thead>
		<tr>
			<th><message:message code="de.enwida.userManagement.roleName" /></th>
			<th><message:message code="de.enwida.userManagement.description" /></th>
			<th><message:message code="de.enwida.userManagement.groups" /></th>
			<th><message:message code="de.enwida.userManagement.operation" /></th>
		<tr>
	<thead>
	<tfoot>
		<tr>
			<th colspan="7" class="pager form-horizontal">
				<button type="button" class="btn first"><i class="icon-step-backward"></i></button>
				<button type="button" class="btn prev"><i class="icon-arrow-left"></i></button>
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
		<c:forEach var="role" items="${rolesWithGroups}">
			<tr>
				<td>${role.roleName}</td>
				<td>${role.description}</td>
				<td><c:forEach var="group" items="${role.assignedGroups}"><a href="admin_editgroup.html?groupID=${group.groupID}">${group.groupName}</a>,</c:forEach></td>
				<td>
					<a href='admin_editaspect?roleID=${role.roleID}'> <message:message code="de.enwida.userManagement.details" /></a>
				</td>
			<tr>
		</c:forEach>
	</tbody>
</table>

	<table  id="tblRoleMap">
		<thead>
			<tr>
				<th><message:message code="de.enwida.userManagement.groupName" /></th>
				<th><message:message code="de.enwida.userManagement.roleName" /></th>
			<tr>
		<thead>
		<tbody>
		<tr>
			<td>
				<select name="selectedGroup">
						<c:forEach var="group" items="${groups}">
							<option value="${group.groupID}">${group.groupName}</option>
						</c:forEach>
				</select>
			</td>
			<td>
					<select name="selectedRole">
						<c:forEach var="role" items="${roles}">
							<option value="${role.roleID}">${role.roleName}</option>
						</c:forEach>
					</select>
			</td>
		</tr>
		<tbody>
		<tr>
			<td></td>
			<td>
				<input type="submit" class="btn btn-primary" name="assign"  value="<message:message code="de.enwida.userManagement.assign" />"/>
				<input type="submit" class="btn btn-primary"  name="deassign" value="<message:message code="de.enwida.userManagement.deAssign" />"/>
			</td>
		</tr>
	</table>
</form>