package com.unilog.promotion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.utility.CommonUtility;

public class PromotionUtility
{
  private static PromotionUtility promotionUtility = null;

  public static void setPromotionUtility(PromotionUtility promotionUtility) {
    promotionUtility = promotionUtility;
  }

  public static PromotionUtility getPromotionUtility() {
    return promotionUtility;
  }

  public static PromotionUtility getInstance()
  {
    synchronized (PromotionUtility.class) {
      if (promotionUtility == null) {
        promotionUtility = new PromotionUtility();
      }
    }
    return promotionUtility;
  }

  public PromotionResponse getPromotionByKeyword(String keyWord,String subsetId,String taxonomyId)
  {
    PromotionResponse response = new PromotionResponse();
    try {
      String request = buildRequest(keyWord,subsetId,taxonomyId);
      if (request != null)
        response = promotionService(request);
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return response;
  }

  public PromotionResponse promotionService(String requestString)
  {
    PromotionResponse output = new PromotionResponse();
    try {
      //String jiraUser = "cimm2bclient";
      //String jiraPass = "@c!mm@bcl!ent";
      String jiraUser = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2BCENTRAL_USERNAME"));
	  String jiraPass = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2BCENTRAL_PASSWORD"));
      String authString = CommonUtility.validateString(jiraUser + ":" + jiraPass);
      String connectionUrl = CommonUtility.validateString((String)CommonDBQuery.getSystemParamtersList().get("CIMM2BCENTRAL_ACCESS_URL"));
      String requestUrl = CommonUtility.validateString(connectionUrl +CommonUtility.validateString((String)CommonDBQuery.getSystemParamtersList().get("CIMM2BCENTRAL_SEARCH_PROMOTION")));// "/cimm2bcentral/admin/promotion/findAll");
      Gson gson = new Gson();
      System.out.println(requestString);

      byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
      String authStringEnc = new String(authEncBytes);
      HttpPost postRequest = null;
      HttpClient httpClient = HttpClientBuilder.create().build();
      StringEntity entity = new StringEntity(requestString, "UTF-8");
      postRequest = new HttpPost(requestUrl);
      postRequest.addHeader("accept", "application/json");
      postRequest.addHeader("Content-type", "application/json");
      postRequest.addHeader("Authorization", "Basic " + authStringEnc);
      entity = new StringEntity(requestString, "UTF-8");
      postRequest.setEntity(entity);

      StringBuffer responseData = new StringBuffer();
      HttpResponse response = httpClient.execute(postRequest);
      BufferedReader responseReader = null;
      String line = null;
      if (response != null) {
    	InputStreamReader inStreamReader = new InputStreamReader(response.getEntity().getContent());
        responseReader = new BufferedReader(inStreamReader);
        while ((line = responseReader.readLine()) != null) {
          responseData.append(line);
        }
        System.out.println("CIMM2BC responseData: "+responseData.toString());
        output = (PromotionResponse)gson.fromJson(responseData.toString(), PromotionResponse.class);
        System.out.println(gson.toJson(output));
        System.out.println(responseData.toString());
        ConnectionManager.closeInputStreamReader(inStreamReader);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return output;
  }

  public String buildRequest(String keyWord,String subsetId,String taxonomyId)
  {
    String promoRequest = null;
    try
    {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'UTC'");
      format.setTimeZone(TimeZone.getTimeZone("GMT"));
      Date date = new Date();
      String bannerDate = format.format(date);
      Gson gson = new Gson();
      PromotionRequest pro = new PromotionRequest();
      pro.setPage(1);
      pro.setPageSize(1);
      ArrayList<Criteria> criteria = new ArrayList<Criteria>();
      Criteria c = new Criteria();
      c.setName("keywords");
      c.setValue(keyWord);
      c.setClause("CONTAINS_IGNORE_CASE");
      c.setApplyAnd(true);
      criteria.add(c);

      c = new Criteria();
      c.setName("taxonomyId");
      c.setValue(taxonomyId);
      c.setClause("EQUAL");
      c.setApplyAnd(true);
      criteria.add(c);
      
      c = new Criteria();
      c.setName("subsetId");
      c.setValue(subsetId);
      c.setClause("EQUAL");
      c.setApplyAnd(true);
      criteria.add(c);
      
      
      c = new Criteria();
      c.setName("startDate");
      c.setValue(bannerDate);
      c.setClause("LESS_THAN_EQUAL");
      c.setApplyAnd(true);
      criteria.add(c);

      c = new Criteria();
      c.setName("endDate");
      c.setValue(bannerDate);
      c.setClause("GREATER_THAN_EQUAL");
      c.setApplyAnd(true);
      criteria.add(c);

      ArrayList<Sort> sortList = new ArrayList<Sort>();
      Sort sort = new Sort();
      sort.setName("name");
      sort.setIgnoreCase(true);
      sort.setAscending(true);
      sortList.add(sort);

      pro.setCriteria(criteria);
      pro.setSort(sortList);
      promoRequest = gson.toJson(pro);
    } catch (Exception e) {
      promoRequest = null;
      e.printStackTrace();
    }
    return promoRequest;
  }
}