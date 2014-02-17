<%@ page import="java.util.ArrayList" %>
<%@ page import="edu.ucla.cs.cs144.ShoppingCart"%>
<%@ page import="edu.ucla.cs.cs144.Item"%>

<!DOCTYPE html>
<html>
<head>
	<title>Item Result</title>
	<link rel="stylesheet" type="text/css" href="style.css"/>
	<link media="only screen and (max-device-width: 480px)" 
    href="iPhone.css" type="text/css" rel="stylesheet" />
	<meta name="viewport" content="width=device-width,user-scalable=no, initial-scale=0, maximum-scale=0"/>
	
	<!-- include jQuery for collapsible menus --->
	<!--<script src="jquery-1.10.2.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$('ul.menu ul').hide();
			$('ul.menu li').click(function(){
				$(this).next('ul').slideToggle();
			});
		});
	</script>
	-->

	<script type="text/javascript">
	<!--
	var firsttime = 0;
	function erase(x) {
		//in case a user clicks somewhere in the page and then focuses back on the text so it wont erase it
		if(firsttime == 0) {
			x.value = "";
			x.style.color="black";
			firsttime = 1;
		}
	}
	-->
	</script>
	<script type="text/javascript">
	<!--
	var firsttime = 0;
	function erase(x) {
		//in case a user clicks somewhere in the page and then focuses back on the text so it wont erase it
		if(firsttime == 0) {
			x.value = "";
			x.style.color="black";
			firsttime = 1;
		}
	}
	-->
	</script>

	<% // Get location for Google Maps, and also to print out
		String loc = (String)request.getAttribute("loc");
		String country = (String)request.getAttribute("country"); %>
	<!-- Google Maps -->
	<!--use to also have html and body with #map-canvas style-->
   <!-- <style>
    	#map-canvas {
    		height: 100%;
    		margin: 0px;
    		padding: 0px
		}
    </style>-->
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
    <script>
		var geocoder;
		var map;
		var latlng;
		var marker; 
		var infowindow = "";
		var loc = '<% out.print(loc); %>';
		var country = '<% out.print(country); %>';
		loc = loc.replace(/"/g,'&quot;');
		country = country.replace(/"/g,'&quot;');

		function initialize(address) {
			geocoder = new google.maps.Geocoder();
			//latlng = new google.maps.LatLng(34.068921, -118.44518119999998); //default positions
			var mapOptions = {
				zoom: 11,
				//center: ,
				mapTypeId: google.maps.MapTypeId.ROADMAP
			}
			map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
			codeAddress();
			
		}

			function codeAddress() {
				// first try just location
				address = loc + ", " + country;
				var region = "";
				if(country === 'USA')
					region = 'us';
				geocoder.geocode( {'address': address, 'region':region}, function(results, status) {
					if (status == google.maps.GeocoderStatus.OK) {
						map.setCenter(results[0].geometry.location);
						if(marker)
						{
							marker.setMap(null);
						}
						infowindow = new google.maps.InfoWindow({
							content: address
						});
						marker = new google.maps.Marker({
							map: map,
							position: results[0].geometry.location,
							title: address,
							draggable: false 
						});
						
						google.maps.event.addListener(marker, 'click', function() {
							infowindow.open(map,marker);
						});
					}
					else
					{
						// try country only
						if(!country) // no country specified (empty)--show Area 51
						{
							// zoom out and show the earth
							map.setZoom(0);
							// show area 51 and pin saying 'No country!'
							if(marker)
							{
								marker.setMap(null);
							}
							var area51LatLng = new google.maps.LatLng(37.2350, -115.8111);
							map.setCenter(area51LatLng);
							infowindow = new google.maps.InfoWindow({
								content: '<h3>No country listed</h3><br />Hiding like Edward Snowden' 
							});
							marker = new google.maps.Marker({ map: map, position: area51LatLng, title: 'No country!'});
							
							google.maps.event.addListener(marker, 'click', function() {
								infowindow.open(map,marker);
							});
						}
						else
						{
							// try country only
							geocoder.geocode( {'address': country}, function(results, status) {
								if (status == google.maps.GeocoderStatus.OK) {
									if(marker)
									{
										marker.setMap(null);
									}
									map.setZoom(4);
									map.setCenter(results[0].geometry.location);
									// don't need marker - showing whole country
								}
							});
						}
					}
				});
			}

		google.maps.event.addDomListener(window, 'load', initialize);
    </script>
</head>

<body>
	<div class="imgheader">
	<img id="header-img" src="ebay2.jpg" alt="ebay search page" />
  </div>
  <div id="main_body">
  <div>
  <div class="links">
		<a class="page_link" href="./keywordSearch.html">Search Items</a> | 
		<a class="page_link" href="./getItem.html">ItemID Search</a>
    </div>
	<div class="itemSearchForm">
		<form method="GET" action="./item">
			<input type="text" name="id" value="Look up another item by ID..." onfocus="erase(this)"  id="searchBox" maxlength="10" />
			<input type="submit" value="Search"/>
		</form>
	</div>
	
	</div>
	<div class="item_info">
	<br />
	<h1>Item Information</h1>

	<% String error = (String)request.getAttribute("error");
	if(error == null) { %>
		<div id="basic-info">
			<strong>Name:</strong> 
				<% out.print(request.getAttribute("name")); %> <br />
				<%
				String buyprice = (String)request.getAttribute("buyprice");
				String iid = (String)request.getParameter("id");
				String iname = (String) request.getAttribute("name"); 
				if(!buyprice.equals("none")) {
					Item[] items = new Item[0];
					if(session.isNew()) {
						items = new Item[1];
						items[0] = new Item(iid, iname, buyprice);
						session.setAttribute("items", items);
						
					}
					else {
						
						if(session.getAttribute("items") != null) {
							
							items = (Item[])session.getAttribute("items");
						}
					
						int addnew = 1;
						
						for(int i=0; i < items.length; i++) {
							if(items[i].getId().equals(iid)) {
								addnew = 0;
								break;	
							}
						}
						
						if(addnew==1) {
						
							Item[] items2 = new Item[items.length+1];
							items2[items.length] = new Item(iid,iname, buyprice);
							for(int i=0; i < items.length;i++) {
								items2[i] = items[i];
							}
							session.setAttribute("items",items2);
							
						}
					}
					
					
				%>
				
				<strong>buy price:</strong> <%= buyprice%>
				<% String path = "./buy"; %>
				<a href="<%= path%>">buy now</a>
				<br/>
				<% } %>
			<strong>Categories: </strong>
				<ul>
				<% 
				ArrayList<String> cats = new ArrayList<String>();
				cats = (ArrayList)request.getAttribute("cats");
				for(int i=0; i<cats.size(); i++)
				{
					out.println("<li>" + cats.get(i) + "</li>");
				}
				%>
				</ul>
			<strong>Location: </strong>
				<% out.print(request.getAttribute("loc")); %> <br />
			<strong>Country: </strong>
				<% out.print(request.getAttribute("country")); %> <br />
			<strong>Started: </strong>
				<% out.print(request.getAttribute("started")); %> <br />
			<strong>Ends: </strong>
				<% out.print(request.getAttribute("ends")); %> <br />
			<strong>Seller ID: </strong>
				<% out.print(request.getAttribute("seller")); %> 
			<strong> Seller Rating: </strong>
				<% out.print(request.getAttribute("rating")); %> <br />
			<strong>Description: </strong>
				<% out.print(request.getAttribute("desc")); %> <br />
		</div>

		<div id="bids">
			<h2>Bids</h2>
			<strong>Number of Bids:</strong> 
					<% out.print(request.getAttribute("numBids")); %> <br />
			<strong>Currently:</strong>	
				<% out.print(request.getAttribute("currBid")); %> <br />
			<strong>First Bid:</strong>	
				<% out.print(request.getAttribute("firstBid")); %> <br />
			<% // Can skip over listing Bids if Number_of_Bids is 0
			// (also assume that null count for number of bids means no bids)
			// Would be left as null by the servlet in that case - don't want to print
			// an empty field
			if(request.getAttribute("hasBids").equals("true"))
			{ %>
				<table class="bids" border="1" style="border-collapse:collapse">
				<%
				String[][] bids = (String[][])request.getAttribute("bids");
				int j = 0;
				for(int i=bids.length-1; i>=0; i--) // iterate through Bid nodes
				{ %>
					
					<tr>
						<td colspan="4">
							<strong>Bid <%= j+1 %></strong>
						</td>
					</tr>
					<tr>
						<td colspan="4" style="text-align:center">
							<strong>Bidder Information</strong>
						</td>
					</tr>
					<tr>
						<td><strong>UserID</strong></td>
						<td><strong>Rating</strong></td>
						<td><strong>Location</strong></td>
						<td><strong>Country</strong></td>
					</tr>
					<tr>
						<td><%= bids[i][0]  %></td>
						<td><%= bids[i][1]  %></td>
						<td><%= bids[i][2]  %></td>
						<td><%= bids[i][3]  %></td>
					</tr>
					<tr>
						<td><strong>Time</strong></td><td colspan="3"><%= bids[i][4]  %></td>
					</tr>
					<tr>
						<td><strong>Amount</strong></td><td colspan="3"><%= bids[i][5]  %></td>
					</tr>
					<% if(j < bids.length-1) { %>
					<tr style="height:30px"><td colspan="4"></td></tr>
					<% 
						} %>
				<%
						j++;
					} //end for loop
				%>
				</table>
			<% } %>
		</div>

	<% } else { %>

		<!-- TODO else case - output 'nothing found' div -->
		<div id="error">
			<h2>Error</h2>
			<p><% out.println(error); %></p>
		</div>
	<% }; %>

	<div id="map">
		<h1>Map</h1>
		<div id="map-canvas"></div>
	</div>

	</div> <!--end div with class=item_info-->
	
	</div> <!--end div with id=main_body-->
</body>
</html>
