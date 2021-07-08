package com.unilog.users;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

public class AsyncUserSession implements Runnable {
	private HttpSession session;
	private HashMap<String, String> userDetails;
	public AsyncUserSession(HttpSession _session,HashMap<String, String> _userDetails) {
		this.session = _session;
		this.userDetails = _userDetails;
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		LoginUserSessionObject.setAsyncUserSessionObject(session, userDetails);
		
	}

}
