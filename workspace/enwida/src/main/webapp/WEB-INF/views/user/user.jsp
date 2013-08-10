<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="message"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>

<h1>User : ${user.userName}</h1>
<div>
	<form method='POST'>
		<table>
			<tr>
				<td><message:message code="de.enwida.userManagement.firstName" />:</td>
				<td><form:input path="firstName" /></td>
			</tr>
			<tr>
				<td><message:message code="de.enwida.userManagement.lastName" />:</td>
				<td><form:input path="lastName" /></td>
			</tr>
			<tr>
				<td>Join date:</td>
				<td>${user.joiningDate}</td>
			</tr>
			<tr>
				<td><message:message code="de.enwida.userManagement.company" />:</td>
				<td>${user.companyName}</td>
			</tr>
			<tr>
				<td><message:message code="de.enwida.userManagement.telephone" />:</td>
				<td><form:input path="telephone" value="${user.telephone}" /></td>
			</tr>
			<tr>
				<td>Joined On</td>
				<td>${user.joiningDate}</td>
			</tr>
			<tr>
				<td></td>
				<td><a href="admin_userlog.html?user=${user.userName}"><message:message code="de.enwida.userManagement.more" /></a></td>
			</tr>
			<tr>
				<td><message:message code="de.enwida.userManagement.groups" /></td>
				<td>${user.groups} 
					<a href="admin_editgroup.html?userID=${user.userID}"><message:message code="de.enwida.admin_editgroup.title" /></a>
				</td>
			</tr>
			<tr>
				<td><message:message code="de.enwida.userManagement.roles" /></td>
				<td>${user.roles}</td>
			</tr>
		</table>
		<a href="">ResetPassword</a> <a href="">Edit Group</a> <a href="">Reset
			Password</a> <input type="submit" value="Save Changes" />
	</form>
</div>
