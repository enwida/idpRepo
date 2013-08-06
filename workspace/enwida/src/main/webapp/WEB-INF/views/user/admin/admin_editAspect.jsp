<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="message"%>
<%@ page session="false" %>
<div>
	<h1>
	<form action="admin_editaspect.html" method="GET">
		Role: <select name="roleID" id="myselect" onchange="this.form.submit()">
						<c:forEach var="role" items="${roles}">
							<option value="${role.roleID}">${role.roleName}</option>
						</c:forEach>
					</select>
	</form>
	</h1>
	<script>
$("#myselect").val(get_url_parameter('roleID'));
</script>
	<table id="tblAspects" class="tablesorter">
	<thead>
		<tr>
			<th><message:message code="de.enwida.userManagement.aspect" /></th>
			<th><message:message code="de.enwida.userManagement.product" /></th>
			<th><message:message code="de.enwida.userManagement.t1" /></th>
			<th><message:message code="de.enwida.userManagement.t2" /></th>
			<th><message:message code="de.enwida.userManagement.resolution" /></th>
			<th><message:message code="de.enwida.userManagement.tso" /></th>
			<th><message:message code="de.enwida.userManagement.enabled" /></th>
		<tr>
	<thead>
	<tbody>
		<c:forEach var="right" items="${aspectRights}">
			<tr>
				<td>${right.aspect}</td>
				<td>${right.product}</td>
				<td>${right.timeFrom}</td>
				<td>${right.timeTo}</td>
				<td>${right.resolution}</td>
				<td>${right.tso}</td>
				<td><input type="checkbox"
					onclick="enableDisableAspect(${right.rightID},this.checked);"
					${right.enabled == 'true' ? 'checked' : ''}></td>
			<tr>
		</c:forEach>
	</tbody>
</table>
</div>