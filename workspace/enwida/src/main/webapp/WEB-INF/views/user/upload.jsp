<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta charset="utf-8" />
<title>Upload User Data</title>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<!-- <link rel="stylesheet" href="/resources/demos/style.css" /> -->
<script>
$(function() {
	
	$( "#upload-file-form-div" ).dialog({
		autoOpen: false,
		height: 300,
		width: 450,
		modal: true,
		buttons: {
			"Upload File": function() {
				$("#upload-file-form").submit();				
			},
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			//TODO: Perform actions on closing of Dialog
		}
	});
	
	$( "#replace-file-form-div" ).dialog({
		autoOpen: false,
		height: 300,
		width: 450,
		modal: true,
		buttons: {
			"Replace File": function() {
				$("#replace-file-form").submit();				
			},
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			//TODO: Perform actions on closing of Dialog
		}
	});

	$( "#upload-file" )
		.button()
		.click(function() {
			$( "#upload-file-form-div" ).dialog( "open" );
		});
	
	$( "#replace-file" )
	.button()
	.click(function() {
		$( "#replace-file-form-div" ).dialog( "open" );
	});
});
</script>
</head>
<body>

	<div style="text-align: center;">
		<h2>Import CSV File</h2>
	</div>

	<div style="clear: both"></div>
	<div style="width: 100%">
		<div style="width: 30%; float: left;"></div>
		<div style="text-align: center; width: 40%; margin-left: 35%">
			<button id="upload-file">Upload File</button>

			<form:form method="POST" commandName="fileReplace"
				enctype="multipart/form-data">
				<table border="1">
					<tr>
						<th>Id</th>
						<th>File Name</th>
						<th>Upload Date</th>
						<th>Revision</th>
						<th>Delete</th>
						<th>Replace</th>
						<th>Download</th>
					</tr>
					<c:choose>
						<c:when test="${not empty uploadedfiletable}">
							<c:forEach var="file" items="${uploadedfiletable}">
								<tr>
									<td><c:out value="${file.id}" /></td>
									<td><c:out value="${file.displayFileName}" /></td>
									<td><c:out value="${file.displayUploadDate}" /></td>
									<td><c:out value="${file.revision}" /></td>
									
									<td><input type="file" name="file" /></td>
									
									<td><button id="replace-file">Replace File</button></td>
									
									<td><a target="_blank"
										href="./files/<c:out value='${file.id}'/>">download</a></td>
								</tr>
							</c:forEach>

						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="7">No data found</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</table>
			</form:form>
			<c:if test="${not empty errormsg}">
				<small><font color="red"> <c:out value="${errormsg}" />
				</font></small>
			</c:if>
			<c:out value="${successmsg}" />
		</div>
		<div style="width: 30%;"></div>
	</div>
	<div style="clear: both"></div>

	<!-- FileUpload Div -->
	<div id="upload-file-form-div" title="Upload new File">
		<p class="validateTips">All form fields are required.</p>

		<form:form method="POST" id="upload-file-form" modelAttribute="fileUpload" enctype="multipart/form-data" >
			<form:errors path="*" cssClass="errorblock" element="div" />
				Please Select a file to upload: 
				<form:input path="file" type="file" name="file" />
			<!-- <input type="submit" value="Upload" /> -->
			<span> 
				<form:errors path="file" cssClass="error" /> 
				<c:out value="${invalidFileMessage}"></c:out>
			</span>
		</form:form>
	</div>
	<!-- /FileUpload Div -->
	
	<!-- FileReplace Div -->
	<div id="replace-file-form-div" title="Replace File">
		<p class="validateTips">All form fields are required.</p>

		<form:form method="POST" id="replace-file-form" modelAttribute="fileReplace" enctype="multipart/form-data" >
			<form:errors path="*" cssClass="errorblock" element="div" />
				Please Select a file to upload: 
				<form:input path="file" type="file" name="file" />
			<!-- <input type="submit" value="Upload" /> -->
			<span> 
				<form:errors path="file" cssClass="error" /> 
				<c:out value="${invalidFileMessage}"></c:out>
			</span>
		</form:form>
	</div>
	<!-- /FileReplace Div -->
</body>
</html>