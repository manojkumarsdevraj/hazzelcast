package com.unilog.cenpos.paymentgateway;


import java.util.Random;
import java.util.UUID;

/**
 *
 * @author Elsan
 */
public class CenposEncryptor {

	private final String merchantId;
	private final String key;
	private final String sessionPostData;
	private final String sessionData;

	public CenposEncryptor(String mid,String key) {
		this.merchantId = mid;
		this.key = key;

		String GUID = UUID.randomUUID().toString();
		Random r= new Random();
		int begin = r.nextInt(GUID.length());
		int len = (GUID.length()-begin);

		String encryptMe = begin+"," + len +","+GUID+","+this.merchantId;

		String substr = GUID.substring(begin,len+begin);

		this.sessionPostData = this.merchantId+substr;

		CenposBase64Wrapper encoder = new CenposBase64Wrapper(this.key);

    	this.sessionData = encoder.encryptAndEncode(encryptMe);
    }

    public String getSessionData() {
		return sessionData;
	}

    public String getMerchantId() {
		return merchantId;
	}

	public String getSessionPostData() {
		return sessionPostData;
	}

	public String getKey() {
		return this.key;
	}

	public static void main(String args[]) {
		try {
			if(args!=null && args.length!=2) {
				System.out.println("Usage: CenposEncryptor MerchentId Password");
			} else {
				CenposEncryptor cenposEncryptor = new CenposEncryptor(args[0],args[1]);
				System.out.println("Merchant Id : "+cenposEncryptor.getMerchantId());
				System.out.println("Key : "+cenposEncryptor.getKey());
				System.out.println("Session Post Data : "+cenposEncryptor.getSessionPostData());
				System.out.println("Session Data : "+cenposEncryptor.getSessionData());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}