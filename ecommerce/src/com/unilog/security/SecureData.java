package com.unilog.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
public class SecureData 
{
	
	 private static byte[] linebreak = {}; // Remove Base64 encoder default linebreak
	 private static String secret = "tvnw63ufg9gh5392"; // secret key length must be 16
	 private static SecretKey key;
	 private static Cipher cipher;
	 private static Base64 coder;

	 static {
	 try {
	     key = new SecretKeySpec(secret.getBytes(), "AES");
	     cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
	     coder = new Base64(32,linebreak,true);
	 } catch (Throwable t) {
	     t.printStackTrace();
	 }
	 }
	 
	 
	 
	 private static synchronized String encrypt(String plainText) throws Exception {
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	        byte[] cipherText = cipher.doFinal(plainText.getBytes());
	        return  new String(coder.encode(cipherText));
	 }

	 private static synchronized String decrypt(String codedText) throws Exception {
	        byte[] encypted = coder.decode(codedText.getBytes());
	        cipher.init(Cipher.DECRYPT_MODE, key);
	        byte[] decrypted = cipher.doFinal(encypted);  
	        return new String(decrypted);
	 }
	 
	 protected String securePassword(String userPassword)
	 {
		try {
			return encrypt(userPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	 }
	 
	 public static String getsecurePassword(String userPassword)
	 {
		try {
			return encrypt(userPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	 }
	 
	 public static String getPunchoutSecurePassword(String userPassword)
	 {
		try {
			return encrypt(userPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	 }
	 
	 public String validatePassword(String userPassword)
	 {
		 
		 try {
				return decrypt(userPassword);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	 }
	 
	 public static void main(String[] ar) throws Exception
	 {
		 String value = getsecurePassword("change4now");
		 System.out.println(value);
	 }
}
