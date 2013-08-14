<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="message"%>
<%@ page session="false" %>
<div>
	<h1>
	<form action="admin_editaspect.html" method="GET">
		<message:message code="de.enwida.userManagement.roleName" />: <select name="roleID" id="myselect" onchange="this.form.submit()">
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
		<c:forEach var="right" items="${aspectRights}">
			<tr>
				<td>${right.aspect}</td>
				<td>${right.product}</td>
				<td>${right.timeRange.from}</td>
				<td>${right.timeRange.to}</td>
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