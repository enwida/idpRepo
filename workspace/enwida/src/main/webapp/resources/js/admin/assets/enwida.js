//sets all sortable tables
$(function () {
	 $.extend($.tablesorter.themes.bootstrap, {
		    // these classes are added to the table. To see other table classes available,
		    // look here: http://twitter.github.com/bootstrap/base-css.html#tables
		    table      : 'table table-bordered',
		    header     : 'bootstrap-header', // give the header a gradient background
		    footerRow  : '',
		    footerCells: '',
		    icons      : '', // add "icon-white" to make them white; this icon class is added to the <i> in the header
		    sortNone   : 'bootstrap-icon-unsorted',
		    sortAsc    : 'icon-chevron-up',
		    sortDesc   : 'icon-chevron-down',
		    active     : '', // applied when column is sorted
		    hover      : '', // use custom css here - bootstrap class may not override it
		    filterRow  : '', // filter row class
		    even       : '', // odd row zebra striping
		    odd        : ''  // even row zebra striping
		  });

		  // call the tablesorter plugin and apply the uitheme widget
		  $(".tablesorter").tablesorter({
		    // this will apply the bootstrap theme if "uitheme" widget is included
		    // the widgetOptions.uitheme is no longer required to be set
		    theme : "bootstrap",

		    widthFixed: true,

		    headerTemplate : '{content} {icon}', // new in v2.7. Needed to add the bootstrap icon!

		    // widget code contained in the jquery.tablesorter.widgets.js file
		    // use the zebra stripe widget if you plan on hiding any rows (filter widget)
		    widgets : [ "uitheme", "filter", "zebra" ],

		    widgetOptions : {
		      // using the default zebra striping class name, so it actually isn't included in the theme variable above
		      // this is ONLY needed for bootstrap theming if you are using the filter widget, because rows are hidden
		      zebra : ["even", "odd"],

		      // reset filters button
		      filter_reset : ".reset"

		      // set the uitheme widget to use the bootstrap theme class names
		      // this is no longer required, if theme is set
		      // ,uitheme : "bootstrap"

		    }
		  })
		  .tablesorterPager({

		    // target the pager markup - see the HTML block below
		    container: $(".pager"),

		    // target the pager page select dropdown - choose a page
		    cssGoto  : ".pagenum",

		    // remove rows from the table to speed up the sort of large tables.
		    // setting this to false, only hides the non-visible rows; needed if you plan to add/remove rows with the pager enabled.
		    removeRows: false,

		    // output string - default is '{page}/{totalPages}';
		    // possible variables: {page}, {totalPages}, {filteredPages}, {startRow}, {endRow}, {filteredRows} and {totalRows}
		    output: '{startRow} - {endRow} / {filteredRows} ({totalRows})'

		  });
});

//Gets companyname from the mail
function getCompany(email) {
	var company = email.substring(email.indexOf('@') + 1, email.length);
	$("#companyName").val(company);
	getImages(company);
}

//Gets logo from the company name
function getImages(company) {
	$("#companyImages").empty();
	if(company!=null){
		$.ajax({
			  url: "checkImages?company="+company
			}).done(function(data) {
			  $("#companyImages").append(data);
			  $("#companyImages img").click(function() {
				  $("#companyImages img").css('border', "");
				    $(this).css('border', "solid 2px red");  
					$("#companyLogo").val($(this).attr("src"));
			  });
			});
	}
}

//Gets the paremeter from querystring
function get_url_parameter( name )
{
	name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	var regexS = "[\\?&]"+name+"=([^&#]*)";
	var regex = new RegExp( regexS );
	var results = regex.exec( window.location.href );
	if ( results == null )
	  return "";
	else
	  return results[1];
}

var QueryString = function () {
	  // This function is anonymous, is executed immediately and 
	  // the return value is assigned to QueryString!
	  var query_string = {};
	  var query = window.location.search.substring(1);
	  var vars = query.split("&");
	  for (var i=0;i<vars.length;i++) {
	    var pair = vars[i].split("=");
	    	// If first entry with this name
	    if (typeof query_string[pair[0]] === "undefined") {
	      query_string[pair[0]] = pair[1];
	    	// If second entry with this name
	    } else if (typeof query_string[pair[0]] === "string") {
	      var arr = [ query_string[pair[0]], pair[1] ];
	      query_string[pair[0]] = arr;
	    	// If third or later entry with this name
	    } else {
	      query_string[pair[0]].push(pair[1]);
	    }
	  } 
	    return query_string;
	} ();
	


function enableDisableUser(userID,checked){
	$.ajax({
     	  url: "enableDisableUser?userID="+userID+"&enabled="+checked
    });
};

function enableDisableAspect(userID,checked){
	$.ajax({
     	  url: "enableDisableAspect?rightID="+userID+"&enabled="+checked
    });
};

function enableAutoPass(groupID,checked){
	$.ajax({
     	  url: "enableAutoPass?groupID="+groupID+"&enabled="+checked
    });
};
