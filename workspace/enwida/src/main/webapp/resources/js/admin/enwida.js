//sets all sortable tables
$(function () {
	$(".tablesorter").tablesorter(); 
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