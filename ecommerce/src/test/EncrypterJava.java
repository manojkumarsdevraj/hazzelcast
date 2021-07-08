/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.Random;

/**
 *
 * @author Master
 */
public class EncrypterJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
    	
    	//String MID = "12722441";
    	//String GUID = "D0C0F020-D180-48C6-B0CA-925991D10E05";
    	String MID = "10000074";
		String GUID = "07781B39-4FD7-4155-BA4D-836DBE348399";
		
		//int Begin = 4;//random(0,GUID.length());
		//int End = 19;
		System.out.println("MID : "+MID);
		System.out.println("GUID : "+GUID);
		
		
		Random rand = new Random();
		
		int Begin = rand.nextInt((GUID.length() - 0) + 1) + 0;
		int End =  rand.nextInt((GUID.length() - Begin) + 1) + Begin;
		
		
		System.out.println("Begin : "+Begin);
		System.out.println("End : "+End);
		
		String substr = GUID.substring(Begin, End);
		String sessionDataPOST = MID+substr;
		System.out.println("sessionDataPOST : "+sessionDataPOST);
    	  	
    	
    	
    	String encryptMe;
        String encrypted;
        String decrypted;
        
        
        //encryptMe = "4,31,e472e3d6-78aa-4353-a241-dce124156281,12520251";
        encryptMe = MID+","+End+","+GUID+","+MID;
        //System.out.printf("encryptMe = %s\n", encryptMe);
        
        encrypted = new AesBase64Wrapper().encryptAndEncode(encryptMe);
        System.out.printf("SessionData = %s\n", encrypted);
        
        decrypted = new AesBase64Wrapper().decodeAndDecrypt(encrypted);
        
       // System.out.printf("decrypted = %s\n", decrypted);
    }
}
