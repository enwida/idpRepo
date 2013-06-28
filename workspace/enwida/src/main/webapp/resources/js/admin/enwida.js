function getCompany(email) {
	var company = email.substring(email.indexOf('@') + 1, email.length);
	$("#companyName").val(company);
}

function getImages(email) {
	var company = email.substring(email.indexOf('@') + 1, email.length);
	document.getElementById("companyName").value = company;
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
