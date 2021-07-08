package com.unilog.defaults;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class ULLog {
	
	private static org.apache.log4j.Logger logger = Logger.getLogger("com.unilog.UNLog");
	private static org.apache.log4j.Logger sqlLogger = Logger.getLogger("com.unilog.foundation");
	private static org.apache.log4j.Logger eclipseWebIntegration = Logger.getLogger("com.unilog.eclipseWebIntegration");
	private static org.apache.log4j.Logger eclipseSend = Logger.getLogger("com.nce.eclipseSend");
	private static org.apache.log4j.Logger eclipseResponse = Logger.getLogger("com.nce.eclipseResponse");
	public static void log(String message)
	{
		logger.log(Priority.DEBUG, message);
	}
	
	public static void fatal(String message)
	{
		if(message != null)
			logger.fatal(message);
	}
	
	public static void error(String message)
	{
		if(message != null)
			logger.error(message);
	}
	
	public static void errorWithEmail(String message)
	{
		if(message != null)
			logger.error(message);
	}
	
	public static void errorWithEmail(String subject, String message)
	{
		if(message != null)
			logger.error(message);
	}

	
	public static void warn(String message)
	{
		if(message != null)
			logger.warn(message);
	}
	
	public static void info(String message)
	{
		if(message != null)
			logger.info(message);
	}
	
	public static void debug(String message)
	{
		if(message != null)
			logger.debug(message);
	}
	
	public static void sqlTrace(String message)
	{
		if(message != null)
			sqlLogger.info(message);
	}
	
	public static void sqlTrace(String message1,String message2)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(message1);
		sb.append("\n").append(message2);	

		sqlLogger.info(sb.toString());
	}
	
	public static void sqlTrace(String message1,String message2,String message3)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(message1);
		sb.append("\n").append(message2).append(",");
		sb.append(message3);	

		sqlLogger.info(sb.toString());
	}
	public static void sqlTrace(String message1,String message2,String message3,String message4)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(message1);
		sb.append("\n").append(message2).append(",");
		sb.append(message3).append(",").append(message4);	

		sqlLogger.info(sb.toString());
	}	
	public static void sqlTrace(String message1,String message2,String message3,String message4,String message5)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(message1);
		sb.append("\n").append(message2).append(",");
		sb.append(message3).append(",").append(message4);
		sb.append(",").append(message5);	

		sqlLogger.info(sb.toString());
	}
	public static void sqlTrace(String message1,String message2,String message3,String message4,String message5,String message6)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(message1);
		sb.append("\n").append(message2).append(",");
		sb.append(message3).append(",").append(message4);
		sb.append(",").append(message5).append(",").append(message6);	

		sqlLogger.info(sb.toString());
	}	

	
	 public static void errorTrace(Throwable fillInStackTrace)
     {
        if(fillInStackTrace != null)
		logger.error(fillInStackTrace);
     }
	 
		
	 public static void eclipseResponseLogs(String message)
		{
			if(message != null)
	                {
	                        eclipseResponse.debug(message);
				    }
		}
	 public static void eclipseSendLogs(String message)
		{
			if(message != null)
	                {
	                        eclipseSend.debug(message);
	                }
		}

	/**
	 * @param eclipseWebIntegration the eclipseWebIntegration to set
	 */
	public static void eclipseWebIntegrationTrace(String message1) {
		eclipseWebIntegration.info(message1);
		eclipseWebIntegration.debug(message1);
		eclipseWebIntegration.warn(message1);			
	}

	
}