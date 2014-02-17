<!DOCTYPE html>
<%@ page import="edu.ucla.cs.cs144.SearchResult" %>

<html>
	<head>
		<title>Search Results Page</title>
    
		<link rel="stylesheet" type="text/css" href="style.css"/>
		<link media="only screen and (max-device-width: 480px)" 
    href="iPhone.css" type="text/css" rel="stylesheet" />
	<meta name="viewport" content="width=device-width,user-scalable=no, initial-scale=0, maximum-scale=0"/>
		<script  type="text/javascript" src="autoSuggest.js"></script>
		<script type="text/javascript">
 
			function InitAutoSuggest() {
				document.getElementById("searchBox").value = "search";
				var oTextBox = new AutoSuggestionControl(document.getElementById("searchBox"), document.getElementById("results"), new QuerySuggestions());
				//document.getElementById("query").onkeyup = sendAjaxRequest;
			}
		</script>
		<script type="text/javascript">
			<!--
			var firsttime = 0;
			function erase(x) {
				//in case a user clicks somewhere in the page and then focuses back on the text so it wont erase it
				if(firsttime==0) { 
					x.value = "";
					x.style.color="black";
					firsttime=1;
				}
			}
			-->
		</script>
  </head>

  <body onload="InitAutoSuggest();">
    
    <% 
	SearchResult[] results = (SearchResult[])request.getAttribute("results");  
	int skipped = (Integer)request.getAttribute("skipped");
	int returned = (Integer)request.getAttribute("returned");
	String q = (String)request.getAttribute("q");
	String validity = (String)request.getAttribute("validity");
	//out.print(validity);
	%>
    <div class="imgheader">
      <img id="header-img" src="ebay2.jpg" alt="ebay search page"/>
    </div>

    <div id="main_body">
	<div class="links">
		<a class="page_link" href="./keywordSearch.html">Search Items</a> | 
		<a class="page_link" href="./getItem.html">ItemID Search</a>
    </div>
		<div class="searchform">
			<form method="GET" action="search">
				<input type="text" name="q" value="Search..." id="searchBox" onfocus="erase(this);"  autocomplete="off"/>
				<input type="hidden" name="numResultsToSkip" value="0" />
				<input type="hidden" name="numResultsToReturn" value="20" />
				<input type="submit"  value="Search" />
			</form>
			<div id="results" style="margin-top:0px"></div>
		</div><!--end div class=searchform-->
	
      
      <div class="searchResults">
		<br /><br />
		<!--either has query and number of results, or shows that no results where found-->
		<p style="clear:both"> 
		<%--only advance if query was valid (i.e. not null or whitespace, or index error) and that we got some results --%>
		<%if(validity.equals("true") && results.length > 0) { %>
		<%=	"Search Query: "+ q+" ("+results.length+" results)" 	%>
			<%} else {%>
				<%= "your query did not match any results"%>
			<% }%>
		</p>
	  <%
		if(validity.equals("true")) {
			if(results.length > 0) {
	  %>
	<table class="centered" style="border-collapse:collapse">
	  <tr><th class="idheader">Item ID</th><th class="nheader">Name</th></tr>
	<%
	   for(int i = 0; i < results.length; i++) {
	      if(i%2==0 && !results[i].getItemId().equals("-1")) { %> <%--make sure that it doesn't return that error with id=-1--%>
	    <tr style="background-color:#FFFF66" class="itemrow">
			<td class="itemid">  <% out.print("<a href='./item?id="+results[i].getItemId()+"'>"); %><%= results[i].getItemId() %> </a></td>
			<td class="itemname"> <%= results[i].getName() %> </td>
	    </tr>
	<%    }  
	      else if(!results[i].getItemId().equals("-1")){ %>
	    <tr style="background-color:#D1FFD1" class="itemrow">
			<td class="itemid"><% out.print("<a href='./item?id="+results[i].getItemId()+"'>"); %><%= results[i].getItemId() %></a></td>
			<td class="itemname"><%= results[i].getName()%></td>
	    </tr>
	<%
	      }
		  %>
		  <tr style="height:30px" class="itemrow"></tr> <!--add some distance between items-->
		  
	   <% } %>  <%-- end of for loop --%>
	</table>

	<table>
		<tr>
			<td>
	   <%
	   if(skipped > 0) {
			int skip = skipped-returned;
			if(skip<0) skip = 0;
			%>
					<a href="./search?q=<%= q %>&numResultsToSkip=<%= skip %>&numResultsToReturn=<%= returned %>">
						Previous  
					</a>
	<%
		}%>
			</td>
			<td>
		<%
		if (results.length != 0 && returned <= results.length && (returned > 0 || skipped > 0)) { %>
				<a href="./search?q=<%= q %>&numResultsToSkip=<%= skipped + returned %>&numResultsToReturn=<%= returned %>">
					Next
				</a>
	<%
		}%>
			</td>
		</tr>
	</table>
	<% } else { %> <%-- end of outer if loop--%>
	
	<% 
			if(skipped > 0) {
				int skip = skipped-returned;
				if(skip<0) skip=0;
				%>
				<a class="next_prev" href="./search?q=<%= q%>&numResultsToSkip=<%= skip%>&numResultsToReturn=<%= returned%>">
					Previous
				</a>
				
			<% }
			}
		}  //end if loop for:validity.equals("true")
		
		%>
		
      </div>
    </div><!--end div class=main_body-->
  </body>
</html>
