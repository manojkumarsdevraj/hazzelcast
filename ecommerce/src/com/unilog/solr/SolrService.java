package com.unilog.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.products.ProductHunterModel;

import com.unilog.products.ProductHunterUtility;
import com.unilog.promotion.BannerEntity;
import com.unilog.promotion.PromotionList;
import com.unilog.promotion.PromotionResponse;
import com.unilog.promotion.PromotionUtility;
import com.unilog.utility.CommonUtility;

public class SolrService {
	private static SolrService instance = new SolrService();
	private SolrService() {}
		
	public static SolrService getInstance() {
		return instance;
	}
	
	public QueryResponse executeSolrQuery(String solrUrl,SolrQuery query) throws SolrServerException{
		HttpSolrServer solrServer = ConnectionManager.getSolrClientConnection(solrUrl);
		QueryResponse response = solrServer.query(query,METHOD.POST);
		System.out.println("Query : " + query);
		ConnectionManager.closeSolrClientConnection(solrServer);
		return response;
	}
	
	
	public SolrQuery queryBuilder(ProductHunterModel pModel){
		
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		if(pModel.getKeyWord()!=null && !pModel.getKeyWord().trim().equalsIgnoreCase("")){
			String keyWord = ProductHunterUtility.getInstance().buildSearchKeyword(pModel);
			query.setQuery(keyWord);
		}
		query.setRequestHandler("/mainitem_keywordsearch_nrt");
		query.set("expand", true);
		ProductHunterUtility.getInstance().getSortOption(pModel.getRequestType(), pModel.getSortBy(), query);
		if(pModel.getRequestType().trim().equalsIgnoreCase("NAVIGATION")){
			categoryNavigationQuery(query, pModel);
			
		}else if(pModel.getRequestType().trim().equalsIgnoreCase("SHOP_BY_BRAND")){
			brandQuery(query, pModel);
		}else if(pModel.getRequestType().trim().equalsIgnoreCase("SHOP_BY_MANF")){
			manufacturerQuery(query, pModel);
		}else if(pModel.getRequestType().trim().equalsIgnoreCase("SEARCH")){
			searchQuery(query, pModel);
			promotionQueryBuilder(query, pModel);
			//buildBoostQuery(query, pModel.getKeyWord(), "search");
		}
		ProductHunterModel filterAndFacet = ProductHunterUtility.getInstance().getFilterAndFacet(pModel);
		query.addFilterQuery(filterAndFacet.getAttrFtr());
		if(filterAndFacet.getFacetField()!=null && filterAndFacet.getFacetField().length > 0){
			query.addFacetField(filterAndFacet.getFacetField());
		}
		return query;
	}
	
	public void categoryNavigationQuery(SolrQuery query,ProductHunterModel pModel){
		StringBuilder categoryFilter = new StringBuilder();
		categoryFilter.append("taxonomy_").append(pModel.getTaxonomyId()).append(":").append(pModel.getTreeId());
		query.setFilterQueries(categoryFilter.toString());
	}
	
	public void manufacturerQuery(SolrQuery query,ProductHunterModel pModel){
		StringBuilder manufcaturerFilter = new StringBuilder();
		manufcaturerFilter.append("manufacturerId:").append(pModel.getBrandId());
		query.setFilterQueries(manufcaturerFilter.toString());
	}
	
	public void brandQuery(SolrQuery query,ProductHunterModel pModel){
		StringBuilder brandFilter = new StringBuilder();
		brandFilter.append("brandId:").append(pModel.getBrandId());
		query.setFilterQueries(brandFilter.toString());
	}
	
	public void searchQuery(SolrQuery query,ProductHunterModel pModel){
		
		 query.set("spellcheck", "true");
		 query.set("spellcheck.extendedResults", "true");
		 query.set("spellcheck.collate", "true");
		 query.set("spellcheck.q", pModel.getKeyWord());
		 
		 
		 
	}
	
	public void promotionQueryBuilder(SolrQuery query,ProductHunterModel pModel){
		try{
			
			boolean isPromotion=false;
			boolean isExcludeItems=false;
			String elevateIds=null;
			String excludeItems=null;
			BannerEntity bannerEntity=null;
	        int bannerId = 0;
	        if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SEARCH_PROMOTIONS")).equalsIgnoreCase("Y")){
	        	//PromotionResponse promotions = PromotionUtility.getInstance().getPromotionByKeyword(pModel.getKeyWord());
	        	PromotionResponse promotions = PromotionUtility.getInstance().getPromotionByKeyword(pModel.getKeyWord(),CommonUtility.validateParseIntegerToString(pModel.getSubsetId()),CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
		        if ((promotions != null) && (promotions.getData() != null) && (promotions.getData().getResultSet() != null) && (promotions.getData().getResultSet().size() > 0)) {
		          bannerId = ((PromotionList)promotions.getData().getResultSet().get(0)).getBannerId(); 
		          bannerEntity = ((PromotionList)promotions.getData().getResultSet().get(0)).getBannerEntity();
		          pModel.setBannerEntity(bannerEntity);
		          elevateIds = ((PromotionList)promotions.getData().getResultSet().get(0)).getDocuments();
		          excludeItems = ((PromotionList)promotions.getData().getResultSet().get(0)).getExcludeDocuments();
		          if ((elevateIds != null) && (!elevateIds.trim().equalsIgnoreCase(""))) {
		            elevateIds = elevateIds.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "");
		            if (elevateIds.length() > 0) {
		              isPromotion = true;
		            }
		          }

		          if ((excludeItems != null) && (!excludeItems.trim().equalsIgnoreCase(""))) {
		            excludeItems = excludeItems.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "");
		            if (excludeItems.length() > 0) {
		              isExcludeItems = true;
		            }
		          }

		        }
	        }
			if ((isPromotion) || (isExcludeItems)) {
		        query.setRequestHandler("/elevate");
		        query.set("forceElevation", new String[] { "true" });
		        if (isPromotion) {
		          query.set("elevateIds", new String[] { elevateIds });
		        }
		        if (isExcludeItems)
		          query.set("excludeIds", new String[] { excludeItems });
		     }
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public  void buildBoostQuery(SolrQuery query,String keyWord,String actionType){
		try{
			keyWord = keyWord.toLowerCase();
			String key = new StringBuilder().append(actionType).append("Keyword_").toString();
			String val =  new StringBuilder().append("_val_:").append(actionType).append("KeywordScore_").toString();;
			StringBuilder bq = null;
			for(int i=0;i<10;i++){
				bq = new StringBuilder();
				bq.append(key).append(i+1).append(":\"").append(keyWord).append("\"");
				bq.append(" AND ").append(val).append(i+1);
				System.out.println(bq.toString());
				query.add("bq",bq.toString());
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	

}
