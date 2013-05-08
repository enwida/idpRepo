require.config({
    baseUrl: "resources/js/lib",
  });

require(["line_chart", "bar_chart", "carpet_chart"], 
  function(LineChart, BarChart, CarpetChart) {
	
  $.ajax("data.json?type=rl_ab1&pro=210&res=15min&t1=20101230&locale=en", {
	  success: drawExample
  });

  function drawExample(data) {
	  var parseDate = d3.time.format("%H:%M").parse;
	  var chartData = data.rows.map(function(row) {
	    var x = parseDate(row.c[0].v);
	    var y = row.c[1].v;
	    return { x: x, y: y };
	  });
	
	  var lineChart = LineChart.init({
	    parent: "#chart",
	    data: chartData,
	    scale: {
	      x: {
	        type: "date",
	      }
	    }
	  });
	  lineChart.draw();
  }
});
