package com.unilog.products;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONArray;

import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;



public class XSSRequestWrappers extends HttpServletRequestWrapper {
	
	 HttpServletRequest request;
	 HttpServletResponse response;
	    

 
   private static Pattern[] patterns = new Pattern[]{
        // Script fragments
        Pattern.compile("--><script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
         // src='...'
        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // lonely script tags
        Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // eval(...)
        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // expression(...)
        Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // javascript:...
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        // vbscript:...
        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
        // onload(...)=...
        Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("<(\\\"[^\\\"]*\\\"|'[^']*'|[^'\\\">])*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("alert(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("prompt()=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL), 
        Pattern.compile("prompt(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
        
    };

 

	public XSSRequestWrappers(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
     

        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }

        return encodedValues;
    }

  
    
    @Override
    public String getParameter(String parameter) {
        String values = super.getParameter(parameter);
        if (values == null) {
            return null;
        }
       if(CommonUtility.validateString(values).length()>0  && !CommonUtility.escapeFields(parameter) && !isValidJson(values)){
    	   values =stripXSS(values);
       }    
       return values;
    }
    
  public boolean isValidJson(String jsonStr) {
	  try {
		  Object json = new JSONTokener(jsonStr).nextValue();
	        if (json instanceof JSONObject || json instanceof JSONArray) {
	          return true;
	        } else {
	          return false;
	        }
	  }catch (Exception e) {
          return false;
	}
   }
   

    @Override
    public String getHeader(String name) {
        String values = super.getHeader(name);
        if (values == null) {
            return null;
        }
        return stripXSS(values);
    }

	public String stripXSS(String values) {

		try {

			if (values != null) {
				if (CommonDBQuery.getSystemParamtersList() !=null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SECURITY_POLICY")).equalsIgnoreCase("Y")) {
					// Avoid null characters
					values = values.replaceAll("\0", "");
					values = values.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
					//values = values.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
					values = values.replaceAll("'", "& #39;");
					values = values.replaceAll("eval\\((.*)\\)", "");
					values = values.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
					values = values.replaceAll("^\"|\"$", "");
					values = values.replaceAll("%E5%98%8A", "");
					values = values.replaceAll("%E5%98%8D", "");
					values = values.replaceAll("%E5%98%BE", "");
					values = values.replaceAll("%E5%98%BC", "");
					values = values.replaceAll("%E5%98%A0", "");
					values = values.replaceAll("%E5%98", "");
					// values = values.replace("\"", " ");
					// values = values.toLowerCase().replaceAll("script", "");
					for (Pattern scriptPattern : patterns) {
						values = scriptPattern.matcher(values).replaceAll("");
					}
					values = CommonUtility.escapeHtml(values);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return values;

	}
    
}
