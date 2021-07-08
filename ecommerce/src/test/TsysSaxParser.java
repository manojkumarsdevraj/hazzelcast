package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import test.TsysModel;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TsysSaxParser {
	
	private InputStream is;
	public String value = "";
	private boolean statusFlag = false;
	private boolean hostInfoFlag = false;
	private boolean deviceInfoFlag = false;
	private TsysModel tsysModel = null;
	/*private Status status = null;
	private HostInfo hostInfo = null;
	private DeviceInfo deviceInfo = null; */
	
	
	public boolean isStatusFlag() {
		return statusFlag;
	}
	public TsysModel getTsysModel() {
		return tsysModel;
	}
	public void setTsysModel(TsysModel tsysModel) {
		this.tsysModel = tsysModel;
	}
	public void setStatusFlag(boolean statusFlag) {
		this.statusFlag = statusFlag;
	}
	public boolean isHostInfoFlag() {
		return hostInfoFlag;
	}
	public void setHostInfoFlag(boolean hostInfoFlag) {
		this.hostInfoFlag = hostInfoFlag;
	}
	public boolean isDeviceInfoFlag() {
		return deviceInfoFlag;
	}
	public void setDeviceInfoFlag(boolean deviceInfoFlag) {
		this.deviceInfoFlag = deviceInfoFlag;
	}
	/*public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public HostInfo getHostInfo() {
		return hostInfo;
	}
	public void setHostInfo(HostInfo hostInfo) {
		this.hostInfo = hostInfo;
	}
	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}*/
	
	public TsysSaxParser(InputStream stringBufferInputStream){
		this.is=stringBufferInputStream;
		excute();
	}   
	
	
	void excute() {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
				
			DefaultHandler handler = new DefaultHandler() {
			private Stack<String> elementStack = new Stack<String>();
		
			  public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
				  
				 this.elementStack.push(qName);
				 value = "";
				 
				  if(qName.equalsIgnoreCase("AuthenticationRS")){
					  tsysModel = new TsysModel();
			      }
				  if(qName.equalsIgnoreCase("Status")){
					  statusFlag = true;
					  //status = new Status();
			      }
				  if(qName.equalsIgnoreCase("Host_Info")){
					  hostInfoFlag = true;
					  //hostInfo = new HostInfo();
			      }
				  if(qName.equalsIgnoreCase("Device_Info")){
					  deviceInfoFlag = true;
					  //deviceInfo = new DeviceInfo();
			      }
			  }
	
		  
			  public void endElement(String uri, String localName,String qName) throws SAXException {
				  
				  if("Code".equalsIgnoreCase(currentElement()) && statusFlag){
					  tsysModel.setCode(value);
			      }else if("Message".equalsIgnoreCase(currentElement()) && statusFlag){
			    	  tsysModel.setMessage(value);
			      }else if("Host_ID".equalsIgnoreCase(currentElement()) && hostInfoFlag){
			    	  tsysModel.setHostID(value);
			      }else if("Host_Password".equalsIgnoreCase(currentElement()) && hostInfoFlag){
			    	  tsysModel.setHostPassword(value);
			      }else if("New_Host_Password".equalsIgnoreCase(currentElement()) && hostInfoFlag){
			    	  tsysModel.setNewHostPassword(value);
			      }else if("Merchant_ID".equalsIgnoreCase(currentElement()) && deviceInfoFlag){
			    	  tsysModel.setDeviceID(value);
			      }else if("Device_ID".equalsIgnoreCase(currentElement()) && deviceInfoFlag){
			    	  tsysModel.setMerchantID(value);
			      }else if("New_Unique_Key".equalsIgnoreCase(currentElement()) && deviceInfoFlag){
			    	  tsysModel.setNewUniqueKey(value);
			      }
				  
				  this.elementStack.pop();
				  
				  if(qName.equalsIgnoreCase("Status")){
					  statusFlag = false;
			      }
				  if(qName.equalsIgnoreCase("Host_Info")){
					  hostInfoFlag = false;
			      }
				  if(qName.equalsIgnoreCase("Device_Info")){
					  deviceInfoFlag = false;
			      }
			  }
	
			  public void characters(char ch[], int start, int length) throws SAXException {
				  value = value + new String(ch, start, length).trim();
				  if(value.length() == 0) return; // ignore white space
			  }
		  
			  private String currentElement() {
			    return this.elementStack.peek();
			  }
			  
			  public InputSource resolveEntity(String publicId, String systemId)throws IOException, SAXException {
				 InputSource isrc = new InputSource(new FileInputStream(new File("D:\\forChml\\TSYS.dtd")));
			 	 return isrc;
			  }
		  
			};	
		  saxParser.parse(is, handler);
		}catch (Exception e) {
			  e.printStackTrace();
		}
	}
	  
	 
}
	
	/*class TsysModel{
		
		private String code;
		private String message;
		private String hostID;
		private String hostPassword;
		private String newHostPassword;
		private String merchantID;
		private String deviceID;
		private String newUniqueKey;
		
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getHostID() {
			return hostID;
		}
		public void setHostID(String hostID) {
			this.hostID = hostID;
		}
		public String getHostPassword() {
			return hostPassword;
		}
		public void setHostPassword(String hostPassword) {
			this.hostPassword = hostPassword;
		}
		public String getNewHostPassword() {
			return newHostPassword;
		}
		public void setNewHostPassword(String newHostPassword) {
			this.newHostPassword = newHostPassword;
		}
		public String getMerchantID() {
			return merchantID;
		}
		public void setMerchantID(String merchantID) {
			this.merchantID = merchantID;
		}
		public String getDeviceID() {
			return deviceID;
		}
		public void setDeviceID(String deviceID) {
			this.deviceID = deviceID;
		}
		public String getNewUniqueKey() {
			return newUniqueKey;
		}
		public void setNewUniqueKey(String newUniqueKey) {
			this.newUniqueKey = newUniqueKey;
		}
		
	}*/
	/*
	class Status{
		
		private String code;
		private String message;
		
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}
	class HostInfo{
		
		private String hostID;
		private String hostPassword;
		private String newHostPassword;
		
		public String getHostID() {
			return hostID;
		}
		public void setHostID(String hostID) {
			this.hostID = hostID;
		}
		public String getHostPassword() {
			return hostPassword;
		}
		public void setHostPassword(String hostPassword) {
			this.hostPassword = hostPassword;
		}
		public String getNewHostPassword() {
			return newHostPassword;
		}
		public void setNewHostPassword(String newHostPassword) {
			this.newHostPassword = newHostPassword;
		}
	}
	class DeviceInfo{
		
		private String merchantID;
		private String deviceID;
		private String newUniqueKey;
		
		public String getMerchantID() {
			return merchantID;
		}
		public void setMerchantID(String merchantID) {
			this.merchantID = merchantID;
		}
		public String getDeviceID() {
			return deviceID;
		}
		public void setDeviceID(String deviceID) {
			this.deviceID = deviceID;
		}
		public String getNewUniqueKey() {
			return newUniqueKey;
		}
		public void setNewUniqueKey(String newUniqueKey) {
			this.newUniqueKey = newUniqueKey;
		}
	}*/

