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
		<c:forEach var="right" items="${aspectRights}">
			<tr>
				<td>${right.aspectName}</td>
				<td>${right.product}</td>
				<td>${right.t1}</td>
				<td>${right.t2}</td>
				<td>${right.resolution}</td>
				<td>${right.tso}</td>
				<td><input type="checkbox" checked="${right.enabled}"/></td>
			<tr>
		</c:forEach>
	</tbody>
</table>
</div>