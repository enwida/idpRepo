google.load("visualization", "1", {packages: ["corechart"]});
google.setOnLoadCallback(
function () {
	var chart="rl_ab1";
	load(chart);
	setupPage(chart);
});

function setupPage(chart){
	//All event bindings can be done here
	$(function() {
		
//		$('#downloadLink').click(function(){ downloadCsv(chart); return false; });
	    
		$("#datepicker").datepicker({
	        changeMonth: true,
	        changeYear: true,
	        setDate: new Date(),
	        onChangeMonthYear: function() {
			   var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
	 	       var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
		 	   $(this).datepicker('setDate', new Date(year, month, 1));
	 		   }
	    });

	    $( "#datepicker" ).datepicker( "setDate", new Date());
	});
}


function drawChart(data,options) {
    	var googleDataTable = new google.visualization.DataTable(data); 
        var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
        chart.draw(googleDataTable, options);
}

function load(){
   	$.ajax({
     	  url: "../init.json",
     	  success: function(options){
     		 loadChartData(options);
     	  }
    });
}    

function loadChartData(options){
	$.ajax({
     	  url: "../data.json?"+
     		   "aspect="+$("#aspect").val() +
     		   "&pro=211&res=1h&locale=en"+
     		  // "&t1="+$(instance).datepicker('getDate').format('yyyyMMdd'),
     		   "&t1=20101227" +
     		   "&t2=20101231",
     	  success: function(data){
     		 drawChart(data,options);
     	  }
    });
}	        


function chgRange(chart,id){
	var instance="#datepicker";
	
	//default remove unused events
	$(instance).empty();
	$('#ui-datepicker-div .ui-datepicker-calendar tr').off();
	$('#ui-datepicker-div .ui-datepicker-calendar tr').off();
	if (id==0){ //day
		$(instance).datepicker("option", "dateFormat", "mm/dd/yy" );
	}	
	if (id==1){ //week
		setWeekRange(chart);
		$(instance).datepicker("option", "dateFormat", "mm/dd/yy" );
		$('.ui-datepicker-calendar tr').on('mousemove', function() { $(this).find('td a').addClass('ui-state-hover'); });
		$('.ui-datepicker-calendar tr').on('mouseleave', function() { $(this).find('td a').removeClass('ui-state-hover'); });
	}
	else if (id==2){//month
		$(instance).datepicker("option", "dateFormat", "mm/yy" );		
		$(instance).html('<style> .ui-datepicker-calendar { display: none;}</style>'); 

	}
	else if (id==3){//year 
		$(instance).datepicker("option", "dateFormat", "yy" );
		$(instance).html('<style> ui-corner-all { display: none;}</style>'); 
		$(instance).html('<style> .ui-datepicker-calendar,.ui-datepicker-month,.ui-datepicker-prev,.ui-datepicker-next  { display: none;}</style>'); 

	}
}

function downloadCsv(chart){
	var instance="#datepicker";
   	var url= "../export?"+
   		   "type="+chart+"&pro=210&res=15min&locale=en"+
   		   "&t1="+$(instance).datepicker('getDate').format('yyyyMMdd');

	$(location).attr('href',url);
}

function setWeekRange(chart) {
$(function() {
	var startDate;
	var instance="#datepicker";
    $(instance).datepicker( {
        onSelect: function(dateText, inst) { 
            var date = $(this).datepicker('getDate');
            var startDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() - date.getDay());
			alert(startDate);
            
            $(instance).find('.ui-datepicker-current-day a').addClass('ui-state-active');
        },
        beforeShowDay: function(date) {
            var cssClass = '';
            if(date >= startDate && date <= endDate)
                cssClass = 'ui-datepicker-current-day';
            return [true, cssClass];
        }
    });
   });

}
