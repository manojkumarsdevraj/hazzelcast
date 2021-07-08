package com.unilog.defaults;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
 
public class SessionCounterListener implements HttpSessionListener {
 
  private static int totalActiveSessions;
  private static ArrayList<String> activeSessionId = new ArrayList<String>();
 
  
  public static ArrayList<String> getTotalActiveSessionId(){
		return activeSessionId;
	  }
  public static int getTotalActiveSession(){
	return totalActiveSessions;
  }
 
  public void sessionCreated(HttpSessionEvent arg0) {
	  
	  HttpSession session = arg0.getSession();
	  String id = session.getId();
	  
if(activeSessionId!=null && activeSessionId.size()<1000)
{
	  if(!activeSessionId.contains(id))
	  {
		  activeSessionId.add(id);
	  }
}
	totalActiveSessions++;
	System.out.println("sessionCreated - add one session into counter : " + id);
  }
 
  public void sessionDestroyed(HttpSessionEvent arg0) {
	totalActiveSessions--;
	HttpSession session = arg0.getSession();
	  String id = session.getId();
	  if(activeSessionId!=null && activeSessionId.size()>0)
	 activeSessionId.remove(id);
	System.out.println("sessionDestroyed - deduct one session from counter");
  }	
}