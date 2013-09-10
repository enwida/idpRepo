<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta charset="utf-8" />
<title>Upload User Data</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="/enwida/resources/js/chart/date.format.js"></script>

<!-- <link rel="stylesheet" href="/resources/demos/style.css" /> -->
<script>

</script>
</head>
<body>
	<div style="text-align: center;"></div>

	<div style="clear: both"></div>
	<div style="width: 100%">
		<div style="width: 15%; float: left;"></div>
		<div style="text-align: center; width: 70%; margin-left: 15%">
			<h2>Import CSV File</h2>
			<br/>
			<br/>

			<c:if test="${not empty errormsg}">
				<small><font color="red"> <c:out value="${errormsg}" />
				</font></small>
			</c:if>
			<c:out value="${successmsg}" />
		</div>
		<div style="width: 15%;"></div>
	</div>
	<div style="clear: both"></div>

	
	<c:choose>
		<c:when test="${userSession.userLoggedIn}">
	<!-- FileReplace Div -->
	<div id="replace-file-form-div" title="Replace File">
		<p class="validateTips">All form fields are required.</p>

		<form:form method="POST" id="replace-file-form" modelAttribute="fileReplace" enctype="multipart/form-data" action="files/replace" >
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
	
	<!-- FileSetDelete Div -->
	<div id="delete-file-set-form-div" title="Delete File">
		<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>This file set will be permanently deleted and cannot be recovered. Are you sure?</p>		
	</div>
	<!-- /FileSetDelete Div -->
	
	<!-- FileDelete Div -->
	<div id="delete-file-form-div" title="Delete File">
		<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>This file will be permanently deleted and cannot be recovered. Are you sure?</p>		
	</div>
	<!-- /FileDelete Div -->
	
	<!-- Show Revisions File Div -->
	<div id="show-revisions-file-div" title="File Revisions">
		<div id="show-revisions-file-div-inner" style="display: table;font-size: 15px;">
		</div>
	</div>
	<!-- /Show Revisions File Div -->
	
	<!-- FileDelete Success Div -->
	<div id="delete-file-form-success-div" title="File Deleted">
		<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Deleted successfully.</p>		
	</div>
	<!-- /FileDelete Success Div -->
	<!-- FileDelete Failure Div -->
	<div id="delete-file-form-failure-div" title="File Not Deleted">
		<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Deletion was not successful.</p>		
	</div>
	<!-- /FileDelete Failure Div -->
	
		<table border="1" id="tblFiles" class="tablesorter">
		<thead>
				<tr>
					<th><spring:message code="de.enwida.upload.file_name" text="File Name" /></th>
					<th><spring:message code="de.enwida.upload.upload_date" text="Upload Date" /></th>
					<th><spring:message code="de.enwida.upload.revision" text="Revision" /></th>
					<th><spring:message code="de.enwida.upload.revision_active" text="Revision Active" /></th>
					<th><spring:message code="de.enwida.upload.aspect" text="Aspect" /></th>
					<th><spring:message code="de.enwida.upload.actions" text="Actions" /></th>
			  </tr>
		</thead>
		<tbody>
		<form name='f' method='POST'>	
			<c:choose>
						<c:when test="${not empty uploadedfiletable}">
							<c:forEach var="file" items="${uploadedfiletable}">
								<tr>
									<%-- <td><c:out value="${file.uploadedFileId.id}" /></td> --%>
									<td><c:out value="${file.displayFileName}" /></td>
									<td><c:out value="${file.displayUploadDate}" /></td>
									<td>
										<button class="btn btn-primary"  id="show-revisions-file-${file.uploadedFileId.id}">Show Revisions</button>
										<%-- <c:choose>
											<c:when test="${file.revision > 1}">
												<button class="btn btn-primary" id="show-revisions-file-${file.uploadedFileId.id}">Show Revisions</button>
											</c:when>
											<c:otherwise>
												<c:out value="${file.revision}" />
											</c:otherwise>
										</c:choose> --%>
									</td>
									<td></td>
									<td><spring:message code="${file.metaData.aspectKey}" text="${file.metaData.aspect}"/></td>
									<td>
										<button class="btn btn-primary" id="replace-file-${file.uploadedFileId.id}-${file.uploadedFileId.revision}">Replace</button>
										<%-- <a href="<c:url value='/upload/files/${file.uploadedFileId.id}' />" id="download-file-${file.uploadedFileId.id}">Download</a> --%>
										<button class="btn btn-primary" id="delete-file-set-${file.uploadedFileId.id}">Delete</button>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="7">No data found</td>
							</tr>
						</c:otherwise>
					</c:choose>
		
		</tbody>
		</form>
		<tfoot>
			<tr>
				<th colspan="7" class="pager form-horizontal">
					<button type="button" class="btn first"><i class="icon-step-backward"></i></button>
					<button type="button" class="btn prev"><i class="icon-arrow-left"></i></button>
					<span class="pagedisplay"></span> <!-- this can be any element, including an input -->
					<button type="button" class="btn next"><i class="icon-arrow-right"></i></button>
					<button type="button" class="btn last"><i class="icon-step-forward"></i></button>
					<select class="pagesize input-mini" title="Select page size">
						<option selected="selected" value="10">10</option>
						<option value="20">20</option>
						<option value="30">30</option>
						<option value="40">40</option>
					</select>
					<select class="pagenum input-mini" title="Select page number"></select>
				</th>
			</tr>
		</tfoot>
		<tfoot>
			<tr>
				<td colspan=7>
					<!-- FileUpload Div -->
						<div id="upload-file-form-div" title="Upload new File">
					
							<form:form method="POST" id="upload-file-form" modelAttribute="fileUpload" enctype="multipart/form-data" >
								<form:errors path="*" cssClass="errorblock" element="div" />
									<div>
									    <label for="aspect">Aspect</label>
									    <form:select path="aspect" name="aspect">
									      <c:forEach var="aspect" items="${aspects}">
									        <option value="${aspect.key}"><spring:message code="${aspect.value}" text="${aspect.key}"/></option>
									      </c:forEach>
									    </form:select>
								    </div>
								    <div>
									    <label for="file">Please Select a file to upload</label>
										<form:input  path="file" type="file" name="file" />
									</div>
								<input class="btn btn-primary" type="submit" value="Upload" />
								<span> 
									<form:errors path="file" cssClass="error" /> 
									<c:out value="${invalidFileMessage}"></c:out>
								</span>
							</form:form>
						</div>
					<!-- /FileUpload Div -->
				</td>
			</tr>
		</tfoot>		
		</table>
		</c:when>
		<c:when test="${not userSession.userLoggedIn}">
			<div>
				Please log in to use upload files feature.
			</div>
			
		</c:when>
	</c:choose>
	
</body>
</html>