package com.unilog.services.impl;

import com.unilog.services.UnilogFactoryInterface;

public class HirschCustomServices implements UnilogFactoryInterface {
	private static UnilogFactoryInterface serviceProvider;
	private HirschCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (HirschCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new HirschCustomServices();
				}
			}
		return serviceProvider;
	}
	@Override
	public String setFirstLoginTrueForInactiveUser() {
		return "Y";
	}

}
