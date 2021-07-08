package com.unilog.mail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.utility.CommonUtility;

public class UsersSendMailDAO {
	public static int getSendMailCount(String ipaddress) {
		Connection conn = null;
		PreparedStatement preStat = null;
		ResultSet rs = null;
		int requestCount = 0;
		String sqlQuery = null;
		Date date = new Date();
		int timePerIp = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TIME_LIMIT_PERIP"));
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MMM-dd");
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		Date today = new Date();

		Date todayWithZeroTime = null;
		try {
			todayWithZeroTime = formatter.parse(formatter.format(today));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String currentDate = formatter.format(date);
		Date lastDate = new Date();
		long lastHit=0;
		int cnt = 0;
		long current_time = System.currentTimeMillis();
		try {
			conn = ConnectionManager.getDBConnection();
			sqlQuery = "SELECT REQUEST_COUNT,UPDATED_DATETIME,LAST_HIT_TIME FROM SEND_MAIL_REQUEST WHERE USER_IP_ADDRESS = ?";
			preStat = conn.prepareStatement(sqlQuery);
			preStat.setString(1, ipaddress);
			rs = preStat.executeQuery();
			if (rs.next()) {
				requestCount = rs.getInt("REQUEST_COUNT");
				lastDate = rs.getDate("UPDATED_DATETIME");
				lastHit = rs.getLong("LAST_HIT_TIME");	
			}
			long Seconds = (System.currentTimeMillis()-lastHit)/1000;
			lastDate = formatter.parse(formatter.format(lastDate));
			if((todayWithZeroTime!=null && todayWithZeroTime.compareTo(lastDate)>0)||(Seconds>timePerIp)){
				
				ConnectionManager.closeDBPreparedStatement(preStat);
				
				sqlQuery = "UPDATE SEND_MAIL_REQUEST SET REQUEST_COUNT = ?, LAST_HIT_TIME =?, UPDATED_DATETIME=to_date(?,'yyyy-mm-dd HH24:MI:SS') WHERE USER_IP_ADDRESS = ?";
				preStat = conn.prepareStatement(sqlQuery);
				preStat.setInt(1, 0);
				preStat.setLong(2,current_time);
				preStat.setString(3, simpledateformat.format(date));
				preStat.setString(4, ipaddress);
				cnt = preStat.executeUpdate();
				requestCount = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.closeDBPreparedStatement(preStat);
			ConnectionManager.closeDBConnection(conn);
		} // finally

		return requestCount;
	}
	public static int saveSendPageMail(String ipaddress, int requestcount, String sessionId) {
		ResultSet rs = null;
		String sqlQuery = null;
		Connection conn = null;
		Date date = new Date();
		PreparedStatement pstmt = null;
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		long current_time = System.currentTimeMillis();
		int cnt = 0;
		int total_count = 0;

		try {
			conn = ConnectionManager.getDBConnection();
			sqlQuery = "SELECT TOTAL_COUNT FROM SEND_MAIL_REQUEST WHERE USER_IP_ADDRESS = ?";
			pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setString(1, ipaddress);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				total_count = rs.getInt("TOTAL_COUNT");
			}
			if(total_count<1){
				ConnectionManager.closeDBStatement(pstmt);
				sqlQuery = "INSERT INTO SEND_MAIL_REQUEST (USER_IP_ADDRESS, REQUEST_COUNT, SESSION_ID, UPDATED_DATETIME, TOTAL_COUNT, LAST_HIT_TIME) VALUES (?, ?, ?, to_date(?,'yyyy-mm-dd HH24:MI:SS'),?,?)";
				pstmt = conn.prepareStatement(sqlQuery);
				pstmt.setString(1, ipaddress);
				pstmt.setInt(2, requestcount);
				pstmt.setString(3, sessionId);
				pstmt.setString(4, simpledateformat.format(date));
				pstmt.setInt(5, requestcount);
				pstmt.setLong(6, current_time);
				cnt = pstmt.executeUpdate();
			}else{
				updateSendPageMail(requestcount,ipaddress);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return cnt;
	}
	public static void updateSendPageMail(int requestCount, String ipaddress) {
		Connection conn = null;
		PreparedStatement preStat = null;
		ResultSet rs = null;
		String sqlQuery = null;
		int cnt = 0;
		int total_count = 0;
		Date date = new Date();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MMM-dd");
		try {
			conn = ConnectionManager.getDBConnection();
			sqlQuery = "SELECT TOTAL_COUNT FROM SEND_MAIL_REQUEST WHERE USER_IP_ADDRESS = ?";
			preStat = conn.prepareStatement(sqlQuery);
			preStat.setString(1, ipaddress);
			rs = preStat.executeQuery();
			if (rs.next()) {
				total_count = rs.getInt("TOTAL_COUNT");
			}
			ConnectionManager.closeDBStatement(preStat);
			sqlQuery = "UPDATE SEND_MAIL_REQUEST SET REQUEST_COUNT = ?,TOTAL_COUNT =?,UPDATED_DATETIME=to_date(?,'yyyy-mm-dd HH24:MI:SS')  WHERE USER_IP_ADDRESS = ?";
			preStat = conn.prepareStatement(sqlQuery);
			preStat.setInt(1, requestCount);
			preStat.setInt(2, total_count+1);
			preStat.setString(3, simpledateformat.format(date));
			preStat.setString(4, ipaddress);
			cnt = preStat.executeUpdate();

			ConnectionManager.closeDBPreparedStatement(preStat);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.closeDBPreparedStatement(preStat);
			ConnectionManager.closeDBConnection(conn);
		} // finally

		return;
	}
	/*spam email changes start*/
	public static boolean saveSpamEmailsInDB(String toAddress, String cc, String bcc, String user_id, String emailSubject) {

		boolean success = false;
		ResultSet rs = null;
		String sqlQuery = null;
		Connection conn = null;
		Date date = new Date();
		PreparedStatement pstmt = null;
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

		try {
			conn = ConnectionManager.getDBConnection();
			sqlQuery = "INSERT INTO BLOCKED_MAIL_CONTENT(BLOCKED_EMAIL_ID, TO_EMAIL, CC_EMAIL, BCC_EMAIL, USER_EDITED, UPDATED_DATETIME, SUBJECT) VALUES (BLOCKED_EMAIL_SEQ.NEXTVAL, ?, ?, ?, ?, to_date(?,'yyyy-mm-dd HH24:MI:SS'),?)";
			// pstmt = (OraclePreparedStatement)
			// conn.prepareStatement(sqlQuery);
			pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setString(1, toAddress);
			if (bcc == null) {
				pstmt.setString(2, "");
			} else {
				pstmt.setString(2, cc);
			}
			if (bcc == null) {
				pstmt.setString(3, "");
			} else {
				pstmt.setString(3, bcc);
			}
			pstmt.setString(4, user_id);
			pstmt.setString(5, simpledateformat.format(date));
			pstmt.setString(6, emailSubject);
			pstmt.executeUpdate();
			success = true;

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return success;
	}

}
