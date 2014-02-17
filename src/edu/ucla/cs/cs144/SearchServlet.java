package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
		String query = "";
		String isValid = "true";
		try {
			query = request.getParameter("q");
		} catch (Exception e) {
			query="";
			isValid="false";
		}
		//if no query was sent or whitespace is send, then redirect to search page
		if(query == null || query.trim().isEmpty()) {
			response.sendRedirect("keywordSearch.html");
			return;
			
		}
		
		int returned = 0;
		int skipped = 0;
		
		try {
			String resultsToReturn = (String)request.getParameter("numResultsToReturn");
			String resultsToSkip = request.getParameter("numResultsToSkip");
			returned = Integer.parseInt(resultsToReturn);
			skipped = Integer.parseInt(resultsToSkip);
		} catch (NumberFormatException e) {
			returned  = 20;
			skipped = 0;
		}
		if(returned != 20) returned = 20;
		if(skipped < 0) skipped = 0;
		
		SearchResult[] results = AuctionSearchClient.basicSearch(query, skipped, returned);
		if(results.length != 0 && results[0].getItemId().equals("-1")) {
			
			isValid = "false";
		}
	
		request.setAttribute("validity", isValid);
		request.setAttribute("results", results);
		request.setAttribute("returned", returned);
		request.setAttribute("skipped", skipped);
		request.setAttribute("q", query);
		request.getRequestDispatcher("/searchResult.jsp").forward(request, response);
		
    }
}
