package com.unilog.api.bronto;

import static com.bronto.api.model.ObjectBuilder.newObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.bronto.api.BrontoApi;
import com.bronto.api.BrontoClient;
import com.bronto.api.ObjectOperations;
import com.bronto.api.model.ContactField;
import com.bronto.api.model.ContactObject;
import com.bronto.api.model.ContactStatus;
import com.bronto.api.model.DeliveryObject;
import com.bronto.api.model.DeliveryRecipientObject;
import com.bronto.api.model.DeliveryRecipientSelection;
import com.bronto.api.model.DeliveryRecipientType;
import com.bronto.api.model.DeliveryType;
import com.bronto.api.model.FieldObject;
import com.bronto.api.model.MailListObject;
import com.bronto.api.model.MessageFieldObject;
import com.bronto.api.model.MessageObject;
import com.bronto.api.model.OrderObject;
import com.bronto.api.model.ProductObject;
import com.bronto.api.model.ResultItem;
import com.bronto.api.model.WriteResult;
import com.bronto.api.operation.ContactOperations;
import com.bronto.api.operation.MailListOperations;
import com.bronto.api.operation.OrderOperations;
import com.bronto.api.request.ContactReadRequest;
import com.bronto.api.request.FieldReadRequest;
import com.bronto.api.request.MailListReadRequest;
import com.bronto.api.request.MessageReadRequest;
import com.erp.service.cimm2bcentral.utilities.ProductsUtility;
import com.erp.service.model.ProductManagementModel;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.SendMailModel;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

import WS.Nxtrend.OEPricingMultipleV3InputProduct;
import WS.Nxtrend.OEPricingMultipleV3Response;
public class BrontoUtility {


	public final static int ABANDONED_CART = 0;
	public final static int REGISTRATION = 1;
	public final static int EMAIL_SIGNUP = 2;
	public final static int SEND_ORDER_DETAILS_TO_CONTACT = 3;

	//private static BrontoApi client = null;
	private static BrontoUtility brontoContactField=null;
	private boolean reInitializeClient = true;

	public static BrontoUtility getInstance() {
		synchronized(BrontoUtility.class) {
			if(brontoContactField==null) {
				brontoContactField = new BrontoUtility();				
			}
		}
		return brontoContactField;
	}
	private void initializeClient() {/*
		FIRST_NAME = "firstname";
		LAST_NAME = "lastname";
		STATE = "state";
		ADDRESS = "address";
		CITY = "city";
		ZIP_CODE = "zipcode";
		SX_CUSTOMER_NUMBER = "sxcustomernumber";
		HOME_PHONE = "homephone";
		RECEIVE_EMAIL_OFFERS = "receiveemailoffers";

		FIRST_NAME_LABLE = "First Name";
		LAST_NAME_LABLE = "Last Name";
		STATE_LABLE = "State";
		ADDRESS_LABLE = "Address";
		CITY_LABLE = "City";
		ZIP_CODE_LABLE = "Zipcode";
		SX_CUSTOMER_NUMBER_LABLE = "SX Customer Number";
		HOME_PHONE_LABLE = "Home Phone";
		RECEIVE_EMAIL_OFFERS_LABLE = "Receive Email Offers";
		reInitializeClient = false;
	 */}

