package com.unilog.geolocator.service.yahoo;

import com.google.gson.Gson;
import com.unilog.geolocator.GeoLocatorRequest;
import com.unilog.geolocator.GeoLocatorResponse;
import com.unilog.geolocator.IGeoLocatorService;

import oauth.signpost.OAuthConsumer;

public class YahooGeoLocatorServiceImpl extends AbstractYahooServiceImpl implements IGeoLocatorService {

	private Gson Gson;
	private static YahooGeoLocatorServiceImpl yahooGeoLocatorServiceImpl;
	private OAuthConsumer consumer = null;

	public OAuthConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(OAuthConsumer consumer) {
		this.consumer = consumer;
	}

	@Override
	public GeoLocatorResponse locateUser(GeoLocatorRequest geoLocatorRequest) {/*
		GeoLocatorResponse geoLocatorResponse = new GeoLocatorResponse();
		String params = getCALL_TYPE();
		if(CommonUtility.validateString(geoLocatorRequest.getSearchString()).length()>0){
			params = params.concat("?postal=");
			params = params.concat(URLEncoder.encode(geoLocatorRequest.getSearchString(), StandardCharsets.UTF_8.name()));
		}

		if(params!=null){
			//String url = yahooServer + params;
			OAuthConsumer consumer = new DefaultOAuthConsumer(GEO_API_KEY, GEO_API_SECRET);
			setConsumer(consumer);

				try {
					HttpURLConnection uc = getConnection(getYahooApiUrl(params));
					int responseCode = uc.getResponseCode();
					if (responseCode == HttpStatus.SC_OK) {
						BufferedReader rd = new BufferedReader(new InputStreamReader(responseCode == 200 ? uc.getInputStream() : uc.getErrorStream()));
						StringBuffer sb = new StringBuffer();
						String line;
						while ((line = rd.readLine()) != null) {
							sb.append(line);
						}
						setResponseBody(sb.toString());
						try {
							DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
							DocumentBuilder builder = factory.newDocumentBuilder();
							InputSource is = new InputSource(new StringReader(sb.toString()));
							Document doc = builder.parse(is);
							NodeList nodeList = doc.getElementsByTagName("latitude");
							Node node = nodeList.item(0);
							NodeList fstNm = node.getChildNodes();
							YahooBossSupport.setLatitude((fstNm.item(0)).getNodeValue());
							locaDetail.setLatitude((fstNm.item(0)).getNodeValue());
							nodeList = doc.getElementsByTagName("longitude");
							node = nodeList.item(0);
							fstNm = node.getChildNodes();
							YahooBossSupport.setLongitude((fstNm.item(0)).getNodeValue());
							locaDetail.setLongitude((fstNm.item(0)).getNodeValue());
							nodeList = doc.getElementsByTagName("city");
							node = nodeList.item(0);
							fstNm = node.getChildNodes();
							locaDetail.setCity((fstNm.item(0)).getNodeValue());
							// YahooBossSupport.setCity((fstNm.item(0)).getNodeValue());
							nodeList = doc.getElementsByTagName("country");
							node = nodeList.item(0);
							fstNm = node.getChildNodes();
							locaDetail.setCountry((fstNm.item(0)).getNodeValue());
							// YahooBossSupport.setCounty((fstNm.item(0)).getNodeValue());
							nodeList = doc.getElementsByTagName("state");
							node = nodeList.item(0);
							fstNm = node.getChildNodes();
							locaDetail.setState((fstNm.item(0)).getNodeValue());
							// YahooBossSupport.setState((fstNm.item(0)).getNodeValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						rd.close();
					}
				} catch (MalformedURLException ex) {
					throw new IOException(locaDetail.getElementSetupUrl() + " is not valid");
				} catch (IOException ie) {
					throw new IOException("IO Exception " + ie.getMessage());
				}
				return locaDetail;
			
				if(locationDetail.getResultCount() == HTTP_STATUS_OK) {
					System.out.println("Response ");
				} else {
					System.out.println("Error in response due to status code = " + locationDetail.getResultCount());
				}
				System.out.println(httpRequest.getResponseBody());

			} catch(UnsupportedEncodingException e) {
				System.out.println("Encoding/Decording error");
			} catch (IOException e) {
				System.out.println("Error with HTTP IO");
			} catch (Exception e) {
				System.out.printf(httpRequest.getResponseBody(), e);
				return null;
			}
		*/

		return null;
	}

	public static YahooGeoLocatorServiceImpl getInstance() {
		synchronized(YahooGeoLocatorServiceImpl.class) {
			if(yahooGeoLocatorServiceImpl == null) {
				yahooGeoLocatorServiceImpl = new YahooGeoLocatorServiceImpl();
			}
			yahooGeoLocatorServiceImpl.init();
		}
		return yahooGeoLocatorServiceImpl;
	}

	protected String getYahooApiUrl(String param) {

		return GEO_API_URL + param;
	}


	/*public HttpURLConnection getConnection(String url) throws IOException, OAuthMessageSignerException,
	OAuthExpectationFailedException, OAuthCommunicationException {
		try {
			URL u = new URL(url);

			HttpURLConnection uc = (HttpURLConnection) u.openConnection();

			if (consumer != null) {
				try {
					log.info("Signing the oAuth consumer");
					consumer.sign(uc);

				} catch (OAuthMessageSignerException e) {
					log.error("Error signing the consumer", e);
					throw e;

				} catch (OAuthExpectationFailedException e) {
					log.error("Error signing the consumer", e);
					throw e;

				} catch (OAuthCommunicationException e) {
					log.error("Error signing the consumer", e);
					throw e;
				}
				uc.connect();
			}
			return uc;
		} catch (IOException e) {
			log.error("Error signing the consumer", e);
			throw e;
		}
	}*/

}
