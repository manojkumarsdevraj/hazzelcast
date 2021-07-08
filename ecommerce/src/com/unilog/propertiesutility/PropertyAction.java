package com.unilog.propertiesutility;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Properties;

import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

public class PropertyAction {
	private static final long serialVersionUID = 2180666661436590280L;
	public static LinkedHashMap<String,String> SqlContainer = null;
	public static LinkedHashMap<String,String> impBasicConfigurations = null;
	public static Properties basicConfiguration = new Properties();
	private static LinkedHashMap<String,String> versionControl=null;
	static String versionControlPath = CommonDBQuery.getSystemParamtersList().get("VERSION_CONTROL_PATH");
	
	static
	{
		loadBasicConfigs();
	}
	
	public static void loadBasicConfigs(){
		String basicConfigFile = CommonDBQuery.getSystemParamtersList().get("BASIC_CONFIG_FILE");
		if(!CommonUtility.validateString(basicConfigFile).isEmpty()){
			basicConfigFile = MessageFormat.format(basicConfigFile, CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
		}
		System.out.println("basicConfigFile : " + basicConfigFile);
		impBasicConfigurations = new LinkedHashMap<String, String>();
		try {
			basicConfiguration.load(new FileInputStream(basicConfigFile));
			Enumeration<Object> em = basicConfiguration.keys();
			while(em.hasMoreElements()){
				  String str = (String)em.nextElement();
				  impBasicConfigurations.put(str, (String)basicConfiguration.get(str));
			}
			System.out.println("BasicConfiguration Loaded Successfully");
			if(impBasicConfigurations.get("SQLContainerFile")!=null && !impBasicConfigurations.get("SQLContainerFile").equalsIgnoreCase("")){
				SqlContainer = new LinkedHashMap<String, String>();
				SqlContainer = getDataFromPropertyFile("SQLContainerFile");
				System.out.println("SQLContainerFile Loaded Successfully");
			}
			if(impBasicConfigurations.get("versionControlFile")!=null && !impBasicConfigurations.get("versionControlFile").equalsIgnoreCase("")){
				versionControl = new LinkedHashMap<String, String>();
				versionControl = getDataFromPropertyFile("versionControlFile");
				System.out.println("----------version control files Loaded Successfully-------------");
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	public static Properties ReadFile(String fileName){
		Properties prop = new Properties();
		try {
			if(CommonUtility.validateString(fileName).length()>0){
				if(fileName.trim().equalsIgnoreCase("SqlContainer")){
					InputStream input = PropertyAction.class.getClassLoader().getResourceAsStream("SqlContainer.properties");
					prop.load(input);
				}else if(fileName.trim().equalsIgnoreCase("SystemParameter")){
					if(impBasicConfigurations!=null && !impBasicConfigurations.get("SystemParameterFilePath").trim().equals("")) {
						prop.load(new FileInputStream(impBasicConfigurations.get("SystemParameterFilePath")+fileName+".properties"));
					}else {
					InputStream input = PropertyAction.class.getClassLoader().getResourceAsStream("SystemParameter.properties");
					prop.load(input);
					}
				}else if(fileName.trim().equalsIgnoreCase("versionsControl")){
					prop.load(new FileInputStream(versionControlPath+fileName+".properties"));
				}else{
					if(impBasicConfigurations!=null && !impBasicConfigurations.get("propertyFileLocation").trim().equals("")){
						prop.load(new FileInputStream(impBasicConfigurations.get("propertyFileLocation")+fileName+".properties"));
					}
				}
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	public static LinkedHashMap<String,String> getDataFromPropertyFile(String fileName){
		LinkedHashMap<String,String> ObjectName = null;
		try{
			if(impBasicConfigurations.get(fileName)!=null && !impBasicConfigurations.get(fileName).equalsIgnoreCase("")){
				ObjectName = new LinkedHashMap<String, String>();
				Properties propertyFile = new Properties();
				propertyFile = ReadFile(impBasicConfigurations.get(fileName));
				Enumeration<Object> enums = propertyFile.keys();
				while(enums.hasMoreElements()){
					  String str = (String)enums.nextElement();
					  ObjectName.put(str, (String)propertyFile.get(str));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ObjectName;
	} 

	public static void setImpBasicConfigurations(
			LinkedHashMap<String, String> impBasicConfigurations) {
		PropertyAction.impBasicConfigurations = impBasicConfigurations;
	}
	public static LinkedHashMap<String, String> getImpBasicConfigurations() {
		return impBasicConfigurations;
	}

	public static LinkedHashMap<String,String> getVersionControl() {
		return versionControl;
	}

	public static void setVersionControl(LinkedHashMap<String,String> versionControl) {
		PropertyAction.versionControl = versionControl;
	}
}
