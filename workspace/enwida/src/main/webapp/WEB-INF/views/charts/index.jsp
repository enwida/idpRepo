<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!doctype html>
<html>
<head>
	<title>Enwida Home Page</title>
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
<div>
	<h3>Common requests</h3>
	<ul id="templates">
		<li><a href="#chart">lines?chartId=0&product=212&startTime=2010-12-30&endTime=2010-12-31&resolution=HOURLY</a></li>
		<li><a href="#chart">lines?type=rl_ab1&product=211&startTime=2010-12-01&endTime=2010-12-31&resolution=DAILY</a></li>
		<li><a href="#chart">lines?type=rl_ab2&product=311&startTime=2010-12-20&endTime=2010-12-31&resolution=DAILY</a></li>
		<li><a href="#chart">lines?type=rl_abg1&product=314&startTime=2009-01-01&endTime=2010-01-01&resolution=WEEKLY</a></li>
		<li><a href="#chart">lines?type=rl_vol1&product=211&startTime=2008-01-01&endTime=2011-01-01&resolution=MONTHLY</a></li>
		<!--<li><a href="#">lines?type=rl_geb1&product=211&startTime=2010-12-01&endTime=2010-12-31&resolution=DAILY</a></li>-->
	</ul>
</div>
<div id="chart" data-chart-id="0">
	<h3></h3>
</div>

</body>
</html>