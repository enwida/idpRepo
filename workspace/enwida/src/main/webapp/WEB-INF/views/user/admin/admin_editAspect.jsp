<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="message"%>
<%@ page session="false" %>
<div>
	<h4>
	<form action="admin_editaspect.html" method="GET">
	<table>
		<tr>
			<td>
				<message:message code="de.enwida.userManagement.roleName" />: <select name="roleID" id="myselect">
						<c:forEach var="role" items="${roles}">
							<option value="${role.roleID}">${role.roleName}</option>
						</c:forEach>
					</select>
			</td>
			<td><message:message code="de.enwida.userManagement.max" />:<input type='text' name='max' id='max'   value="10" /></td>
			<td><input name="submit" type="submit" value='ok' class="btn btn-primary" />
			<td><input name="all" type="submit" value='<message:message code="de.enwida.userManagement.allAspects" />' class="btn btn-primary" /></td>
		</tr>
	</table>			
	</form>
	</h4>
	<script>
$("#myselect").val(get_url_parameter('roleID'));
</script>
	<table id="tblAspects" class="tablesorter">
	<thead>
		<tr>
			<th>Right ID</th>
			<th><message:message code="de.enwida.userManagement.aspect" /></th>
			<th><message:message code="de.enwida.userManagement.product" /></th>
			<th><message:message code="de.enwida.userManagement.t1" /></th>
			<th><message:message code="de.enwida.userManagement.t2" /></th>
			<th><message:message code="de.enwida.userManagement.resolution" /></th>
			<th><message:message code="de.enwida.userManagement.tso" /></th>
			<th><message:message code="de.enwida.userManagement.enabled" /></th>
			<c:forEach var="role" items="${roles}">
				<th>
					${role.roleName}
				</th>
			</c:forEach>
		</tr>
	</thead>
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
				<td>${right.rightID}</td>
				<td>${right.aspect}</td>
				<td>${right.product}</td>
				<td>${right.timeRange.from.time}</td>
				<td>${right.timeRange.to.time}</td>
				<td>${right.resolution}</td>
				<td>${right.tso}</td>
				<td><input type="checkbox" onclick="enableDisableAspect(${right.rightID},this.checked);"  ${right.enabled == 'true' ? 'checked' : ''}></td>
				
				<c:forEach var="role" items="${roles}">
					<td>
						<input type="checkbox" onclick="enableDisableAspectForRole(${right.rightID},${role.roleID},this.checked);"  ${role.hasRight(right) == 'true' ? 'checked' : ''}/>
					</td>
				</c:forEach>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>