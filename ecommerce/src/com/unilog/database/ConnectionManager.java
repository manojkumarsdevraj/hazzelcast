package com.unilog.database;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.unilog.utility.CommonUtility;

public class ConnectionManager {
	
	public static Connection getDBConnection() throws SQLException
	{
		InitialContext initCtx = null;
        Connection con = null;

		try
		{            
			initCtx = new InitialContext();
			DataSource ds = (javax.sql.DataSource) initCtx.lookup("java:/Cimm2V4DS");
			//con = ds.getConnection();			
			/**If DataSource is NULL, it will show timed out exception. Hence added Null check condition. Please address if any issues-- Chetan Sandesh**/	
			
			if(ds!=null) {
				con = ds.getConnection();
			}else {
				ds = (javax.sql.DataSource) initCtx.lookup("java:/Cimm2V4DS");
				con = ds.getConnection();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		}                  
		finally
		{
			if (initCtx != null)
			{
				try 
				{ 
				  initCtx.close(); 
				}
				catch(NamingException ne) 
				{ 
				  ne.printStackTrace();
				}
			}
		}
        return con;
	}
	
	
	/**
	 *
		* @param ResultSet rs
		*/
		public static void closeDBResultSet(ResultSet rs) {
			
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 *
		 * @param Statement stmt
		 */
		public static void closeDBStatement(Statement stmt) {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 *
		 * @param Statement stmt
		 */
		public static void closeDBPreparedStatement(PreparedStatement pstmt) {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}

	public static final void closeDBConnection(Connection conn)
	{       
		try{ 
			if (conn != null) {
				if (!conn.isClosed()){
					conn.close();
				} 
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HttpSolrServer getSolrClientConnection(String solrUrlString){
		HttpSolrServer server = null;
		try {
			solrUrlString = CommonUtility.getSolrSiteUrl(solrUrlString);
		    server = new HttpSolrServer(CommonUtility.validateString(solrUrlString));
		    //server.setSoTimeout(TIMEOUT_SO); // socket read timeout
		    //server.setConnectionTimeout(TIMEOUT_CONNECTION);
		    server.setDefaultMaxConnectionsPerHost(100);
		    server.setMaxTotalConnections(100);
		    server.setFollowRedirects(false); // defaults to false
		    server.setAllowCompression(true);
		    server.setMaxRetries(1); // defaults to 0. > 1 not recommended.
		    // server.setParser(new XMLResponseParser()); // binary parser is used by default
		    //server.setRequestWriter(new BinaryRequestWriter());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return server;
	}
	
	public static final void closeSolrClientConnection(HttpSolrServer server){       
		try{ 
			if (server != null){
				server.shutdown();
			}
		}catch (Exception e){ 
			e.printStackTrace();
		}
	}
	public static final void closeInputStreamReader(InputStreamReader stream){       
		try{ 
			if (stream != null){
				stream.close();
			}
		}catch (Exception e){ 
			e.printStackTrace();
		}
	}
	
	public static final void closeInputStream(InputStream stream){       
		try{ 
			if (stream != null){
				stream.close();
			}
		}catch (Exception e){ 
			e.printStackTrace();
		}
	}
	public static final void closeFileReader(FileReader stream){       
		try{ 
			if (stream != null){
				stream.close();
			}
		}catch (Exception e){ 
			e.printStackTrace();
		}
	}
	public static final void closeFileWriter(FileWriter stream){       
		try{ 
			if (stream != null){
				stream.close();
			}
		}catch (Exception e){ 
			e.printStackTrace();
		}
	}
	public static final void closePrintWriter(PrintWriter stream){       
		try{ 
			if (stream != null){
				stream.close();
			}
		}catch (Exception e){ 
			e.printStackTrace();
		}
	}
	
}
