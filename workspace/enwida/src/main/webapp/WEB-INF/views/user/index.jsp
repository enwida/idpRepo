<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="message"%>
<%@ page session="false"%>

<h1><message:message code="de.enwida.userManagement.hello" />:${username}</h1>
<a href="admin/"><message:message code="de.enwida.userManagement.admin" /></a>

<a href="download"><message:message code="de.enwida.userManagement.download" /></a>
<a href="${userStatusURL}">${userStatus}</a>
