package edu.ucla.cs.cs144;

import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
public class BuyItemServlet extends HttpServlet implements Servlet {
       
    public BuyItemServlet() {}
	

protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
            HttpSession session = request.getSession(true);
			session.setMaxInactiveInterval(3600); //time out after an hour
			
			//if there was no session then show no item info
            if (session.isNew())
			{
                request.setAttribute("secure", "0");
            }
		   else {
				//should only get to buy page using the link to buy in item.jsp
				//session.setAttribute("referringPage", request.getHeader("Referer"));
				String referer = "";
				String uriID = "";//to store the id of the page refering to this one
				try {
					referer = (String)request.getHeader("Referer");
					if(!referer.equals("")) {
						uriID = referer.substring(referer.length()-10,referer.length());
					}
				} catch (Exception e) {
					referer = "";
					uriID = "";
				}
				//make sure id os not empty
				if(!uriID.equals("")) {
					int idfound = 0; //1 if id was in session, 0 otherwise
					Item[] items = new Item[0];
					try {
						items = (Item[])session.getAttribute("items");
						//Item[] items = (Item[])session.getAttribute("items");
						
						for(int i=0; i < items.length;i++) {
							if(items[i].getId().equals(uriID)) {
								idfound = 1;
								request.setAttribute("name",items[i].getName());
								request.setAttribute("price",items[i].getPrice());
								request.setAttribute("id", items[i].getId());
								request.setAttribute("secure","1");
								break;
							}
						}
					} catch (Exception e) {
						idfound = 0;
					}
					if(idfound==0) {
						//session.invalidate();
						request.setAttribute("secure","0");
					}
				}
				else {
					//session.invalidate();
					request.setAttribute("secure","0");
				}
			}
				
            request.getRequestDispatcher("/buyitem.jsp").forward(request, response);        
    }	
	
}
