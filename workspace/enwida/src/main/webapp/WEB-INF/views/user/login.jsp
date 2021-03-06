<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="message"%>
<html>
<head>
<title><message:message code="de.enwida.userManagement.login" /></title>
<style>
.errorblock {
	color: #ff0000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding: 8px;
	margin: 16px;
}
</style>
</head>
<body onload='document.loginForm.j_username.focus();'>
	<h3><message:message code="de.enwida.userManagement.login" /></h3>

	<c:if test="${not empty error}">
		<div class="errorblock">
			<message:message code="de.enwida.userManagement.loginFailed" />.<br /> Caused :
			${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
		</div>
	</c:if>

	<form name='loginForm' id='loginForm' action="<c:url value='/j_spring_security_check' />"
		method='POST'>

		<table>
			<tr>
				<td><message:message code="de.enwida.userManagement.mailAddress" />:</td>
				<td><input type='text' name='j_username' id='j_username' value=''></td>
			</tr>
			<tr>
				<td><message:message code="de.enwida.userManagement.password" />:</td>
				<td><input type='password' name='j_password' id='j_password' /></td>
			</tr>
			<tr>
				<td><a href="register.html"><message:message code="de.enwida.userManagement.register" /></a></td>
				<td><input name="submit" type="submit" value=<message:message code="de.enwida.login" /> /></td>
			</tr>
			<tr>
				<td><a href="forgotPassword"><message:message code="de.enwida.userManagement.forgotPassword" />?</a></td>
				<td><input id="j_remember" name="_spring_security_remember_me" type="checkbox" /><message:message code="de.enwida.userManagement.rememberMe" /></td>
			</tr>
		</table>

	</form>
</body>
</html>