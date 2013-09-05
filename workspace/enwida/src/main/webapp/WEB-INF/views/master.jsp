<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!doctype html>
<html>
<head>
	<script src="~/resources/js/admin/assets/assets.js"></script>
	<link rel="stylesheet" href=<c:url value="resources/css/admin/assets/assets.css"/> />
</head>
<body>
	<div><br><br>
		<a Style="color: red;">${error}</a><br> <a Style="color: blue;">${info}</a><br>
		<jsp:include page="${content}.jsp"></jsp:include>
	</div>
</body>
</html>