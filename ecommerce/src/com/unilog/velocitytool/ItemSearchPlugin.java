package com.unilog.velocitytool;

import java.util.ArrayList;

import org.apache.velocity.tools.config.DefaultKey;

import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesAction;



@DefaultKey("getItemDetails")
public class ItemSearchPlugin {
	
	 public ArrayList<ProductsModel> arrayOfPartNumbers(String partNumbers){
		 ArrayList<ProductsModel>itemDetailsData = null;
		 try{
			if(partNumbers!=null && partNumbers.trim().length()>0){
				String partNumberArray[] = partNumbers.split(",");
				if(partNumberArray!=null && partNumberArray.length>0){
					ArrayList<String> partNumberArrayInput = new ArrayList<String>();
					for(String pNumber : partNumberArray){
						if(pNumber!=null && pNumber.trim().length()>0){
							partNumberArrayInput.add(pNumber);
						}
					}
					SalesAction salesAction = new SalesAction();
					itemDetailsData = salesAction.getDetailsFromPartNumbers(partNumberArrayInput);
				}
				
			}
		 }catch(Exception e){
				e.printStackTrace();
			}
		return itemDetailsData;
	 }

}
