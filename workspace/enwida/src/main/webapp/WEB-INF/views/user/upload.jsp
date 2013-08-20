<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta charset="utf-8" />
<title>Upload User Data</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script src="/enwida/resources/js/chart/date.format.js"></script>
<!-- <link rel="stylesheet" href="/resources/demos/style.css" /> -->
<script>
$(function() {
	var fileIdToDelete =  -1;
	var fileIdToGetRevisions =  -1;
	
	<c:forEach var="file" items="${uploadedfiletable}">		
		$( "#replace-file-${file.id}" )
			.button()
			.click(function() {
				$("#fileIdToBeReplaced").val($(this).attr("id").split("-")[2]);				
				$("#replace-file-form-div" ).dialog( "open" );
		});
		
		$( "#download-file-${file.id}" )
			.button()
			.click(function() {				
		});
		
		$( "#delete-file-${file.id}" )
			.button()
			.click(function(event) {
				fileIdToDelete = $(this).attr("id").split("-")[2];
				$( "#delete-file-form-div" ).dialog( "open" );				
		});
		
		$( "#show-revisions-file-${file.id}" )
			.button()
			.click(function(event) {
				fileIdToGetRevisions = $(this).attr("id").split("-")[3];
				$( "#show-revisions-file-div" ).dialog( "open" );				
		});	
		
	</c:forEach>
	
	$( "#upload-file" )
		.button()
		.click(function() {
			$( "#upload-file-form-div" ).dialog( "open" );
	});
		
	$( "#upload-file-form-div" ).dialog({
		autoOpen: false,
		height: 300,
		width: 450,
		modal: true,
		buttons: {
			"Upload File": function() {
				$("#upload-file-form").submit();
				$( this ).dialog( "close" );
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
				$( this ).dialog( "close" );
			},
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			$("#fileIdToBeReplaced").val("");
		}
	});
	
	$( "#delete-file-form-div" ).dialog({
		autoOpen: false,
		resizable: false,
	    height:300,
	    width: 450,
	    modal: true,
	    buttons: {
	    	"Delete this file": function() {
	    		
	    		$.ajax({
	    	         url: "<c:url value='/user/files/delete' />",
	    	         type: 'GET',            
	    	         data:  { 	
	    	        	 	fileId : fileIdToDelete,
	    	         },
	    	         success: function(result) {
	    	        	 $( "#delete-file-form-success-div" ).dialog( "open" );
	    	         }, 
	    	         error: function(xhr, ajaxOptions, thrownError) {
	    	        	 $( "#delete-file-form-failure-div" ).dialog( "open" );
	    	       	 }
	    	     });
	    		
	    		$( this ).dialog( "close" );
	    	},
	    	Cancel: function() {
	    		$( this ).dialog( "close" );
	    	}
		},
		close: function() {
			fileIdToDelete = -1;
		}
	});
	
	$( "#delete-file-form-failure-div" ).dialog({
		autoOpen: false,
		height: 200,
		width: 350,
		modal: true,
		buttons: {
			Ok: function() {
				$( this ).dialog( "close" );
			}
		}
	});
	
	$( "#delete-file-form-success-div" ).dialog({
		autoOpen: false,
		height: 200,
		width: 350,
		modal: true,
		buttons: {
			Ok: function() {
				$( this ).dialog( "close" );
			}
		}
	});
	
	$( "#show-revisions-file-div" ).dialog({
		autoOpen: false,
		height: 400,
		width: 650,
		modal: true,
		open: function() {
			
			$.ajax({
   	         url: "<c:url value='/user/files/revisions' />",
   	         type: 'GET',
   	         data:  { 	
   	        	 	fileId : fileIdToGetRevisions,
   	         },
   	         success: function(result) {
   	        	$("#show-revisions-file-div-inner").html("");
   	        	$.each(result, function(i, item) {
   	        		$("#show-revisions-file-div-inner").append("<p><span style=\"float: left; margin: 0 7px 20px 0;\"></span><div style=\"display: table-row;\">" + 
   	        		"<div style=\"display: table-cell;padding: 5px;\"> " + item.id + "</div>" + 
   	        		"<div style=\"display: table-cell;padding: 5px;\"> " + item.displayFileName + "</div>" + 
   	        		"<div style=\"display: table-cell;padding: 5px;\"> " + (new Date(item.uploadDate)).format() + "</div>" + 
   	        		"<a class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" style=\"display: table-cell;padding: 5px;\" href=\"<c:url value='/user/files/" + item.id + "' />\" id=\"download-file-" + item.id + "\">Download</a>" + 
   	        		"<button class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" style=\"display: table-cell;padding: 5px;\" id=\"delete-file-" + item.id + "\">Delete</button> </div></p>");
   	        	});
   	         }, 
   	         error: function(xhr, ajaxOptions, thrownError) {
   	        	alert(thrownError);
   	       	 }
   	     });
	    },
		buttons: {
			Ok: function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			fileIdToGetRevisions = -1;
		}
	});
	
});
</script>
</head>
<body>

	<div style="text-align: center;"></div>

	<div style="clear: both"></div>
	<div style="width: 100%">
		<div style="width: 15%; float: left;"></div>
		<div style="text-align: center; width: 70%; margin-left: 15%">
			<h2>Import CSV File</h2>
			<button id="upload-file">Upload File</button>
			<br/>
			<br/>
			<table border="1">
				<tr>
					<th>Id</th>
					<th>File Name</th>
					<th>Upload Date</th>
					<th>Revision</th>
					<th>Options</th>
				</tr>
				<c:choose>
					<c:when test="${not empty uploadedfiletable}">
						<c:forEach var="file" items="${uploadedfiletable}">
							<tr>
								<td><c:out value="${file.id}" /></td>
								<td><c:out value="${file.displayFileName}" /></td>
								<td><c:out value="${file.displayUploadDate}" /></td>
								<td>
									<c:choose>
										<c:when test="${file.revision > 1}">
											<button id="show-revisions-file-${file.id}">Show Revisions</button>
										</c:when>
										<c:otherwise>
											<c:out value="${file.revision}" />
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									<button id="replace-file-${file.id}">Replace</button>
									<a href="<c:url value='/user/files/${file.id}' />" id="download-file-${file.id}">Download</a>
									<button id="delete-file-${file.id}">Delete</button>
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="5">No data found</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>

			<c:if test="${not empty errormsg}">
				<small><font color="red"> <c:out value="${errormsg}" />
				</font></small>
			</c:if>
			<c:out value="${successmsg}" />
		</div>
		<div style="width: 15%;"></div>
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

		<form:form method="POST" id="replace-file-form" modelAttribute="fileReplace" enctype="multipart/form-data" action="upload/replace" >
			<form:errors path="*" cssClass="errorblock" element="div" />
				Please Select a file to upload: 
				<form:input path="file" type="file" name="file" />
				<form:input path="fileIdToBeReplaced" type="hidden" id ="fileIdToBeReplaced" name="fileIdToBeReplaced"  value="" />
			<span> 
				<form:errors path="file" cssClass="error" /> 
				<c:out value="${invalidFileMessage}"></c:out>
			</span>
		</form:form>
	</div>
	<!-- /FileReplace Div -->
	
	<!-- FileDelete Div -->
	<div id="delete-file-form-div" title="Delete File">
		<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>This file will be permanently deleted and cannot be recovered. Are you sure?</p>		
	</div>
	<!-- /FileDelete Div -->
	
	<!-- Show Revisions File Div -->
	<div id="show-revisions-file-div" title="File Revisions">
		<div id="show-revisions-file-div-inner" style="display: table;">
		</div>
	</div>
	<!-- /Show Revisions File Div -->
	
	<!-- FileDelete Success Div -->
	<div id="delete-file-form-success-div" title="File Deleted">
		<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>File has been deleted successfully.</p>		
	</div>
	<!-- /FileDelete Success Div -->
	<!-- FileDelete Failure Div -->
	<div id="delete-file-form-failure-div" title="File Not Deleted">
		<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>File was not deleted successfully.</p>		
	</div>
	<!-- /FileDelete Failure Div -->
</body>
</html>