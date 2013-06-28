function getCompany(email) {
	var company = email.substring(email.indexOf('@') + 1, email.length);
	$("#companyName").val(company);
	getImages(company);
}

function getImages(company) {
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

//function checkEmail(email) {
//	if (email == "") {
//		return;
//	}
//
//	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
//		xmlhttp = new XMLHttpRequest();
//	} else {// code for IE6, IE5
//		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
//	}
//	xmlhttp.onreadystatechange = function() {
//		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
//			var avail = xmlhttp.responseText;
//			if (avail == "true") {
//				document.getElementById("userError").innerHTML = "This email is already in use by some other user.";
//				document.getElementById("companyName").value = "";
//			} else {
//				document.getElementById("userError").innerHTML = "";
//				getCompany(email);
//			}
//		}
//	};
//
//	xmlhttp.open("GET", "checkEmail?email=" + email, true);
//	xmlhttp.send();
//}
