<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Enwida Admin Page</title>
	<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
	<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
	<script src="../resources/js/admin/jquery.tablesorter.js"></script>
	<script src="../resources/js/admin/enwida.js"></script>
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
	<link rel="stylesheet" href="../resources/css/table.css" />
</head>
<body>
	<img src="../resources/images/banner2.jpg">
	<jsp:include page="${content}.jsp"></jsp:include>
</body>
</html>