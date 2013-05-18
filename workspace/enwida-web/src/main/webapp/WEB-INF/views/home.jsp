<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
	<link rel="stylesheet" href="resources/css/chart.css" />
	<script src="resources/js/assets.min.js"></script>
	<script src="resources/js/main.js"></script>
</head>
<body>
<h1>
	<h1>Hello : ${username}</h1>	
</h1>
<div id="chart"></div>
<a id="login" href="login">login</a>
</body>
</html>