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
public class ConfirmServlet extends HttpServlet implements Servlet {
       
    public ConfirmServlet() {}
	
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		 request.getRequestDispatcher("/error.jsp").forward(request, response);
	}
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
            HttpSession session = request.getSession(true);
			session.setMaxInactiveInterval(3600); //time out after an hour
			
			String postid = (String) request.getParameter("iid");
			
                if (session.isNew())
                {
                        request.setAttribute("secure", "0");
                }
				else if(request.isSecure())
				{
					if(!postid.trim().equals("")) {
						String cc = "";
							int idfound = 0; //1 if id was in session, 0 otherwise
						//Item[] items = (Item[])session.getAttribute("items");
						Item[] items = new Item[0];
						try {
							items = (Item[])session.getAttribute("items");
					
							for(int i=0; i < items.length;i++) 
							{
								if(items[i].getId().equals(postid)) 
								{
									idfound = 1;
									request.setAttribute("name",items[i].getName());
									request.setAttribute("price",items[i].getPrice());
									request.setAttribute("id", items[i].getId());
									cc = request.getParameter("credit-card");
									//request.setAttribute("credit-card", request.getParameter("credit-card"));
									//request.setAttribute("secure","1");
									break;
								}
							}
						} catch (Exception e) {
							idfound = 0;
						}
						
						if(idfound == 1 && !cc.equals("") && cc.length()==16 && cc.matches("^[0-9]{16}$")) 
						{
							String creditCard = (String)request.getParameter("credit-card");
							request.setAttribute("credit-card", "************"+creditCard.substring(12,16));
							request.setAttribute("secure","1");
						}
						else {
							//session.invalidate();
							request.setAttribute("secure","0");
					}
				}
				else {
					//session.invalidate();
					request.setAttribute("secure", "0");
				}
				
				
                request.getRequestDispatcher("/confirm.jsp").forward(request, response);        
			}
    }	
	
}
