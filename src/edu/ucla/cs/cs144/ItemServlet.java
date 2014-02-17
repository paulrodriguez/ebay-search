package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;
// DOM HTML parser
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.StringReader;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

	/* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		String error = null;
		String id = request.getParameter("id");
		String xml = AuctionSearchClient.getXMLDataForItemId(id);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(xml)));

			NodeList nodes = doc.getElementsByTagName("Item");
			int nSize = nodes.getLength();
			if(nSize < 1) // no item with ID found
			{
				error = "No item with ID #" + id + " found";
				request.setAttribute("error", error);
			}
			else
			{
				// only get the first--shouldn't have more than one Item for this ID
				Element e = (Element)nodes.item(0);

				//buy price
				String bp = "";
				Element buyprice = getElementByTagNameNR(e,"Buy_Price");
				if(buyprice!=null){
					bp = getElementText(buyprice);
					request.setAttribute("buyprice",bp);
				}
				else {
					request.setAttribute("buyprice","none");
				}
				
				// Name
				String name = e.getElementsByTagName("Name").item(0).getFirstChild().getTextContent();
				request.setAttribute("name", name);

				// Categories
				ArrayList<String> cats = new ArrayList<String>();
				NodeList catsNodes = e.getElementsByTagName("Category");
				for(int i=0; i<catsNodes.getLength(); i++)
				{
					cats.add(catsNodes.item(i).getFirstChild().getTextContent());
				}
				request.setAttribute("cats", cats);

				// Location
				//String loc = e.getElementsByTagName("Location").item(0).getFirstChild().getTextContent();
				//request.setAttribute("loc", loc);
				Element loc = getElementByTagNameNR(e,"Location");
				request.setAttribute("loc", getElementText(loc));
				// Country 
				String country = e.getElementsByTagName("Country").item(0).getFirstChild().getTextContent();
				request.setAttribute("country", country);
			
				// Number_of_Bids
				String numBids = e.getElementsByTagName("Number_of_Bids").item(0).getFirstChild().getTextContent();
				request.setAttribute("numBids", numBids);

				// Currently 
				request.setAttribute("currBid", 
					e.getElementsByTagName("Currently").item(0).getFirstChild().getTextContent());
				// First_Bid
				request.setAttribute("firstBid", 
					e.getElementsByTagName("First_Bid").item(0).getFirstChild().getTextContent());

				// Bids
				// Can skip over listing Bids if Number_of_Bids is 0
				// (also assume that null count for number of bids means no bids)
				if(Integer.parseInt(numBids) > 0)
				{
					request.setAttribute("hasBids", "true");

					// outer Bids node 
					NodeList bidsNodes = e.getElementsByTagName("Bids");
					Element bse = (Element)bidsNodes.item(0);
					// inner Bid nodes
					NodeList bidNodes = bse.getElementsByTagName("Bid"); // each Bid within Bids
					int bnSize = bidNodes.getLength();
					// array to hold each bid, then fields: 1) Bidder ID, 2) Bidder Rating, 3) Bidder Location, 4) Bidder Country, 5) Time, 6) Amount
					String bids[][] = new String[bnSize][6];

					// iterate through each Bid node
					for(int i=0; i<bnSize; i++)
					{
						Element bne = (Element)bidNodes.item(i);
						Element bidderTag = (Element)bne.getElementsByTagName("Bidder").item(0);
						bids[i][0] = bidderTag.getAttribute("UserID");
						bids[i][1] = bidderTag.getAttribute("Rating");
						bids[i][2] = bidderTag.getElementsByTagName("Location").item(0).getFirstChild().getTextContent();
						bids[i][3] = bidderTag.getElementsByTagName("Country").item(0).getFirstChild().getTextContent();
						bids[i][4] = bne.getElementsByTagName("Time").item(0).getFirstChild().getTextContent();
						bids[i][5] = bne.getElementsByTagName("Amount").item(0).getFirstChild().getTextContent();
					}

					request.setAttribute("bids", bids);
				}
				else
				{
					request.setAttribute("hasBids", "false");
				}

				// Started
				request.setAttribute("started", e.getElementsByTagName("Started").item(0).getFirstChild().getTextContent());

				// Ends
				request.setAttribute("ends", e.getElementsByTagName("Ends").item(0).getFirstChild().getTextContent());

				// Seller
				// UserID
				Element sellerTag = (Element)e.getElementsByTagName("Seller").item(0);
				String seller = sellerTag.getAttribute("UserID");
				request.setAttribute("seller", seller);
				// Rating
				String rating = sellerTag.getAttribute("Rating");
				request.setAttribute("rating", rating);

				// Description
				String desc = e.getElementsByTagName("Description").item(0).getFirstChild().getTextContent();
				request.setAttribute("desc", desc);

			}

		} catch(Exception e)
		{
			error = "No item with ID #" + id + " found";
			request.setAttribute("error", error);
		}

		request.getRequestDispatcher("/item.jsp").forward(request, response);

    }
}
