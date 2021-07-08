package com.unilog.api.bronto;

import com.unilog.utility.CommonUtility;

public class BrontoAbandonedCartEmailTriggerThread implements Runnable {

	private int userId;
	private int subsetId;
	private String key;
	private String sessionId;
	private boolean isBeforeLogin;

	public BrontoAbandonedCartEmailTriggerThread(int userId, int subsetId, String key, String sessionId,
			boolean isBeforeLogin) {

		this.userId = userId;
		this.subsetId = subsetId;
		this.key = key;
		this.sessionId = sessionId;
		this.isBeforeLogin = isBeforeLogin;
	}

	@Override
	public void run() {
		try {

			int count = BrontoDAO.updateAbandonedCartStatus(userId, key, "S", "", sessionId);
			if (count > 0) {
				BrontoUtility.getInstance().abandonedCart(userId, 0, "",
						CommonUtility.validateParseIntegerToString(subsetId), sessionId, isBeforeLogin);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Send Abandoned cart email failed: " + e.getMessage() + " (" + e.getClass().getName()
					+ ")\n Reverting status");
			BrontoDAO.updateAbandonedCartStatus(userId, key, "N", "", sessionId);
		}
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getSubsetId() {
		return subsetId;
	}

	public void setSubsetId(int subsetId) {
		this.subsetId = subsetId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean isBeforeLogin() {
		return isBeforeLogin;
	}

	public void setBeforeLogin(boolean isBeforeLogin) {
		this.isBeforeLogin = isBeforeLogin;
	}
}
