package test;

import java.util.Random;

import org.apache.commons.lang.math.RandomUtils;

public class CenposSessionDetails {
	
	
	
	public static void main(String[] args) {
		
		try {
			
			String MID = "10000074";
			String GUID = "07781B39-4FD7-4155-BA4D-836DBE348399";
			System.out.println("GUID.length : "+GUID.length());
			Random rand = new Random();
			int begin = rand.nextInt((GUID.length() - 0) + 1) + 0;
			int end =  rand.nextInt((GUID.length() - begin) + 1) + begin;
			System.out.println(begin+" : "+end);
			int Begin = 4;//random(0,GUID.length());
			int End = 19;
			String substr = GUID.substring(Begin, End);
			System.out.println(substr);
			String sessionDataPOST = MID+substr;
			//String SessionData = ASC(Begin,End,GUID,MID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
