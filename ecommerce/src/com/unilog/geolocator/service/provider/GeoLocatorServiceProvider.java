package com.unilog.geolocator.service.provider;

import com.unilog.geolocator.GeoLocatorEnums.GeoLocatorProvider;
import com.unilog.geolocator.IGeoLocatorService;
import com.unilog.geolocator.service.google.GoogleGeoLocatorServiceImpl;

public class GeoLocatorServiceProvider {


	
	private GeoLocatorServiceProvider() {
		//avoid instantiation instead use the factory method
	}	

	public static IGeoLocatorService getServiceProvider(GeoLocatorProvider geoLocatorProvider) {
		
		IGeoLocatorService iGeoLocatorService = null;
		switch(geoLocatorProvider) {
			case YAHOO:
				//iGeoLocatorService = GoogleGeoLocatorServiceImpl.getInstance();
			break;
			case GOOGLE:
				iGeoLocatorService = GoogleGeoLocatorServiceImpl.getInstance();
			break;
			
			default:
				throw new IllegalArgumentException(
							"No GeoLocator Service Provider registered with name: " + geoLocatorProvider.name());
		}
		return iGeoLocatorService;
	}
}
