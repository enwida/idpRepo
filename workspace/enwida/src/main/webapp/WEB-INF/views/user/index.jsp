<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>

<h1>Hello : ${username}</h1>
<a href="admin/">admin</a>
<a href="${userStatusURL}">${userStatus}</a>

<a href="download">download</a>
<a href="<c:url value="/j_spring_security_logout" />" > Logout</a>
