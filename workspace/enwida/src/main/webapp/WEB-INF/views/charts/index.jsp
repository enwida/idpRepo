<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!doctype html>
<html>
<head>
	<title>Enwida Home Page</title>
	<link rel="stylesheet" href="/enwida/resources/css/chart/assets.css" >
	<link rel="stylesheet" href="/enwida/resources/css/chart/chart.css" >
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
<div class="chart" data-chart-id="3" data-width="960" data-chart-type="carpet"></div>
<div class="chart" data-chart-id="2" data-width="960" data-chart-type="minmax"></div>
<div class="chart" data-chart-id="1" data-width="960" data-chart-type="bar"></div>
<div class="chart" data-chart-id="0" data-width="960" data-chart-type="line"></div>
<!--  <div class="chart" data-chart-id="1" data-chart-type="carpet"></div> -->

</body>
</html>