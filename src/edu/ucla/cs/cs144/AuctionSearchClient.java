package edu.ucla.cs.cs144;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

import edu.ucla.cs.cs144.FieldName;
import edu.ucla.cs.cs144.SearchConstraint;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearchClient {
	
	private static final String ENDPOINT_URL =
		"http://sunflower.cs.ucla.edu:41000/axis2/services/AuctionSearchService";
	private static final String TARGET_NAMESPACE =
		"http://cs144.cs.ucla.edu";
	
	@SuppressWarnings("unused")
	public static SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		try {
			RPCServiceClient rpcClient = new RPCServiceClient();
			Options options = rpcClient.getOptions();
			EndpointReference targetEndpoint = new EndpointReference(ENDPOINT_URL);
			options.setTo(targetEndpoint);
			QName basicSearchOp = new QName(TARGET_NAMESPACE, "basicSearch");
			Object[] parameters = new Object[] { query, new Integer(numResultsToSkip),
					new Integer(numResultsToReturn) };
			Class[] returnTypes = new Class[] { SearchResult[].class };
			Object[] reply = rpcClient.invokeBlocking(basicSearchOp, parameters, returnTypes);
			return (SearchResult[])reply[0];
		} catch(AxisFault e) {
			e.printStackTrace();
		}
		return new SearchResult[0];
	}
	
	@SuppressWarnings("unused")
	public static SearchResult[] advancedSearch(SearchConstraint[] constraints, 
			int numResultsToSkip, int numResultsToReturn) {
		try {
			RPCServiceClient rpcClient = new RPCServiceClient();
			Options options = rpcClient.getOptions();
			EndpointReference targetEndpoint = new EndpointReference(ENDPOINT_URL);
			options.setTo(targetEndpoint);
			QName advancedSearchOp = new QName(TARGET_NAMESPACE, "advancedSearch");
			Object[] parameters = new Object[] { constraints, new Integer(numResultsToSkip),
					new Integer(numResultsToReturn) };
			Class[] returnTypes = new Class[] { SearchResult[].class };
			Object[] reply = rpcClient.invokeBlocking(advancedSearchOp, parameters, returnTypes);
			return (SearchResult[])reply[0];
		} catch(AxisFault e) {
			e.printStackTrace();
		}
		return new SearchResult[0];
	}
	
	@SuppressWarnings("unused")
	public static String getXMLDataForItemId(String itemId) {
		try {
			RPCServiceClient rpcClient = new RPCServiceClient();
			Options options = rpcClient.getOptions();
			EndpointReference targetEndpoint = new EndpointReference(ENDPOINT_URL);
			options.setTo(targetEndpoint);
			QName getXmlOp = new QName(TARGET_NAMESPACE, "getXMLDataForItemId");
			Object[] parameters = new Object[] { itemId };
			Class[] returnTypes = new Class[] { String.class };
			Object[] reply = rpcClient.invokeBlocking(getXmlOp, parameters, returnTypes);
			return (String)reply[0];
		} catch(AxisFault e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	public static String echo(String message) {
		try {
			RPCServiceClient rpcClient = new RPCServiceClient();
			Options options = rpcClient.getOptions();
			EndpointReference targetEndpoint = new EndpointReference(ENDPOINT_URL);
			options.setTo(targetEndpoint);
			QName echoOp = new QName(TARGET_NAMESPACE, "echo");
			Object[] parameters = new Object[] { message };
			Class[] returnTypes = new Class[] { String.class };
			Object[] reply = rpcClient.invokeBlocking(echoOp, parameters, returnTypes);
			return (String)reply[0];
		} catch(AxisFault e) {
			e.printStackTrace();
		}
		return null;
	}
}
