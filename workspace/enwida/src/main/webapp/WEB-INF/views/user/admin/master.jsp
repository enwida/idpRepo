<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!doctype html>
<html>
<head>
<title>Enwida Admin Page</title>
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script src="../../resources/js/admin/jquery.tablesorter.js"></script>
<script src="../../resources/js/admin/admin.js"></script>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<link rel="stylesheet" href="../../resources/css/table.css" />
</head>
<body>
<script>
$(function() {
    $(".tablesorter").tablesorter(); 
  });
</script>
	<img src="../../resources/images/banner2.jpg">
	<div>
		<div id="nav">
			<a href="userList">UserList</a>
			 <a href="editGroup">GroupList</a>
			 <a	href="roleList">roleList</a>
		</div>
	</div>
	<div>
		<a Style="color : red;">${error}</a><br>
		<a Style="color : blue;">${info}</a><br>
		<jsp:include page="${content}.jsp"></jsp:include>
	</div>
</body>
</html>