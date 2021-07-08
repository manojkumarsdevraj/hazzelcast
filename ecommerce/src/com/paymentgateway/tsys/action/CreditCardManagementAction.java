package com.paymentgateway.tsys.action;

import java.util.Random;

import com.paymentgateway.model.CreditCardManagementModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.sales.CreditCardModel;

public class CreditCardManagementAction {
	
	public CreditCardModel creditCardTransactionSetup(CreditCardManagementModel creditCardInput){
		CreditCardModel creditCardOutput  = new CreditCardModel();
		
		try {
			
			Integer externalRefNumInt = randomnum();
		    Integer invoiceInt = invoicenum();
		    String invoice = invoiceInt.toString();
		    String externalRefNumber = externalRefNumInt.toString();
			
		    creditCardInput.setInvoiceNumber(invoice);
		    creditCardInput.setExternalRefNumber(externalRefNumber);
		    
			System.out.println("creditCardInput.getTotal() : " + creditCardInput.getTotal());
			System.out.println("creditCardInput.getMerchantId() : " + creditCardInput.getMerchantId());
			System.out.println("creditCardInput.getHostPassword() : " + creditCardInput.getHostPassword());
			System.out.println("creditCardInput.getExternalRefNumber() : " + creditCardInput.getExternalRefNumber());
			System.out.println("creditCardInput.getHostKey() : " + creditCardInput.getHostKey());
			System.out.println("creditCardInput.getInvoiceNumber() : " + creditCardInput.getInvoiceNumber());
		    
			String amount= String.valueOf(creditCardInput.getTotal());
		    amount = amount.replace(".", "");
			
		    System.out.println("amount : " + amount);
		    
		    String encryptedManifest = com.tsys.THPCrypt.encryptManifest(creditCardInput.getMerchantId(), creditCardInput.getHostPassword(), amount, creditCardInput.getExternalRefNumber(), creditCardInput.getHostKey());
			System.out.println("Encrypted Manifest : " + encryptedManifest);
			System.out.println("Decrypted Manifest : " + com.tsys.THPCrypt.decryptText(encryptedManifest, creditCardInput.getHostKey()));
			
			creditCardOutput.setInvoiceNumber(invoice);
			creditCardOutput.setExternalRefNumber(externalRefNumber);
			creditCardOutput.setEncryptedManifest(encryptedManifest);
			creditCardOutput.setAmount(amount);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return creditCardOutput;
	}
	
	private static int randomnum() {
		Random generator = new Random();
		return 10000000 + generator.nextInt(99999999);
	}
	
	private static int invoicenum() {
		Random generator = new Random();
		return 1000000 + generator.nextInt(9999999);
	}

}
