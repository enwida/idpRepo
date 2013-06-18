<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page session="true" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Hello World with Spring 3 MVC</title>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
	</head>
	<body>
		<h1>Registration Form</h1><br />
		<form:form commandName="USER">
		<table>
			<tr><td>Mail Address(*) : </td><td><form:input path="userName" /><form:errors path="userName" cssStyle="color : red;"/></td></tr>
			<tr><td>First Name(*) : </td><td><form:input path="firstName" /><form:errors path="firstName" cssStyle="color : red;"/></td></tr>
			<tr><td>Last Name(*) : </td><td><form:input path="lastName" /><form:errors path="lastName" cssStyle="color : red;"/></td></tr>
			<tr><td>Password(*) : </td><td><form:input path="password" /><form:errors path="password" cssStyle="color : red;"/></td></tr>
			<tr><td>Password(*)(Repeat) : </td><td><form:input path="confirmPassword" /><form:errors path="confirmPassword" cssStyle="color : red;"/></td></tr>
			<tr><td>Telephone : </td><td><form:input path="telephone" /><form:errors path="telephone" cssStyle="color : red;"/></td></tr>
			<tr><td>Company : </td><td><form:input path="company" /><form:errors path="company" cssStyle="color : red;"/></td></tr>
			<tr><td>Logo : </td><form:input path="" /></tr>
			<tr><td><input type="reset" value="Reset" /></td><td><input type="submit" value="Save Changes" /></td></tr>
		</table>
		</form:form>
	</body>
</html>