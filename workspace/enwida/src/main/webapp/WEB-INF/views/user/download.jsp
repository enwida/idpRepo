<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>


<html>
      <head>
       <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
       <script src="../resources/js/dateformat.js"></script>
       <script type="text/javascript" src="https://www.google.com/jsapi"></script>
	   <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
       <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
       <script src="../resources/js/enwida.js"></script>
	</head>
	<body>
		<a href="${userStatusURL}">${userStatus}</a>
        <input id="datepicker"  onchange="load()">
        <select id="rl_ab1_selProduct1"  onchange="load('rl_ab1')">
        	  <option value="1">SCR</option>
        	  <option value="2">TCR</option>
        </select>
        <select id="rl_ab1_selProduct2"  onchange="load('rl_ab1')">
        	  <option value="1">pos</option>
        	  <option value="2">neg</option>
        </select>
        <select id="rl_ab1_selProduct3"  onchange="load('rl_ab1')">
        	  <option value="1">PT</option>
        	  <option value="2">OPT</option>
        </select>
        <select id="rl_ab1_selRange" onChange="chgRange('rl_ab1',this.selectedIndex);">
        	  <option value="1">1 day</option>
        	  <option value="2">1 week</option>
        	  <option value="3">1 month</option>
        	  <option value="4">1 year</option>
        </select>
        <br>
        
        <h3>Preview:</h3>
        
        <br>
           <br>  
             <br>
             <br>
             <br>
             <br>
            <br> 
        
        
        <div id="chart_div"></div>
        <a id="downloadLink" onclick="downloadCsv('rl_ab1');" href="#">download</a>
    </body>	
</html>