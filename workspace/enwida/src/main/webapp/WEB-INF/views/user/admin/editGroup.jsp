<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

	<h1>User : ${username}</h1>
	<div>
		<table>
			<tr>
				<td>Available Groups</td>
				<td></td>
				<td>Assigned Groups</td>
			</tr>
			
			<tr>
				<td>
				<select name="FavWebSite" size="5">
					<option value="SM">http://www.scriptingmaster.com</option>
					<option value="Google">http://www.google.com</option>
					<option value="MSN">http://www.msn.com</option>
					<option value="yahoo">http://www.yahoo.com</option>
					<option value="microsoft">http://www.microsoft.com</option>
				</select>				
				</td>
				<td>
					<button >>></button><br>
					<button ><<</button>
				</td>
				<td>
				<select name="FavWebSite" size="5">
					<option value="SM">http://www.scriptingmaster.com</option>
					<option value="Google">http://www.google.com</option>
					<option value="MSN">http://www.msn.com</option>
					<option value="yahoo">http://www.yahoo.com</option>
					<option value="microsoft">http://www.microsoft.com</option>
				</select>	
				</td>
			</tr>
		</table>
	</div>
