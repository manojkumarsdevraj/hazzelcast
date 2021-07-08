package com.unilog.model.form;

public class FormData {
	private int id;
	private String formName;
	private String status;
	private String formNotification;
	private String saveToDB;
	private String emailSent;
	private String emailSubject;
	private String htmlCode;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFormNotification() {
		return formNotification;
	}
	public void setFormNotification(String formNotification) {
		this.formNotification = formNotification;
	}
	public String getSaveToDB() {
		return saveToDB;
	}
	public void setSaveToDB(String saveToDB) {
		this.saveToDB = saveToDB;
	}
	public String getEmailSent() {
		return emailSent;
	}
	public void setEmailSent(String emailSent) {
		this.emailSent = emailSent;
	}
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	public String getHtmlCode() {
		return htmlCode;
	}
	public void setHtmlCode(String htmlCode) {
		this.htmlCode = htmlCode;
	}
	
	
}
