package com.erp.service.rest.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;
import javax.ws.rs.HttpMethod;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unilog.database.CommonDBQuery;
import com.unilog.exception.RestServiceException;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.RetryPolicy;

public class HttpConnectionUtility {
	static final Logger logger = Logger.getLogger(HttpConnectionUtility.class);
	private static HttpConnectionUtility httpConnectionUtility = null;
	private Gson gson;
	private PoolingHttpClientConnectionManager connectionManager;
	private ConnectionKeepAliveStrategy connectionKeepAliveStrategy;
	private LaxRedirectStrategy laxRedirectStrategy;
	private RequestConfig requestConfig;

	public static HttpConnectionUtility getInstance() throws Exception {
		synchronized (HttpConnectionUtility.class) {
			if (httpConnectionUtility == null) {
				httpConnectionUtility = new HttpConnectionUtility();
			}
		}
		return httpConnectionUtility;
	}

	private HttpConnectionUtility() throws Exception {
		gson = new GsonBuilder().disableHtmlEscaping().create();
		initialize();
	}

	private void initialize() throws Exception {
		requestConfig = RequestConfig.custom().setConnectTimeout(getConnectionTimeOut())
				.setSocketTimeout(this.getConnectionSocketTimeOut())
				.setConnectionRequestTimeout(getConnectionRequestTimeOut()).build();
		this.createPoolingConnectionManager();
		this.createConnectionKeepAliveStrategy();
		this.laxRedirectStrategy = new LaxRedirectStrategy();
		scheduleConnectionMonitor();
	}

	private CloseableHttpClient createHttpClient() {

		return HttpClients.custom().setConnectionManager(this.connectionManager)
				.setRedirectStrategy(this.laxRedirectStrategy).setKeepAliveStrategy(this.connectionKeepAliveStrategy)
				.setDefaultRequestConfig(requestConfig).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
				.setConnectionManagerShared(true).build();
	}

