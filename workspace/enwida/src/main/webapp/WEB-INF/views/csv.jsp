<%@page contentType="text/html;charset=UTF-8"%><%@page pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%><fmt:message key="${title}"/>;
<fmt:message key="de.enwida.common.description"/>:;<fmt:message key="${description}"/>;
<fmt:message key="de.enwida.common.product"/>:;<fmt:message key="${type}"/> <fmt:message key="${sign}"/> <fmt:message key="${block}"/>;
<fmt:message key="de.enwida.common.datafrom"/>:;${from};
<fmt:message key="de.enwida.common.datato"/>:;${to};

${content}
