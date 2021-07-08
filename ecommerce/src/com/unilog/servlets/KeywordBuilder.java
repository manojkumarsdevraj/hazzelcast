package com.unilog.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.unilog.database.ConnectionManager;
import com.unilog.users.UsersDAO;


public class KeywordBuilder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public KeywordBuilder() {
        super();
       
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ResultSet rs = null;
		PreparedStatement pstmt=null;
	    String sql = "";
	    Connection  conn = null;
		ArrayList<Integer> userIdList = null;
		ArrayList<Integer> buyingCompanyIdList = null;
		try{
			//HttpSession session = request.getSession();
			conn = ConnectionManager.getDBConnection();
			
			
			String requestType = request.getParameter("REQUEST");
			boolean userKeyword = true;
			boolean customerKeyword = true;
			if(requestType!=null && requestType.trim().length()>0){
				userKeyword = false;
				customerKeyword = false;
				if(requestType.trim().equalsIgnoreCase("USER")){
					userKeyword = true;
				}
				if(requestType.trim().equalsIgnoreCase("CUSTOMER")){
					customerKeyword = true;
				}
			}
			
			if(userKeyword){
				userIdList = new ArrayList<Integer>();
				String status = "Y";
				if(request.getParameter("STATUS")!=null && request.getParameter("STATUS").trim().length()>0){
					status = request.getParameter("STATUS").trim();
					sql = "SELECT * FROM CIMM_USERS WHERE STATUS = ?";
			        pstmt = conn.prepareStatement(sql);
			        pstmt.setString(1, status);
				}else{
					sql = "SELECT * FROM CIMM_USERS";
			        pstmt = conn.prepareStatement(sql);
				}
			    rs = pstmt.executeQuery();
			    while(rs.next()){
			        	userIdList.add(rs.getInt("USER_ID"));
			    }
			    ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
			}
			
			if(customerKeyword){
			   buyingCompanyIdList = new ArrayList<Integer>();
			   String status = "A";
			   
			   if(request.getParameter("STATUS")!=null && request.getParameter("STATUS").trim().length()>0){
					status = request.getParameter("STATUS").trim();
					sql = "SELECT * FROM BUYING_COMPANY WHERE STATUS = ?";
			        pstmt = conn.prepareStatement(sql);
			        pstmt.setString(1, status);
			   }else{
				    sql = "SELECT * FROM BUYING_COMPANY";
			        pstmt = conn.prepareStatement(sql);
			   }
			   		
			    rs = pstmt.executeQuery();
			    while(rs.next()){
			       buyingCompanyIdList.add(rs.getInt("BUYING_COMPANY_ID"));
			    }
			    ConnectionManager.closeDBResultSet(rs);
			    ConnectionManager.closeDBPreparedStatement(pstmt);
			}
			ConnectionManager.closeDBConnection(conn);
			if(userIdList!=null && userIdList.size()>0){
				for(Integer userId : userIdList){
					UsersDAO.buildKeyWord("USER", userId);  //buildUserKeyWord
					System.out.println("USER Keyword built For : "+userId);
				}
			}
			
			if(buyingCompanyIdList!=null && buyingCompanyIdList.size()>0){
				for(Integer buyingCompanyId : buyingCompanyIdList){
					UsersDAO.buildKeyWord("BUYINGCOMPANY", buyingCompanyId); //buildUserKeyWord
					System.out.println("Customer Keyword built For : "+buyingCompanyId);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
