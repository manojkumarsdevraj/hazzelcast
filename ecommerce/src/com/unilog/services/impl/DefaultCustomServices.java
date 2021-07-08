package com.unilog.services.impl;


import com.unilog.services.UnilogFactoryInterface;


public class DefaultCustomServices implements UnilogFactoryInterface{
private static UnilogFactoryInterface serviceProvider;
	
	private DefaultCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (DefaultCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new DefaultCustomServices();
				}
			}
		return serviceProvider;
	}
}
