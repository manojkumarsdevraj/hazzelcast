package com.unilog.services.impl;

import com.unilog.services.UnilogFactoryInterface;

public class BlackmanPlumbingSupplyCustomServices implements UnilogFactoryInterface{

	private static UnilogFactoryInterface serviceProvider;

	public static UnilogFactoryInterface getInstance() {
		if(serviceProvider == null) {
			synchronized (BlackmanPlumbingSupplyCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new BlackmanPlumbingSupplyCustomServices();
				}
			}
		}
		return serviceProvider;
	}


}

