<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page session="true"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Hello World with Spring 3 MVC</title>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1251">

	<script src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
	<script type="text/javascript">
	
		function checkEmailAvailability() {
			var email = document.getElementById("userName").value;
			alert(email);
			$
					.ajax({
						type : "GET",
						url : "/enwida/user/userAvailability?email=" + email,
						success : function(response) {
							if (response == false) {
								getCompanyName(email);
								return true;
							} else {
								document.getElementById("userNameErrorField").value = "Username already in use.";
								return false;
							}
						}
					});
		}

		function getCompanyName(email) {
			var domain = email.substring(email.indexOf("@") + 1,
					(email.length));
			alert(domain + email);
			document.getElementById("companyName").value = domain;
		}
	</script>
</head>
<body>
	<h1>Registration Form</h1>
	<br />
	<form:form commandName="USER">
		<table>
			<tr>
				<td>Mail Address(*) :</td>
				<td><form:input path="userName" id="userName"
						onchange="checkEmailAvailability()" /> <form:errors
						id="userNameErrorField" path="userName" cssStyle="color : red;" /></td>
			</tr>
			<tr>
				<td>First Name(*) :</td>
				<td><form:input path="firstName" /> <form:errors
						path="firstName" cssStyle="color : red;" /></td>
			</tr>
			<tr>
				<td>Last Name(*) :</td>
				<td><form:input path="lastName" /> <form:errors
						path="lastName" cssStyle="color : red;" /></td>
			</tr>
			<tr>
				<td>Password(*) :</td>
				<td><form:input path="password" /> <form:errors
						path="password" cssStyle="color : red;" /></td>
			</tr>
			<tr>
				<td>Password(*)(Repeat) :</td>
				<td><form:input path="confirmPassword" /> <form:errors
						path="confirmPassword" cssStyle="color : red;" /></td>
			</tr>
			<tr>
				<td>Tel :</td>
				<td><form:input path="telephone" /> <form:errors
						path="telephone" cssStyle="color : red;" /></td>
			</tr>
			<tr>
				<td>Company :</td>
				<td><form:input path="companyName" /> <form:errors
						path="companyName" cssStyle="color : red;" /></td>
			</tr>
			<tr>
				<td>Logo :</td>
				<td><form:input path="" /></td>
			</tr>
			<tr>
				<td><input type="reset" value="Reset" /></td>
				<td><input type="submit" value="Save Changes" onsubmit="checkEmailAvailability()"/></td>
			</tr>
		</table>
	</form:form>
</body>
</html>