<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="message"%>
<html>
<head>
<title><message:message code="de.enwida.login" /></title>
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
<body>
<a href="login"><message:message code="de.enwida.login" /></a>
	<h3><message:message code="de.enwida.userManagement.forgotPassword" /></h3>
 
	<a>${error}</a>
 
	<form name='f' method='POST'>
 
		<table>
			<tr>
				<td><message:message code="de.enwida.userManagement.password" />:</td>
				<td><input type='text' name='email' value=''>
				</td>
			</tr>
			<tr>
				<td colspan='2'><input type="submit"	value='<message:message code="de.enwida.userManagement.getPassword" />' />
				</td>
			</tr>
		</table>
 
	</form>
</body>
</html>