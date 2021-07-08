package com.unilog.users;

import javax.servlet.http.HttpSession;

public class AsyncShipEntity implements Runnable {

	private HttpSession session;
	private UsersModel usersModel;
	private String shipToWarehouseCode;
	public AsyncShipEntity(HttpSession _session,UsersModel _usersModel,String _shipToWarehouseCode) {
		this.session = _session;
		this.usersModel = _usersModel;
		this.shipToWarehouseCode = _shipToWarehouseCode;
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		UsersAction.asyncAssignShipEntity(usersModel, session, shipToWarehouseCode);
		
	}


}
