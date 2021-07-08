package com.unilog.defaults;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jboss.resource.adapter.jdbc.WrappedConnection;

import com.unilog.database.ConnectionManager;

import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;


/**
 * Servlet implementation class SessionListen
 */
public class SessionListen extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SessionListen() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse respose)throws ServletException, IOException 
    {
    	OutputStream os = respose.getOutputStream();
    	Connection  conn = null;
	    CallableStatement stmt = null;
	    ResultSet searchResult = null;
	    String reqLevel = request.getParameter("id");
	    String buyId = request.getParameter("buyId");
	    boolean getSessionUser = true;
	    int requestLevel = 0;
	    int buyingCompanyId = 0;
	    if(reqLevel==null)
	    	reqLevel="1";
	    if(buyId==null)
	    	buyId = "0";
	    
	    buyingCompanyId = Integer.parseInt(buyId);
	    
	    requestLevel = Integer.parseInt(reqLevel);
	    
	    
	    try {
            conn = ConnectionManager.getDBConnection();
           
        } catch (SQLException e) { 
            e.printStackTrace();
        }	 
        try
        {
        	StringBuilder sb = new StringBuilder();
        	if(requestLevel==3 && buyingCompanyId==0)
        	{
        		getSessionUser = false;
        		sb.append("0~Buying Company Id not found.");
        	}
        	
        		if(getSessionUser)
        		{
        			String FILTERATTRID[] = null;
                	int numberOfLogUser = 0;
                	int numOfSession = SessionCounterListener.getTotalActiveSession();
                	int loggedInUser = 0;
                	ArrayList<String> numberOfActiveId = SessionCounterListener.getTotalActiveSessionId();
                	StringBuilder sb1 = null;
                	if(numberOfActiveId.size()>0)
                	{
                		sb1 = new StringBuilder();
                		
                	Connection oracleConnection = null;
             		if (conn instanceof org.jboss.resource.adapter.jdbc.WrappedConnection) {
         				WrappedConnection wc = (WrappedConnection) conn;
         				// with getUnderlying connection method , cast it to Oracle
         				// Connection
         				oracleConnection = wc.getUnderlyingConnection();
         			}
             		FILTERATTRID = new String[numberOfActiveId.size()];
             		int count = 0;
             		for(String numOfuser:numberOfActiveId)
             		{
             			FILTERATTRID[count] = numOfuser.toString();
             			count++;
             		}
             		
                	ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor( "STRING_ARRAY", oracleConnection );
             		ARRAY FILTER_ATTR_ID = new ARRAY( descriptor, oracleConnection, FILTERATTRID );
             		
             		stmt = conn.prepareCall("{call ACTIVESESSION_PROC(?,?,?,?)}");
             		stmt.setArray(1, FILTER_ATTR_ID);
             		stmt.setInt(2, requestLevel);
             		stmt.setInt(3, buyingCompanyId);
             		stmt.registerOutParameter(4,OracleTypes.CURSOR);
             		stmt.execute();
             		searchResult = (ResultSet) stmt.getObject(4);
             		
             		if(requestLevel==1)
             		{
             			
             			if(searchResult.next())
             			{
             				loggedInUser = searchResult.getInt("CNT");
             			}
             			sb.append("Non Logged In User^"+(numOfSession-loggedInUser)+"~Logged In User^"+loggedInUser);
             		}
             		else if(requestLevel==2)
             		{
             			String c = "";
             			
             			while(searchResult.next())
             			{
             				sb.append(c).append(searchResult.getInt("BUYING_COMPANY_ID")).append("^").append(searchResult.getString("CUSTOMER_NAME")).append("^").append(searchResult.getInt("CNT"));
             				c = "~";
             			}
             			System.out.println("Session BC List : " + sb.toString());
             		}
             		else if(requestLevel==3)
             		{
             			String buyingCompanyName = null;
             			while(searchResult.next())
                 		{
                 			
                 			 sb1.append("<tr class=\"rich-table-row\"><td class=\"rich-table-cell\">"+searchResult.getString("USER_ID")+"</td><td class=\"rich-table-cell\">"+searchResult.getString("USER_NAME")+"</td><td class=\"rich-table-cell\">"+searchResult.getString("CUSTOMER_NAME")+"</td><td class=\"rich-table-cell\">"+searchResult.getString("USER_IP_ADDRESS")+"</td></tr>");
                 			System.out.println("User Name : " + searchResult.getString("USER_NAME"));
                 			System.out.println("USER IP ADDRESS : " + searchResult.getString("USER_IP_ADDRESS"));
                 			System.out.println("Buying Company : " + searchResult.getString("CUSTOMER_NAME"));
                 			if(buyingCompanyName==null)
                 				buyingCompanyName = searchResult.getString("CUSTOMER_NAME");
                 			
                 		}
             			
                		sb.append("<table class=\"rich-table\"");
                 	    sb.append("<thead>");
                 	    sb.append("<tr class=\"rich-table-subheader\"><td colspan='4' class=\"rich-table-subheadercell\">").append("Logged In User Details").append("</td></tr>");
                 	    sb.append("<tr class=\"rich-table-subheader\"><td class=\"rich-table-subheadercell\">User Id</td><td class=\"rich-table-subheadercell\">User Name</td><td class=\"rich-table-subheadercell\">Buying Company</td><td class=\"rich-table-subheadercell\">Ip Address</td></tr>");
                 	    sb.append("</thead>");
                	    sb.append("<tbody>");
                	    sb.append(sb1.toString());
             			sb.append("</tbody>");
                		sb.append("</table>");
             		}
                	}
                	else
                	{
                		sb.append("Non Logged In User^"+numOfSession+"~Logged In User^"+loggedInUser);
                	}
           		
        		}
        		os.write(sb.toString().getBytes());
        	
        	
        	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	ConnectionManager.closeDBResultSet(searchResult);
        	ConnectionManager.closeDBStatement(stmt);
        	ConnectionManager.closeDBConnection(conn);
        	
        }
    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
