package com.unilog.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.cenpos.paymentgateway.CenposEncryptor;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.StaticPageModel;
import com.unilog.maplocation.LocationModel;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.services.factory.UnilogEcommFactory;
import com.unilog.users.ShipVia;
import com.unilog.users.UsersDAO;
import com.unilog.users.WarehouseModel;
import com.unilog.velocitytool.CIMM2VelocityTool;
import com.unilognew.util.ECommerceEnumType.RequestHandlers;
import org.apache.commons.lang3.StringEscapeUtils;
import java.util.Arrays;
import com.unilog.products.XSSRequestWrappers;

public class CommonUtility {
	private static HttpServletRequest request;
	public static UnilogFactoryInterface customServiceUtility() {
		UnilogFactoryInterface serviceClass = null;
		try {
			serviceClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
			return serviceClass;
		} catch (Exception e) {
			e.printStackTrace();
			serviceClass = null;
		}
		return serviceClass;
	}

	public enum Months {
		JANUARY("01"), FEBRUARY("02"), MARCH("03"), APRIL("04"), MAY("05"), JUNE("06"), JULY("07"), AUGUST("08"),
		SEPTEMBER("09"), OCTOBER("10"), NOVEMBER("11"), DECEMBER("12");

		Months(String month) {
			this.month = month;
		}

		private String month;

		public String getMonth() {
			return month;
		}
	}

