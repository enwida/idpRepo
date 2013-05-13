<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>


<html>

<head>

</head>
<body>
	<div id="box_inhalt_hintergrund">


		<form name="f" action="<c:url value='j_spring_security_check'/>"
			method="POST">
			<table>
				<tr>
					<td>Benutzername:</td>
					<td><input type='text' name='j_username'
						value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>' /></td>
				</tr>
				<tr>
					<td>Passwort:</td>
					<td><input type='password' name='j_password'></td>
				</tr>
				<tr>
					<td colspan='1'><input name="submit" type="submit"
						value="Login"></td>
					<td><a id="register" href="register">register</a></td>
				</tr>
			</table>

		</form>

	</div>
</body>
</html>