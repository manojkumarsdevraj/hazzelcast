package com.unilog.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet implementation class VcfGenerate
 */
public class VcfGenerate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VcfGenerate() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String name= "";
		String firstName= "";
		String lastName= "";
		String organization = "";
		String jobTitle = "";
		String workTelPhone = "";
		String homeTelPhone = "";
		String workAddress = "";
		String city = "";
		String state = "";
		String zip = "";
		String country = "";
		String homeAddress = "";
		String email = "";
		String workUrl = "";
		String faxNumber = "";
		String infoNoteAfterHoursNumber = "";
		
		try {
			
			if(request.getParameter("infoNames")!=null && request.getParameter("infoNames").trim().length()>0){
				name = request.getParameter("infoNames");
			}
			if(request.getParameter("infoFirstNames")!=null && request.getParameter("infoFirstNames").trim().length()>0){
				firstName = request.getParameter("infoFirstNames");
			}
			if(request.getParameter("infoLastNames")!=null && request.getParameter("infoLastNames").trim().length()>0){
				lastName = request.getParameter("infoLastNames");
			}
			if(request.getParameter("inforOrganization")!=null && request.getParameter("inforOrganization").trim().length()>0){
				organization = request.getParameter("inforOrganization");
			}
			if(request.getParameter("infoJobTitle")!=null && request.getParameter("infoJobTitle").trim().length()>0){
				jobTitle = request.getParameter("infoJobTitle");
			}
			if(request.getParameter("infoPhone")!=null && request.getParameter("infoPhone").trim().length()>0){
				workTelPhone = request.getParameter("infoPhone");
			}
			if(request.getParameter("infoFax")!=null && request.getParameter("infoFax").trim().length()>0){
				faxNumber = request.getParameter("infoFax");
			}
			if(request.getParameter("infoNote")!=null && request.getParameter("infoNote").trim().length()>0){
				infoNoteAfterHoursNumber = request.getParameter("infoNote");
			}
			if(request.getParameter("infoHomePhone")!=null && request.getParameter("infoHomePhone").trim().length()>0){
				homeTelPhone = request.getParameter("infoHomePhone");
			}
			if(request.getParameter("infoStreet")!=null && request.getParameter("infoStreet").trim().length()>0){
				workAddress = request.getParameter("infoStreet");
			}
			if(request.getParameter("infoHomeStreet")!=null && request.getParameter("infoHomeStreet").trim().length()>0){
				homeAddress = request.getParameter("infoHomeStreet");
			}
			if(request.getParameter("infoEmail")!=null && request.getParameter("infoEmail").trim().length()>0){
				email = request.getParameter("infoEmail");
			}
			if(request.getParameter("inforWorkUrl")!=null && request.getParameter("inforWorkUrl").trim().length()>0){
				workUrl = request.getParameter("inforWorkUrl");
			}
			if(request.getParameter("infoCity")!=null && request.getParameter("infoCity").trim().length()>0){
				city = request.getParameter("infoCity");
			}
			if(request.getParameter("infoState")!=null && request.getParameter("infoState").trim().length()>0){
				state = request.getParameter("infoState");
			}
			if(request.getParameter("infoZip")!=null && request.getParameter("infoZip").trim().length()>0){
				zip = request.getParameter("infoZip");
			}
			if(request.getParameter("infoCountry")!=null && request.getParameter("infoCountry").trim().length()>0){
				country = request.getParameter("infoCountry");
			}

			String str="BEGIN:VCARD\n" +
	        "VERSION:4.0\n" +
	        "N:"+lastName+";"+firstName+";;;\n" + //Name
	        "FN:"+name+"\n"+ //Full Name
	        "ORG:"+organization+"\n"+	//Organisation
	        "TITLE:"+name+"\n"+ //Job Title
	       /* "TEL;TYPE=work,voice;VALUE=uri:tel:"+workTelPhone+"\n"+ //Work Tel. No:
	        "TEL;TYPE=home,voice;VALUE=uri:tel:"+homeTelPhone+"\n"+ //HOme Tel. No:
	        "TEL;TYPE=afterhour,voice;VALUE=uri:tel:"+homeTelPhone+"\n"+ //HOme Tel. No:
	       */	        
	        "TEL;TYPE=work,voice;VALUE=uri:"+workTelPhone+"\n"+ //Work Tel. No:
			"TEL;TYPE=home,voice;VALUE=uri:"+homeTelPhone+"\n"+ //HOme Tel. No:
			"TEL;TYPE=fax,voice;VALUE=uri:"+faxNumber+"\n"+ //Fax Tel. No:
			"TEL;TYPE=afterhour,voice;VALUE=uri:"+infoNoteAfterHoursNumber+"\n"+ //AfterHoursNumber Tel. No:
	        "ADR;TYPE=work;LABEL=:;;"+workAddress+";"+city+";"+state+";"+zip+";"+country+"\n"+ //Work Address
	        "ADR;TYPE=home;LABEL=:;; ; ; ; ; \n"+ //Home Address
	        "EMAIL:"+email+"\n"+ //Email
	        "REV:20080424T195243Z\n"+
	        "URL;WORK:"+workUrl+"\n"+
	        "URL:"+workUrl+"\n"+
	        "END:VCARD";
	
			if(name!=null && name.trim().length()>0){}
			else{
				name = "VcfFile";
			}
			
			response.setHeader("Content-Disposition", "attachment; filename="+name+".vcf");
			response.setContentType("text/x-vcard");
			System.out.println("Generating Vcard");
			PrintWriter out = response.getWriter(); out.println(str); out.close();
		} catch (Exception e) {
			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
