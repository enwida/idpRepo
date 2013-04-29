google.load("visualization", "1", {packages:["corechart"]});
google.setOnLoadCallback(init);

$(document).ready(function() {
	// Set up datepickers
	var dateFormat = "yy-mm-dd";
	$("#dateFrom").datepicker({
		dateFormat:  dateFormat,
		minDate: "2013-04-01"
	});
	$("#dateFrom").datepicker("setDate", "2013-04-01");
	$("#dateTo").datepicker({
		dateFormat: dateFormat,
	});
	$("#dateTo").datepicker("setDate", new Date());
	
	// Set up chart refreshing triggers
	$("#dateFrom").change(refreshChart);
	$("#dateTo").change(refreshChart);
	$("#refreshChart").click(refreshChart);
});

function init() {
	// Render data initially
	refreshChart();
}

function refreshChart() {
	// Get the dates from the datepickers
	var start = $("#dateFrom").datepicker("getDate");
	var end = $("#dateTo").datepicker("getDate");
	
	// Let the end date point to the end of the corresponding day
	if (end) {
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
	}
	
	// Fetch data and redraw the chart
	fetchDataPoints(start, end, drawChartWithDataPoints);
}

function drawChart(chartData) {
  var options = {
	  title: 'Occupation',
	  hAxis: {
		  format: "yyyy-MM-dd HH:mm"
	  },
	  vAxis: {
		  baseline: 0,
		  minValue: 0,
		  maxValue: 4
	  }
  };
  
  var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
  chart.draw(chartData, options);
}

function drawChartWithDataPoints(dataPoints) {
	var dataPointsJson = JSON.parse(dataPoints);
	var chartData = initChartData();
	
	addDataPoints(chartData, dataPointsJson);
	drawChart(chartData);
}


function fetchDataPoints(start, end, callback) {
	// Convert ms to seconds / assign default values
	start = (start / 1000) || 0;
	end = (end / 1000) || 2147483647;
	
	// Request data points from server
	$.ajax("/demo/rangedata.json?start=" + start + "&end=" + end, {
		success: callback,
		error: function(err) {
			alert("Error: " + JSON.stringify(err));
		}
	});
}

function initChartData() {
	var chartData = new google.visualization.DataTable();
	chartData.addColumn("datetime", "Timestamp");
	chartData.addColumn("number", "Count");
	return chartData;
}

function addDataPoints(chartData, jsonDataPoints) {
	for (var i = 0; i < jsonDataPoints.length; i++) {
		var dataPoint = jsonDataPoints[i];
		
		// Convert data point to chart data row
		var epochSecs = parseInt(dataPoint["timestamp"]);
		var timestamp = dateFromEpochSecs(epochSecs);
		chartData.addRow([timestamp, dataPoint["count"]]);
	}
}

function dateFromEpochSecs(secs) {
	var result = new Date(0);
	result.setSeconds(secs, 0);
	return result;
}
