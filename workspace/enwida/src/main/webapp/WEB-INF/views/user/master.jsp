<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!doctype html>
<html>
<head>
<script src="../../resources/js/admin/assets/admin.js"></script>
<script src="../../resources/js/admin/assets/jquery.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script src="../../resources/js/admin/assets/jquery.tablesorter.js"></script>
<script src="../../resources/js/admin/assets/jquery.tablesorter.widgets.js"></script>
<script src="../../resources/js/admin/assets/jquery.tablesorter.pager.js"></script>
<script src="../../resources/js/admin/assets/enwida.js"></script>
<link rel="stylesheet" href="../../resources/css/admin/assets/assets.css" />
</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div>
			<div>
				<ul class="nav navbar-nav">
					<li><a href="http://localhost:8080/enwida/user/">Enwida</a></li>
					<li><a href="admin_userlist">Users</a></li>
					<li><a href="admin_editgroup">Groups</a></li>
					<li><a href="admin_rolelist">Roles</a></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
	<div><br><br>
		<a Style="color: red;">${error}</a><br> <a Style="color: blue;">${info}</a><br>
		<jsp:include page="${content}.jsp"></jsp:include>
	</div>
</body>
</html>