	public String createOrUpdateContact(BrontoApi client, final BrontoModel brontoModel, String listName, int reqType){
		String result = "";
		if(reInitializeClient){
			initializeClient();
			reInitializeClient = false;
		}
		brontoModel.setContentGroup(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")));
		brontoModel.setSource("CIMM2");
		final List<ContactField> contactFields = new ArrayList<ContactField>();
		/*BrontoApi client = new BrontoClient(apiToken);
		client.login();*/
		ObjectOperations<FieldObject> fieldOps = client.transport(FieldObject.class);
		FieldReadRequest fieldRead = new FieldReadRequest();
		System.out.println("Reading fields");
		List<FieldObject> fieldObjects = fieldOps.read(fieldRead);


		for (FieldObject field : fieldObjects) {

			System.out.println(String.format("Field %s: %s: %s (%s)", field.getName(), field.getId(), field.getLabel(), field.getType()));
			ContactField contactField = new ContactField();
			contactField.setFieldId(field.getId());
			if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.firstName")) && CommonUtility.validateString(brontoModel.getFirstName()).length() > 0){
				contactField.setContent(brontoModel.getFirstName());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.lastName")) && CommonUtility.validateString(brontoModel.getLastName()).length() > 0){
				contactField.setContent(brontoModel.getLastName());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.userName")) && CommonUtility.validateString(brontoModel.getUserName()).length() > 0){
				contactField.setContent(brontoModel.getUserName());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.phone")) && CommonUtility.validateString(brontoModel.getPhone()).length() > 0){
				contactField.setContent(brontoModel.getPhone());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.commercialDiscount")) && brontoModel.isInterestedInCommercialDiscounts()){
				contactField.setContent(String.valueOf(brontoModel.isInterestedInCommercialDiscounts()));
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.state")) && CommonUtility.validateString(brontoModel.getState()).length() > 0){
				contactField.setContent(brontoModel.getState());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.address1")) && CommonUtility.validateString(brontoModel.getAddress1()).length() > 0){
				contactField.setContent(brontoModel.getAddress1());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.address2")) && CommonUtility.validateString(brontoModel.getAddress2()).length() > 0){
				contactField.setContent(brontoModel.getAddress1());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.city")) && CommonUtility.validateString(brontoModel.getCity()).length() > 0){
				contactField.setContent(brontoModel.getCity());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.zipcode")) && CommonUtility.validateString(brontoModel.getZipcode()).length() > 0){
				contactField.setContent(brontoModel.getZipcode());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.customerNumber")) && brontoModel.getSxCustomerNumber() > 0){
				contactField.setContent(CommonUtility.validateParseIntegerToString(brontoModel.getSxCustomerNumber()));
				contactFields.add(contactField);
			}/*else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.password")) && brontoModel.getPassword().length() > 0){
				contactField.setContent(brontoModel.getPassword());
				contactFields.add(contactField);
			}*/else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.company")) && CommonUtility.validateString(brontoModel.getCompany()).length() > 0){
				contactField.setContent(brontoModel.getCompany());
				contactFields.add(contactField);
			}/*else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.securityQuestion")) && brontoModel.getSecurityQuestion().length() > 0){
				contactField.setContent(brontoModel.getSecurityQuestion());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.securityAnswer")) && brontoModel.getSecurityAnswer().length() > 0){
				contactField.setContent(brontoModel.getSecurityAnswer());
				contactFields.add(contactField);
			}*/else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.email")) && CommonUtility.validateString(brontoModel.getEmail()).length() > 0){
				contactField.setContent(brontoModel.getEmail());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.language")) && CommonUtility.validateString(brontoModel.getLanguage()).length() > 0){
				contactField.setContent(brontoModel.getLanguage());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.country")) && CommonUtility.validateString(brontoModel.getCountry()).length() > 0){
				contactField.setContent(brontoModel.getCountry());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.taxId")) && CommonUtility.validateString(brontoModel.getTaxId()).length() > 0 && brontoModel.isInterestedInCommercialDiscounts()){
				contactField.setContent(brontoModel.getTaxId());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.businessTypeOther")) && CommonUtility.validateString(brontoModel.getBusinessTypeOther()).length() > 0 && brontoModel.isInterestedInCommercialDiscounts()){
				contactField.setContent(brontoModel.getBusinessTypeOther());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.businessType")) && CommonUtility.validateString(brontoModel.getBusinessType()).length() > 0 && brontoModel.isInterestedInCommercialDiscounts()){
				contactField.setContent(brontoModel.getBusinessType());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.fax")) && CommonUtility.validateString(brontoModel.getFax()).length() > 0 && brontoModel.isInterestedInCommercialDiscounts()){
				contactField.setContent(brontoModel.getFax());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.howDidYouHearAboutUs")) && CommonUtility.validateString(brontoModel.getHowDidYouHearAboutUs()).length() > 0 && brontoModel.isInterestedInCommercialDiscounts()){
				contactField.setContent(brontoModel.getHowDidYouHearAboutUs());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.brandsCommonlyPurchased")) && CommonUtility.validateString(brontoModel.getBrandsCommonlyPurchased()).length() > 0 && brontoModel.isInterestedInCommercialDiscounts()){
				contactField.setContent(brontoModel.getBrandsCommonlyPurchased());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.brandsCommonlyPurchasedOthers")) && CommonUtility.validateString(brontoModel.getBrandsCommonlyPurchasedOthers()).length() > 0 && brontoModel.isInterestedInCommercialDiscounts()){
				contactField.setContent(brontoModel.getBrandsCommonlyPurchasedOthers());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.annualPartsPurchase")) && CommonUtility.validateString(brontoModel.getAnnualPartsPurchase()).length() > 0 && brontoModel.isInterestedInCommercialDiscounts()){
				contactField.setContent(brontoModel.getAnnualPartsPurchase());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.recieveEmailOffers")) && brontoModel.isRecieveEmailOffers()){
				contactField.setContent(String.valueOf(brontoModel.isRecieveEmailOffers()));
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.ContentGroup")) && CommonUtility.validateString(brontoModel.getContentGroup()).length() > 0){
				contactField.setContent(brontoModel.getContentGroup());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.Source")) && CommonUtility.validateString(brontoModel.getSource()).length() > 0){
				contactField.setContent(brontoModel.getSource());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.HasBought")) && brontoModel.isHasBought()){
				contactField.setContent("YES");
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.CouponTotalDiscounts")) && CommonUtility.validateString(brontoModel.getCouponTotalDiscounts()).length() > 0){
				contactField.setContent(brontoModel.getCouponTotalDiscounts());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.OrderFirstDate")) && brontoModel.getOrderFirstDate() != null){
				contactField.setContent(brontoModel.getOrderFirstDate());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.OrderMostRecentShipDate")) && CommonUtility.validateString(brontoModel.getOrderMostRecentShipDate()).length() > 0){
				contactField.setContent(brontoModel.getOrderMostRecentShipDate());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.OrderLastDate")) && brontoModel.getOrderLastDate() != null){
				contactField.setContent(brontoModel.getOrderLastDate());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.OrderTotalNumber")) && CommonUtility.validateString(brontoModel.getOrderTotalNumber()).length() > 0){
				contactField.setContent(brontoModel.getOrderTotalNumber());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.CouponFirstCode")) && CommonUtility.validateString(brontoModel.getCouponFirstCode()).length() > 0){
				contactField.setContent(brontoModel.getCouponFirstCode());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.CouponTotalNumber")) && CommonUtility.validateString(brontoModel.getCouponTotalNumber()).length() > 0){
				contactField.setContent(brontoModel.getCouponTotalNumber());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.OrderAverageTotal")) && CommonUtility.validateString(brontoModel.getOrderAverageTotal()).length() > 0){
				contactField.setContent(brontoModel.getOrderAverageTotal());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.OrderLastTotal")) && CommonUtility.validateString(brontoModel.getOrderLastTotal()).length() > 0){
				contactField.setContent(brontoModel.getOrderLastTotal());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.OrderTotalRevenue")) && CommonUtility.validateString(brontoModel.getOrderTotalRevenue()).length() > 0){
				contactField.setContent(brontoModel.getOrderTotalRevenue());
				contactFields.add(contactField);
			}else if(field.getName().equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("bronto.contact.field.CouponLastCode")) && CommonUtility.validateString(brontoModel.getCouponLastCode()).length() > 0){
				contactField.setContent(brontoModel.getCouponLastCode());
				contactFields.add(contactField);
			}

		}
		ContactOperations contactOps = new ContactOperations(client);
		ContactObject contact = null;
		ContactStatus contactStatus = null;
		if(reqType == REGISTRATION || reqType == EMAIL_SIGNUP){
			contactStatus = ContactStatus.TRANSACTIONAL;
			if(brontoModel.isInterestedInCommercialDiscounts() || brontoModel.isRecieveEmailOffers() || reqType == EMAIL_SIGNUP){
				contactStatus = ContactStatus.ACTIVE;
			}
			contact = contactOps.newObject()
					.set("email", brontoModel.getEmail())
					.set("status", contactStatus)
					.get();
		}else{
			contact = contactOps.newObject()
					.set("email", brontoModel.getEmail())
					.get();
		}

		if(contactFields!=null && contactFields.size() > 0){
			contact.getFields().addAll(contactFields);
		}

		try {
			WriteResult contactUpdateResult = contactOps.addOrUpdate(contact);
			List<ResultItem> resultItems =  contactUpdateResult.getResults();
			for(ResultItem resultItem : resultItems){


				if(CommonUtility.validateString(resultItem.getErrorString()).length() > 0){
					result = "1|"+resultItem.getErrorString();
					System.out.println("Contact create or update Result --" + resultItem.getErrorString());
				}else{
					System.out.println("Contact create or update Result -- Success");
					result = "0|Contact updated successfully.";
					if(!brontoModel.isBeforeLoginUser()){
						MailListOperations listOps = new MailListOperations(client);
						MailListObject list = listOps.get(new MailListReadRequest().withName(listName));
						listOps.addToList(list, contact);
						System.out.println("Contact "+ brontoModel.getEmail() + " added to list " + listName);
					}
				}

			}

		} catch (Exception e) {
			// Handle exception
		}

		return result;
	}


	public String sendCartEmail(BrontoApi client, String messageType, ProductsModel otherDetails, ArrayList<ProductsModel> cartItemList, SendMailModel sendMailModel, String sessionId, boolean isBeforeLogin){

		if(reInitializeClient){
			initializeClient();
			reInitializeClient = false;
		}
		String response = "";
		if(CommonUtility.validateString(sendMailModel.getToEmailId()).length() > 0){


			/*BrontoApi client = new BrontoClient(apiToken);
			client.login();*/
			String contactListName = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BRONTO_ABANDONED_CART_CONTACT_LIST"));
			/*List<String> listIds = BrontoUtility.getInstance().getContactListIds(client,contactListName);
			String listId = "";
			if(listIds != null && listIds.size() > 0){
				listId = listIds.get(0);
			}*/
			ObjectOperations<MessageObject> messageOps = client.transport(MessageObject.class);

			MessageObject message = messageOps.get(new MessageReadRequest().withName(messageType));
			System.out.println(message.getName());

			ContactReadRequest activeContacts = new ContactReadRequest()
			.withIncludeLists(true)
			.withEmail(sendMailModel.getToEmailId());

			ContactOperations contactOps = new ContactOperations(client);
			System.out.println("Reading active contacts");
			List<ContactObject> contactList = contactOps.read(activeContacts);


			BrontoModel brontoModel = new BrontoModel();
			brontoModel.setFirstName(sendMailModel.getFirstName());
			brontoModel.setLastName(sendMailModel.getLastName());
			brontoModel.setEmail(sendMailModel.getToEmailId());
			createOrUpdateContact(client, brontoModel, contactListName, ABANDONED_CART);
			contactList = contactOps.read(activeContacts);

			ObjectOperations<DeliveryObject> deliveryOps = client.transport(DeliveryObject.class);

			DeliveryRecipientObject recipient = new DeliveryRecipientObject();
			recipient.setDeliveryType(DeliveryRecipientSelection.SELECTED.getApiValue());
			recipient.setType(DeliveryRecipientType.CONTACT.getApiValue());
			recipient.setId(contactList.get(0).getId());
			try {
				GregorianCalendar c = new GregorianCalendar();
				c.setTime(new Date());
				XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

				String secretKey = BrontoDAO.getAbandonedCartKey(otherDetails.getUserId(), sessionId);
				List<MessageFieldObject> messageFieldObjects = getMessageFieldObjects(otherDetails, cartItemList, sendMailModel, secretKey, isBeforeLogin, sessionId);

				DeliveryObject delivery = new DeliveryObject();
				delivery.setType(DeliveryType.TRANSACTIONAL.getApiValue());
				delivery.setMessageId(message.getId());
				delivery.setFromEmail(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BRONTO_ABANDONED_CART_FROM_EMAIL")));
				delivery.setFromName(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BRONTO_ABANDONED_CART_FROM_NAME")));
				delivery.setStart(date2);
				delivery.getRecipients().add(recipient);
				delivery.getFields().addAll(messageFieldObjects);

				WriteResult result = deliveryOps.add(delivery);
				List<ResultItem> resultItems =  result.getResults();
				for(ResultItem resultItem : resultItems){
					if(resultItem.getErrorString()!=null && resultItem.getErrorString().length() > 0){
						System.out.println("There was a problem sending Abandoned cart email: \n" + resultItem.getErrorString());
						response = "0|Failed";
					}else{
						int count = BrontoDAO.updateAbandonedCartStatus(otherDetails.getUserId(), secretKey, "Y","", sessionId);
						if(count > 0){
							response = "1|Success";
						}else{
							response = "0|Failed";
						}
					}
				}
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("No email address associated.");
		}
		System.out.println("Send Abandoned Cart Email response -- "+response);
		return response;
	}

	private List<MessageFieldObject> getMessageFieldObjects(ProductsModel otherDetails, ArrayList<ProductsModel> cartItemList, SendMailModel sendMailModel, String secretKey, boolean isBeforeLogin, String sessionId){
		List<MessageFieldObject> messageFieldObjects = new ArrayList<MessageFieldObject>();
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		String webSiteUrl = CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS");
		String storePhoneNumber = CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER");

		MessageFieldObject storePhone = newObject(MessageFieldObject.class).set("type", "html").set("name", "storePhone").set("content", storePhoneNumber).get();
		MessageFieldObject storeURL = newObject(MessageFieldObject.class).set("type", "html").set("name", "storeURL").set("content", webSiteUrl).get();
		MessageFieldObject firstName = newObject(MessageFieldObject.class).set("type", "html").set("name", "firstName").set("content", sendMailModel.getFirstName()).get();
		MessageFieldObject lastName = newObject(MessageFieldObject.class).set("type", "html").set("name", "lastName").set("content", sendMailModel.getLastName()).get();
		MessageFieldObject storeName = newObject(MessageFieldObject.class).set("type", "html").set("name", "storeName").set("content", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"))).get();
		MessageFieldObject quoteurl = null;
		if(isBeforeLogin){
			quoteurl = newObject(MessageFieldObject.class).set("type", "html").set("name", "quoteurl").set("content", webSiteUrl+"ACWL/"+sessionId).get();
		}else{
			quoteurl = newObject(MessageFieldObject.class).set("type", "html").set("name", "quoteurl").set("content", webSiteUrl+"ACL/"+secretKey + "/"+sessionId).get();
		}

		MessageFieldObject subTotal = newObject(MessageFieldObject.class).set("type", "html").set("name", "subTotal").set("content", CommonUtility.validateString(otherDetails.getTotal()+"")).get();
		MessageFieldObject webAddress = newObject(MessageFieldObject.class).set("type", "html").set("name", "webAddress").set("content", webSiteUrl).get();

		messageFieldObjects.add(webAddress);
		messageFieldObjects.add(storeURL);
		messageFieldObjects.add(firstName);
		messageFieldObjects.add(lastName);
		messageFieldObjects.add(storeName);
		messageFieldObjects.add(quoteurl);
		messageFieldObjects.add(subTotal);
		messageFieldObjects.add(storePhone);

		String[] itemFields = {"productUrl_","productImgUrl_","productShortDesc_","productPrice_","productQty_","productPartNumber_","productManufacturerPartNumber_","productUom_","productExtPrice_"};
		String value = "";

		for (int i = 0; i < itemFields.length; i++) {
			for (int j = 0; j < cartItemList.size(); j++) {

				switch (i) {
				case 0:

					break;
				case 1:
					if(CommonUtility.validateString(cartItemList.get(j).getImageName()).contains("http")){
						value = cartItemList.get(j).getImageName();
					}else{
						//value = CommonDBQuery.getSystemParamtersList().get("DETAILIMAGE") + cartItemList.get(j).getImageName();
						value = webSiteUrl + CommonDBQuery.getSystemParamtersList().get("DETAILIMAGE") + cartItemList.get(j).getImageName();
					}
					break;
				case 2:
					value = cartItemList.get(j).getShortDesc();
					break;
				case 3:
					value = formatter.format(cartItemList.get(j).getPrice());
					value = CommonUtility.validateParseDoubleToString(cartItemList.get(j).getPrice());
					break;
				case 4:
					value = formatter.format(cartItemList.get(j).getQty());
					break;
				case 5:
					value = cartItemList.get(j).getPartNumber();
					break;
				case 6:
					value = cartItemList.get(j).getManufacturerPartNumber();
					break;
				case 7:
					value = cartItemList.get(j).getUom();
					break;
				case 8:
					value = CommonUtility.validateParseDoubleToString(cartItemList.get(j).getExtendedPrice());
					break;
				default:
					break;
				}

				MessageFieldObject messageFieldObject = new MessageFieldObject();
				messageFieldObject.setType("html");
				messageFieldObject.setName(itemFields[i]+j);
				messageFieldObject.setContent(value);

				messageFieldObjects.add(messageFieldObject);
			}
		}

		return messageFieldObjects;
	}


	public BrontoApi brontoLogin(String apiToken){
		String sessionId = "";
		BrontoApi client = null;
		if(client == null){
			client = new BrontoClient(apiToken);
			sessionId = client.login();
		}

		System.out.println("Bronto sission Id - "+sessionId);
		return client;
	}

	/*public List<String> getContactListIds(BrontoApi client, String listName){

		If listName is null returns all available list ids else returns only the list id of listName variable

		List<String> listId = null;
		boolean getSpecificListId = false;

		MailListOperations listOps = new MailListOperations(client);
		MailListReadRequest listRead = new MailListReadRequest();

		System.out.println("Reading lists");

		List<MailListObject> lists = listOps.read(listRead);
		listId = new ArrayList<String>();

		if(CommonUtility.validateString(listName).length() > 0){
			getSpecificListId = true;
		}

		for(MailListObject list : lists){
			System.out.println(String.format("MailList %s: %s", list.getId(), list.getName()));
			if(!getSpecificListId){
				listId.add(list.getId());
			}else if(list.getName().equalsIgnoreCase(listName)){
				listId.add(list.getId());
			}
		}

		return listId;
	}*/

	public String addOrUpdateOrderToContact(BrontoApi client, SalesModel salesOrderDetail, ArrayList<SalesModel> salesOrderItem, SendMailModel sendMailModel){
		if(reInitializeClient){
			initializeClient();
			reInitializeClient = false;
		}
		String response = "";
		try {

			GregorianCalendar c = new GregorianCalendar();
			c.setTime(new Date());
			XMLGregorianCalendar date2;

			date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

			List<ProductObject> productObjects = getProductObjects(salesOrderItem);

			OrderObject orderObject = newObject(OrderObject.class)
					.set("id", salesOrderDetail.getExternalSystemId())
					.set("email", sendMailModel.getToEmailId())
					.set("products", productObjects)
					.set("orderDate", date2).get();

			OrderOperations orderOps = new OrderOperations(client);

			WriteResult result = orderOps.addOrUpdate(orderObject);
			List<ResultItem> resultItems =  result.getResults();
			for(ResultItem resultItem : resultItems){
				if(resultItem.getErrorString()!=null && resultItem.getErrorString().length() > 0){
					System.out.println("There was a problem adding or updating the order: \n" + resultItem.getErrorString());
					response = "0|Failed";
				}else if(resultItem.isIsNew()){
					System.out.println("The order has been added.  Id: " + resultItem.getId());
					response = "1|Success";
				}else{
					System.out.println("The order information has been updated.  Id: " + resultItem.getId());
					response = "1|Success";
				}
			}
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}	


	private List<ProductObject> getProductObjects(ArrayList<SalesModel> salesOrderItem){
		List<ProductObject> productObjects = new ArrayList<ProductObject>();
		String webAddress = CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS");
		BigDecimal bigDecimal = null;
		for(SalesModel orderItem : salesOrderItem){
			bigDecimal = new BigDecimal(orderItem.getTotal());
			String imageUrl = "";
			if(CommonUtility.validateString(orderItem.getImageName()).contains("http")){
				imageUrl = orderItem.getImageName();
			}else{
				imageUrl = CommonDBQuery.getSystemParamtersList().get("DETAILIMAGE") + orderItem.getImageName();
			}
			ProductObject productObject = newObject(ProductObject.class)
					.set("sku",orderItem.getPartNumber())
					.set("name", orderItem.getManufacturerPartNumber())
					.set("description", orderItem.getShortDesc())
					.set("image",imageUrl)
					.set("url", webAddress+orderItem.getItemPriceId() +"/product/")
					.set("quantity",orderItem.getOrderQty())
					.set("price",bigDecimal).get();

			productObjects.add(productObject);

		}
		return productObjects;
	}

	public String sendOrderDetailsToContact(BrontoApi client, int userId, int orderId){

		SalesModel orderDetail = new SalesModel(); 
		//double orderTotal = 0d;
		ArrayList<SalesModel> orderItemList = new ArrayList<SalesModel>();
		HashMap<String, Object> order = new LinkedHashMap<String, Object>();

		UsersModel usersModel = UsersDAO.getUserDetailById(userId);


		HashMap<String, String> userdetails = UsersDAO.getUserPasswordAndUserId(usersModel.getUserName(), "Y");
		String firstName = userdetails.get("firstName");
		String lastName = userdetails.get("lastName");
		String toEmail = userdetails.get("userEmailAddress");

		String contactListName = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BRONTO_CHECKOUT_CONTACT_LIST"));

		BrontoModel brontoModel = new BrontoModel();
		brontoModel.setFirstName(firstName);
		brontoModel.setLastName(lastName);
		brontoModel.setEmail(toEmail);
		brontoModel.setHasBought(true);

		brontoModel = BrontoDAO.getOrderAndCouponDetails(userId, brontoModel);

		createOrUpdateContact(client, brontoModel, contactListName, SEND_ORDER_DETAILS_TO_CONTACT);

		SendMailModel mailModel = new SendMailModel();
		mailModel.setToEmailId(toEmail);
		mailModel.setFirstName(firstName);
		mailModel.setLastName(lastName);
		String result = "";
		String apiToken = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BRONTO_API_TOKEN"));
		client = BrontoUtility.getInstance().brontoLogin(apiToken);
		order = BrontoDAO.getOrderDetailsForBronto(orderId, userId);

		orderDetail = (SalesModel) order.get("orderDetail");
		orderDetail.setUserId(userId);
		//orderTotal = (Double) order.get("orderTotal");
		orderItemList = (ArrayList<SalesModel>) order.get("orderItemList");
		result = BrontoUtility.getInstance().addOrUpdateOrderToContact(client, orderDetail, orderItemList, mailModel);
		return result;

	}

	public String abandonedCart(int userId, int orderId, String reqType, String subsetId, String sessionId, boolean isBeforeLogin){

		UsersModel usersModel = UsersDAO.getUserDetailById(userId);

		HashMap<String, String> userdetails = UsersDAO.getUserPasswordAndUserId(usersModel.getUserName(), "Y");
		String firstName = userdetails.get("firstName");
		String lastName = userdetails.get("lastName");
		String toEmail = userdetails.get("userEmailAddress");

		SendMailModel mailModel = new SendMailModel();
		mailModel.setToEmailId(toEmail);
		mailModel.setFirstName(firstName);
		mailModel.setLastName(lastName);
		String result = "";
		String apiToken = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BRONTO_API_TOKEN"));
		BrontoApi client = BrontoUtility.getInstance().brontoLogin(apiToken);

		String messageName = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BRONTO_MESSAGE_NAME"));
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();

		String generalCatalog = CommonDBQuery.getSystemParamtersList().get("GENERALCATALOGID");
		contentObject = BrontoDAO.getShoppingCartForAbandonedCart(userId, sessionId, CommonUtility.validateNumber(subsetId), CommonUtility.validateNumber(generalCatalog), contentObject, isBeforeLogin);

		ArrayList<ProductsModel> cartItemList = (ArrayList<ProductsModel>) contentObject.get("productListData");
		if(cartItemList!=null && cartItemList.size() > 0){
			ProductsModel otherDetails = new ProductsModel();
			otherDetails.setUserId(userId);
			otherDetails.setTotal((Double)contentObject.get("cartTotal"));
			result = BrontoUtility.getInstance().sendCartEmail(client, messageName, otherDetails, cartItemList, mailModel, sessionId, isBeforeLogin);
		}else{
			String secretKey = BrontoDAO.getAbandonedCartKey(userId, sessionId);
			BrontoDAO.updateAbandonedCartStatus(userId, secretKey, "E", "", sessionId);
			result = "No Cart items available";
		}

		return result;
	}

	public ArrayList<ProductsModel> massProductInquiry(ProductManagementModel priceInquiryInput) {

		ArrayList<ProductsModel> partnumpricelist = new ArrayList<ProductsModel>();
		OEPricingMultipleV3InputProduct[] arrayProduct=null; 

		String wareHousecode = priceInquiryInput.getWareHouse();
		String customerId = priceInquiryInput.getCustomerId(); //7932
		String customerCountry = priceInquiryInput.getCustomerCountry();
		//String buyingCompany = (String)session.getAttribute("buyingCompanyId");
		String shipToId = priceInquiryInput.getShipToId();

		try {

			if(shipToId!=null && shipToId.trim().length()>0){//&& CommonDBQuery.getSystemParamtersList().get("SEND_SX_SHIP_TO_ID")!=null && CommonDBQuery.getSystemParamtersList().get("SEND_SX_SHIP_TO_ID").trim().equalsIgnoreCase("Y")
				shipToId = priceInquiryInput.getShipToId();
			}else{
				shipToId = "";
			}
			//ArrayList<String> partNumberList = priceInquiryInput.getPartIdentifier();
			ArrayList<ProductsModel> productsInput = priceInquiryInput.getPartIdentifier();
			ArrayList<String> partNumberList = new ArrayList<String>();
			for(ProductsModel eachProduct:productsInput){
				partNumberList.add(eachProduct.getPartNumber());
			}
			ArrayList<Integer> partNumberQuantityList = priceInquiryInput.getPartIdentifierQuantity();

			int productCount =0;
			if(partNumberList!=null && partNumberList.size()>0){

				OEPricingMultipleV3InputProduct oePricingMultipleV3InputProduct = new OEPricingMultipleV3InputProduct();
				arrayProduct =  new OEPricingMultipleV3InputProduct[partNumberList.size()];
				//Moved to CustomerItemRelationShipImpl.java
				String distributionCenter = "";
				distributionCenter = ProductsDAO.getDistributionCenterFromWarehouseCustomFieldByWarehouse(wareHousecode);
				/*if(distributionCenter!=null && distributionCenter.trim().length()>0){
					session.setAttribute("distributionCenter",distributionCenter.trim());
				}*/
				/*if(session!=null && session.getAttribute("distributionCenter")!=null){
				distributionCenter = (String) session.getAttribute("distributionCenter");
			}else{
				//distributionCenter = ProductsDAO.getDistributionCenterFromWarehouseCustomField(CommonUtility.validateNumber(buyingCompany));
				distributionCenter = ProductsDAO.getDistributionCenterFromWarehouseCustomFieldByWarehouse(wareHousecode);
				if(distributionCenter!=null && distributionCenter.trim().length()>0){
					session.setAttribute("distributionCenter",distributionCenter.trim());
				}
			}*/

				String wareHouseCodeToERP = "";

				for(ProductsModel tempPartNumber:productsInput){
					oePricingMultipleV3InputProduct = new OEPricingMultipleV3InputProduct();
					oePricingMultipleV3InputProduct.setProductCode(tempPartNumber.getPartNumber());
					if(CommonUtility.validateString(tempPartNumber.getUom()).length()>0){
						oePricingMultipleV3InputProduct.setUnitOfMeasure(tempPartNumber.getUom());
					}

					/*if(priceInquiryInput.getUomPartNumberMap()!=null && CommonUtility.validateString(priceInquiryInput.getUomPartNumberMap().get(tempPartNumber)).length()>0){
						oePricingMultipleV3InputProduct.setUnitOfMeasure(priceInquiryInput.getUomPartNumberMap().get(tempPartNumber));	
					}*/
					if(distributionCenter!=null && wareHousecode!=null && distributionCenter.trim().equalsIgnoreCase(wareHousecode)){
						wareHouseCodeToERP = wareHousecode;
					}else{
						if(distributionCenter!=null && distributionCenter.trim().length()>0){
							wareHouseCodeToERP = distributionCenter+","+wareHousecode;
						}else{
							wareHouseCodeToERP = wareHousecode;
						}
					}
					if(priceInquiryInput.getAllBranchavailabilityRequired()!=null && priceInquiryInput.getAllBranchavailabilityRequired().trim().equalsIgnoreCase("Y")){
						wareHouseCodeToERP = priceInquiryInput.getWareHouse();
					}
					oePricingMultipleV3InputProduct.setWarehouse(CommonUtility.validateString(wareHouseCodeToERP));
					oePricingMultipleV3InputProduct.setSequenceNumber(1);

					double quantitySelected = 0.0;
					if(partNumberQuantityList!=null && partNumberQuantityList.size()==partNumberList.size()){
						quantitySelected = partNumberQuantityList.get(productCount);
					}
					if(quantitySelected > 0){
						oePricingMultipleV3InputProduct.setQuantity(quantitySelected);
					}else{
						oePricingMultipleV3InputProduct.setQuantity(1.0);
					}
					arrayProduct[productCount] = oePricingMultipleV3InputProduct;
					productCount++;
				}
				System.out.println("----------------------------");
				System.out.println("Warehouse : "+wareHousecode);
				System.out.println("----------------------------");
				//Comented as SX direct integration packages were removed
				/*OEPricingMultipleV3Response priceResponse = CustomerItemRelationShipImpl.getOEPricingMultipleV3(CommonUtility.validateNumber(customerId),true , true, "", "", "", false, shipToId, true, arrayProduct,customerCountry);

				if(priceResponse.getErrorMessage().toString().trim().length()==0 && priceResponse.getArrayPrice()!=null && priceResponse.getArrayPrice().length>0){
					partnumpricelist = ProductsUtility.productPriceWrapper(priceResponse);
				}*/


				//Removed to get a separate ajax Call
				/*if(partnumpricelist!=null){
				ArrayList<ProductsModel> partnumpricelistTemp = new ArrayList<ProductsModel>();
				for(ProductsModel productModel : partnumpricelist){
					if(CommonUtility.validateString(productModel.getPartNumber()).length()>0){

						ArrayList<ProductsModel> itemResponseArray = CustomerItemRelationShipImpl.getICGetWhseProductListV2(productModel);

						if(itemResponseArray!=null && !itemResponseArray.isEmpty()){
							productModel.setBasePrice(itemResponseArray.get(0).getBasePrice());
							productModel.setListPrice(itemResponseArray.get(0).getListPrice());
							productModel.setLeadTime(itemResponseArray.get(0).getLeadTime());
							productModel.setProductCategory(itemResponseArray.get(0).getProductCategory());
							//Removed As confirmed by Nick from Weingartz
							//if(CommonUtility.validateString(itemResponseArray.get(0).getProductCategory()).length()>0 && CommonUtility.validateString(itemResponseArray.get(0).getProductCategory()).equalsIgnoreCase("Part")){
							//	productModel.setPrice(itemResponseArray.get(0).getListPrice());
							//	productModel.setExtendedPrice(itemResponseArray.get(0).getListPrice()*productModel.getQty());
							//}

						}
						partnumpricelistTemp.add(productModel);

					}
				}

				partnumpricelist = partnumpricelistTemp;
			}*/

				//---------- For distributionCenter
				/*if(partnumpricelist!=null && partnumpricelist.size()>0){
				CustomerItemRelationShipImpl.getDistributionCenterAvailabilityForPartnumbmer(partnumpricelist);
			}*/
				//---------- For distributionCenter


				// Price Break Not implemented

			}


		} catch (Exception e) {
			e.printStackTrace();
		}


		return partnumpricelist;

	}
}