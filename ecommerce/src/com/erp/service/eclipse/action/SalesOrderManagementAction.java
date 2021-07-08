package com.erp.service.eclipse.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.erp.eclipse.parser.ElementParser;
import com.unilog.database.CommonDBQuery;
import com.unilog.sales.SalesModel;
import com.unilog.security.SecureData;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;

public class SalesOrderManagementAction {

	public static UsersModel elementSetup(String userName, String sessionId, String cardHolder, String address1,
			String zipCode, String responseUrl) {

		UsersModel wsLogin = new UsersModel();
		String byUser = "No";
		String bySession = "No";

		String eclipseLogin = CommonDBQuery.getSystemParamtersList().get("ECLIPSELOGIN");
		String eclipseURL = CommonDBQuery.getSystemParamtersList().get("ERPCONNECTIONURL");
		String eclipseXMLPath = CommonDBQuery.getSystemParamtersList().get("ERPTEMPLATEPATH");

		if (eclipseLogin != null && eclipseLogin.trim().equalsIgnoreCase("ByUser")) {
			SecureData validUserPass = new SecureData();
			HashMap<String, String> userDetails = UsersDAO.getUserPasswordAndUserId(userName, "Y");
			String password = userDetails.get("password");
			wsLogin.setLoginID(userName);
			wsLogin.setLoginPassword(validUserPass.validatePassword(password));
			byUser = "Yes";
		} else {
			wsLogin.setSessionId(sessionId);
			bySession = "Yes";
		}

		SalesModel wsElementAccountSetup = new SalesModel();
		wsElementAccountSetup.setCardholder(cardHolder);
		wsElementAccountSetup.setPostalCode(zipCode);
		wsElementAccountSetup.setReturnUrl(responseUrl);
		wsElementAccountSetup.setStreetAddress(address1);
		UsersModel userDetailList = null;
		HttpURLConnection eclipseConn = null;
		try {

			VelocityEngine velocityTemplateEngine = new VelocityEngine();
			velocityTemplateEngine.setProperty("file.resource.loader.path", eclipseXMLPath);
			velocityTemplateEngine.init();
			Template t = velocityTemplateEngine.getTemplate("ElementAccountSetup.xml");
			VelocityContext context = new VelocityContext();
			context.put("cardholder", wsElementAccountSetup.getCardholder());
			context.put("postalCode", wsElementAccountSetup.getPostalCode());
			context.put("returnUrl", wsElementAccountSetup.getReturnUrl());
			context.put("streetAddress", wsElementAccountSetup.getStreetAddress());
			context.put("loginId", wsLogin.getLoginID());
			context.put("loginPassword", wsLogin.getLoginPassword());
			context.put("sessionId", wsLogin.getSessionId());
			context.put("byUser", byUser);
			context.put("bySession", bySession);

			/* now render the template into a StringWriter */
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			/* show the World */
			StringBuffer finalMessage = new StringBuffer();
			finalMessage.append(writer.toString());
			System.out.println(finalMessage.toString());
			eclipseConn = (HttpURLConnection) new URL(eclipseURL).openConnection();

			eclipseConn.setDoOutput(true);
			OutputStream os = eclipseConn.getOutputStream();
			os.write(finalMessage.toString().getBytes());
			// System.out.println(xmlRequest.toString());
			// InputStream xmlResponse = eclipseConn.getInputStream();

			BufferedReader in = new BufferedReader(new InputStreamReader(eclipseConn.getInputStream()));
			String line = null;

			StringBuffer responseData = new StringBuffer();

			while ((line = in.readLine()) != null) {

				responseData.append(line);
			}

			long lStartTime = new Date().getTime(); // start time
			System.out.println("Output : " + responseData.toString());

			@SuppressWarnings("deprecation")
			ElementParser elementParser = new ElementParser(new StringBufferInputStream(responseData.toString()));
			elementParser.getUserDetail();
			userDetailList = new UsersModel();
			userDetailList = elementParser.getUserDetail();
			System.out.println("element setup url :  " + userDetailList.getElementSetupUrl());
			System.out.println("element setup ID :  " + userDetailList.getElementSetupId());
			long lEndTime = new Date().getTime(); // end time

			long difference = lEndTime - lStartTime; // check difference

			System.out.println("Elapsed milliseconds: " + difference);
			os.close();
			in.close();

		} catch (ResourceNotFoundException ex) {
			ex.printStackTrace();

		} catch (ParseErrorException ex) {
			ex.printStackTrace();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (eclipseConn != null) {
				eclipseConn.disconnect();
			}
			;
		}
		return userDetailList;
	}
}
