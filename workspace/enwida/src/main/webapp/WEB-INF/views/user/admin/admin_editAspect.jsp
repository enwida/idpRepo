<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
			<th>Aspect</th>
			<th>Product</th>
			<th>T1</th>
			<th>T2</th>
			<th>Res</th>
			<th>tso</th>
			<th>Enable/disabled</th>
		<tr>
	<thead>
	<tbody>
		<c:forEach var="right" items="${aspectRights}">
			<tr>
				<td>${right.aspectName}</td>
				<td>${right.product}</td>
				<td>${right.t1}</td>
				<td>${right.t2}</td>
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