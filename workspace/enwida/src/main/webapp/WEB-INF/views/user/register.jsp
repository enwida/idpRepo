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
			<tr><td colspan="2"><form:errors path="*" cssStyle="color : red;"/></td></tr>
			<tr><td>Mail Address(*) : </td><td><form:input path="userName" /></td></tr>
			<tr><td>First Name(*) : </td><td><form:input path="firstName" /></td></tr>
			<tr><td>Last Name(*) : </td><td><form:input path="lastName" /></td></tr>
			<tr><td>Password(*) : </td><td><form:input path="password" /></td></tr>
			<tr><td>Password(*)(Repeat) : </td><td><form:input path="password" /></td></tr>
			<tr><td>Tel : </td><td><form:input path="password" /></td></tr>
			<tr><td><img src="https://confluence.atlassian.com/download/attachments/216957808/captcha.png"></img> </td><td><form:input path="password" /></td></tr>
			<tr><td><input type="reset" value="Reset" /></td><td><input type="submit" value="Save Changes" /></td></tr>
		</table>
		</form:form>
	</body>
</html>