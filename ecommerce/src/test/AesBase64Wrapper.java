/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.spec.KeySpec;
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
public class AesBase64Wrapper {
    
    //IV = Password + Cenpos
    //Salt = Password + Salt
    
    private static String IV = "dWFtJuF6QUCenpos"; 
    private static String PASSWORD = "dWFtJuF6QU"; 
    private static String SALT = "dWFtJuF6QUSalt"; 

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
        byte[] decodedValue = Base64.decode(URLDecoder.decode(encrypted, "UTF-8") );
        Cipher c = getCipher(Cipher.DECRYPT_MODE);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    private byte[] getBytes(String str) throws UnsupportedEncodingException {
        return str.getBytes("UTF-8");
    }

    private Cipher getCipher(int mode) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = getBytes(IV);
        c.init(mode, generateKey(), new IvParameterSpec(iv));
        return c;
    }

    private Key generateKey() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        char[] password = PASSWORD.toCharArray();
        byte[] salt = getBytes(SALT);

        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        byte[] encoded = tmp.getEncoded();
        return new SecretKeySpec(encoded, "AES");
    }
}
