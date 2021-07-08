/**
 * 
 */
package com.unilog.security;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import com.unilog.defaults.ULLog;
import com.unilog.users.UsersDAO;
public class ULCustomLoginModule implements LoginModule 
{
	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map<String, ?> sharedState;
	private Map<String, ?> options;
	private boolean debug = false;
	private boolean verifySucceeded = false;
	private boolean commitSucceeded = false;
	private byte[] userName = null;
	private byte[] userPassword = null;
	private Vector roleName = new Vector();
	private ULEnterprisePrincipal enterprisePrincipal = null;
	private ULEnterpriseRoles enterpriseRoles = null;
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		
		
		this.subject = subject;
	    this.callbackHandler = callbackHandler;
	    this.sharedState = sharedState;
	    this.options = options;

	}

	public boolean login() throws LoginException {

		ULLog.debug("In CustomLoginModule.login");

	       verifySucceeded = false;

	       if (callbackHandler == null)
	       {
		     throw new LoginException("ERROR: no CallbackHandler available to request authentication information from the application.");
	       }

	       /*
	        * Ask the invoking application to provide a username and password
	        */

	       NameCallback nc = new NameCallback("userName");	     
	       PasswordCallback pc = new PasswordCallback("userPassword", false);

	       Callback[] callbacks = new Callback[2];
	       callbacks[0] = nc;	      
	       callbacks[1] = pc;

	       try
	       {
		     callbackHandler.handle(callbacks);

		     String tmpUsername = nc.getName();
		     if ((tmpUsername != null) && (tmpUsername.length() > 0))
		     {
		       userName = tmpUsername.getBytes();
		     }
		     else
		     {
		       throw new LoginException("ERROR: the username is an empty string.");
		     }
		     
		     StringBuffer passwordTrans = new StringBuffer();
		     char[] tmpPassword = pc.getPassword();
		     if ((tmpPassword != null) && (tmpPassword.length > 0))
		     {
	              /* assume a 1-to-1 character to byte conversion */
		       userPassword = new byte[tmpPassword.length];

		       for (int i = 0; i < tmpPassword.length; i++)
		       {
		    	   passwordTrans.append(tmpPassword[i]);
		    	   userPassword[i] = (byte) tmpPassword[i];
		       }
		     }
		     else
		     {
		        throw new LoginException("ERROR: the password is an empty string.");
		     }	
		     
		     NameCallback nameCallback = (NameCallback) callbacks[0];
			PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];

			 String name = nameCallback.getName();
			 String password = new String(passwordCallback.getPassword());
			
		     int userId=UsersDAO.authenticatUser(name, password,"Y");
		     if(userId>0)
		     {		    	
		    	verifySucceeded=true;
		    	
		    	//TODO ROLES AND PRIVILAGES PART
		    	
		    	return true;
		     }
		     nc.setName(null);
		     pc.clearPassword(); 
		     
	       }
	       catch (java.io.IOException exception)
	       {
		     throw new LoginException(exception.getMessage());
	       }
	       catch (UnsupportedCallbackException exception)
	       {
		     throw new LoginException("ERROR: The Callback class " +
					   exception.getCallback().toString() +
					   " is not understood by the application.");
	       }

	       ULLog.debug("Authentication failed!");
		  
		   verifySucceeded = false;

		   throw new FailedLoginException("Password Incorrect");
	    }

	public boolean commit() throws LoginException 
	{
		
		commitSucceeded = false;

		if (verifySucceeded == false)
		{
			return false;
		}
		else
		{	
			String tmpUsername = new String(userName);
			enterprisePrincipal = new ULEnterprisePrincipal(tmpUsername);
			Set<Principal> principalset = subject.getPrincipals();
			if (principalset.contains(enterprisePrincipal) == false)
			{
				principalset.add(enterprisePrincipal);
			}
			for (int i=0; i<roleName.size(); i++)
			{
				String role =(String)roleName.elementAt(i);
				ULLog.debug("Role:"  + role);
				enterpriseRoles = new ULEnterpriseRoles(tmpUsername, role);

				if(principalset.contains(enterpriseRoles) == false)
				{
					principalset.add(enterpriseRoles);
				}
			} 

			commitSucceeded = true;
			ULLog.debug("added EnterprisePrincipal to Subject");
			ULLog.debug("added XrefPrincipal to Subject");

			return true;
		}
	}

 public boolean abort() throws LoginException 
 {
		ULLog.debug("In CustomLoginModule.abort");
		if (verifySucceeded == false) {

			return false;
		} else if ((verifySucceeded == true) && (commitSucceeded == false)) {

			verifySucceeded = false;   			
			enterprisePrincipal = null;
			enterpriseRoles = null;
		} else {
			/*
			 * assert: Our authentication succeeded and commit succeeded, but
			 * some other login modules commit failed.
			 */
			logout();
		}

		return true;
	}

	public boolean logout() throws LoginException 
	{
		
		ULLog.debug("In CustomLoginModule.logout");
	      Set<?> principalset = subject.getPrincipals();
	      principalset.remove(enterprisePrincipal);
	      principalset.remove(enterpriseRoles);
	      enterprisePrincipal = null;
	      enterpriseRoles = null;

	      verifySucceeded = false;
	      commitSucceeded = false;
		return false;
	}

}
