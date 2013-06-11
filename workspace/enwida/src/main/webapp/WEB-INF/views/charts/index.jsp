<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!doctype html>
<html>
<head>
	<title>Enwida Home Page</title>
	<link rel="stylesheet" href="/enwida/resources/css/bootstrap.css" >
	<link rel="stylesheet" href="/enwida/resources/css/datepicker.css" >
	<link rel="stylesheet" href="/enwida/resources/css/tipsy.css" >
	<link rel="stylesheet" href="/enwida/resources/css/chart.css" >
    <script src="/enwida/resources/js/chart/assets.js"></script>
    <script src="/enwida/resources/js/chart/charts.js"></script>
    
    <style>
    	li {
    		margin: 10px 0;
    	}
    	a {
    		color: steelblue;
    		text-decoration: none;
    	}
    	a:hover {
    		border-bottom: 1px dotted steelblue;
    	}
    	#chart {
    		margin-top: 60px;
    	}
    	#chart h3 {
    		margin-left: 20px;
    	}
    	.legend-box {
    		fill: transparent;
    		stroke: #222;
    	}
    	.legend-items {
    		font-size: 16px;
    	}
    </style>
</head>
<body>

<h1>Chart Experiments</h1>

<input type="text" id="url" style="width: 80%; padding: 10px; font-size: 13px; margin: 10px;" />
<div class="chart" data-chart-id="0"></div>
<div class="chart" data-chart-id="1"></div>

</body>
</html>