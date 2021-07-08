package com.unilog.singlesignon;
/* SSO oAuth  */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.unilog.utility.CommonUtility;


/**
 * Servlet implementation class OAuth2Client
 */
public class OAuth2Client extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OAuth2Client() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String caller = request.getParameter(OAuthConstants.CALLER);
		String code = request.getParameter(OAuthConstants.CODE);
		
		//Load the properties file
		Properties config = OAuthUtils.getClientConfigProps(OAuthConstants.CONFIG_FILE_PATH);
				
		//Generate the OAuthDetails bean from the config properties file
		OAuth2Details oauthDetails = OAuthUtils.createOAuthDetails(config);
				
		//Validate Input
		List<String> invalidProps = OAuthUtils.validateInput(oauthDetails);
		if(!(CommonUtility.validateString(code).length()>0) && CommonUtility.customServiceUtility()!=null) {
			CommonUtility.customServiceUtility().connectToSso(response,oauthDetails);
		}
		if(invalidProps!=null && invalidProps.size() == 0){
			//Validation successful
			System.out.println("Entered to SSO");
			if(OAuthUtils.isValid(caller)){
				//Request was sent from web application. 
				//Check type of request
				if(caller.equalsIgnoreCase(OAuthConstants.TOKEN)){
					//Request for Access token
					oauthDetails.setAccessTokenRequest(true);
					String location = OAuthUtils.getAuthorizationCode(oauthDetails);
					location =location.replace("<!-- Check if this is the PingOne Mobile App -->", "");
					StringBuilder redirectUri = new StringBuilder();
					//Send redirect to location returned by endpoint
					//response.sendRedirect(location);
					response.setContentType("text/html");
					PrintWriter output = response.getWriter(); 
					//output.write(location);
					redirectUri.append(oauthDetails.getAuthenticationServerUrl()).append("?client_id=").append(oauthDetails.getClientId()).append("&client_password=").append(oauthDetails.getClientSecret()).append("&response_type=code&redirect_uri=").append(oauthDetails.getRedirectURI());
					System.out.println("SSO Uri : " + redirectUri.toString());
					response.sendRedirect(redirectUri.toString());
					return;
					
				}else{
					//Request for accessing protected resource
					if(!OAuthUtils.isValid(oauthDetails.getResourceServerUrl())){
						invalidProps.add(OAuthConstants.RESOURCE_SERVER_URL);
					}
					
					if(!OAuthUtils.isValid(oauthDetails.getAccessToken())){
						if(!OAuthUtils.isValid(oauthDetails.getRefreshToken())){
							invalidProps.add(OAuthConstants.REFRESH_TOKEN);
						}
					}
					
					if(invalidProps.size() > 0){
						sendError(response, invalidProps);
						return;
					}
					
					Map<String,String> map = OAuthUtils.getProtectedResource(oauthDetails);
					response.getWriter().println(new Gson().toJson(map));
					return;
				}
				
			}else if(OAuthUtils.isValid(code)){
			/*	Gson gson = new Gson();*/
				//Callback from endpoint with code.
				Map<String, String> map = null;
				map = OAuthUtils.getAccessToken(oauthDetails, code);
				Map<String,String> map1 = OAuthUtils.getUserInfo(oauthDetails, map.get("access_token"));
				
				if(!map1.isEmpty() && map1.size()>0 && CommonUtility.validateString(map1.get("guid")).length()>0) {
					System.out.println("Guid Received from Remote Host: "+ map1.get("guid"));
					request.setAttribute("externalSystemId", CommonUtility.validateString(map1.get("guid")));  
					request.setAttribute("ssoUserDetail", map1);
					request.getRequestDispatcher("/doLogin.action").forward(request, response); 
				}else {
					String queryString = request.getQueryString();
					String error = "Invalid request";
					if(OAuthUtils.isValid(queryString)){
						error = queryString;
					}
					response.getWriter().println(error);
				}
				/*Object obj = map1.get("access_token");
				System.out.println(obj.getClass());
				JSONObject jObj =(JSONObject) obj;
				System.out.println(jObj.get("SESAID"));*/
				/*System.out.println(map1.get("access_token"));
				 Type token = new TypeToken<LinkedHashMap<String, String>>(){}.getType();
				 LinkedHashMap<String, String> userDetail = gson.fromJson(map1.get("access_token"), token);
				 System.out.println(userDetail.get("SESAID"));*/
				//System.out.println(map1.get("access_token"));
				//response.getWriter().println(new Gson().toJson(map1));
				/*FacesContext fcForAccess = FacesUtil.getFacesContext(request, response);
				LoginBean lb = new LoginBean();
				lb.setExternalSystem2UserId((String)jObj.get("SESAID"));
				String loginStatus = lb.login();
				if(loginStatus!=null){
					response.sendRedirect(request.getContextPath()+"/cimm/templates/dashboardMain.jsf");
				}else{
					response.sendRedirect(request.getContextPath()+"/cimm/index.jsf");
				}*/
				return;
			
			}else{
				System.out.println("---Found some error in oAuth---");
				//Invalid request/error response
				String queryString = request.getQueryString();
				String error = "Invalid request";
				if(OAuthUtils.isValid(queryString)){
					error = queryString;
				}
				response.getWriter().println(error);
				return;
			}
		
		}else{
			sendError(response, invalidProps);
			System.out.println("SSO error");
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void sendError(HttpServletResponse response, List<String> invalidProps) throws IOException{
		PrintWriter pw = response.getWriter();
			pw.println("<HTML>");
			pw.println("<HEAD>");
			pw.println("<H1>");
			pw.println("Invalid Input in config file Oauth2Client.config");
			pw.println("</H1>");
			pw.println("</HEAD>");
			pw.println("<BODY>");
			pw.println("<BODY>");
			pw.println("<P>");
			pw.println("Please provide valid values for the following properties");
			pw.println(new Gson().toJson(invalidProps));
			pw.println("</P>");
			pw.println("</HTML>");
		pw.flush();
		pw.close();
		
		return;
	}
}