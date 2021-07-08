package com.unilog.geolocator;

import com.unilog.utility.CommonUtility;

public class GeoLocatorEnums {

	public enum GeoLocatorProvider {
		YAHOO, GOOGLE
	};

	public enum AddressComponentType {
		POSTAL_CODE, LOCALITY, ADMINISTRATIVE_AREA_LEVEL_2, ADMINISTRATIVE_AREA_LEVEL_1, COUNTRY
	};

	public static AddressComponentType getAddressComponentType(String actionType) {
		AddressComponentType addressComponentType = null;
		if (!CommonUtility.validateString(actionType).isEmpty()) {

			if (actionType.equalsIgnoreCase(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1.name())) {
				addressComponentType = AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1;
			} else if (actionType.equalsIgnoreCase(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2.name())) {
				addressComponentType = AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2;
			} else if (actionType.equalsIgnoreCase(AddressComponentType.COUNTRY.name())) {
				addressComponentType = AddressComponentType.COUNTRY;
			} else if (actionType.equalsIgnoreCase(AddressComponentType.LOCALITY.name())) {
				addressComponentType = AddressComponentType.LOCALITY;
			} else if (actionType.equalsIgnoreCase(AddressComponentType.POSTAL_CODE.name())) {
				addressComponentType = AddressComponentType.POSTAL_CODE;
			}
		}
		return addressComponentType;
	}

	public static GeoLocatorProvider getGeoLocatorType(String geoLocatorType) {
		GeoLocatorProvider geoLocatorProvider = null;
		if (!CommonUtility.validateString(geoLocatorType).isEmpty()) {

			if (geoLocatorType.equalsIgnoreCase(GeoLocatorProvider.GOOGLE.name())) {
				geoLocatorProvider = GeoLocatorProvider.GOOGLE;
			} else if (geoLocatorType.equalsIgnoreCase(GeoLocatorProvider.YAHOO.name())) {
				geoLocatorProvider = GeoLocatorProvider.YAHOO;
			}
		}
		return geoLocatorProvider;
	}
}
