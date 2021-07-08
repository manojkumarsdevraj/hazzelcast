package com.unilognew.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil {

	private Properties props;
	private static PropertiesUtil propertiesUtil;
	
	public static PropertiesUtil getInstance() throws IOException {
		try{
			synchronized(PropertiesUtil.class) {
				if(propertiesUtil==null) {
					propertiesUtil = new PropertiesUtil();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return propertiesUtil;
	}
	
	// private constructor
	private PropertiesUtil() throws IOException {
		loadPropertiesFromClasspath();
	}
	
	public String getProperty(String key) {
		return props.getProperty(key);
	}

	public Set<Object> getkeys() {
		return props.keySet();
	}

	public Properties getProperties() {
		return props;
	}

	/**
	 * loads properties file from classpath
	 *
	 * @param propFileName
	 * @return
	 * @throws IOException
	 */
	public void loadPropertiesFromClasspath()
			throws IOException {
		props = new Properties();
		
		String propFileName = "/SiteConfiguration.properties";
		
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader()
					.getResourceAsStream(propFileName);

			if (inputStream == null) {
				throw new FileNotFoundException("property file '"
						+ propFileName + "' not found in the classpath");
			}
			props.load(inputStream);
		} finally {
			if(inputStream!=null) {
				inputStream.close();
			}
		}
		
	}
}