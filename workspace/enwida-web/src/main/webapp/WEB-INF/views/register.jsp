<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>


<html>
    <head>

	</head>
	<body>
	<form name="f" action="<c:url value='register'/>"
				method="POST">
				<table>
					<tr>
						<td>Username:</td>
						<td><input type='text' name='j_username'
							value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>' /></td>
					</tr>
					<tr>
						<td>Password:</td>
						<td><input type='password' name='j_password'></td>
					</tr>
					<tr>
						<td>Name:</td>
						<td><input type='text' name='j_name'></td>
					</tr>
										<tr>
						<td>Last Name:</td>
						<td><input type='text' name='j_lastname'></td>
					</tr>
					<tr>
						<td colspan='2'><input name="submit" type="submit"
							value="Register"></td>
					</tr>
				</table>

		</form>
    </body>	
</html>