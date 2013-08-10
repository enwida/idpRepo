<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="message"%>
<html>
<body>
	<h3>You are about the logout User</h3>	
	<a href="<c:url value="/j_spring_security_logout" />" > <message:message code="de.enwida.logout" /></a>
</body>
</html>