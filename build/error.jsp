<!DOCTYPE html>
<html>
<head>
	<title>Invalid Page Access</title>
	
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
		<br />
		<p>request invalid. you cannot access the contents of this page because this is an unsecure port. please click on one fo the links above.</p>
	</div>
</body>
</html>
