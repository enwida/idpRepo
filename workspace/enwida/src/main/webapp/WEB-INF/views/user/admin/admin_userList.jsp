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
			<th><message:message code="de.enwida.userManagement.telephone" /></th>
			<th><message:message code="de.enwida.userManagement.company" /></th>
			<th><message:message code="de.enwida.userManagement.operation" /></th>
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
		<c:forEach var="user" items="${users}">
			<tr>
				<td><a href='admin_user.html?userID=${user.userID}'>${user.userName}</a></td>
				<td><input type="checkbox"
					onclick="enableDisableUser(${user.userID},this.checked);"
					${user.enabled == 'true' ? 'checked' : ''}></td>
				<td>${user.firstName} ${user.lastName}</td>
				<td>${user.lastLogin}</td>
				<td>${user.telephone}</td>
				<td>${user.companyName}</td>
				<td>
					<a href='admin_editgroup.html?userID=${user.userID}'><message:message code="de.enwida.userManagement.userList.editGroup" /></a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>