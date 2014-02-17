<!DOCTYPE html>
<html>
<head>
	<title>Buy Item Confirmation</title>
	<link rel="stylesheet" type="text/css" href="style.css" />
	<link media="only screen and (max-device-width: 480px)" 
    href="iPhone.css" type="text/css" rel="stylesheet" />
	<meta name="viewport" content="width=device-width,user-scalable=no, initial-scale=0, maximum-scale=0"/>
	<script type="text/javascript">
	function disable() {
		//document.getElementById("cc").onkeydown = validate;
		if(document.getElementById("submitbtn") != null) {
			document.getElementById("submitbtn").disabled = true;
			validate(document.getElementById("cc").value);
		}
	}
	function validate(val) {
	
		if(val.match(/[0-9]{16}/g) != null) {
			document.getElementById("submitbtn").disabled = false;
		}
		else {
			document.getElementById("submitbtn").disabled = true;
		}
	}
	</script>
</head>
<body onload="disable();">
<div class="imgheader">
      <img id="header-img" src="ebay2.jpg" alt="ebay search page"/>
    </div>
	

    <div id="main_body">

<% String secure = (String)request.getAttribute("secure"); %>
<% if(secure.equals("1")) { %>
item id: <%= request.getAttribute("id")%><br />
price: <%= request.getAttribute("price")%><br />
name: <%=  request.getAttribute("name") %> <br />
<% String confirmpage = "https://"+request.getServerName()+":8443"+request.getContextPath()+"/confirm"; %>
<form method="POST" action="<%= confirmpage%>" id="pay">
<input type="hidden" name="iid" value="<%= request.getAttribute("id")%>"/><br />
credit card (numbers only. no dashes, spaces, etc): <br /><input type="text" name="credit-card" value="" maxlength="16" id="cc" onkeyup="validate(this.value);" /> <br />
<input type="submit" value="Confirm" id="submitbtn" />
</form>

<%
	}
	else { %>
	<p>the page you entered is not trusted, there was no session, or you did not choose an item. please go back or close this page.</p>
	<%}

 %>
 </div> <!--end div id=main_body-->
</body>
</html>