package com.unilog.products;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.utility.CommonUtility;

public class ZeroInSearchUltimate {
	
	
	
	
	public static String getFacetCategoryList(String narrowKeyword, String queryString,String connUrl,String[] attrFtr,int fromRow,int resultPerPage,String innerUrl,String fields[],String origKeyWord,String sortField,ORDER sortOrder, boolean includePartNumberSearch,boolean isAdvancedSearch, ArrayList<ProductsModel> itemLevelFilterData, String customerPartIdList)
	{
		ProductsModel solrSearchVal = new ProductsModel();
		String categGroup = "";
		HttpSolrServer server = null;
		try
		{
			server = ConnectionManager.getSolrClientConnection(connUrl);
			SolrQuery query = new SolrQuery();
			
			String c = "";

				query.setQuery(queryString);
				query.addSortField("goldenitem", SolrQuery.ORDER.desc );
				query.addSortField(sortField, sortOrder);
				
				query.set("defType", "edismax");
				
				if(isAdvancedSearch)
				{
					query.set("qf", "partkeywords");
					query.set("pf", "partkeywords^50");
				}
				else
				{
					query.set("qf", "partkeywords keywords");
					query.set("pf", "partkeywords^100 keywords^50");
				}
				
				query.set("ps", "0");
				
				query.set("spellcheck", "true");
				 query.set("spellcheck.extendedResults", "true");
				 query.set("spellcheck.collate", "true");
				 query.set("spellcheck.q", origKeyWord);
				 if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				 {
				 if(attrFtr!=null && attrFtr.length>0)
					 attrFtr[attrFtr.length-1] = null;
				 }
				
				//fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
			
				query.setStart(fromRow);
				query.setRows(resultPerPage);
				query.addFacetField("category");
				query.addFilterQuery(attrFtr);
				if(customerPartIdList!=null && !customerPartIdList.trim().equalsIgnoreCase(""))
				{
				    query.addFilterQuery("-itemid:("+customerPartIdList+")");
				    
				}
				System.out.println("1 : " + solrSearchVal.getSolrQuery());
				QueryResponse response = server.query(query,METHOD.POST);
				
				int resultCount = (int) response.getResults().getNumFound();
				List<FacetField> facetFeild = response.getFacetFields();

					c = "";
					facetFeild = response.getFacetFields();
					
					int prevCount = 0;
					if(itemLevelFilterData.size()>0)
					{
						prevCount = itemLevelFilterData.get(0).getResultCount();
						resultCount = resultCount + prevCount;
						itemLevelFilterData.get(0).setResultCount(resultCount);
					}
					
					if(resultCount>0 && resultPerPage==0)
					{
						ProductsModel itemModel = new ProductsModel();
						itemModel.setResultCount(resultCount);
						itemLevelFilterData.add(itemModel);
					}
					
					if(itemLevelFilterData.size()>0)
					{
						for(FacetField facetFilter:facetFeild)
						{
							
							if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
							{
						
								List<Count> attrValArr = facetFilter.getValues();
								for(Count attrArr : attrValArr)
								{
									categGroup = categGroup + c + ZeroInSearch.escapeQueryCulprits(attrArr.getName());
									c = " OR ";
								}
								
							}
							
						}
					}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		
		return categGroup;
	}

	public static ProductsModel zeroInsolrSearchResult(String narrowKeyword, String queryString,String connUrl,String[] attrFtr,int fromRow,int resultPerPage,String innerUrl,String fields[],String origKeyWord,String sortField,ORDER sortOrder, boolean includePartNumberSearch,boolean isAdvancedSearch, ArrayList<ProductsModel> itemLevelFilterData, String customerPartIdList)
	{
		HttpSolrServer server = null;
		ProductsModel solrSearchVal = new ProductsModel();
		try
		{
			server = ConnectionManager.getSolrClientConnection(connUrl);
			SolrQuery query = new SolrQuery();
			SolrQuery queryNew = new SolrQuery();
			String idList = "";
			String c = "";
			String[] narrowrAttrFtr = attrFtr;
				
				query.setQuery(queryString);
				query.addSortField("goldenitem", SolrQuery.ORDER.desc );
				query.addSortField(sortField, sortOrder);
				
				query.set("defType", "edismax");
				
				if(isAdvancedSearch)
				{
					query.set("qf", "partkeywords");
					query.set("pf", "partkeywords^50");
				}
				else
				{
					query.set("qf", "partkeywords keywords");
					query.set("pf", "partkeywords^100 keywords^50");
				}
				
				query.set("ps", "0");
				
				query.set("spellcheck", "true");
				 query.set("spellcheck.extendedResults", "true");
				 query.set("spellcheck.collate", "true");
				 query.set("spellcheck.q", origKeyWord);
				 if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				 {
				 if(attrFtr!=null && attrFtr.length>0)
					 attrFtr[attrFtr.length-1] = null;
				 }
				
				//fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
			
				query.setStart(fromRow);
				query.setRows(resultPerPage);
				
				
				
				query.addFilterQuery(attrFtr);
				if(customerPartIdList!=null && !customerPartIdList.trim().equalsIgnoreCase(""))
				{
				    query.addFilterQuery("-itemid:("+customerPartIdList+")");
				    queryNew.addFilterQuery("-itemid:("+customerPartIdList+")");
				}

				
				
				queryNew.setQuery(queryString);
				queryNew.addSortField("goldenitem", SolrQuery.ORDER.desc );
				queryNew.addSortField(sortField, sortOrder);
				
				queryNew.set("defType", "edismax");
				
				if(isAdvancedSearch)
				{
					queryNew.set("qf", "partkeywords");
					queryNew.set("pf", "partkeywords^50");
				}
				else
				{
					queryNew.set("qf", "partkeywords keywords");
					queryNew.set("pf", "partkeywords^100 keywords^50");
				}
				
				queryNew.set("ps", "0");
				
				queryNew.set("spellcheck", "true");
				 queryNew.set("spellcheck.extendedResults", "true");
				 queryNew.set("spellcheck.collate", "true");
				 queryNew.set("spellcheck.q", origKeyWord);
				 queryNew.setStart(fromRow);
				queryNew.setRows(resultPerPage);
				
				
				
				queryNew.addFilterQuery(attrFtr);
				
				System.out.println("query : " + query);
				SolrQuery attributeQuery = queryNew;
				attributeQuery.set("wt", "json");
				attributeQuery.set("json.nl","map");
				solrSearchVal.setSolrQuery(attributeQuery);
				System.out.println("1 : " + solrSearchVal.getSolrQuery());
				solrSearchVal.setQueryString(attributeQuery.toString());
				
				System.out.println("1 : " + solrSearchVal.getSolrQuery());
				if(fields!=null && fields.length>0)
				{
					query.addFacetField(fields);
					query.setFacetLimit(-1);
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ATTRIBUTE_VALUE_SORT_BY")).equalsIgnoreCase("COUNT")){
						query.setFacetSort(true);
					}else{
						query.setFacetSort(false);
					}
				}
				System.out.println("1 : " + solrSearchVal.getSolrQuery());
				QueryResponse response = server.query(query,METHOD.POST);
				
				SolrDocumentList documents = response.getResults();
				int resultCount = (int) response.getResults().getNumFound();
				List<FacetField> facetFeild = response.getFacetFields();
				LinkedHashMap<String, List<Count>> attributeList = new LinkedHashMap<String, List<Count>>();
				
				
				
				if(resultCount>0 && narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				{
					String narrowKeywordOrg = narrowKeyword;
					boolean narrowOrSearch = true;
					 narrowKeyword = narrowKeyword.replaceAll("\\s+", " AND ");
					
					String pattern = "[^A-Za-z0-9]";
					narrowKeyword = narrowKeyword.toUpperCase();
					String scrubbedKeyword = narrowKeyword.replaceAll(pattern,"");
				
					 narrowKeyword = narrowKeyword.trim();
					
					 ArrayList<String> buildNarrowKeyword = ProductHunterSolrUltimate.buildNarrowKeyword(narrowKeyword, scrubbedKeyword);
					 
					 
					 for(String narrowQuery: buildNarrowKeyword)
					 {
						 solrSearchVal = ProductHunterSolrUltimate.solrNarrowSearchResult(narrowQuery, queryString, connUrl, narrowrAttrFtr, fromRow, resultPerPage, innerUrl, fields, origKeyWord, sortField, sortOrder,includePartNumberSearch,false);
							if(solrSearchVal.getItemDataList().size()>0)
							{
								narrowOrSearch = false;
								break;
							}
					 }
					 
					 
					 if(narrowOrSearch)
					 {
						 narrowKeyword = narrowKeywordOrg.toUpperCase();
					  		scrubbedKeyword = narrowKeyword.replaceAll(pattern,"");
						
						 narrowKeyword = narrowKeyword.trim();
			
						
						buildNarrowKeyword = ProductHunterSolrUltimate.buildNarrowKeyword(narrowKeyword, scrubbedKeyword);
						 
						 
						 for(String narrowQuery: buildNarrowKeyword)
						 {
							 solrSearchVal = ProductHunterSolrUltimate.solrNarrowSearchResult(narrowQuery, queryString, connUrl, narrowrAttrFtr, fromRow, resultPerPage, innerUrl, fields, origKeyWord, sortField, sortOrder,includePartNumberSearch,false);
								if(solrSearchVal.getItemDataList().size()>0)
								{
									break;
								}
						 }
					 }
					 
					
				}
				
				else
				{
					SpellCheckResponse spellCheckRes = response.getSpellCheckResponse();
					if(spellCheckRes!=null){
						System.out.println(spellCheckRes.isCorrectlySpelled());
						solrSearchVal.setSuggestedValue(spellCheckRes.getCollatedResult());
						System.out.println("DOCUMENTS Suggestion : " + spellCheckRes.getCollatedResult());
						
						if(!spellCheckRes.getSuggestions().isEmpty()){
							Iterator<Suggestion> suggestionArray = spellCheckRes.getSuggestions().iterator();
							if(suggestionArray!=null){
								List<String> suggestionList = suggestionArray.next().getAlternatives();
								solrSearchVal.setSuggestedValueList(new ArrayList<String>(suggestionList));
							}
							
						}
					}
					Iterator<SolrDocument> itr = documents.iterator();
					System.out.println("DOCUMENTS");
					idList = "";
					c = "";
					facetFeild = response.getFacetFields();
					attributeList = new LinkedHashMap<String, List<Count>>();

					LinkedHashMap<String, ArrayList<ProductsModel>> filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
					ArrayList<ProductsModel>  attrList = new ArrayList<ProductsModel>();
					
					ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
					
					System.out.println("DOCUMENTS");
					
					//itemLevelFilterData = new ArrayList<ProductsModel>();
					
					boolean appendResult = false;
					int prevCount = 0;
					if(itemLevelFilterData.size()>0)
					{
						prevCount = itemLevelFilterData.get(0).getResultCount();
						resultCount = resultCount + prevCount;
						itemLevelFilterData.get(0).setResultCount(resultCount);
					}
					
					if(resultCount>0 && resultPerPage==0)
					{
						ProductsModel itemModel = new ProductsModel();
						itemModel.setResultCount(resultCount);
						itemLevelFilterData.add(itemModel);
					}
					while (itr.hasNext()) {
						
						if(resultPerPage>1)
						{
						
							try{
							
							ProductsModel itemModel = new ProductsModel();
							SolrDocument doc = itr.next();
							idList = idList + c + doc.getFieldValue("itemid").toString();
							
							itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
							itemModel.setMinOrderQty(1);
							itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
							//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
							itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
							itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
							itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
							
							itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
							
								
							itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
							itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
							itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
							itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
							itemModel.setResultCount(resultCount);
							String imageName = null;
			       			String imageType = null;
			       			
			       			String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
			       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
			       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
			       			}
			       			
			       			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
			       			String pattern = "[^A-Za-z0-9]";
			   			 itemUrl = itemUrl.replaceAll(pattern," ");
			   			 itemUrl = itemUrl.replaceAll("\\s+","-");
			            	itemModel.setItemUrl(itemUrl);
			       			
			       			if(doc.getFieldValue("imageName")!=null)
			       				imageName = doc.getFieldValue("imageName").toString();
			       			
			       					       			
			       			if(doc.getFieldValue("imageType")!=null)
			       				imageType = doc.getFieldValue("imageType").toString();
			       			if(doc.getFieldValue("upc")!=null)
			       				itemModel.setUpc(doc.getFieldValue("upc").toString());	
			       			
			       			if(imageName==null)
			       			{
			       				imageName = "NoImage.png";
			       				imageType = "IMAGE";
			       			}
			       			itemModel.setImageName(imageName.trim());
			       			itemModel.setImageType(imageType);
							c = " OR ";
							itemLevelFilterData.add(itemModel);
							
							}catch (Exception e) {
								System.out.println("Error Occured While retriving data from solr.");
								e.printStackTrace();
							}	
						}
						else if(!appendResult && itemLevelFilterData.size()>0)
						{
							itemLevelFilterData.get(0).setResultCount(resultCount);
							appendResult = true;
						}
						
						
					}
					if(itemLevelFilterData.size()>0 && resultPerPage > 0)
					{
						for(FacetField facetFilter:facetFeild)
						{
							ProductsModel tempVal = new ProductsModel();
							
							if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
							{
								filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
								attrList = new ArrayList<ProductsModel>();
						
								List<Count> attrValArr = facetFilter.getValues();
								for(Count attrArr : attrValArr)
								{
									ProductsModel attrListVal = new ProductsModel();
									attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
									attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
									attrListVal.setAttrValue(attrArr.getName());
									attrListVal.setResultCount((int) attrArr.getCount());
									attrList.add(attrListVal);
									
								}
								
								filteredList.put(facetFilter.getName().replace("attr_", ""), attrList);
								tempVal.setAttrFilterList(filteredList);
								attrFilteredList.add(tempVal);
								attributeList.put(facetFilter.getName().replace("attr_", ""), facetFilter.getValues());
							}
							
						}
					}
					solrSearchVal.setItemDataList(itemLevelFilterData);
					solrSearchVal.setAttributeDataList(attrFilteredList);
					solrSearchVal.setIdList(idList);
					
					System.out.println("At the end : " + solrSearchVal.getSuggestedValue());
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return solrSearchVal;
	}
	
	public static String  getFaucetFieldFull(String connUrl,String categoryName,String queryString,String innerUrl,int treeId, ArrayList<ProductsModel> refinedList,boolean isCategory,boolean isBrand,boolean isSubCategory,String indexType)
	{
		HttpSolrServer server1 = null;
		String idList = "";
		try
		{
				String c = "";
				String fq = "type:"+indexType;
		  		server1 =  ConnectionManager.getSolrClientConnection(connUrl);
		  		SolrQuery query = new SolrQuery();
				query = new SolrQuery();
				query.setQuery("category:("+categoryName+")");
				query.addFilterQuery(fq);
				query.setStart(0);
				query.setRows(100);
				System.out.println("Category Query : " + query);
				QueryResponse response = server1.query(query);
				
				SolrDocumentList documents = response.getResults();
				Iterator<SolrDocument> itr = documents.iterator();
				System.out.println("DOCUMENTS");
		
				while (itr.hasNext()) {
					SolrDocument doc = itr.next();
					idList = idList + c + doc.getFieldValue("categoryID").toString();
					c = " OR ";
				}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server1);
		}
		
		return idList;
	}
}
