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
public class ULEnterprisePrincipal implements Principal, Serializable  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 664117596946724606L;
	private String userName;

    
    public ULEnterprisePrincipal(String userName)
    {
	if (userName == null)
	{
	   throw new NullPointerException("userName cannot be null");
	}

	this.userName = userName;
    }

   
    public String getName()
    {
       return userName;
    }

   
    public String toString()
    {
       return("EnterprisePrincipal:  " + userName);
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
 
      if ((o instanceof ULEnterprisePrincipal) == false)
      {
        return false;
      }
       
      ULEnterprisePrincipal that = (ULEnterprisePrincipal) o;

      if (this.getName().equals(that.getName()))
      {
	 return true;
      }

      return false;
    }
 
    /**
     * Return a hash code for this <code>EnterprisePrincipal</code>.
     *
     * <p>
     *
     * @return hashcode
     */
    public int hashCode()
    {
       return userName.hashCode();
    }
}
