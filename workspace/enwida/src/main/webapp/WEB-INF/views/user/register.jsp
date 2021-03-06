<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page session="true" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="message"%>
	<head>
		<title>Enwida Registration</title>
	</head>

<a href="login"><message:message code="de.enwida.userManagement.login" /></a>
<h1><message:message code="de.enwida.userManagement.registrationForm" /></h1><br />
<form:form commandName="USER" name="registrationForm">
<label id="userErrorLabel" cssStyle="color : red;">${emailAvailabilityError}</label> 
<table>
	<tr><td><message:message code="de.enwida.userManagement.mailAddress" />(*) : </td><td><form:input path="email" onchange="getCompany(this.value)"/><form:errors id="userError" path="email" cssStyle="color : red;"/></td></tr>
	<tr><td><message:message code="de.enwida.userManagement.firstName" />(*) : </td><td><form:input path="firstName" /><form:errors path="firstName" cssStyle="color : red;"/><label id="userErrorLabel" cssStyle="color : red;">${usernameCheck}</label></td></tr>
	<tr><td><message:message code="de.enwida.userManagement.lastName" />(*) : </td><td><form:input path="lastName" /><form:errors path="lastName" cssStyle="color : red;"/></td></tr>
	<tr><td><message:message code="de.enwida.userManagement.password" />(*)<message:message code="de.enwida.userManagement.passwordSize" /> : </td><td><form:password path="password" /><form:errors path="password" cssStyle="color : red;"/></td></tr>
	<tr><td><message:message code="de.enwida.userManagement.passwordRepeat" />(*): </td><td><form:password path="confirmPassword" /><form:errors path="confirmPassword" cssStyle="color : red;"/></td></tr>
	<tr><td><message:message code="de.enwida.userManagement.telephone" /> : </td><td><form:input path="telephone" /><form:errors path="telephone" cssStyle="color : red;"/></td></tr>
	<tr><td><message:message code="de.enwida.userManagement.company" /> : </td><td><form:input path="companyName" id="companyName"/><form:errors path="companyName" cssStyle="color : red;"/></td></tr>
	<tr><td><message:message code="de.enwida.userManagement.logo" /> : </td><td><form:input path="companyLogo" id="companyLogo"/><form:errors path="companyLogo" cssStyle="color : red;"/></td></tr>
	<tr><td><input type="reset" value="Reset" /></td><td><input type="submit" name="submit" value="Register" /></td></tr>
</table>

<div id="companyImages">

</div>
</form:form>
