package com.unilog.products.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.unilog.products.ProductHunterModel;
import com.unilog.products.ProductsModel;

public interface ProductHunter {
	public HashMap<String, ArrayList<ProductsModel>> solrSearchNavigation(ProductHunterModel pModel);
}
