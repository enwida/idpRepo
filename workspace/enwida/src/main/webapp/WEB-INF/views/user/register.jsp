<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page session="true" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<a href="login">login</a>
	<head>
		<title>Enwida Registration</title>
	</head>

<h1>Registration Form</h1><br />
<form:form commandName="USER" name="registrationForm">
<table>
	<tr><td>Mail Address(*) : </td><td><form:input path="userName" onchange="getCompany(this.value)"/><label id="userErrorLabel" cssStyle="color : red;">${emailAvailabilityError}</label> <form:errors id="userError" path="userName" cssStyle="color : red;"/></td></tr>
	<tr><td>First Name(*) : </td><td><form:input path="firstName" /><form:errors path="firstName" cssStyle="color : red;"/></td></tr>
	<tr><td>Last Name(*) : </td><td><form:input path="lastName" /><form:errors path="lastName" cssStyle="color : red;"/></td></tr>
	<tr><td>Password(*) : </td><td><form:password path="password" /><form:errors path="password" cssStyle="color : red;"/></td></tr>
	<tr><td>Password(*)(Repeat) : </td><td><form:password path="confirmPassword" /><form:errors path="confirmPassword" cssStyle="color : red;"/></td></tr>
	<tr><td>Telephone : </td><td><form:input path="telephone" /><form:errors path="telephone" cssStyle="color : red;"/></td></tr>
	<tr><td>Company : </td><td><form:input path="companyName" id="companyName"/><form:errors path="companyName" cssStyle="color : red;"/></td></tr>
	<tr><td>Logo : </td><td><form:input path="companyLogo" id="companyLogo"/><form:errors path="companyLogo" cssStyle="color : red;"/></td></tr>
	<tr><td><input type="reset" value="Reset" /></td><td><input type="submit" name="saveChanges" value="Save Changes" /></td></tr>
</table>
<div id="companyImages">

</div>
</form:form>
