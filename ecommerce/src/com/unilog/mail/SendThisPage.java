package com.unilog.mail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;

public class SendThisPage extends ActionSupport {
	
	private static final long serialVersionUID = 4245602390176404970L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String renderContent;
	private String toName;
	private String toEmail;
	private String mailSubject;
	public String getRenderContent() {
		return renderContent;
	}



	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}


	private String fromName;
	private String fromEmail;
	private String mailBody;
	private String contentPart;
	private String mailLink;
	private String descPart;
	private String imgPart;
	private String pricePart;
	private String jcaptcha;
	private String sessionId;
	
	
	public String getSessionId() {
		return sessionId;
	}



	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}



	public String getJcaptcha() {
		return jcaptcha;
	}
	public void setJcaptcha(String jcaptcha) {
		this.jcaptcha = jcaptcha;
	}
	
	
	
	public String getToName() {
		return toName;
	}



	public String getToEmail() {
		return toEmail;
	}



	public String getMailSubject() {
		return mailSubject;
	}



	public String getFromName() {
		return fromName;
	}



	public String getFromEmail() {
		return fromEmail;
	}



	public String getMailBody() {
		return mailBody;
	}



	public String getContentPart() {
		return contentPart;
	}



	public String getMailLink() {
		return mailLink;
	}



	public String getDescPart() {
		return descPart;
	}



	public String getImgPart() {
		return imgPart;
	}



	public String getPricePart() {
		return pricePart;
	}



	public void setToName(String toName) {
		this.toName = toName;
	}



	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}



	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}



	public void setFromName(String fromName) {
		this.fromName = fromName;
	}



	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}



	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}



	public void setContentPart(String contentPart) {
		this.contentPart = contentPart;
	}



	public void setMailLink(String mailLink) {
		this.mailLink = mailLink;
	}



	public void setDescPart(String descPart) {
		this.descPart = descPart;
	}



	public void setImgPart(String imgPart) {
		this.imgPart = imgPart;
	}



	public void setPricePart(String pricePart) {
		this.pricePart = pricePart;
	}

	
	public String sendPage(){
		SendDetailMailUtility sendObj = new SendDetailMailUtility();
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		/*spam email changes start*/
		int requestCount =0;
		int ipMailLimit = CommonUtilityMailPage.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAX_MAIL_PERIP"));
		String captchaRequired = "N";
		String ipaddress = request.getHeader("X-Forwarded-For");
		if (ipaddress == null) {
			ipaddress = request.getRemoteAddr();// userDefaultAddress
		}
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtilityMailPage.validateNumber(sessionUserId);
		requestCount = UsersSendMailDAO.getSendMailCount(ipaddress);
		if (requestCount < 1) {
			session.setAttribute("captcha_required", "N");
			UsersSendMailDAO.saveSendPageMail(ipaddress, 1, sessionId);
		} else {
			requestCount++;
			UsersSendMailDAO.updateSendPageMail(requestCount,ipaddress);
		}
		if(userId<2 && requestCount>ipMailLimit){
			session.setAttribute("captcha_required", "Y");
			captchaRequired = "Y";
		}
		String userCaptchaResponse = jcaptcha;
		//String captchaRequired = (String)CommonDBQuery.getSystemParamtersList().get("CAPTCHA_REQUIRED");
		/*if(!CommonUtilityMailPage.validateString((String)session.getAttribute("captcha_required")).equalsIgnoreCase("")){
			captchaRequired = (String)session.getAttribute("captcha_required");
		}*/
		if(userId<2){
			captchaRequired = (String)CommonDBQuery.getSystemParamtersList().get("CAPTCHA_REQUIRED");
		}else{
			captchaRequired = "N"; 
		}
		boolean captchaPassed = false;
		if(CommonUtilityMailPage.validateString(captchaRequired).equalsIgnoreCase("N")){
			captchaPassed = true;
		}else{
			String captcha = (String) session.getAttribute("captcha");
			if (captcha != null && userCaptchaResponse != null) {

				if (captcha.equals(userCaptchaResponse)) {
					captchaPassed = true;
				} 
			}
		}
		if(captchaPassed){
		/*spam email changes end*/
		boolean sendMail = true;
		if(CommonUtilityMailPage.validateString(toName).length()==0){
			sendMail = false;
		}
		if(!CommonUtilityMailPage.validateEmail(toEmail)){
			sendMail = false;
		}
		if(CommonUtilityMailPage.validateString(mailSubject).length()==0){
			sendMail = false;
		}
		if(CommonUtilityMailPage.validateString(fromName).length()==0){
			sendMail = false;
		}
		if(!CommonUtilityMailPage.validateEmail(fromEmail)){
			sendMail = false;
		}
		if(CommonUtilityMailPage.validateString(mailBody).length()==0){
			sendMail = false;
		}
		/*if(CommonUtility.validateString(descPart).length()==0){
			sendMail = false;
		}*/
		if(CommonUtilityMailPage.validateString(contentPart).length()==0){
			sendMail = false;
		}
		/*if(CommonUtility.validateString(imgPart).length()==0){
			sendMail = false;
		}*/
		if(CommonUtilityMailPage.validateString(mailLink).length()==0){
			sendMail = false;
		}
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		System.out.println("AJAX - :"+ajax);
		if(sendMail && ajax){
			boolean flag = sendObj.sendProductMail(toName, toEmail, mailSubject, fromName, fromEmail,mailBody,descPart,pricePart,contentPart,imgPart,mailLink);
			if(flag){
				renderContent = "0|success";
			}else{
				renderContent = "1|Fail";
			}
		}else{
			response = ServletActionContext.getResponse();
			response.setStatus(404);
			renderContent = "1|Fail";
		}/*spam email changes start*/
	}else{
		renderContent = "2|Invalid Captcha. Please try again.";
	}
		/*spam email changes end*/
		
		return SUCCESS;
	}

}
