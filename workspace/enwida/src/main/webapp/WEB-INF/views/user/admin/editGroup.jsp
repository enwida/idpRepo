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
					<option value="MSN">group3</option>
					<option value="yahoo">group4</option>
					<option value="microsoft">group5</option>
				</select>				
				</td>
				<td>
					<button >>></button><br>
					<button ><<</button>
				</td>
				<td>
				<select name="FavWebSite" size="5">
					<option value="SM">group1</option>
					<option value="Google">group2</option>
				</select>		
				</td>
			</tr>
		</table>
	</div>
