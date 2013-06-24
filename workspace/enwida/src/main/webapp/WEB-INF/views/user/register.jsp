<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page session="true" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Enwida Registration</title>

		<script type="text/javascript" >
		
			function getCompany(email)
			{
				var company = email.substring( email.indexOf('@') + 1, email.length);
				document.getElementById("companyName").value = company;
			}
			
			function checkEmail (email)
			{
				if (email == "") 
				{					
					return;
				}
				
				if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
					xmlhttp = new XMLHttpRequest();
				} else {// code for IE6, IE5
					xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
				}
				xmlhttp.onreadystatechange = function() 
				{
					if (xmlhttp.readyState == 4 && xmlhttp.status == 200) 
					{					
						if(xmlhttp.responseText == true)
						{
							alert("true");
							document.getElementById("userError").value = "Email already in use.";
						}
						else
						{
						 	getCompany(email);
						}
					}
				}
				
				xmlhttp.open("GET", "checkEmail?email=" + email, true);
				xmlhttp.send();
			}
			
		</script>
		
	</head>
	<body>
		<h1>Registration Form</h1><br />
		<form:form commandName="USER">
		<table>
			<tr><td>Mail Address(*) : </td><td><form:input path="userName" onchange="checkEmail(this.value)" />${emailAvailabilityError}<form:errors id="userError" path="userName" cssStyle="color : red;"/></td></tr>
			<tr><td>First Name(*) : </td><td><form:input path="firstName" /><form:errors path="firstName" cssStyle="color : red;"/></td></tr>
			<tr><td>Last Name(*) : </td><td><form:input path="lastName" /><form:errors path="lastName" cssStyle="color : red;"/></td></tr>
			<tr><td>Password(*) : </td><td><form:password path="password" /><form:errors path="password" cssStyle="color : red;"/></td></tr>
			<tr><td>Password(*)(Repeat) : </td><td><form:password path="confirmPassword" /><form:errors path="confirmPassword" cssStyle="color : red;"/></td></tr>
			<tr><td>Telephone : </td><td><form:input path="telephone" /><form:errors path="telephone" cssStyle="color : red;"/></td></tr>
			<tr><td>Company : </td><td><form:input path="companyName" id="companyName"/><form:errors path="companyName" cssStyle="color : red;"/></td></tr>
			<tr><td>Logo : </td></tr>
			<tr><td><input type="reset" value="Reset" /></td><td><input type="submit" value="Save Changes" /></td></tr>
		</table>
		</form:form>
	</body>
</html>