<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<title>Enwida Home Page</title>
</head>
<body>

<h2><spring:message code="de.enwida.status.siteinprogress" /></h2>

<span style="float: right">
    <a href="?lang=en">en</a> | <a href="?lang=de">de</a>
</span>
  <a href="user/"><spring:message code="de.enwida.userManagement.dashboard" /></a>
  <a href="user/register"><spring:message code="de.enwida.userManagement.registration" /></a>

</body>
</html>