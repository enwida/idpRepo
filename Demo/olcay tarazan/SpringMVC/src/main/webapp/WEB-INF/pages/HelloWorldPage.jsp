<html>
    <head>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="resources/jquery.js"></script>
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.js" type="text/javascript"></script>
    </head>
    <body>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript">
            google.load("visualization", "1", {packages: ["corechart"]});
            google.setOnLoadCallback(

            function () {
                drawChart();
            });


            function drawChart() {
                var options = {
                    title: 'Company Performance',
                    'width': 400,
                    'height': 300
                };
				var data = google.visualization.arrayToDataTable([
                    ['Year', 'Sales', 'Expenses', 'Other', 'Other1', 'Other2', 'Other3'],
                    ['2004', 1000, 400, 402, 403, 400, 410],
                    ['2005', 1170, 460, 300, 420, 430, 400]
                ]);
                var view = new google.visualization.DataView(data);
                var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
                chart.draw(view, options);
                google.visualization.events.addListener(chart, 'select', function() {
					var selection = chart.getSelection();
					var selectedItem = selection[0];
					alert(selectedItem);
					drawChart();

				});
            }
            
        </script>
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.js" type="text/javascript"></script>
        <div id="chart_div"></div>
    	<h2>${msg}</h2>
            <link href="resources/bootstrap.css" rel="stylesheet">
		    <link href="resources/datepicker.css" rel="stylesheet">
		     <input id="dp2" type="text">
		    <script src="resources/jquery.js"></script>
		    <script src="resources/bootstrap-datepicker.js">
			</script>
			<script>
				$(function(){
					$('#dp2').datepicker();
				});
			</script>
    </body>

</html>