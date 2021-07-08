package org.ConnectionUtility;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;

import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;
import com.unilog.velocitytool.contextkeyinterface.SocialMediaContextkey;

public class Cimm2BCentralConnection {
	
	private final String USER_AGENT = "Mozilla/5.0";
	private final String CONTENT_TYPE = "Application/json";
	
	public HttpURLConnection connectCim2BCentralByGetMethod(LinkedHashMap<String, String> contentObject){
		HttpURLConnection conn = null;
		try{
			URL url =  new URL(CommonUtility.validateString(contentObject.get(SocialMediaContextkey.CIMM2BCENTRAL_FINAL_REQUEST_URL)));
			conn  = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Authorization",CommonDBQuery.getCimm2bCentralAuthorization());
			if(CommonUtility.validateString(contentObject.get(SocialMediaContextkey.CIMM2BCENTRAL_CLIENT_ID)).length()>0){
			conn.setRequestProperty("Clientid", CommonUtility.validateString(contentObject.get(SocialMediaContextkey.CIMM2BCENTRAL_CLIENT_ID)));
			}
			conn.setRequestProperty("SiteId", CommonUtility.validateString(contentObject.get(SocialMediaContextkey.SITE_ID)));
			conn.setRequestProperty("Content-type", CONTENT_TYPE);
			conn.setRequestProperty("User-Agent", USER_AGENT);
			conn.setRequestMethod("GET");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/*public HttpURLConnection connectCim2BCentralByGetMethodMain(LinkedHashMap<String, Object> contentObject){
		HttpURLConnection conn = null;
		try {
			URL url =  new URL("https://mcmcbeta.cimm2.com/cimm2bcentral/social/facebook/getPosts?noOfPosts=5");
			conn  = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Authorization","Basic "+Base64Encoder.encode ("cimm2bclient:@c!mm@bcl!ent"));
			//conn.setRequestProperty("Authorization",CommonDBQuery.getCimm2bCentralAuthorization());
			conn.setRequestProperty("Clientid", "1");
			conn.setRequestProperty("SiteId", "21");
			conn.setRequestProperty("Content-type", CONTENT_TYPE);
			conn.setRequestProperty("User-Agent", USER_AGENT);
			conn.setRequestMethod("GET");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[] args) {
		LinkedHashMap<String, Object> contentObject = null;
		try {
			Cimm2BCentralConnection cimm2BCentralConnection = new Cimm2BCentralConnection();
			HttpURLConnection conn = cimm2BCentralConnection.connectCim2BCentralByGetMethodMain(contentObject);
			//int responseCode = conn.getResponseCode();
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			String outputTxt = "";
			while ((inputLine = in.readLine()) != null) {
				outputTxt = outputTxt + inputLine;
			}
			Gson gson = new Gson();
			ResponseFaceBook output = new ResponseFaceBook();
			output = gson.fromJson(outputTxt, ResponseFaceBook.class);
			System.out.println("output : "+output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
