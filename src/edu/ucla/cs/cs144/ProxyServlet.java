package edu.ucla.cs.cs144;

import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
		

		HttpURLConnection conn = null;
		String query = "";
		//query = request.getParameter("q");
		try {
			query = (String)request.getParameter("q");
		} catch (Exception e) {
			return;
		}
		
		//String query2= query;
		
		
		//PrintWriter fout = response.getWriter();
		//fout.println("the query: "+query);
		if(	query == null || query.trim().isEmpty()) {
			return;
		}
		
		
        String xml_response = "";
		URL link = null;
		//only output xml if query is not null or empty
		
			try {
			
				response.setContentType("text/xml"); //if valid query, send back an xml page
				query = query.replace(" ","%20");
				link = new URL("http://google.com/complete/search?output=toolbar&q="+query);
				//link = new URL("http://google.com/complete/search?output=toolbar&q="+query);
				conn = (HttpURLConnection) link.openConnection();
				conn.setRequestMethod("GET");
				conn.setDoOutput(true);
				//conn.setDoInput(true);
				conn.setReadTimeout(10000);
				conn.connect();
			
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				StringBuilder rs = new StringBuilder();
				
				while((xml_response = rd.readLine()) != null) {
					rs.append(xml_response+"\n");
				}
		  
				rd.close();
				
				PrintWriter out = response.getWriter();
				
				out.append(rs.toString());
				
				out.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.disconnect();
				conn = null;
				link = null;
			}
    }
}
