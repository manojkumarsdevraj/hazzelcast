package test;
import test.TsysModel;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class Tsys {

	private static int randomnum() {
		Random generator = new Random();
		return 10000000 + generator.nextInt(99999999);
	}
	
	private static int invoicenum() {
		Random generator = new Random();
		return 1000000 + generator.nextInt(9999999);
	}
	public static void main(String[] args) throws Exception {
			String encryptionKey = "yV0YCARQO25J-AZ1";
			String merchantId = "888000001653";
			String hostPassword = "HRWEZKO0TETH";
			String amount = "";
			
			Integer externalRefNumInt = randomnum();
		    Integer invoiceInt = invoicenum();
		    String invoice = invoiceInt.toString();
		    String externalRefNumber = externalRefNumInt.toString();
		    externalRefNumber ="";
			
			String encryptedManifest = com.tsys.THPCrypt.encryptManifest(merchantId, hostPassword, amount, externalRefNumber, encryptionKey);
			//System.out.println("Encrypted Manifest : " + encryptedManifest);
			//System.out.println("Decrypted Manifest : " + com.tsys.THPCrypt.decryptText(encryptedManifest, encryptionKey));
			authXmlReq(encryptedManifest);

}
	
	public static String authXmlReq(String manifest){
		
		 HttpURLConnection tsysConnection = null;
		try {
		
		String merchantId = "888000001653";//888000001653 
		String deviceId = "88800000165301";//88800000165301 
		String productCode = "IFX_CPASS";//IFX_CPASS
		String uniqueId = "Unilog";//Unilog
		String uniqueKey = "yV0YCARQO25J-AZ1";//yV0YCARQO25J-AZ1
		String hostId = "88800000165301H";//88800000165301H
		String hostPassword = "HRWEZKO0TETH";
		
		String url = "https://counterpass.transitpass.com/hpm/launchTransnoxScreen.hpm";
		//url = "https://stagegw.transnox.com/servlets/TransNox_API_Server";
		url="https://stagegw.transnox.com/servlets/TransNox_API_Server";
		
		VelocityEngine velocityTemplateEngine = new VelocityEngine();
 	    velocityTemplateEngine.setProperty("file.resource.loader.path", "D:\\forChml\\");
 	    velocityTemplateEngine.init();

 	    	Template t = velocityTemplateEngine.getTemplate("TsysAuthenticationRQ.xml");
             VelocityContext context = new VelocityContext();
             
             context.put("merchantId", merchantId);
             context.put("deviceId", deviceId);
             context.put("productCode", productCode);
             context.put("uniqueId", uniqueId);
             context.put("uniqueKey", uniqueKey);
             context.put("hostId", hostId);
             context.put("hostPassword", hostPassword);
             context.put("manifest", manifest);

          
             /* now render the template into a StringWriter */
             StringWriter writer = new StringWriter();
             t.merge(context, writer);
             /* show the World */
             StringBuffer finalMessage= new StringBuffer();
             finalMessage.append(writer.toString());
             System.out.println(finalMessage.toString());
             
            tsysConnection = (HttpURLConnection) new URL(url).openConnection();
            tsysConnection.setRequestProperty("Accept","*/*");
            tsysConnection.setRequestProperty("Content-Type", "text/xml");
            tsysConnection.setDoOutput(true);
 			OutputStream os = tsysConnection.getOutputStream();
 			os.write(finalMessage.toString().getBytes());
 			
 			BufferedReader in = new BufferedReader(new InputStreamReader(tsysConnection.getInputStream()));
 			String line = null;
 		
 			StringBuffer responseData = new StringBuffer();
 			
 			while((line = in.readLine()) != null) {
 				responseData.append(line);
 			}
 			System.out.println("Response \n :"+responseData.toString());
 			
 			TsysSaxParser xmlFileSAX	=new TsysSaxParser(new StringBufferInputStream(responseData.toString()));
			String statusResult = xmlFileSAX.value;
			System.out.println("statusResult : "+statusResult);
			TsysModel tsysModel = xmlFileSAX.getTsysModel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
