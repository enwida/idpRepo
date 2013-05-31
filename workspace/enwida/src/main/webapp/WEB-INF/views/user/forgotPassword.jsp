<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Login Page</title>
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
	<h3>Forgot Password</h3>
 
	<a>${error}</a>
 
	<form name='f' method='POST'>
 
		<table>
			<tr>
				<td>Email:</td>
				<td><input type='text' name='email' value=''>
				</td>
			</tr>
			<tr>
				<td colspan='2'><input type="submit"	value="GetPassword" />
				</td>
			</tr>
		</table>
 
	</form>
</body>
</html>