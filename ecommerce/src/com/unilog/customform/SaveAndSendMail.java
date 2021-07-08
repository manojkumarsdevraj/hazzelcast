package com.unilog.customform;

import java.io.StringWriter;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.opensymphony.xwork2.ActionSupport;
import com.unilog.captcha.GoogleRecaptchaV3;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailUtility;
import com.unilog.utility.CommonUtility;

public class SaveAndSendMail extends ActionSupport implements
		ServletResponseAware {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String successMsg;
	private String result;
	private String renderContent;

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	private LinkedHashMap<String, String> Parameters = new LinkedHashMap<String,String>();
    
    
	public LinkedHashMap<String, String> getParameters() {
		return Parameters;
	}

	public void setParameters(LinkedHashMap<String, String> parameters) {
		Parameters = parameters;
	}

	private HttpServletResponse response;

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	private HttpServletRequest request;
	private String FromMailAddress;

	
	

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	@SuppressWarnings("rawtypes")
	public String execute() throws Exception {
		
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		boolean hasCaptcha = false;
		Enumeration names = null;
		String emailTo = null;
		String emailCC = null;
		String formName = null;
		String body = "";
		String sendTo = "";
		boolean saveToDb = true;
		boolean sendMailTo = true;
		boolean captchaPassed = true;
		String subject = "No Subject";
		String values = "";
		VelocityContext context = new VelocityContext();
		try {
			Map<String, Object> formFields = new LinkedHashMap<>();
			names = request.getParameterNames();
		
			body = "<table style='width:auto;font-family:\"Arial\",Helvetica,sans-serif;font-size:12px;'>";
			while (names!=null && names.hasMoreElements() ) {
				String name = (String) names.nextElement();
				if(name.equalsIgnoreCase("NotificationName")){
					emailTo = request.getParameter(name);
				}else if(name.equalsIgnoreCase("Email")){
					emailCC = request.getParameter(name);
					formFields.put(name, request.getParameter(name));
			    	context.put(name, request.getParameter(name));
			    	values = request.getParameter(name);
			    	if(CommonUtility.validateString(name+"_Label").length()>0 && CommonUtility.validateString(request.getParameter(name+"_Label")).length()>0){
				    	name = CommonUtility.validateString(request.getParameter(name+"_Label"));
				    }
				    if(CommonUtility.validateString(name).indexOf("_Label")<0){
				    	body = body + "<tr><td><b>"+name.replaceAll("\\_", " ")+":&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td><td>"+values+"</td></tr>";
				    }
				}else if(name.equalsIgnoreCase("formName")){
					formName = request.getParameter(name);
				}else if(name.equalsIgnoreCase("sendTo")){
					sendTo = request.getParameter(name);
					formFields.put(name, request.getParameter(name));
				}else if(name.equalsIgnoreCase("SaveToDb")){
					String isSaveTo = request.getParameter(name);
					if(isSaveTo!=null && isSaveTo.trim().equalsIgnoreCase("N"))
						saveToDb = false;
				}else if(name.equalsIgnoreCase("SendMail")){
					String isSaveTo = request.getParameter(name);
					if(isSaveTo!=null && isSaveTo.trim().equalsIgnoreCase("N"))
						sendMailTo = false;
				}else if(name.equalsIgnoreCase("Subject")){
					String isSaveTo = request.getParameter(name);
					if(isSaveTo!=null && !isSaveTo.trim().equalsIgnoreCase(""))
						subject = isSaveTo;
				}else if(name.equalsIgnoreCase("jCaptcha")){ //name.equalsIgnoreCase("hasCaptcha")
					String chkCaptcha = request.getParameter(name);
					if(chkCaptcha!=null )//&& chkCaptcha.trim().equalsIgnoreCase("Y")
						hasCaptcha = true;
				}else {
					if(!request.getParameter(name).equalsIgnoreCase("") && !(name.equalsIgnoreCase("jCaptcha") || name.equalsIgnoreCase("g-recaptcha-response"))){
					   	Parameters.put(name, request.getParameter(name));
					    String checklist[] =  request.getParameterValues(name);
					    values = "";
					    if(checklist.length > 1){
					    	context.put(name, checklist);
					    	for(int i = 0; i<checklist.length;i++){
					    		if(i == checklist.length -1){
					    			values = values + checklist[i];
					    		}else{
					    			values = values + checklist[i]+",";
					    		}
					    		//values = values + checklist[i]+",";
					    	}
					    }else{
					    	formFields.put(name, request.getParameter(name));
					    	context.put(name, request.getParameter(name));
					    	values = request.getParameter(name);
					    }
					    if(CommonUtility.validateString(name+"_Label").length()>0 && CommonUtility.validateString(request.getParameter(name+"_Label")).length()>0){
					    	name = CommonUtility.validateString(request.getParameter(name+"_Label"));
					    }
					    if(CommonUtility.validateString(name).indexOf("_Label")<0){
					    	body = body + "<tr><td><b>"+name.replaceAll("\\_", " ")+":&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td><td>"+values+"</td></tr>";
					    }
					  	
					}
				}
			}
			
			String webAddress = CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"); 
			String siteName = CommonDBQuery.getSystemParamtersList().get("SITE_NAME");
			String siteDisplayName = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME");
			String webThemes = "/WEB_THEMES/";
			boolean isEvent = CommonUtility.convertToBoolean(request.getParameter("isEvent"));
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES")).length() > 0){
				webThemes = CommonDBQuery.getSystemParamtersList().get("WEB_THEMES");
			}
			body = body + "<tr><td><p style='margin:0;'>&nbsp;</p></td></tr><tr> <td colspan='2'><p style='margin:0 0 5px 0;'>Thank you,</p><a href='"+webAddress+"'> <img src='"+webAddress+webThemes+"/"+siteName+"/images/mailTemplateLogo.png' alt='"+siteDisplayName+"' style='border: none;display: block;' title='"+siteDisplayName+"' /></a> </td></tr> </table>";
			SaveCustomFormDetails scfd = new SaveCustomFormDetails();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = Integer.parseInt(sessionUserId);
			if(hasCaptcha){
				String userCaptchaResponse = request.getParameter("jcaptcha");
				captchaPassed = false;
				
				if(userId>1){
					captchaPassed = true;
					result = "1|Request Sent Successfully.";
				}else{
					String captcha = (String) session.getAttribute("captcha");
	
	
					if (captcha != null && userCaptchaResponse != null) {
	
						if (captcha.equals(userCaptchaResponse)) {
							captchaPassed = true;
							session.removeAttribute("captcha");
							result = "1|Request Sent Successfully.";
						} 
					}
	
				}
			}
			if(userId<2 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RECAPTCHAV3_STATICPAGES")).equalsIgnoreCase("Y") && !isEvent){
				hasCaptcha = true;
				if(CommonUtility.validateString(request.getParameter("g-recaptcha-response")).length()>0){
					GoogleRecaptchaV3 reCaptcha =new GoogleRecaptchaV3();
					String captchToken= reCaptcha.isValid(request.getParameter("g-recaptcha-response"));
	                 if(captchToken.equalsIgnoreCase("false"))
	                 {
	                	 captchaPassed = false;
	                 }
				}else {
					captchaPassed = false;
				}
			}

			if(saveToDb && captchaPassed)
			{
				
				int count = scfd.saveToDataBase(Parameters,formName);
				result = "1|Request Sent Successfully.";
				successMsg = "1|Request Sent Successfully.";
				System.out.println("no of fields details added = "+count );
			}
			
			if(sendMailTo && captchaPassed)
			{
				LinkedHashMap<String,String> notification = scfd.getNotificationDetails(emailTo);
				System.out.println(notification.entrySet());
				int loop = 0;
				SendMailUtility sendMail = new SendMailUtility();
				Address[] toAddress = null;
				Address[] ccAddress = null;
				Address[] bccAddress = null;
				StringWriter writer = new StringWriter();
				context.put("body",body);
				context.put("formFields",formFields);
	            context.put("webAddress",webAddress);
	            context.put("siteName",siteName);
	            context.put("siteDisplayName",siteDisplayName);
	            
	           /* if(emailCC!=null && emailCC.trim().length()>0){
					notification.put("TO_EMAIL", notification.get("TO_EMAIL")==null?emailCC:notification.get("TO_EMAIL")+";"+emailCC);
				}*/
				if(notification.get("TO_EMAIL")!=null && !notification.get("TO_EMAIL").trim().equalsIgnoreCase(""))
				{
					loop = 0;
					String splitToAddress[] = notification.get("TO_EMAIL").split(";");
					if(splitToAddress!=null && splitToAddress.length>0)
					{
						toAddress = new Address[splitToAddress.length];
						for(String toAddr:splitToAddress)
						{
							toAddress[loop] = new InternetAddress(toAddr.trim());
							loop++;
						}
					}
					
				}else if(CommonUtility.validateEmail(sendTo)){
					notification.put("TO_EMAIL", sendTo);
					toAddress = new Address[1];
					toAddress[0] = new InternetAddress(CommonUtility.validateString(sendTo));
				}
				
				if(emailCC!=null && emailCC.trim().length()>0){
					notification.put("CC_EMAIL", notification.get("CC_EMAIL")!=null?emailCC+";"+notification.get("CC_EMAIL"):emailCC);
				}
				if(notification.get("CC_EMAIL")!=null && !notification.get("CC_EMAIL").trim().equalsIgnoreCase(""))
				{
					loop = 0;
					String splitToAddress[] = notification.get("CC_EMAIL").split(";");
					if(splitToAddress!=null && splitToAddress.length>0)
					{
						ccAddress = new Address[splitToAddress.length];
						for(String toAddr:splitToAddress)
						{
							ccAddress[loop] = new InternetAddress(toAddr.trim());
							loop++;
						}
					}
					
				}
				if(notification.get("BCC_EMAIL")!=null && !notification.get("BCC_EMAIL").trim().equalsIgnoreCase(""))
				{
					loop = 0;
					String splitToAddress[] = notification.get("BCC_EMAIL").split(";");
					if(splitToAddress!=null && splitToAddress.length>0)
					{
						bccAddress = new Address[splitToAddress.length];
						for(String toAddr:splitToAddress)
						{
							bccAddress[loop] = new InternetAddress(toAddr.trim());
							loop++;
						}
					}
					
				}
		        
				String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
				if(CommonUtility.validateString(notification.get("FROM_EMAIL")).length()>0){
					fromEmail = notification.get("FROM_EMAIL");
				}
				
				if(notification.get("DESCRIPTION")!=null && !notification.get("DESCRIPTION").trim().equalsIgnoreCase("")){
					Velocity.evaluate(context, writer, "",  notification.get("DESCRIPTION"));
				}else{
					Velocity.evaluate(context, writer, "",  body);
				}
		        StringBuilder finalMessage= new StringBuilder();
		        finalMessage.append(writer.toString());
		        /*  flag = sendMailUtility.sendNotification(notification,subject, fromEmail, finalMessage.toString());*/
				//boolean mailStatus = sendMail.sendMultipleMailRecepient(toAddress, subject, fromEmail, body, ccAddress, bccAddress);
		        boolean mailStatus = sendMail.sendMultipleMailRecepient(toAddress, subject, fromEmail, finalMessage.toString(), ccAddress, bccAddress);
				if(mailStatus)
				{
					result = "1|Request Sent Successfully.";
					successMsg = "1|Request Sent Successfully.";
				}
				else
				{
					result = "0|Problem occured during process please try again Later.";
					successMsg = "0|Problem occured during process please try again Later.";
				}
			}
			
			if(!captchaPassed && hasCaptcha){
				result = "0|Invalid Captcha. Please Try Again.";
				successMsg = "0|Invalid Captcha. Please Try Again.";
			}
			setRenderContent(result);
			System.out.println(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
		}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}

	public String getRenderContent() {
		return renderContent;
	}
    
	
	
}