	public static String sanitizeHtml(String value) {
		try {
			value = validateString(value);
			if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SECURITY_POLICY")).equalsIgnoreCase("Y")){
				 if (value.length() > 0) {
						value = Htmlsanitizer.POLICY_DEFINITION.sanitize(value);
						value = addWhiteSpaceAfterLeftArrow(value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static String addWhiteSpaceAfterLeftArrow(String value) {
		try {
			if (validateString(value).length() > 0 && validateString(value).contains("<")) {
				value = validateString(value).replaceAll("<", "< ");
				value = validateString(value).replaceAll("<  ", "< ");
				value = validateString(value).replaceAll("<   ", "< ");
				value = validateString(value).replaceAll("<    ", "< ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static int validateNumber(String num) {
		int intNum = 0;
		try {
			intNum = Integer.parseInt(num);
		} catch (Exception e) {
			intNum = 0;
		}
		return intNum;
	}

	public static String validateString(String s) {
		try {
			if (s == null) {
				s = "";
			} else {
				if (s.trim().equalsIgnoreCase("NAN")) {
					s = "";
				} else {
					s = s.trim();
				}
			}
		} catch (Exception e) {

			s = "";
		}
		return s;
	}

	public static String validateParseIntegerToString(Integer number) {
		String numberString = "";
		if (number == null) {
			return "";
		}
		try {
			numberString = String.valueOf(number);
		} catch (Exception e) {
			numberString = "";
		}
		return numberString;
	}

	public static String validateParseDoubleToString(Double number) {
		String numberString = "";
		if (number == null) {
			return "";
		}
		try {
			numberString = new Double(number).toString();
		} catch (Exception e) {
			numberString = "";
		}
		return numberString;
	}

	public static DecimalFormat getPricePrecision(HttpSession session) {
		DecimalFormat df = new DecimalFormat("####0.00");
		try {
			String pricePrecision = "";
			if (session != null && session.getAttribute("pricePrecision") != null) {
				pricePrecision = validateString((String) session.getAttribute("pricePrecision"));
			} else if (CommonUtility
					.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision"))
					.length() > 0) {
				pricePrecision = CommonUtility
						.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision"));
			} else {
				pricePrecision = "2";
			}

			if (validateString(pricePrecision).equalsIgnoreCase("4")) {
				df = new DecimalFormat("####0.0000");
			} else if (validateString(pricePrecision).equalsIgnoreCase("3")) {
				df = new DecimalFormat("####0.000");
			} else {
				df = new DecimalFormat("####0.00");
			}
		} catch (Exception e) {
			df = new DecimalFormat("####0.00");
		}
		return df;
	}

	public static double validateDoubleNumber(String num) {
		double doubleNum = 0.0;
		try {
			doubleNum = Double.parseDouble(num);
		} catch (Exception e) {
			doubleNum = 0.0;
		}
		return doubleNum;
	}

	public static int genrateRandomSevenDigitInteger() {
		int intNum = 0;
		try {
			intNum = (int) ((Math.random() * 9000000) + 1000000);
		} catch (Exception e) {
			intNum = 0;
		}
		return intNum;
	}

	public static boolean validateURl(String url) {
		boolean validUrl = false;
		try {
			String urlRegex = "\\b(https?|ftp|file|ldap)://" + "[-A-Za-z0-9+&@#/%?=~_|!:,.;]"
					+ "*[-A-Za-z0-9+&@#/%=~_|]";
			if (validateString(url).length() > 0 && url.matches(urlRegex)) {
				validUrl = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validUrl;
	}

	public static boolean validateEmail(String emailAddress) {
		boolean isValid = false;
		try {
			if (validateString(emailAddress).length() > 0) {
				String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[_A-Z-a-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
				CharSequence inputStr = emailAddress;
				Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(inputStr);
				if (matcher.matches()) {
					isValid = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isValid;
	}

	public static boolean validatePhoneNumber(String phoneNumber) {
		boolean isValid = false;
		try {
			String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
			CharSequence inputStr = phoneNumber;
			Pattern pattern = Pattern.compile(expression);
			Matcher matcher = pattern.matcher(inputStr);
			if (validateString(phoneNumber).length() > 0 && matcher.matches()) {
				isValid = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isValid;
	}

	public static double roundHalfUp(String price) {

		if (CommonUtility.validateString(price).isEmpty()) {
			return 0.00;
		}

		double roundedPrice = 0;
		try {
			int pricePrecision = CommonUtility.validateNumber(CommonUtility
					.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision")));
			if (pricePrecision < 1) {
				pricePrecision = 2;
			}
			String[] numberPartsArray = price.split("\\.");
			int decimalLength = numberPartsArray.length > 1 ? numberPartsArray[1].length() : 0;
			BigDecimal bigDecimal = null;
			BigDecimal roundedBigDecimalresult = null;

			if (pricePrecision > decimalLength) {
				DecimalFormat df = CommonUtility.getPricePrecision(null);
				price = df.format(CommonUtility.validateDoubleNumber(price));
				decimalLength = pricePrecision;
			}
			for (int i = 0; i < decimalLength; i++) {
				bigDecimal = new BigDecimal(
						roundedBigDecimalresult != null ? roundedBigDecimalresult.toString() : price);
				roundedBigDecimalresult = bigDecimal.setScale(decimalLength - i, RoundingMode.HALF_UP);
				if (decimalLength - i == pricePrecision) {
					roundedPrice = roundedBigDecimalresult.doubleValue();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roundedPrice;
	}

	public static String getCountryCode(String countryCode, String methodName) {
		try {
			if (validateString(countryCode).equalsIgnoreCase("US")
					|| validateString(countryCode).equalsIgnoreCase("USA")) {
				if (validateString(methodName).equalsIgnoreCase("Registration")) {
					countryCode = "USA";
				} else {
					countryCode = "US";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return countryCode;
	}

	public static Boolean convertToBoolean(String value) {

		if (value != null) {
			value = value.trim();
			if (value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("T") || value.equalsIgnoreCase("TRUE")) {
				return true;
			} else if (value.equalsIgnoreCase("N") || value.equalsIgnoreCase("F") || value.equalsIgnoreCase("FALSE")) {
				return false;
			} else {
				try {
					int iValue = Integer.parseInt(value);
					if (iValue != 0) {
						return true;
					} else {
						return false;
					}
				} catch (NumberFormatException nfe) {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	public static LinkedHashMap sortHashMapByValuesIntegerString(HashMap passedMap) {
		List mapKeys = new ArrayList(passedMap.keySet());
		List mapValues = new ArrayList(passedMap.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		LinkedHashMap sortedMap = new LinkedHashMap();
		try {
			Iterator valueIt = mapValues.iterator();
			while (valueIt.hasNext()) {
				Object val = valueIt.next();
				Iterator keyIt = mapKeys.iterator();

				while (keyIt.hasNext()) {
					Object key = keyIt.next();
					String comp1 = passedMap.get(key).toString();
					String comp2 = val.toString();

					if (comp1.equals(comp2)) {
						passedMap.remove(key);
						mapKeys.remove(key);
						sortedMap.put((Integer) key, (String) val);
						break;
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sortedMap;
	}

	public static boolean checkDBColumn(ResultSet rs, String columnName) {
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();
			for (int x = 1; x <= columns; x++) {
				if (columnName.equals(rsmd.getColumnName(x))) {
					return true;
				}
			}
		} catch (SQLException e) {
			return false;
		}

		return false;
	}

	public static LinkedHashMap<String, String> getBrowserCookies(HttpServletRequest request) {
		LinkedHashMap<String, String> httpCookie = new LinkedHashMap<String, String>();
		try {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				System.out.println("--------- Cookies ----------");
				for (Cookie cookie : cookies) {
					System.out.println(cookie.getName() + " : " + cookie.getValue());
					httpCookie.put(cookie.getName(), cookie.getValue());
				}
				System.out.println("--------- Cookies ----------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpCookie;
	}

	public static String validatePricePrecisionString(String s) {
		try {
			if (s == null) {
				s = "2";
			} else {
				if (s.trim().equalsIgnoreCase("NAN")) {
					s = "2";
				} else {
					s = s.trim();
				}
			}
		} catch (Exception e) {

			s = "2";
		}
		return s;
	}

	public static boolean copyFile(File from, File to, HttpSession session) {
		try {
			FileInputStream fileInStream = new FileInputStream(from);
			FileOutputStream fileOutStream = new FileOutputStream(to);
			boolean emptyFile = true;
			byte buf[] = new byte[1024];
			int i = 0;

			while ((i = fileInStream.read(buf)) != -1) {
				emptyFile = false;
				fileOutStream.write(buf, 0, i);
			}
			fileInStream.close();
			fileOutStream.close();
			if (emptyFile == true) {
				to.delete();
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyAddToCartRestrictions(ProductsModel product, int qty) {
		boolean status = true;
		if (qty <= 0) {
			status = false;
		} else if (qty < product.getMinOrderQty()) {
			status = false;
		} else if (qty % product.getOrderInterval() != 0) {
			status = false;
		}
		return status;
	}

	public static String convertXMLToHTML(Source xml, Source xslt) {
		StringWriter sw = new StringWriter();

		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer trasform = tFactory.newTransformer(xslt);
			trasform.transform(xml, new StreamResult(sw));
			// System.out.println("product.html generated successfully "+sw.toString());
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

	public static long startTimeDispaly() {
		long startTime = 0;
		if (validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_LOG_PRINT_FOR_TIME_ELAPSED"))
				.equalsIgnoreCase("Y")) {
			try {
				startTime = new Date().getTime(); // start time
				// System.out.println("@ startTimeDispaly Start Time milliseconds: "+startTime);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error in CommonUtility startTime()");
			}
		}
		return startTime;
	}

	public static void endTimeAndDiffrenceDisplay(long startTime) {
		long endTime = 0;
		if (validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_LOG_PRINT_FOR_TIME_ELAPSED"))
				.equalsIgnoreCase("Y")) {
			try {
				endTime = new Date().getTime();
				long difference = endTime - startTime; // check difference
				long seconds = TimeUnit.MILLISECONDS.toSeconds(difference);
				long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);
				StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
				System.out.println("\n******************************************************");
				System.out.println("Class Name  :" + stackTraceElements[2].getClassName());
				System.out.println("File Name   :" + stackTraceElements[2].getFileName());
				System.out.println("Line Number :" + stackTraceElements[2].getLineNumber());
				System.out.println("Method Name :" + stackTraceElements[2].getMethodName());
				System.out.println("Start Time milliseconds :" + startTime);
				System.out.println("End Time milliseconds   :" + endTime);
				System.out.println("Elapsed milliseconds    :" + difference);
				System.out.println("Elapsed Seconds         :" + seconds);
				System.out.println("Elapsed Minutes         :" + minutes);
				System.out.println("\n******************************************************\n");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error in CommonUtility endTimeAndDiffrence()");
			}
		}
	}

	public static Map<String, WarehouseModel> getWarehousesAsMap(List<WarehouseModel> wareHouses) {
		Map<String, WarehouseModel> wareHousesAsMap = new LinkedHashMap<>();
		for (WarehouseModel wareHouse : wareHouses) {
			wareHousesAsMap.put(wareHouse.getWareHouseCode(), wareHouse);
		}
		return wareHousesAsMap;
	}

	public static ShipVia getShipViaByCode(List<ShipVia> shipVias, String shipViaCode) {
		ShipVia shipVia = null;
		for (ShipVia eachShipVia : shipVias) {
			if (eachShipVia.getExternalSystemCode().equalsIgnoreCase(shipViaCode)) {
				shipVia = eachShipVia;
				break;
			}
		}
		return shipVia;
	}

	public static List<ProductsModel> mergeCustomFields(List<ProductsModel> products, int subsetId,
			int generalSubsetId) {
		List<String> itemIds = getItemIds(products);
		LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldValuesByItems = ProductHunterSolr
				.getCustomFieldValuesByItems(subsetId, generalSubsetId, StringUtils.join(itemIds, " OR "), "itemid");
		if (customFieldValuesByItems != null && customFieldValuesByItems.size() > 0) {
			for (ProductsModel product : products) {
				product.setCustomFieldVal(customFieldValuesByItems.get(product.getItemId()));
			}
		}
		return products;
	}

	public static List<String> getItemIds(List<ProductsModel> products) {
		List<String> itemIds = new ArrayList<>();
		for (ProductsModel product : products) {
			itemIds.add(String.valueOf(product.getItemId()));
		}
		return itemIds;
	}

	public static JsonObject extractLatAndLang(JsonObject location) {
		JsonObject latAndLang = null;
		try {
			JsonArray results = location.getAsJsonArray("results");
			for (JsonElement obj : results) {
				if (obj.getAsJsonObject() != null) {
					if (obj.getAsJsonObject().getAsJsonObject("geometry") != null) {
						latAndLang = obj.getAsJsonObject().getAsJsonObject("geometry").getAsJsonObject("location");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return latAndLang;
	}

	public static List<ProductsModel> sortBasedOnClosestLocation(List<ProductsModel> productsWithWareHouseDetails,
			String zipCode) {
		JsonObject location = CIMM2VelocityTool.geoLocationByZipCode(zipCode);
		if (location != null && location.get("status").toString().replace("\"", "").equals("OK")) {
			JsonObject extractLatAndLang = extractLatAndLang(location);
			if (extractLatAndLang != null) {
				double lat1 = Double.parseDouble(extractLatAndLang.get("lat").toString());
				double lng1 = Double.parseDouble(extractLatAndLang.get("lng").toString());

				for (ProductsModel wareHouse : productsWithWareHouseDetails) {
					// wareHouse.setDistance(BranchLocation.distanceCalculatorBetweenTwoPoints(lat1,
					// lng1, wareHouse.getLatitude(), wareHouse.getLongitude(), "K"));
					wareHouse.setDistance(
							calculateDistBetweenPoints(lat1, lng1, wareHouse.getLatitude(), wareHouse.getLongitude()));
				}
				Collections.sort(productsWithWareHouseDetails, new Comparator<ProductsModel>() {
					@Override
					public int compare(ProductsModel o1, ProductsModel o2) {
						return (int) (o1.getDistance() - o2.getDistance());
					}
				});
			}
		}
		return productsWithWareHouseDetails;
	}
	
	public static ArrayList<LocationModel> sortBasedOnClosestLocationwareHouse(ArrayList<LocationModel> locationList, String zipCode){
		JsonObject location = CIMM2VelocityTool.geoLocationByZipCode(zipCode);
		if(location != null && location.get("status").toString().replace("\"", "").equals("OK")) {
			JsonObject extractLatAndLang = extractLatAndLang(location);
			if(extractLatAndLang != null) {
				double lat1 = Double.parseDouble(extractLatAndLang.get("lat").toString());
				double lng1 = Double.parseDouble(extractLatAndLang.get("lng").toString());
				
				for(LocationModel wareHouse : locationList) {
					//wareHouse.setDistance(BranchLocation.distanceCalculatorBetweenTwoPoints(lat1, lng1, wareHouse.getLatitude(), wareHouse.getLongitude(), "K"));
					double wLat = Double.parseDouble(wareHouse.getLatitude());
					double wLan = Double.parseDouble(wareHouse.getLongitude());
					wareHouse.setDistance(calculateDistBetweenPoints(lat1, lng1, wLat, wLan));
					
				}
				Collections.sort(locationList, new Comparator<LocationModel>() {
					@Override
					public int compare(LocationModel o1, LocationModel o2) {
						return (int) (o1.getDistance() - o2.getDistance());
					}
				});
			}
		}
		return locationList;
	}
	
	public static String prefixWarehouseCode(String wareHouseCode) {
		int maxLen = Integer.parseInt(CommonDBQuery.getSystemParamtersList().get("WAREHOUSE_CODE_MAXLEN"));
		if (wareHouseCode.length() < maxLen) {
			for (int i = 0; i <= (maxLen - wareHouseCode.trim().length()); i++) {
				wareHouseCode = "0" + wareHouseCode;
			}
		}
		return wareHouseCode;
	}

	public static String getUrlString(String value) {
		try {
			String strRegEx = "&[^;]*;";
			String strRegExType2 = "[^A-Za-z0-9-]";
			value = validateString(value).toLowerCase().replaceAll(strRegEx, "-").replaceAll(" ", "-")
					.replaceAll(strRegExType2, "").replaceAll("---", "-").replaceAll("--", "-");
		} catch (Exception e) {
			value = "";
			e.printStackTrace();
		}
		return value;
	}

	public static WarehouseModel getWareHouseByCode(String warehouseCode) {
		if (CommonUtility.validateString(warehouseCode).length() > 0) {
			return getWarehousesAsMap(UsersDAO.getWareHouses()).get(warehouseCode);
		}
		return null;
	}

	public static boolean doesBillAddressExist(Cimm2BCentralAddress address) {
		if (CommonUtility.validateString(address.getAddressLine1()).length() <= 0
				|| CommonUtility.validateString(address.getCity()).length() <= 0
				|| CommonUtility.validateString(address.getState()).length() <= 0
				|| CommonUtility.validateString(address.getCountry()).length() <= 0) {
			return false;
		}
		return true;
	}

	@SafeVarargs
	public static List<ProductsModel> mergeLists(List<ProductsModel>... lists) {
		List<ProductsModel> finalList = new ArrayList<>();
		for (List<ProductsModel> eachList : lists) {
			finalList.addAll(eachList);
		}
		return finalList;
	}

	public static double calculateDistBetweenPoints(double lat1, double lan1, double lat2, double lan2) {
		int AVERAGE_RADIUS_OF_EARTH_KM = 6371;
		if(validateNumber(CommonDBQuery.getSystemParamtersList().get("AVERAGE_RADIUS_OF_EARTH_MILES"))!=0) {
			AVERAGE_RADIUS_OF_EARTH_KM = validateNumber(CommonDBQuery.getSystemParamtersList().get("AVERAGE_RADIUS_OF_EARTH_MILES"));
		}
		double latDistance = Math.toRadians(lat1 - lat2);
		double langDistance = Math.toRadians(lan1 - lan2);
		double sinLat = Math.sin(latDistance / 2);
		double sinLng = Math.sin(langDistance / 2);
		double a = sinLat * sinLat
				+ (Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * sinLng * sinLng);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return (AVERAGE_RADIUS_OF_EARTH_KM * c);
	}

	public static int validateInteger(Double num) {
		int intNum = 0;
		try {
			intNum = num.intValue();
		} catch (Exception e) {
			intNum = 0;
		}
		return intNum;
	}

	public static LinkedHashMap<String, Object> loadCenPOSParams(LinkedHashMap<String, Object> contentObject) {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionData = null;
		String sessionDataPOST = null;
		String customerNumber = (String) session.getAttribute("customerId"); 
		String creditCartApprovedStatus = CommonDBQuery.getSystemParamtersList().get("PAYMENT_GATEWAY_APPROVED_STATUS");
		String transactionUrl = CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_TRANSACTION_URL");
		String hostPassword = CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_HOST_PASSWORD");
		String hostKey = CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_HOST_KEY");
		String paymentGateWayResponsePage = CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS")
				+ CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_RESPONSE_PAGE");
		String paymentGateWayErrorPage = CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS")
				+ CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_ERROR_PAGE");
		String paymentGateWayCancelPage = CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS")
				+ CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_CANCEL_PAGE");
		String paymentGateWayDeviceId = CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_DEVICE_ID");
		String paymentGateWayHostID = CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_HOST_ID");
		String paymentGateWayUniqueID = CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_UNIQUE_ID");
		String paymentGateWayResponseType = CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_RESPONSE_TYPE");
		String paymentGateWayTransactionType = CommonDBQuery.getSystemParamtersList()
				.get("PAYMENT_ACCOUNT_TRANSACTION_TYPE");
		String merchantId = CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_MERCHANT_ID");
		String aesEncryptKey = CommonDBQuery.getSystemParamtersList().get("CENPOS_AES_ENCRYPT_KEY");

		if (validateString(merchantId).length() > 0 && validateString(aesEncryptKey).length() > 0) {
			CenposEncryptor cenposEncryptor = new CenposEncryptor(merchantId, aesEncryptKey);
			sessionData = cenposEncryptor.getSessionData();
			sessionDataPOST = cenposEncryptor.getSessionPostData();
		}
		Map<String, String> cenposData = new HashMap<String, String>();
		cenposData.put("aesEncryptKey", aesEncryptKey);
		cenposData.put("sessionData", sessionData);
		cenposData.put("sessionDataPOST", sessionDataPOST);
		cenposData.put("merchantId", merchantId);
		cenposData.put("creditCartApprovedStatus", creditCartApprovedStatus);
		cenposData.put("transactionUrl", transactionUrl);
		cenposData.put("hostPassword", hostPassword);
		cenposData.put("hostKey", hostKey);
		cenposData.put("paymentGateWayResponsePage", paymentGateWayResponsePage);
		cenposData.put("paymentGateWayErrorPage", paymentGateWayErrorPage);
		cenposData.put("paymentGateWayCancelPage", paymentGateWayCancelPage);
		cenposData.put("paymentGateWayDeviceId", paymentGateWayDeviceId);
		cenposData.put("paymentGateWayHostID", paymentGateWayHostID);
		cenposData.put("paymentGateWayUniqueID", paymentGateWayUniqueID);
		cenposData.put("paymentGateWayResponseType", paymentGateWayResponseType);
		cenposData.put("paymentGateWayTransactionType", paymentGateWayTransactionType);
		cenposData.put("customerId",CommonUtility.validateString(customerNumber));
		contentObject.put("cenposAttrubutes", new Gson().toJson(cenposData));
		return contentObject;
	}

	public static boolean validateHttpUrlResponse(String URLName) {
		try {
			//HttpURLConnection.setFollowRedirects(false);
			// note : you may also need
			// HttpURLConnection.setInstanceFollowRedirects(false)
			
			HttpsURLConnection con =null;
			TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws CertificateException {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws CertificateException {
					// TODO Auto-generated method stub
					
				}
			}
		};

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		
			//String userTokenApi = CommonDBQuery.getProps().getProperty("unilog.innovo.api.usertokens");
			URL url1 =  new URL( URLName);
			
			 con = (HttpsURLConnection)url1.openConnection();
			 con.setRequestMethod("HEAD");
			con.setHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
			
			
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Date convertToDateFromString(String dateString) throws java.text.ParseException {
		SimpleDateFormat outSDF = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		String outDate;
		SimpleDateFormat formatter6 = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = formatter6.parse(dateString);
		outDate = outSDF.format(date1);
		Date date2 = outSDF.parse(outDate);
		return date2;

	}
	public static String convertToStringFromDate(Date date) throws ParseException  {
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String formatedDate = (cal.get(Calendar.MONTH) + 1)  + "/" + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
	
		
         String strDate = formatedDate; 
		return strDate;

	}

	public static LinkedHashMap<String, Object> readStaticData(String staticIdPath) {
		LinkedHashMap<String, Object> staticData = new LinkedHashMap<String, Object>();
		try {
			URL url = new URL(staticIdPath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(url.openStream());
			StaticPageModel staticPage = printDocument(doc, System.out);
			staticData.put("pageName", staticPage.getPageName());
			staticData.put("pageTitle", staticPage.getPageTitle());
			staticData.put("pageContent", staticPage.getPageContent());
			staticData.put("metaKeywords", staticPage.getMetaKeywords());
			staticData.put("metaDesc", staticPage.getMetaDesc());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return staticData;
	}

	public static StaticPageModel printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
		StaticPageModel staticPageData = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(StaticPageModel.class);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(out, "UTF-8")));
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			staticPageData = (StaticPageModel) jaxbUnmarshaller.unmarshal(doc);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return staticPageData;
	}
	
	public static String getRequestHandler(RequestHandlers requestType) {
		String requestHandler = "/mainitem_keywordsearch";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try {
			String localeCode="EN";
			if(session!=null && session.getAttribute("localeCode")!=null) {
				localeCode= session.getAttribute("localeCode").toString().toUpperCase();
			}
			switch (requestType) {
			case ATTRIBUTE:
				requestHandler =  LayoutLoader.getMessageProperties().get(localeCode).getProperty("custom.attribute.requesthandler")!=null?LayoutLoader.getMessageProperties().get(localeCode).getProperty("custom.attribute.requesthandler"):"/mainitem_keywordsearch";
				break;
			case CUSTOM:
				requestHandler =  LayoutLoader.getMessageProperties().get(localeCode).getProperty("custom.requesthandler")!=null?LayoutLoader.getMessageProperties().get(localeCode).getProperty("custom.requesthandler"):"/mainitem_keywordsearch";
				break;
			default:
				requestHandler = "/mainitem_keywordsearch";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestHandler;
	}
	
	public static String httpdomain(String url){
		  String domain = null;
	      try{
	       //String url = "http://testabc.com/ip/asdf";
	      Pattern pattern = Pattern.compile("(https?://)([^:^/]*)(:\\d*)?(.*)?");
	      Matcher matcher = pattern.matcher(url);

	      matcher.find();

	      String protocol = matcher.group(1);            
	      domain   = matcher.group(2);
	      String port     = matcher.group(3);
	      String uri      = matcher.group(4);
	      System.out.println(protocol + " - " + domain + " - " + port  + " - " + uri);
	      }catch (Exception e) {
	    e.printStackTrace();// TODO: handle exception
	   }
	      return domain;
	}
	
	public static String escapeHtml(String value) {
		return  StringEscapeUtils.escapeHtml4(value);
	}
	
	public static String[] setArrayValueFields(String[] values ) {
		XSSRequestWrappers xssFilter = new XSSRequestWrappers(request);
		String returnValue[] = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			returnValue[i]=xssFilter.stripXSS(values[i]);
		}
		return returnValue;
	}

	public static boolean escapeFields(String fieldName) {
		String localeCode = "EN";
		String params = LayoutLoader.getMessageProperties().get(localeCode).getProperty("form.label.escapeFields");
		if (validateString(params).length() > 0) {
			String[] field = params.split(",");
			if (Arrays.toString(field).contains(fieldName)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean validateUploadFileExtn(String fileName,String ExtnPattern){
		Pattern fileExtnPtrn = Pattern.compile(ExtnPattern);
	    Matcher mtch = fileExtnPtrn.matcher(fileName);
		if(mtch.matches()){
			 return true;
		}
	 return false;
	}
	
	public static String getSolrSiteUrl(String solrUrlString) {
		String strUrl = solrUrlString;
		String array[] = strUrl.split("/");
		System.out.println("CoreName : "+array[array.length-1]);
		String siteNameSolr = validateString(CommonDBQuery.getJiraKey());
		int index=strUrl.lastIndexOf('/'); 
		System.out.println("SolrUrl : "+strUrl.substring(0,index));
		if(!siteNameSolr.equalsIgnoreCase(""))
		{
			solrUrlString = strUrl.substring(0,index)+"/"+siteNameSolr+"_"+array[array.length-1];
		}
		System.out.println("SolrUrl : "+solrUrlString);		
		return solrUrlString;
	}

}