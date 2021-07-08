package com.unilog.punchout.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.punchout.jaxb.CXML;
import com.unilog.punchout.jaxb.Credential;
import com.unilog.punchout.jaxb.Extrinsic;
import com.unilog.punchout.jaxb.Header;
import com.unilog.punchout.jaxb.PunchOutSetupRequest;
import com.unilog.punchout.model.PunchoutRequestModel;
import com.unilog.punchout.utility.PunchoutRequestDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class ProcessPunchoutRequest {

	private static ProcessPunchoutRequest processPunchoutRequest = null;

	private ProcessPunchoutRequest() {
		//
	}

	public static ProcessPunchoutRequest getInstance() {
		synchronized (ProcessPunchoutRequest.class) {
			if (processPunchoutRequest == null) {
				processPunchoutRequest = new ProcessPunchoutRequest();
			}
		}
		return processPunchoutRequest;
	}

	
	public void processRequest(CXML cxml, PrintStream outputStream, String sessionId) throws JDOMException, IOException
	{
		Document response = null;
		Connection conn = null;
		        
		try{
			
			if(cxml!=null && cxml.getRequest()!=null && cxml.getRequest().getPunchOutSetupRequest()!=null)
			{
				conn = ConnectionManager.getDBConnection();
				PunchoutRequestModel punchoutModel = new PunchoutRequestModel();
				Header header = cxml.getHeader();
				PunchOutSetupRequest punchoutRequest = cxml.getRequest().getPunchOutSetupRequest();
				List<Credential> fromCredential = header.getFrom().getCredential();
				List<Credential> senderCredential = header.getSender().getCredential();
				List<Credential> toCredential = header.getTo().getCredential();
				
				punchoutModel.setSessionId(sessionId);
		    	punchoutModel.setOperationType(punchoutRequest.getOperation());
		    	punchoutModel.setPayloadId(cxml.getPayloadID());
		    	punchoutModel.setTimeStamp(PunchoutRequestDAO.getTimestamp());
		    	if(punchoutRequest != null && punchoutRequest.getShipTo() != null && punchoutRequest.getShipTo().getAddress() != null && punchoutRequest.getShipTo().getAddress().getAddressID() != null) {
		    		punchoutModel.setAddressId(punchoutRequest.getShipTo().getAddress().getAddressID());
		    	}
				if(fromCredential!=null && fromCredential.size()>0)
				{
					punchoutModel.setFromDomain(fromCredential.get(0).getDomain());
					punchoutModel.setFromId(fromCredential.get(0).getIdentity().getContent().get(0).toString());
					
				}
				
				if(toCredential!=null && toCredential.size()>0)
				{
					punchoutModel.setToDomain(toCredential.get(0).getDomain());
					punchoutModel.setToId(toCredential.get(0).getIdentity().getContent().get(0).toString());
				}
				
				
				if(senderCredential!=null && senderCredential.size()>0)
				{
					punchoutModel.setSenderDomain(senderCredential.get(0).getDomain());
					punchoutModel.setSenderId(senderCredential.get(0).getIdentity().getContent().get(0).toString());
					punchoutModel.setSenderSharedSecret(senderCredential.get(0).getSharedSecret().getContent().get(0).toString());
					punchoutModel.setUserAgent(header.getSender().getUserAgent().getContent());
					punchoutModel.setCimmUserName(punchoutModel.getSenderId());
					punchoutModel.setCimmPassword(punchoutModel.getSenderSharedSecret());
					
				}
				
				if(punchoutRequest!=null)
				{
					punchoutModel.setResponseUrl(punchoutRequest.getBrowserFormPost().getURL().getContent());
					if(punchoutRequest.getBuyerCookie()!=null && punchoutRequest.getBuyerCookie().getContent()!=null && punchoutRequest.getBuyerCookie().getContent().size()>0){
						punchoutModel.setBuyerCookie(punchoutRequest.getBuyerCookie().getContent().get(0).toString());
					}

					List<Extrinsic> extrinsicList = punchoutRequest.getExtrinsic();
					for(Extrinsic extrinsicVal : extrinsicList)
					{
						if(extrinsicVal.getContent()!=null && extrinsicVal.getContent().size()>0){
							if(CommonUtility.validateString(extrinsicVal.getName()).trim().equalsIgnoreCase("UserEmail")){
								punchoutModel.setExtrinsicUserEmail(extrinsicVal.getContent().get(0).toString());
							}
							if(CommonUtility.validateString(extrinsicVal.getName()).trim().equalsIgnoreCase("UserId")){
								punchoutModel.setExtrinisicUserId(extrinsicVal.getContent().get(0).toString());
							}
							if(CommonUtility.validateString(extrinsicVal.getName()).trim().equalsIgnoreCase("UniqueName")){
								punchoutModel.setExtrinisicUniqueName(extrinsicVal.getContent().get(0).toString());
							}
							if(CommonUtility.validateString(extrinsicVal.getName()).trim().equalsIgnoreCase("FirstName")){
								punchoutModel.setExtrinisicFirstName(extrinsicVal.getContent().get(0).toString());
							}
							if(CommonUtility.validateString(extrinsicVal.getName()).trim().equalsIgnoreCase("LastName")){
								punchoutModel.setExtrinisicLastName(extrinsicVal.getContent().get(0).toString());
							}
						}
						
					}
				}
				
			    
			   
			    
			   
			    HashMap<String, String> userInfo = PunchoutRequestDAO.getCXMLUserDetail(conn,punchoutModel.getFromId());
			 
			    if(userInfo!=null && userInfo.size()>0)
			    {
			    	punchoutModel.setCimmUserName(userInfo.get("userName"));
			    	punchoutModel.setCimmPassword(userInfo.get("passWord"));
			    	
			    	
			    }
			    
			    UsersModel isValBuyer = PunchoutRequestDAO.getValidBuyer (conn,punchoutModel.getCimmUserName(), punchoutModel.getCimmPassword());
			    
			    if(isValBuyer.isValidBuyer()){
			    	punchoutModel.setTempUserName(getExtrinsicTypeValue(isValBuyer.getExtrensicType(),punchoutModel)) ;
			    	if(CommonUtility.validateString(punchoutModel.getTempUserName()).equalsIgnoreCase("") && !CommonUtility.validateString(punchoutModel.getCimmUserName()).equalsIgnoreCase("")){
			    		punchoutModel.setTempUserName(punchoutModel.getCimmUserName()); 
			    	}else{
			    		String userName = punchoutModel.getTempUserName()+"@"+isValBuyer.getEntityId()+".com";//senderId;
			    		punchoutModel.setTempUserName(userName); 
			    	}
			    	int result = 0;
			    	result = PunchoutRequestDAO.getPunchoutUser(conn, punchoutModel);
			    	if(result > 0){
			    		String url = CommonDBQuery.getSystemParamtersList().get("CXML_URL");
			    		String startPage = url+"?punchoutAuthenticationId="+sessionId;
			    		response = handlePunchOutRequest(startPage);
			    	}
			    }else{
			    	response = createResponse(PunchoutRequestDAO.getTimestamp(),PunchoutRequestDAO.getPayloadId(), "401", "Unauthorized - Access Denied");
			    }
			}else {
				response = createResponse(PunchoutRequestDAO.getTimestamp(),PunchoutRequestDAO.getPayloadId(), "500", "Error - Unknown request ");
			}
			sendResponse (response,outputStream);
		}
		catch (Exception e) {
			e.printStackTrace();
			response = createResponse(PunchoutRequestDAO.getTimestamp(),PunchoutRequestDAO.getPayloadId(), "500", "Error - Unknown request ");
			sendResponse (response,outputStream);
		}finally{
			ConnectionManager.closeDBConnection(conn);
		}
		
	   
	}
	
	public String getExtrinsicTypeValue(String extrinsicType, PunchoutRequestModel punchoutModel)
	{
		String extrinsicValue = "";
		if(CommonUtility.validateString(extrinsicType).trim().equalsIgnoreCase("UniqueName"))
		{
			extrinsicValue = punchoutModel.getExtrinisicUniqueName();
		}else if (CommonUtility.validateString(extrinsicType).trim().equalsIgnoreCase("UserId")) {
			extrinsicValue = punchoutModel.getExtrinisicUserId();
		}else {
			extrinsicValue = punchoutModel.getExtrinsicUserEmail();
		}
		return extrinsicValue;
	}
	
	public Document handlePunchOutRequest (String startPage)throws JDOMException {
      	Document response = createResponse(PunchoutRequestDAO.getTimestamp(),PunchoutRequestDAO.getPayloadId(),"200","OK");
      	
      				response.getRootElement().getChild("Response")
                            .addContent(new Element("PunchOutSetupResponse")
                               .addContent(new Element("StartPage")
                                  .addContent(new Element("URL")
                                     .setText(startPage))));
      				
      				
      return response;
    }
	
	
   
	public Document createResponse (String sTimestamp, String payloadId,String statusCode, String statusText)throws JDOMException {
	   Document response = new Document(new Element("cXML")).setDocType(new DocType("cXML", "http://xml.cXML.org/schemas/cXML/1.2.001/cXML.dtd"));
	   			response.getRootElement().addAttribute("version","1.2.001").addAttribute("xml:lang","en-US")
	      			.addAttribute("timestamp",sTimestamp)
	      				.addAttribute("payloadID",payloadId)
	      					.addContent(new Element("Response")
	      						.addContent(new Element("Status")
	      							.addAttribute("code",statusCode)
	      								.addAttribute("text",statusText)));
	
	  return response;
	  
	  
	 /* Response res = new Response();
	     PunchOutSetupResponse punchoutSetupResponse = new PunchOutSetupResponse();
	     StartPage startPageUrl =  new StartPage();
	     URL value = new URL();
	     value.setContent(startPage);
		startPageUrl.setURL(value );
		punchoutSetupResponse.setStartPage(startPageUrl );
		res.setPunchOutSetupResponse(punchoutSetupResponse );
		cxml.setResponse(value );*/
	}
	
	 public void sendResponse (Document doc,OutputStream out) throws IOException, JDOMException {
		   XMLOutputter fmt = new XMLOutputter();
		   fmt.output(doc, out);
		   }
}
