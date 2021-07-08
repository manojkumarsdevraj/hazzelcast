package com.unilog.products;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class ProductsModelArray {
	@Expose private ArrayList<ProductsModel> products;

	public ArrayList<ProductsModel> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<ProductsModel> products) {
		this.products = products;
	}
}
