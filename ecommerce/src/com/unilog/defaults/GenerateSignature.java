package com.unilog.defaults;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Arrays;
import java.util.Enumeration;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonObject;
import com.unilog.database.CommonDBQuery;

public class GenerateSignature {

	final String STORENAME = CommonDBQuery.getSystemParamtersList().get("PRIVATEKEYPATH");
	final static String STOREPASS = CommonDBQuery.getSystemParamtersList().get("PRIVATEKEYPASSWORD");//"password";
	public String signJSONObject(JsonObject jsonMessage) {
		String token = null;
		String message = String.valueOf(jsonMessage);
		try {
			byte[] dataToSign = message.getBytes("utf8");
			//Base64 base64 = new Base64();
			String encodedMessage = Base64.encodeBase64String((dataToSign));
			System.out.println("Message: " + encodedMessage);
			String signedMessage = signData(message, STORENAME);
			token = encodedMessage;
			token += ".";
			token += signedMessage;
			System.out.println("Here is the token for you:  "+ URLEncoder.encode(token));
			return URLEncoder.encode(token);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String signData(String dataToSign, String keyFile) {
		FileInputStream keyfis = null;
		try {
			keyfis = new FileInputStream(keyFile);
			KeyStore store = KeyStore.getInstance("PKCS12");
			store.load(keyfis, STOREPASS.toCharArray());

			Enumeration<String> aliases = store.aliases();
			String aliaz = "";
			while (aliases.hasMoreElements()) {
				aliaz = aliases.nextElement();
				if (store.isKeyEntry(aliaz)) {
					break;
				}
			}
			KeyStore.PrivateKeyEntry pvk = (KeyStore.PrivateKeyEntry) store
					.getEntry(
							aliaz,
							new KeyStore.PasswordProtection("password"
									.toCharArray()));

			PrivateKey privateKey = (PrivateKey) pvk.getPrivateKey();
			byte[] data = dataToSign.getBytes("utf8");
			/*
			 * MessageDigest md = MessageDigest.getInstance("SHA1"); byte[]
			 * hashed = md.digest(data);
			 */
			Signature rsa = Signature.getInstance("SHA1withRSA");
			rsa.initSign(privateKey);
			rsa.update(data);
			byte[] signed = rsa.sign();
			System.out.println(" Stack Over: " + Arrays.toString(signed));
			System.out.println(Base64.encodeBase64String(signed));

			return Base64.encodeBase64String(signed);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (keyfis != null) {
				try {
					keyfis.close();
				} catch (Exception ex) {
					keyfis = null;
				}
			}
		}
		return null;
	}

}
