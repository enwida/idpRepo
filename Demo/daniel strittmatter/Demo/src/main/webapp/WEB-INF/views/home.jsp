<%@ page session="false" %>
<html>
<head>
	<title>Chart Demo</title>
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" />
	<link rel="stylesheet" href="/demo/resources/bootstrap.min.css" />
	<link rel="stylesheet" href="/demo/resources/style.css" />
	<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
	<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
	<script src="https://www.google.com/jsapi"></script>
	<script src="/demo/resources/script.js"></script>
</head>
<body>
	<h1>Room Occupation Statistics</h1>
	<div id="daterange">
		<h4>Date Range:</h4>
		<p><input type="text" id="dateFrom" /> - <input type="text" id="dateTo" /></p>
		<p><input type="button" id="refreshChart" value="Refresh" /></p>
	</div>	

	<div id="chart_div" style="width: 900px; height: 500px;"></div>
</body>
</html>
