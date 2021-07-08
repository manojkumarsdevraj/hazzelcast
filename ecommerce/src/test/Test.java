package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.google.gson.Gson;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.datasmart.DatasmartConstantVariables;
import com.unilog.defaults.ULLog;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.utility.CommonUtility;

public class Test {

	private static byte[] linebreak = {}; // Remove Base64 encoder default linebreak
	private static String secret = "tvnw63ufg9gh5392"; // secret key length must be 16
	private static SecretKey key;
	private static Cipher cipher;
	private static Base64 coder;

	static {
		try {
			key = new SecretKeySpec(secret.getBytes(), "AES");
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
			coder = new Base64(32, linebreak, true);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// ExchangeRate("USD","CAD");
		// ProductQuery();
		// QueryCustomer();
		// test();
		// QueryCustomerPart();
		// CreateCustomerPart();
		// DeleteCustomerPart();
		/* Comment added */
		// splitNumbers();
		passwordEncriptAndDecript();
		// generateRandomNumber();
		/*
		 * String string = "004|034556"; String[] parts = string.split("|"); String
		 * part1 = parts[0]; // 004 String part2 = parts[1]; // 034556
		 * System.out.println(part1+" : "+part2);
		 */
		// getWeekDays();
	}

	public static void splitNumbers() {
		String creditCardValue = "0102";
		String year = creditCardValue.substring(0, 2);
		String month = creditCardValue.substring(2);
		System.out.println(year + ":" + month);
		HttpURLConnection eclipseConn = null;
		try {
			System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
			VelocityEngine velocityTemplateEngine = new VelocityEngine();
			velocityTemplateEngine.setProperty("file.resource.loader.path", "D:\\PunchoutRequest\\");
			velocityTemplateEngine.init();

			Template t = velocityTemplateEngine.getTemplate("punchout.xml");
			/* create a context and add data */
			VelocityContext context = new VelocityContext();

			/* now render the template into a StringWriter */
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			/* show the World */
			StringBuffer finalMessage = new StringBuffer();
			finalMessage.append(writer.toString());
			System.out.println(finalMessage.toString());

			eclipseConn = (HttpURLConnection) new URL("https://973ecb.cimm2.com/PunchoutRequest.slt")
					.openConnection();

			eclipseConn.setDoOutput(true);
			OutputStream os = eclipseConn.getOutputStream();
			os.write(finalMessage.toString().getBytes());

			BufferedReader in = new BufferedReader(new InputStreamReader(eclipseConn.getInputStream()));
			String line = null;

			StringBuffer responseData = new StringBuffer();

			while ((line = in.readLine()) != null) {

				responseData.append(line);
			}
			System.out.println("responseData.toString() : " + responseData.toString());

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void getWeekDays() {
		DateFormat df = new SimpleDateFormat("EEE dd/MM/yyyy");
		int leadTime = 6;
		Calendar c = Calendar.getInstance();
		System.out.println("Initial Date:" + df.format(c.getTime()));
		if (leadTime > 0) {
			c.add(Calendar.DATE, leadTime);
		}
		// c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		System.out.println("After Lead Time Date:" + df.format(c.getTime()));
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int hourofDay = c.get(Calendar.HOUR_OF_DAY);
		if (hourofDay > 15 && dayOfWeek == Calendar.FRIDAY) {
			c.add(Calendar.DATE, 3);
		} else if (dayOfWeek == Calendar.SATURDAY) {
			c.add(Calendar.DATE, 2);
		} else if (dayOfWeek == Calendar.SUNDAY) {
			c.add(Calendar.DATE, 1);
		}
		System.out.println("After Weekend Date:" + df.format(c.getTime()));

	}

	public static void generateRandomNumber() {
		int aNumber = 0;
		aNumber = (int) ((Math.random() * 9000000) + 1000000);
		System.out.println(aNumber);
	}

	public static void passwordEncriptAndDecript() {
		String encript = "smtp.sendgrid.net";
		String Decript = "q0wWAzqidQk6yfeSO_6OgA";
		Decript = validatePassword(Decript);
		encript = securePassword(encript.trim());
		System.out.println("Encript : " + encript);
		System.out.println("DeCript : " + Decript);

		String urlString = "DIXON&reg; BY TICONDEROGA&reg; 88811";
		String strRegEx = "&[^;]*;";
		String strRegEx2 = "[^A-Za-z0-9-]";
		System.out.println("String : " + urlString.toLowerCase().replaceAll(strRegEx, "-").replaceAll(" ", "-")
				.replaceAll(strRegEx2, "").replaceAll("---", "-").replaceAll("--", "-"));

		/*
		 * String fieldName = "newPWd1BLABEL"; String fieldValue = "Blaaa Bllla Blaaa";
		 * if(CommonUtility.validateString(fieldName).toLowerCase().contains("password")
		 * || CommonUtility.validateString(fieldName).toLowerCase().contains("pwd")) {
		 * fieldValue = "********"; }
		 * System.out.println("fieldName : "+fieldName.indexOf("n"));
		 * System.out.println("fieldValue : "+fieldValue);
		 */
	}

	public static String ExchangeRate(String from, String to) {
		URL url;
		try {
			url = new URL("http://rate-exchange.appspot.com/currency?from=" + from + "&to=" + to + "&format=json");

			URLConnection yc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String jsonObject = "";
			String line;
			while ((line = in.readLine()) != null) {
				jsonObject += line;
			}
			in.close();
			System.out.println(jsonObject);
			Gson gson = new Gson();
			ProductsModel data = gson.fromJson(jsonObject, ProductsModel.class);
			System.out.println(" : " + data.getRate());
			DecimalFormat df = null;// CommonUtility.getPricePrecision();
			System.out.println(df.format(1.229));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void test() {
		Gson gson = new Gson();
		String keyWord = "wire OR 12334 asdf OR 12345";
		String validateForSearch[] = keyWord.split("\\s+", -1);
		Pattern p = Pattern.compile("[^a-zA-Z0-9]");
		Pattern p1 = Pattern.compile("[\\d]");
		boolean includePartNumberSearch = false;
		String singleKeyword = "";
		String multiKeyword = "";
		String multiKeywordOr = "";
		String multiKeywordWildCard = "";
		String pattern = "[^A-Za-z0-9]";

		includePartNumberSearch = true;
		if (validateForSearch != null && validateForSearch.length > 1) {
			String validateOr[] = keyWord.split(" OR ", -1);
			if (validateOr != null && validateOr.length > 1) {
				String s = "";
				for (String sKey : validateOr) {

					String validateWordLength[] = sKey.split("\\s+", -1);
					if (validateWordLength != null && validateWordLength.length > 1) {
						singleKeyword = singleKeyword + s + sKey;
					} else {
						singleKeyword = singleKeyword + s + "partnumbersearch:*" + sKey + "* OR " + sKey;
					}

					s = " OR ";
				}
			} else {
				singleKeyword = keyWord;
			}

		}
		System.out.println(singleKeyword);
	}

	private static synchronized String encrypt(String plainText) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] cipherText = cipher.doFinal(plainText.getBytes());
		return new String(coder.encode(cipherText));
	}

	private static synchronized String decrypt(String codedText) throws Exception {
		byte[] encypted = coder.decode(codedText.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decrypted = cipher.doFinal(encypted);
		return new String(decrypted);
	}

	protected static String securePassword(String userPassword) {
		try {
			return encrypt(userPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String validatePassword(String userPassword) {

		try {
			return decrypt(userPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String generateSiteMapDatasmart() {
		Connection conn = null;
		PreparedStatement preStat = null;
		ResultSet rs = null;
		try {
			conn = ConnectionManager.getDBConnection();
			String sql = "select * from ARI_BRAND_MODEL";
			preStat = conn.prepareStatement(sql);
			rs = preStat.executeQuery();
			while (rs.next()) {
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put("ARIUrl", CommonDBQuery.getSystemParamtersList().get("ARI_SERVICE_URL"));
				contentObject.put(DatasmartConstantVariables.SUBSET, 412);
				contentObject.put(DatasmartConstantVariables.GENERAL_SUBSET, 0);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBPreparedStatement(preStat);
			ConnectionManager.closeDBConnection(conn);
		}

		return null;
	}

}
