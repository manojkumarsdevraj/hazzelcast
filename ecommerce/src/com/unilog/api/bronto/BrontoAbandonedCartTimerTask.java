package com.unilog.api.bronto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.utility.CommonUtility;

public class BrontoAbandonedCartTimerTask extends TimerTask {
	ExecutorService executor = null;
	@Override
	public void run() {

		System.out.println("-------------- Checking Abandoned Cart -----------"); 
		ArrayList<BrontoModel> userIdList = new ArrayList<BrontoModel>();

		Connection  conn = null;
		PreparedStatement preStat=null;
		ResultSet rs = null;

		try {
			conn = ConnectionManager.getDBConnection();
			int interval = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("BRONTO_ABANDAONEDCART_THRESHOLD")+"");
			String sql = PropertyAction.SqlContainer.get("getAllAbandonedCart");
			preStat = conn.prepareStatement(sql);	
			preStat.setInt(1, interval);
			rs =preStat.executeQuery();

			while(rs.next()){
				if(rs.getInt("USER_ID") > 0 && rs.getInt("USER_SUBSET") > 0){
					BrontoModel brontoModel = new BrontoModel();
					brontoModel.setUserId(rs.getInt("USER_ID"));
					brontoModel.setSubsetId(rs.getInt("USER_SUBSET"));
					brontoModel.setSecretKey(rs.getString("SECRET_KEY"));
					brontoModel.setSessionId(rs.getString("SESSIONID"));
					
					if(CommonUtility.validateString(rs.getString("BEFORE_LOGIN")).equalsIgnoreCase("Y")){
						brontoModel.setBeforeLoginUser(true);
					}else{
						brontoModel.setBeforeLoginUser(false);
					}
					userIdList.add(brontoModel);
				}


			}

			if(userIdList.size() > 0){
				System.out.println("Found "+userIdList.size()+ " Abandoned Cart(s)");
				int size = userIdList.size();
				ArrayList<Future> futureTasks = new ArrayList<Future>(size);
				if(executor != null && (!executor.isShutdown() || !executor.isTerminated())){
					executor.shutdownNow();
				}
				for (int i = 0; i < userIdList.size(); i++) {

					executor = Executors.newFixedThreadPool(userIdList.size());
					Runnable taskOne = new BrontoAbandonedCartEmailTriggerThread(userIdList.get(i).getUserId(), userIdList.get(i).getSubsetId(), userIdList.get(i).getSecretKey(), userIdList.get(i).getSessionId(),userIdList.get(i).isBeforeLoginUser());
					executor.execute(taskOne);
				}
			}
		} catch (Exception e) {         
			e.printStackTrace();

		} finally {	    	
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(preStat);
			ConnectionManager.closeDBConnection(conn);	
		} 

	}

}
