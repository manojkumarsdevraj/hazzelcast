package com.unilog.cenpos.paymentgateway;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.spec.KeySpec;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Master
 */
@SuppressWarnings("restriction")
public class CenposBase64Wrapper {

	private final static String SALT = "Salt";
	private final static String CENPOS = "Cenpos";
    private final String key;
    private final String salt;
    private final String iv;

	public CenposBase64Wrapper(String key) {
		this.key = key;
		this.salt = key+SALT;
		this.iv = key+CENPOS;
	}

	public String encryptAndEncode(String raw) {
        try {
            Cipher c = getCipher(Cipher.ENCRYPT_MODE);
            byte[] encryptedVal = c.doFinal(getBytes(raw));
            String s = Base64.encode(encryptedVal);
            return URLEncoder.encode(s, "UTF-8");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

	public String decodeAndDecrypt(String encrypted) throws Exception {
	   byte[] decodedValue = Base64.decode(URLDecoder.decode(encrypted, "UTF-8"));
        Cipher c = getCipher(Cipher.DECRYPT_MODE);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    private byte[] getBytes(String str) throws UnsupportedEncodingException {
        return str.getBytes("UTF-8");
    }

    private Cipher getCipher(int mode) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        System.out.println("IV: "+iv);
    	byte[] iv = getBytes(this.iv);
        c.init(mode, generateKey(), new IvParameterSpec(iv));
        return c;
    }

    private Key generateKey() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        char[] password = this.key.toCharArray();
    	System.out.println("SALT Used in encryption: "+salt);
    	byte[] salt = getBytes(this.salt);

        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        byte[] encoded = tmp.getEncoded();
        return new SecretKeySpec(encoded, "AES");
    }
}