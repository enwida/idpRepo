<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload User Data</title>
<style>
.error {
	color: #ff0000;
}

.errorblock {
	color: #000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding: 8px;
	margin: 16px;
}
</style>
</head>
<body>

	<div style="text-align: center;">
		<h2>Import CSV File</h2>
	</div>

	<div style="clear: both"></div>
	<div style="width: 100%">
		<div style="width: 30%; float: left;"></div>
		<div style="text-align: center; width: 40%; margin-left: 35%">

			<form:form method="POST" commandName="fileUpload"
				enctype="multipart/form-data">
				<form:errors path="*" cssClass="errorblock" element="div" />
 				lease Select a file to upload: 
 				<input type="file" name="file" />
				<input type="submit" value="Upload" />

				<span> <form:errors path="file" cssClass="error" /> <c:out
						value="${invalidFileMessage}"></c:out>
				</span>
			</form:form>
			
		</div>
		<div style="width: 30%;"></div>
	</div>
	<div style="clear: both"></div>
</body>
</html>