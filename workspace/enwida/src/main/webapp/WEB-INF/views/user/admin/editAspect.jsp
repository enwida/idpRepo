<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<div>
	<h1>
		Role: <select>
			<option value="enwida">admin</option>
		</select>

	</h1>
	<table id="tblAspects" class="tablesorter">
	<thead>
		<tr>
			<th>Aspect</th>
			<th>Product</th>
			<th>T1</th>
			<th>T2</th>
			<th>Res</th>
			<th>tso</th>
			<th>Enable/disabled</th>
		<tr>
	<thead>
	<tbody>
		<c:forEach var="group" items="${aspects}">
			<tr>
				<td>${group.aspectName}</td>
				<td>${group.product}</td>
				<td>${group.t1}</td>
				<td>${group.t2}</td>
				<td>${group.res}</td>
				<td>${group.tso}</td>
				<td><input type="checkbox"/></td>
			<tr>
		</c:forEach>
	</tbody>
</table>
</div>