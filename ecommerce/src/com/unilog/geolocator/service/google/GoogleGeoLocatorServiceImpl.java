package com.unilog.geolocator.service.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.httpclient.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unilog.database.CommonDBQuery;
import com.unilog.geolocator.GeoLocatorEnums;
import com.unilog.geolocator.GeoLocatorEnums.AddressComponentType;
import com.unilog.geolocator.GeoLocatorRequest;
import com.unilog.geolocator.GeoLocatorResponse;
import com.unilog.geolocator.IGeoLocatorService;
import com.unilog.geolocator.service.google.model.GoogleAddressComponent;
import com.unilog.geolocator.service.google.model.GoogleGeoLocatorData;
import com.unilog.geolocator.service.google.model.GoogleGeoLocatorResponse;
import com.unilog.utility.CommonUtility;

public class GoogleGeoLocatorServiceImpl extends AbstractGoogleServiceImpl implements IGeoLocatorService {

	private Gson Gson;
	private static GoogleGeoLocatorServiceImpl googleGeoLocatorServiceImpl;

	@Override
	public GeoLocatorResponse locateUser(GeoLocatorRequest locationDetail) {
		GeoLocatorResponse geoLocatorResponse = null;
		String url = getGoogleApiUrl(CommonUtility.validateString(locationDetail.getSearchString()));
		System.out.println("Request -- " + url);
		try {

			URL urlobj = new URL(getGoogleApiUrl(CommonUtility.validateString(locationDetail.getSearchString())));
			HttpURLConnection urlConnection = (HttpURLConnection) urlobj.openConnection();
			urlConnection.setRequestMethod("GET");
			int responseCode = urlConnection.getResponseCode();
			if (responseCode == HttpStatus.SC_OK) {

				Gson = new Gson();
				BufferedReader urlBufferReader = new BufferedReader(new InputStreamReader(
						responseCode == 200 ? urlConnection.getInputStream() : urlConnection.getErrorStream()));
				StringBuffer responseBody = new StringBuffer();
				String line;
				while ((line = urlBufferReader.readLine()) != null) {
					responseBody.append(line);
				}
				String response = responseBody.toString();
				System.out.println("Response -- " + response);
				JsonElement json = new JsonParser().parse(response);
				JsonObject jsonObject = json.getAsJsonObject();
				GoogleGeoLocatorResponse googleGeoLocatorResponse = Gson.fromJson(jsonObject, GoogleGeoLocatorResponse.class);
				ArrayList<GoogleGeoLocatorData> geoLocatorData = googleGeoLocatorResponse.getResults();
				if(geoLocatorData != null && geoLocatorData.size() > 0){
					geoLocatorResponse = new GeoLocatorResponse();
					geoLocatorResponse.setLatitude(CommonUtility.validateParseDoubleToString(geoLocatorData.get(0).getGeometry().getLocation().getLat()));
					geoLocatorResponse.setLongitude(CommonUtility.validateParseDoubleToString(geoLocatorData.get(0).getGeometry().getLocation().getLng()));

					ArrayList<GoogleAddressComponent> addressComponentList = geoLocatorData.get(0).getAddress_components();

					if (addressComponentList != null && addressComponentList.size() > 0) {
						for (GoogleAddressComponent googleAddressComponent : addressComponentList) {
							AddressComponentType addressComponentType = addressComponentEnumContains(googleAddressComponent.getTypes());
							if (addressComponentType != null) {
								switch (addressComponentType) {
								case ADMINISTRATIVE_AREA_LEVEL_1:
									geoLocatorResponse.setState(googleAddressComponent.getShort_name());
									break;
								case ADMINISTRATIVE_AREA_LEVEL_2:
									geoLocatorResponse.setCounty(googleAddressComponent.getShort_name());
									break;
								case COUNTRY:
									geoLocatorResponse.setCountry(googleAddressComponent.getShort_name());
									break;
								case LOCALITY:
									geoLocatorResponse.setCity(googleAddressComponent.getShort_name());
									break;
								case POSTAL_CODE:
									geoLocatorResponse.setZipCode(googleAddressComponent.getShort_name());
									break;
								default:
									break;
								}
							}
						}
					}

				}
			}

		} catch (MalformedURLException ex) {
			System.out.println(url + " is not valid");
			ex.printStackTrace();
		} catch (IOException ie) {
			System.out.println("IO Exception " + ie.getMessage());
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return geoLocatorResponse;
	}

	public static GoogleGeoLocatorServiceImpl getInstance() {
		synchronized(GoogleGeoLocatorServiceImpl.class) {
			if(googleGeoLocatorServiceImpl == null) {
				googleGeoLocatorServiceImpl = new GoogleGeoLocatorServiceImpl();
			}
			googleGeoLocatorServiceImpl.init();
		}
		return googleGeoLocatorServiceImpl;
	}

	protected String getGoogleApiUrl(String searckKey) {
		String defaultCountry = CommonDBQuery.getSystemParamtersList().get("GOOGLE_LOCATION_DEFAULT_COUNTRY");
		
		if(GEO_API_KEY.contains("=")) {
			if(CommonUtility.validateString(defaultCountry).length()>0) {
				return GEO_API_URL + "?" + URL_PARAMETER_ADDRESS + "=" + searckKey + "&" + URL_PARAMETER_COMPONENTS + "=" + "country:"+defaultCountry+"&" + URL_PARAMETER_KEY 
						+ GEO_API_KEY;
			}else {
				return GEO_API_URL + "?" + URL_PARAMETER_ADDRESS + "=" + searckKey + "&" + URL_PARAMETER_COMPONENTS + "=" + "country:US&" + URL_PARAMETER_KEY 
						+ GEO_API_KEY;
			}
			
		}else {
			return GEO_API_URL + "?" + URL_PARAMETER_ADDRESS + "=" + searckKey + "&" + URL_PARAMETER_KEY + "="
					+ GEO_API_KEY;
		}

		
	}

	private AddressComponentType addressComponentEnumContains(ArrayList<String> types) {

		AddressComponentType addressComponentType = null;
		for (AddressComponentType c : AddressComponentType.values()) {
			for (String string : types) {
				if (c.name().equalsIgnoreCase(string)) {
					addressComponentType = GeoLocatorEnums.getAddressComponentType(string);
					break;
				}
			}
		}

		return addressComponentType;
	}
}
