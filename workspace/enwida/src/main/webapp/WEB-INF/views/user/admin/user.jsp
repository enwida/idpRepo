<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>

<form:form commandName="USER" modelAttribute="user">
	<h1>User :${user.userName}</h1>
	<div>
		<table>
			<tr>
				<td>Name:</td>
				<td><form:input path="firstName" /></td>
			</tr>
			<tr>
				<td>Last Name:</td>
				<td><form:input path="lastName" /></td>
			</tr>
			<tr>
				<td>Join date:</td>
				<td>${user.joiningDate}</td>
			</tr>
			<tr>
				<td>Company:</td>
				<td>${user.companyName}</td>
			</tr>
			<tr>
				<td>Tel:</td>
				<td><form:input path="telephone" value="${user.telephone}" /></td>
			</tr>
			<tr>
				<td>Login Info</td>
				<td></td>
			</tr>
			<tr>
				<td>Cookie ID</td>
				<td></td>
			</tr>
			<tr>
				<td>Last Login Information</td>
				<td>............</td>
			</tr>
			<tr>
				<td></td>
				<td><a href="userLog?user=${user.userName}">more</a></td>
			</tr>
			<tr>
				<td>Groups</td>
				<td>${user.groups}</td>
			</tr>
			<tr>
				<td>Roles</td>
				<td>${user.roles}</td>
			</tr>
		</table>
		<input type="submit" name="delete" value="Delete" />
		<input	type="submit" name="editGroup" value="Edit Group" />
	     <input	type="submit" name="resetPassword" value="Reset Password" />
	    <input	type="submit" name="save" value="Save" />
	</div>
</form:form>
   
					
