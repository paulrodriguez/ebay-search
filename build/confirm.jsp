<!DOCTYPE html>

<html>
<head>

<title>Confirmation page for: <%= request.getAttribute("id") %></title>
<link rel="stylesheet" type="text/css" href="style.css"/>
<link media="only screen and (max-device-width: 480px)" 
    href="iPhone.css" type="text/css" rel="stylesheet" />
	<meta name="viewport" content="width=device-width,user-scalable=no, initial-scale=0, maximum-scale=0"/>
</head>
<body>
	<div class="imgheader">
		  <img id="header-img" src="ebay2.jpg" alt="ebay search page"/>
	</div>
	
    <div id="main_body">
	<div style="float:left">
		<a class="page_link" href="http://<%= request.getServerName()%>:8080<%= request.getContextPath()%>/keywordSearch.html">Search Items</a> | 
		<a class="page_link" href="http://<%= request.getServerName()%>:8080<%= request.getContextPath()%>/getItem.html">ItemID Search</a>
    </div>
	<div>
	<br />
<% String secure = (String)request.getAttribute("secure"); 
if(secure.equals("1")) { 
String iid = (String)request.getAttribute("id");
String name = (String)request.getAttribute("name");
String price = (String)request.getAttribute("price");
String cc = (String)request.getAttribute("credit-card");
%>
<p style="margin:0 auto"><b>Transaction Successful</b></p>
<p><strong>item id</strong>: <%= iid%></p>
<p><strong>item name</strong>: <%= name%></p>
<p><strong>item price</strong>: <%= price%></p>
<p><strong>credit card</strong>: <%= cc%></p>
<%}
else { %>
<p>there was an error processing your request due to one of the following reasons:</p>
<ul>
<li>you did not pick and item</li>
<li>the item id was incorrect</li>
<li>this page was accessed through an insecure port</li>
<li>credit card was incorrect</li>
</ul>
<div style="color:blue;cursor:pointer" onclick="window.history.go(-1)">Go Back</div>
<%}
%>
</div>
</div> <!--end div id=main_body-->
</body>
</html>
