package com.unilog.security;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

public class UnilogHttpSessionListener implements HttpSessionListener{
	  public void sessionCreated(HttpSessionEvent event){
		  if(CommonUtility.validateNumber(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("APPLICATION_SESSION_TIME_OUT")))>30) {
			  int timeSec = CommonUtility.validateNumber(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("APPLICATION_SESSION_TIME_OUT"))); //in minutes
			  timeSec = (timeSec*60); //in seconds
			  System.out.println("APPLICATION SESSION TIME OUT set to: "+timeSec+" seconds");
			  event.getSession().setMaxInactiveInterval(timeSec); //in seconds
		  }
	  }
	  public void sessionDestroyed(HttpSessionEvent event){}
}
