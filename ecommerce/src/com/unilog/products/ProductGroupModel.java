package com.unilog.products;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ProductList")
public class ProductGroupModel {

	private List<Integer> list;
	public ProductGroupModel(){
		
		this.list = new ArrayList<Integer>();
	}

	 public ProductGroupModel(List<Integer> list){
	    	this.list=list;
	    }

	@XmlElement(name="ItemId")
    public List<Integer> getList(){
    	return list;
    }
	
}
