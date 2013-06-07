<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<div>
	<h1>Role: ${username}</h1>
	<select id="aspect" onChange="load()">
		<option>VOL_ACTIVATION</option>
		<option>DEGREE_OF_ACTIVATION</option>
		<option>VOL_ACCEPTED</option>
		<option>VOL_OFFERED</option>
		<option>POWERPRICE_ACCEPTED</option>
		<option>POWERPRICE_REJECTED</option>
		<option>WORKPRICE_ACCEPTED</option>
		<option>WORKPRICE_REJECRED</option>
		<option>POWERPRICE_MIN</option>
		<option>POWERPRICE_MID</option>
		<option>POWERPRICE_MAX</option>
		<option>WORKPRICE_ACC_MIN</option>
		<option>WORKPRICE_ACC_MID</option>
		<option>WORKPRICE_ACC_MAX</option>
		<option>WORKPRICE_MARG_MID</option>
		<option>WORKPRICE_MARG_MAX</option>
	</select>
</div>