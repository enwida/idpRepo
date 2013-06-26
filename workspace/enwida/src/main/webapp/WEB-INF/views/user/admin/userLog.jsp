<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>

User Details
    <textarea name="user_eingabe" cols="50" rows="10">    
    	<c:out value="${userLog}" />
    </textarea>