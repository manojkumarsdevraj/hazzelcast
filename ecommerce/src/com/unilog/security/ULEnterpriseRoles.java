/**
 * 
 */
package com.unilog.security;

import java.io.Serializable;
import java.security.Principal;

/**
 * @author satish
 *
 */
public class ULEnterpriseRoles implements Principal, Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3854741541955125711L;

	
    private String userName;   
    private String roleName;

  
    public ULEnterpriseRoles(String userName, String roleName)
    {
	if (userName == null)
	{
	   throw new NullPointerException("userName cannot be null");
	}

	if (roleName == null)
	{
	   throw new NullPointerException("roleName cannot be null");
	}

	this.userName = userName;
	this.roleName = roleName;
    }

  
    public String getName()
    {
       return roleName;
    }

   
    public String toString()
    {
       return("XrefPrincipal:  " + roleName);
    }

    public boolean equals(Object o)
    {
      if (o == null)
      {
        return false;
      }

      if (this == o)
      {
        return true;
      }

      if ((o instanceof ULEnterpriseRoles) == false)
      {
        return false;
      }

      ULEnterpriseRoles that = (ULEnterpriseRoles) o;

      if (this.getName().equals(that.getName()))
      {
    	  return true;
      }
      return false;
    }
    
    public int hashCode()
    {
    	return userName.hashCode();
    }

}
