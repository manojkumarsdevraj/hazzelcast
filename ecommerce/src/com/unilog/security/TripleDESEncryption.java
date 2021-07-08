package com.unilog.security;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;


public class TripleDESEncryption {
	private static TripleDESEncryption tripleDESEncryption=null;
	private String ENCRYPTION_KEY;
	private String INITIALIZATION_VECTOR;
	private String ENCRYPTION_TYPE;
	private String ENCRYPTION_METHOD;
	private String ENCODE_METHOD;
	private String ENABLE_ENCRYPTION;
	private String SERVICE_URL;
	private String URL_PARAMS;
	private String CLIENT_GUID;
	private boolean reInitializeClient = true;
	
	
	public static TripleDESEncryption getInstance() {
		synchronized(TripleDESEncryption.class) {
			if(tripleDESEncryption==null) {
				tripleDESEncryption = new TripleDESEncryption();				
			}
		}
		return tripleDESEncryption;
	}
	private void initializeClient() {
		ENCRYPTION_KEY = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BILLTRUST_ENCRYPTION_KEY"));
		INITIALIZATION_VECTOR = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BILLTRUST_INITIALIZATION_VECTOR"));
		ENCRYPTION_TYPE = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BILLTRUST_ENCRYPTION_TYPE"));
		ENCRYPTION_METHOD = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BILLTRUST_ENCRYPTION_METHOD"));
		ENCODE_METHOD = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BILLTRUST_ENCODE_METHOD"));
		ENABLE_ENCRYPTION= CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BILLTRUST_ENABLE_ENCRYPTION"));
		SERVICE_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BILLTRUST_SERVICE_URL"));
		URL_PARAMS =CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BILLTRUST_URL_PARAMS"));
		CLIENT_GUID = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BILLTRUST_CLIENT_GUID"));
		reInitializeClient = false;
	}
	public String getBillTrustURL(String accountNumber,LinkedHashMap<String, String> customFieldValuesList){
		if(reInitializeClient){
			initializeClient();
		}
		String finalURL = "";
		try{
			Date n = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String urlParam = "AN="+accountNumber;
			if(CommonUtility.validateString(customFieldValuesList.get("EBILL_NOTIFICATION")).equalsIgnoreCase("Y")){
				urlParam = urlParam+";EN=Y";
			}
			if(CommonUtility.validateString(customFieldValuesList.get("PAPER_BILL")).equalsIgnoreCase("Y")){
				urlParam = urlParam+";PB=Y";
			}
			if(CommonUtility.validateString(customFieldValuesList.get("ONLINE_BILLING_CUSTOMERS_ONLY")).equalsIgnoreCase("Y")){
				urlParam = urlParam+";EC=Y";
			}
			if(CommonUtility.validateString(customFieldValuesList.get("INVOICE_NUMBER")).length()>0){
				urlParam = urlParam+";IN="+customFieldValuesList.get("INVOICE_NUMBER");
			}
			if(CommonUtility.validateString(customFieldValuesList.get("MAKE_PAYMENT")).equalsIgnoreCase("Y")){
				urlParam = urlParam+";MP=Y";
			}
			if(CommonUtility.validateString(customFieldValuesList.get("GATEWAY_USER_NAME")).length()>0){
				urlParam = urlParam+";UN="+customFieldValuesList.get("GATEWAY_USER_NAME");
			}
			urlParam = urlParam+";EA="+customFieldValuesList.get("emailAddress");
			urlParam = urlParam+";TS="+formatter.format(n.getTime());
			if(CommonUtility.validateString(ENABLE_ENCRYPTION).equalsIgnoreCase("Y")){
				finalURL = SERVICE_URL+encryptText(urlParam)+"\"&CG=\""+CLIENT_GUID+"\"&ETYPE=1";
			}else{
				finalURL = SERVICE_URL+urlParam+"\"&CG=\""+CLIENT_GUID+"\"&ETYPE=0";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return finalURL;
	}
	public String encryptText(String plainText){
		
		String encryptedString = "";
		try{
			byte[] plaintext = plainText.getBytes();
	        byte[] tdesKeyData = ENCRYPTION_KEY.getBytes();
	        byte[] myIV = INITIALIZATION_VECTOR.getBytes();
	        Cipher c3des = Cipher.getInstance(ENCRYPTION_TYPE);
	        SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, ENCRYPTION_METHOD);
	        IvParameterSpec ivspec = new IvParameterSpec(myIV);
	        c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
	        byte[] cipherText = c3des.doFinal(plaintext);
	        encryptedString = new String(Base64.encodeBase64(cipherText),ENCODE_METHOD);
		}catch(Exception e){
			encryptedString = "Internal Server Error";
		}
        
        return encryptedString;
    }
	public void setReInitializeClient(boolean reInitializeClient) {
		this.reInitializeClient = reInitializeClient;
	}
	public boolean isReInitializeClient() {
		return reInitializeClient;
	}

}
