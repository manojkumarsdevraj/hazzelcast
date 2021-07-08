package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AuthenticateRQ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AuthenticateRQ() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml");
		PrintStream out = new PrintStream(response.getOutputStream());

		String orderXmlString = getXML();
		System.out.println("orderXmlString---------> \n\n"+orderXmlString);
		
		
		String strURL = "https://stagegw.transnox.com/servlets/TransNox_API_Server";
		//strURL = "https://gateway.transit-pass.com/servlets/TransNox_API_Server";
		System.out.println("My Servlet: "+ strURL);
		
		 URLConnection gatewayConn = new URL(strURL).openConnection();
         gatewayConn.setDoOutput(true);
			OutputStream os = gatewayConn.getOutputStream();
			os.write(orderXmlString.toString().getBytes());
								
			BufferedReader in = new BufferedReader(new InputStreamReader(gatewayConn.getInputStream()));
			String line = null;
			
			StringBuffer responseData = new StringBuffer();
			while((line = in.readLine()) != null) {
			
				responseData.append(line);
			}
			
		System.out.println("responseData : "+responseData.toString());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public static String getXML() {
		String XMLString = "";
		
		try {
			
			String merchantId="888000001653";
		      String hostPassword="HRWEZKO0TETH";
		      String amount="";
		      String externalRefNumber="";
		      String encryptionKey="O6YN*NC6XUGHYvB6";
	        

			String encryptedManifest = com.tsys.THPCrypt.encryptManifest(merchantId, hostPassword, amount, externalRefNumber, encryptionKey);
			
			
		XMLString = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" +
				"<InfoNox_Interface>" +
				"<TransNox_API_Interface>"+
				"<AuthenticationRQ>" +
				"<Device_Info><Merchant_ID>888000001653</Merchant_ID><Device_ID>88800000165301</Device_ID>" +
				"<Product_Code>IFX_CPASS</Product_Code><Unique_ID>localhost</Unique_ID><Unique_Key>"+encryptedManifest+"</Unique_Key>" +
				"</Device_Info><Host_Info><Host_ID>O6YN*NC6XUGHYvB6</Host_ID><Host_Password>HRWEZKO0TETH</Host_Password></Host_Info></AuthenticationRQ>" +
				"</TransNox_API_Interface></InfoNox_Interface>";
		
        }catch (Exception e) {
        	 e.printStackTrace();
        }
        return XMLString;
	}
}