	private int getConnectionTimeOut() {
		int timeout = 6000;
		if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2BC_CONN_TIMEOUT"))
				.length() > 0
				&& CommonUtility
						.validateNumber(CommonDBQuery.getSystemParamtersList().get("CIMM2BC_CONN_TIMEOUT")) > 0) {
			timeout = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("CIMM2BC_CONN_TIMEOUT"));
		}
		return timeout;
	}

	private int getConnectionRequestTimeOut() {
		int timeout = 18000;
		if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2BC_CONN_REQUEST_TIMEOUT"))
				.length() > 0
				&& CommonUtility.validateNumber(
						CommonDBQuery.getSystemParamtersList().get("CIMM2BC_CONN_REQUEST_TIMEOUT")) > 0) {
			timeout = CommonUtility
					.validateNumber(CommonDBQuery.getSystemParamtersList().get("CIMM2BC_CONN_REQUEST_TIMEOUT"));
		}
		return timeout;
	}

	private int getConnectionSocketTimeOut() {
		int timeout = 180000;
		if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2BC_CONN_READ_TIMEOUT"))
				.length() > 0
				&& CommonUtility
						.validateNumber(CommonDBQuery.getSystemParamtersList().get("CIMM2BC_CONN_READ_TIMEOUT")) > 0) {
			timeout = CommonUtility
					.validateNumber(CommonDBQuery.getSystemParamtersList().get("CIMM2BC_CONN_READ_TIMEOUT"));
		}
		return timeout;
	}

	private int getMaxConnection() {
		int maxConnection = 1000;
		if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("POOLING_MAX_TOTAL")).length() > 0
				&& CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("POOLING_MAX_TOTAL")) > 0) {
			maxConnection = CommonUtility
					.validateNumber(CommonDBQuery.getSystemParamtersList().get("POOLING_MAX_TOTAL"));
		}
		return maxConnection;
	}

	private int getMaxConnectionPerRoute() {
		int maxPerRoute = 100;
		if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("POOLING_DEFAULT_MAX_ROUTE"))
				.length() > 0
				&& CommonUtility
						.validateNumber(CommonDBQuery.getSystemParamtersList().get("POOLING_DEFAULT_MAX_ROUTE")) > 0) {
			maxPerRoute = CommonUtility
					.validateNumber(CommonDBQuery.getSystemParamtersList().get("POOLING_DEFAULT_MAX_ROUTE"));
		}
		return maxPerRoute;
	}

	private void scheduleConnectionMonitor() {
		Runnable idleConnectionMonitorTask = idleConnectionMonitor();
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(idleConnectionMonitorTask, 3, 60, TimeUnit.SECONDS);
	}

	@PreDestroy
	private void cleanUp() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		connectionManager.shutdown();
	}

	private void createPoolingConnectionManager() throws Exception {
		Registry<ConnectionSocketFactory> socketFactoryRegistry = null;
		SSLConnectionSocketFactory trustSelfSignedSocketFactory = new SSLConnectionSocketFactory(
				new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
				new NoopHostnameVerifier());
		socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", new PlainConnectionSocketFactory()).register("https", trustSelfSignedSocketFactory)
				.build();
		connectionManager = (socketFactoryRegistry != null)
				? new PoolingHttpClientConnectionManager(socketFactoryRegistry)
				: new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(getMaxConnection());
		connectionManager.setDefaultMaxPerRoute(getMaxConnectionPerRoute());
		connectionManager
				.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(this.getConnectionSocketTimeOut()).build());
	}

	private void createConnectionKeepAliveStrategy() {
		connectionKeepAliveStrategy = (HttpResponse response, HttpContext context) -> {
			String value = "1";
			HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
			while (it.hasNext()) {
				HeaderElement he = it.nextElement();
				String param = he.getName();
				value = he.getValue();

				if (value != null && param.equalsIgnoreCase("timeout")) {
					return Long.parseLong(value) * 1000;
				}
			}
			return CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("CONNECTION_KEEP_ALIVE"));

		};
	}

	private Runnable idleConnectionMonitor() {
		return () -> {
			try {
				if (connectionManager != null) {
					System.out.println("run IdleConnectionMonitor - Closing expired and idle connections...");
					connectionManager.closeExpiredConnections();
					System.out.println("Closing expired connections....");
					connectionManager.closeIdleConnections(6000, TimeUnit.SECONDS);
					System.out.println("Closing idle connections....");
				} else {
					System.out.println("run IdleConnectionMonitor - Http Client Connection manager is not initialised");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		};
	}

	public String getApiResponse(String requestUrl, String httpMethod, List<NameValuePair> headerList,
			HttpEntity httpEntity) throws IOException, RestServiceException {

		HttpRequestBase requestBase = null;
		CloseableHttpResponse response = null;
		String result = "";
		HttpResult httpResult = null;
		Header[] headers = this.getHeaders(headerList);

		if (httpMethod == HttpMethod.GET) {
			requestBase = new HttpGet(requestUrl);
		} else if (httpMethod == HttpMethod.POST) {
			requestBase = new HttpPost(requestUrl);
			((HttpPost) requestBase).setEntity(httpEntity);
		} else if (httpMethod == HttpMethod.PUT) {
			requestBase = new HttpPut(requestUrl);
			((HttpPut) requestBase).setEntity(httpEntity);
		} else if(httpMethod.equals(HttpMethod.DELETE)){
			requestBase = new HttpDeleteWithBody(requestUrl);
			((HttpDeleteWithBody) requestBase).setEntity(httpEntity); 
		}
		boolean isRetry = RetryPolicy.getInstance().checkRetryPolicy(requestUrl);
		if (requestBase != null) {
			requestBase.setHeaders(headers);
		}
		int retryCount = CommonUtility
				.validateNumber((CommonDBQuery.getSystemParamtersList().get("CIMM2BC_RETRY_ATTEMPTS")));
		if (retryCount > 0 && isRetry) {
			retryCount++;
		} else {
			retryCount = 0;
		}
		if (requestBase != null) {
			requestBase.addHeader("Connection", "close");
		}
		do {
			httpResult = execute(requestBase);
			if (httpResult.getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
				if (retryCount > 0)
					System.out.println("Retrying....");
			} else {
				retryCount = 0;
			}
			retryCount--;
		} while (retryCount > 0);
		if (httpResult != null) {
			result = httpResult.getResponse();
			if (httpResult.getException() != null) {
				throw new RestServiceException(httpResult.getException().getMessage());
			}
		}
		return result;
	}

	private HttpResult execute(HttpRequestBase requestBase) {
		HttpResult httpResult = new HttpResult();

		try (CloseableHttpClient httpClient = this.createHttpClient();
				CloseableHttpResponse response = httpClient.execute(requestBase);) {
			String result = EntityUtils.toString(response.getEntity()).trim();
			int responseCode = response.getStatusLine().getStatusCode();
			httpResult.setStatusCode(responseCode);
			httpResult.setResponse(result);
			EntityUtils.consume(response.getEntity());
			System.out.println("Response Code : " + responseCode);
			System.out.println("Response for '" + requestBase.getURI().toString() + "' --- " + result);
		} catch (Exception e) {
			httpResult.setException(e);
			httpResult.setStatusCode(HttpStatus.SC_BAD_REQUEST);
		} finally {
			requestBase.releaseConnection();
		}
		return httpResult;
	}

	public static class HttpResult {
		private String response;
		private int statusCode;
		private Exception exception;

		public String getResponse() {
			return response;
		}

		public void setResponse(String response) {
			this.response = response;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}

		public Exception getException() {
			return exception;
		}

		public void setException(Exception exception) {
			this.exception = exception;
		}

	}

	private Header[] getHeaders(List<NameValuePair> headerList) {

		Header[] headers = null;

		/*
		 * headerList.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION,
		 * CommonDBQuery.getCimm2bCentralAuthorization())); headerList.add(new
		 * BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		 * headerList.add(new BasicNameValuePair(HttpConnectionUtility.SITE_ID,
		 * Cimm2BCentralClient.getInstance().SITE_ID)); headerList.add(new
		 * BasicNameValuePair(HttpConnectionUtility.CLIENT_ID,
		 * Cimm2BCentralClient.getInstance().CLIENT_ID));
		 */

		if (headerList != null && headerList.size() > 0) {
			headers = new Header[headerList.size()];
			for (int i = 0; i < headerList.size(); i++) {
				headers[i] = new BasicHeader(headerList.get(i).getName(), headerList.get(i).getValue());
				if (!headerList.get(i).getName().equalsIgnoreCase("CustomerPassword")) {
					System.out.println(
							"Name - " + headerList.get(i).getName() + " Value - " + headerList.get(i).getValue());
				}
			}
		}
		return headers;
	}
}
