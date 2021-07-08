package com.unilog.products.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import com.unilog.products.ProductHunterModel;
import com.unilog.products.ProductHunterSolrV2;
import com.unilog.products.ProductHunterUtility;
import com.unilog.products.ProductsModel;
import com.unilog.products.service.ProductHunter;
import com.unilog.solr.SolrService;
import com.unilog.solr.SolrServiceUtility;

public class ProductHunterImpl implements ProductHunter {

	@Override
	public HashMap<String, ArrayList<ProductsModel>> solrSearchNavigation(ProductHunterModel pModel) {
		
		HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
		try{
			ProductsModel itemList = new ProductsModel();
			int resultCount = 0;
			SolrQuery query = SolrService.getInstance().queryBuilder(pModel);
			QueryResponse response = SolrService.getInstance().executeSolrQuery(ProductHunterSolrV2.solrURL+"/cimmitemmaster", query);
			resultCount = (int) response.getResults().getNumFound();
			LinkedHashMap<String, SolrDocumentList> expandObject = SolrServiceUtility.getInstance().buildProductCount(response);
			itemList = SolrServiceUtility.getInstance().buildItemList(response,resultCount,expandObject,pModel);
			searchResultList = ProductHunterUtility.getInstance().buildNavigationSearchResult(itemList, pModel);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return searchResultList;
		
		
	}

}
