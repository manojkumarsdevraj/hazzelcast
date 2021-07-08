package com.unilog.products;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.RangeFacet;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralShipMethodQuantity;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralWarehouse;
import com.google.gson.annotations.Expose;
import com.unilog.misc.EventModel;
import com.unilog.promotion.BannerEntity;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

/**
 * @author bhks
 *
 */
public class ProductsModel implements Comparator<ProductsModel>{
	public static Comparator<ProductsModel> branchAscendingComparator = new Comparator<ProductsModel>() {
		public int compare(ProductsModel arg0, ProductsModel arg1) {
			if (arg0.getBranchName() == null) {
				return arg1.getBranchName() == null ? 0 : 1;
			}
			if (arg1.getBranchName() == null) {
				return -1;
			}
			return arg0.getBranchName().compareTo(arg1.getBranchName());
		}
	};
	public static Comparator<ProductsModel> cpnAscendingComparator = new Comparator<ProductsModel>() {
		public int compare(ProductsModel arg0, ProductsModel arg1) {
			if (arg0.getCustomerPartNumber() == null) {
				return arg1.getCustomerPartNumber() == null ? 0 : 1;
			}
			if (arg1.getCustomerPartNumber() == null) {
				return -1;
			}
			return arg0.getCustomerPartNumber().compareTo(arg1.getCustomerPartNumber());
		}
	};
	public static Comparator<ProductsModel> cpnDescendingComparator = new Comparator<ProductsModel>() {
		public int compare(ProductsModel arg0, ProductsModel arg1) {
			if (arg0.getCustomerPartNumber() == null) {
				return arg1.getCustomerPartNumber() == null ? 0 : 1;
			}
			if (arg1.getCustomerPartNumber() == null) {
				return -1;
			}

			return arg1.getCustomerPartNumber().compareTo(arg0.getCustomerPartNumber());
		}
	};
	public static Comparator<ProductsModel> interActiveOfferPositionComparator = new Comparator<ProductsModel>() {
		public int compare(ProductsModel s1, ProductsModel s2) {
			int offerPosition1 = s1.getInterActiveOfferPosition();
			int offerPosition2 = s2.getInterActiveOfferPosition();
			//ascending order
			return offerPosition1-offerPosition2;
			//descending order
			//return offerPosition2-offerPosition1;
		}
	};
	public static Comparator<ProductsModel> partNumberAscendingComparator = new Comparator<ProductsModel>() {
		public int compare(ProductsModel arg0, ProductsModel arg1) {
			if (arg0.getPartNumber() == null) {
				return arg1.getPartNumber() == null ? 0 : 1;
			}
			if (arg1.getPartNumber() == null) {
				return -1;
			}
			return arg0.getPartNumber().compareTo(arg1.getPartNumber());
		}
	};
	public static Comparator<ProductsModel> partNumberDescendingComparator = new Comparator<ProductsModel>() {
		public int compare(ProductsModel arg0, ProductsModel arg1) {
			if (arg0.getPartNumber() == null) {
				return arg1.getPartNumber() == null ? 0 : 1;
			}
			if (arg1.getPartNumber() == null) {
				return -1;
			}

			return arg1.getPartNumber().compareTo(arg0.getPartNumber());
		}
	};
	public static Comparator<ProductsModel> groupNameAscendingComparator = new Comparator<ProductsModel>() {
		public int compare(ProductsModel arg0, ProductsModel arg1) {
			if (arg0.getProductListName() == null) {
				return arg1.getProductListName() == null ? 0 : 1;
			}
			if (arg1.getProductListName() == null) {
				return -1;
			}
			return arg0.getProductListName().compareTo(arg1.getProductListName());
		}
	};
	public static Comparator<ProductsModel> getInterActiveOfferPositionComparator() {
		return interActiveOfferPositionComparator;
	}
	public static void setInterActiveOfferPositionComparator(
			Comparator<ProductsModel> interActiveOfferPositionComparator) {
		ProductsModel.interActiveOfferPositionComparator = interActiveOfferPositionComparator;
	}
	@Expose private String altPartNumber1;
	@Expose private String altPartNumber2;
	@Expose private String appvalsenderUserID;
	@Expose private int assignedShipTo;
	@Expose private LinkedHashMap<String, ArrayList<ProductsModel>> attrFilterList=null;
	@Expose private ArrayList<ProductsModel> attributeDataList = new ArrayList<ProductsModel>();
	@Expose private int attrId;
	@Expose private String attrName;
	@Expose private String attrNameEncoded;
	@Expose private int attrType;
	@Expose private String attrUom;
	@Expose private UsersModel userModelObject;
	@Expose private int requestTokenId;
	
	
	public int getRequestTokenId() {
		return requestTokenId;
	}
	public void setRequestTokenId(int requestTokenId) {
		this.requestTokenId = requestTokenId;
	}
	
	public UsersModel getUserModelObject() {
		return userModelObject;
	}
	public void setUserModelObject(UsersModel userModelObject) {
		this.userModelObject = userModelObject;
	}
	@Expose private int attrUomId;
	@Expose private String attrValue;
	@Expose private String attrValueEncoded;
	@Expose private int attrValueId;
	@Expose private int availQty;
	@Expose private double basePrice;
	@Expose private String branchAddress1;
	@Expose private String branchAddress2;
	@Expose private ArrayList<ProductsModel> branchAvail = new ArrayList<ProductsModel>();
	@Expose private String branchAvailability;
	@Expose private String branchAvailQty;
	@Expose private String branchCity;
	@Expose private String branchCountry;
	@Expose private String lastPurchasedDate;
	@Expose private String lastPurchasedQty;
	@Expose private String lastPurchasedUnit;
	@Expose private double totalLineItemAmount;
	@Expose private double specialCharge;
	@Expose private String approvalStatus;
	@Expose private String approverComments;
	@Expose private String alwaysApprover;
	@Expose private String approverSequence;


	public String getAlwaysApprover() {
		return alwaysApprover;
	}
	public void setAlwaysApprover(String alwaysApprover) {
		this.alwaysApprover = alwaysApprover;
	}
	public String getApproverSequence() {
		return approverSequence;
	}
	public void setApproverSequence(String approverSequence) {
		this.approverSequence = approverSequence;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public String getApproverComments() {
		return approverComments;
	}
	public void setApproverComments(String approverComments) {
		this.approverComments = approverComments;
	}
	@Expose private double totalInvoiceAmount;
	
	
	
	
	public double getTotalLineItemAmount() {
		return totalLineItemAmount;
	}
	public void setTotalLineItemAmount(double totalLineItemAmount) {
		this.totalLineItemAmount = totalLineItemAmount;
	}
	public double getSpecialCharge() {
		return specialCharge;
	}
	public void setSpecialCharge(double specialCharge) {
		this.specialCharge = specialCharge;
	}
	public double getTotalInvoiceAmount() {
		return totalInvoiceAmount;
	}
	public void setTotalInvoiceAmount(double totalInvoiceAmount) {
		this.totalInvoiceAmount = totalInvoiceAmount;
	}
	public String getLastPurchasedDate() {
		return lastPurchasedDate;
	}
	public void setLastPurchasedDate(String lastPurchasedDate) {
		this.lastPurchasedDate = lastPurchasedDate;
	}
	public String getLastPurchasedQty() {
		return lastPurchasedQty;
	}
	public void setLastPurchasedQty(String lastPurchasedQty) {
		this.lastPurchasedQty = lastPurchasedQty;
	}
	public String getLastPurchasedUnit() {
		return lastPurchasedUnit;
	}
	public void setLastPurchasedUnit(String lastPurchasedUnit) {
		this.lastPurchasedUnit = lastPurchasedUnit;
	}
	public String getYearToDate() {
		return yearToDate;
	}
	public void setYearToDate(String yearToDate) {
		this.yearToDate = yearToDate;
	}
	@Expose private String yearToDate;
	
	@Expose private Double branchDistance;
	@Expose private String branchEarliestAvailableInfo;
	@Expose private String branchEmail;
	@Expose private String branchID;
	@Expose private String branchLatitude;
	@Expose private String branchLongitude;
	@Expose private ArrayList<ProductsModel> branchMinOrderOrderIntervalList;
	@Expose private String branchName;
	@Expose private String branchPhoneNumber;
	@Expose private String branchPostalCode;
	@Expose private String branchState;
	@Expose private LinkedHashMap<String, ProductsModel> branchTeritory = null;
	@Expose private String branchTollFreeNumber;
	@Expose private int branchTotalQty;
	@Expose private String branchWorkingHours;
	@Expose private int brandId;
	@Expose private String brandImage;
	@Expose private String brandName;
	@Expose private String brandUrl;
	@Expose private int cartCaseCount;
	@Expose private int cartId;
	@Expose private int cartIndividualItemQuantitySum;
	@Expose private int cartItemCount;
	@Expose private double cartTotal;
	@Expose private String catalogId;
	//@Expose private int catalogNumber;
	@Expose private String catalogNumber;
	@Expose private String categoryCode;
	@Expose private int categoryCount;
	@Expose private String categoryName;
	@Expose private Cimm2BCentralWarehouse cimm2BCentralPricingWarehouse;
	@Expose private String clearance;
	@Expose private String collectionAttr;
	@Expose private String comments;
	@Expose private int couponCounter;
	@Expose private String cpnSyncResult;
	@Expose private String currency;
	@Expose private String customerPartNumber;
	@Expose private ArrayList<ProductsModel> customerPartNumberList;
	@Expose private double customerPrice;
	@Expose private LinkedHashMap<String, Object> customFieldVal = null;
	@Expose private String date;
	@Expose private double dDiscountValue;
	@Expose private LinkedHashMap<String, String> defaultHashMap;
	@Expose private String description;
	@Expose private String discountCouponCode;
	@Expose private String discountType;
	@Expose private String discountValue;
	@Expose private String displayFilterText;
	@Expose private String displayFrieghtAlert;
	@Expose private String displayPrice;
	@Expose private String displayPricing;
	@Expose private String distributionCenter;
	@Expose private int distributionCenterAvailablity;
	@Expose private String docCategoryType;
	@Expose private String docDesc;
	@Expose private String docFrom;
	@Expose private String docName;
	@Expose private String docType;
	@Expose private String earliestMoreDate;
	@Expose private int earliestMoreDays;
	@Expose private int earliestMoreQty;
	@Expose private String endDate;
	@Expose private String erpPartNumber;
	@Expose private int erpQty;
	@Expose private String exactMatchNotFound;
	@Expose private double extendedPrice;
	@Expose private List<RangeFacet> facetRange;
	@Expose private String featuredEvent;
	@Expose private boolean flag;
	@Expose private String from;
	@Expose private int generalSubsetId;
	@Expose private String getPriceFrom;
	@Expose private String groupItemCount;
	@Expose private String groupName;
	@Expose private String groupType;
	@Expose private String hazardousMaterial;
	@Expose private double height;
	@Expose private int homeBranchavailablity;
	@Expose private double homeBranchavailablityDouble;
	@Expose private ArrayList<ProductsModel> homeBranchDetails;
	@Expose private String idList;
	@Expose private String imageCaption;
	@Expose private String imageFilter;
	@Expose private String imageFrom;
	@Expose private String imageName;
	@Expose private String imagePosition;
	@Expose private String imageType;
	@Expose private String imageURL;
	@Expose private double imapPrice;
	@Expose private int interActiveOfferId;
	@Expose private String interActiveOfferName;
	@Expose private int interActiveOfferPosition;
	@Expose private String invoiceDesc;
	@Expose private String isEclipseItem;
	@Expose private String isErpItem;
	@Expose private boolean isFoundInWhseFlag;
	@Expose private String isImap;
	@Expose private boolean isPriceBreaksExist;
	@Expose private String isSharedCart;
	@Expose private String itemAttributes;
	@Expose private ArrayList<ProductsModel> itemBreadCrumb;
	@Expose private ArrayList<ProductsModel> itemDataList = new ArrayList<ProductsModel>();
	@Expose private String itemDisplayFilterText;
	@Expose private String itemDocuments;
	@Expose private String itemFeature;
	@Expose private int itemId;
	@Expose private ArrayList<Integer> itemIdList;
	@Expose private String itemImageFilter;
	@Expose private String itemImages;
	@Expose private String itemLevelRequiredByDate;
	@Expose private int itemPriceId;
	@Expose private int itemResultCount;
	@Expose private String itemSubset;
	@Expose private String itemTitleString;
	@Expose private String itemUrl;
	@Expose private String keyword;
	@Expose private int leadTime;
	@Expose private double length;
	@Expose private String level1Menu;
	@Expose private String level2Menu;
	@Expose private String level3Menu;
	@Expose private String level4Menu;
	@Expose private String level5Menu;
	@Expose private String level6Menu;
	@Expose private String level7Menu;
	@Expose private int levelNumber=1;
	@Expose private String lineItemComment;
	@Expose private String lineItemCommentShipVia;
	@Expose private String linkedItems;
	@Expose private LinkedHashMap<String, ArrayList<ProductsModel>> linkedItemsList;
	@Expose private String linkTypeName;
	@Expose private double listPrice;
	@Expose private String longDesc;
	@Expose private String ecommerceProductTitle;
	@Expose private String ecommerceProductDescriptionOne;
	@Expose private String ecommerceProductDescriptionTwo;
	@Expose private int manufacturerId;
	@Expose private String manufacturerLogo;
	@Expose private String manufacturerName;
	@Expose private String manufacturerPartNumber;
	@Expose private String marketingText;
	@Expose private String materialGroup;	
	@Expose private String menuId;
	@Expose private String menuName;
	@Expose private String metaDesc;
	@Expose private String metaKeyword;
	@Expose private double MinimumOrderInterval;
	@Expose private double MinimumOrderQuantity;
	@Expose private int minOrderQty;
	@Expose private String modelName;
	@Expose private String multipleShipVia;
	@Expose private String multipleShipViaDesc;
	@Expose private double netPrice;
	@Expose private String notes;
	@Expose private int orderInterval; 
	@Expose private int orderItemId;
	@Expose private String overRidePriceRule;
	@Expose private int packageFlag;
	@Expose private int packageQty; 
	@Expose private String packDesc;
	@Expose private ArrayList<ProductsModel> staticpageBreadCrumb;
	@Expose private String pageName;
	@Expose private String pageUrl;
	@Expose private String linkName;
	@Expose private String pageTitle;
	@Expose private String parentCategory;
	@Expose private ArrayList<ProductsModel> PartIdentifiersList=null;
	@Expose private int partListSearchCount;
	@Expose private String partNumber;
	@Expose private String plentyDate;
	@Expose private int plentyDays;
	@Expose private double price;
	@Expose private String priceType;
	@Expose private String priceTypeDesc;
	@Expose private String priceUOM;
	@Expose private int pricingQtyMultiple;
	@Expose private String productCategory;
	@Expose private String productCategoryImageName;
	@Expose private String productCategoryImageType;
	@Expose private String productDescription;
	@Expose private String productFeature;
	@Expose private int productId;
	@Expose private int productItemCount;
	@Expose private String productKeywords; 
	@Expose private String productLine; 
	@Expose private int productListId;
	@Expose private String productListName;
	@Expose private String productName;
	@Expose private int productSeardId;
	/*@Expose private String bannerType;
	@Expose private String bannerIsScrollable;
	@Expose private String bannerScrollDelay;*/
	@Expose private String promoCode;
	@Expose private int qty;
	@Expose private String qtyUOM;
	@Expose private ArrayList<String> quantityBreak = new ArrayList<String>();
	@Expose private ArrayList<ProductsModel> quantityBreakList;
	@Expose private String queryString;
	@Expose private int quoteCartId;
	@Expose private double rate;
	@Expose private float rating;
	@Expose private String recordDescription;
	@Expose private String recordType;
	@Expose private String referenceKey;
	@Expose private String requestType;
	@Expose private String restrictiveProduct;
	@Expose private int resultCount;
	@Expose private String resultDescription;
	@Expose private float reviewCount;
	@Expose private String reviewId;
	@Expose private String reviewTime;
	@Expose private int saleQty;
	@Expose private double salesTax;
	@Expose private String salesUom;
	@Expose private int selectedOption;
	@Expose private LinkedHashMap<String, ProductsModel> shipOrderQtyAndIntervalFieldVal = null;
	@Expose private String shipToId;
	@Expose private String shipViaCode;
	@Expose private String  shipViaDesc;
	@Expose private String shortDesc;
	@Expose private SolrQuery solrQuery;
	@Expose private String solrSearchField;
	@Expose private String startDate;
	@Expose private String staticContent;
	@Expose private String staticPageId;
	@Expose private String staticPageTitle;
	@Expose private String status;
	@Expose private int subsetId;
	@Expose private String suggestedValue;
	@Expose private ArrayList<String> suggestedValueList;
	@Expose private String swatchColorCode;
	@Expose private String title;
	@Expose private String to;
	@Expose private double total;
	@Expose private int type;
	@Expose private double unitPrice;
	@Expose private double unitPriceCalculation;
	@Expose private double units;
	@Expose private double unitsPerStocking;
	@Expose private String unitsPerStockingString;
	@Expose private String unspc;
	@Expose private String uom;
	@Expose private ArrayList<ProductsModel> uomList;
	@Expose private int uomQty;
	@Expose private String upc;
	@Expose private String userAdded;
	@Expose private int userId;
	@Expose private double vendorNumber;
	@Expose private String videoCaption;
	@Expose private String videoName;
	@Expose private String videoType;
	@Expose private String wareHouseCode;
	@Expose private String warehouseName;
	@Expose private double weight;
	@Expose private String weightUom;
	@Expose private double width;
	@Expose private String restrictToMultiples;
	@Expose private ArrayList<ProductsModel> restrictionInfo;
	@Expose private boolean quantityBreakFlag = false;
	@Expose private String location;
	@Expose private String addressOne;
	@Expose private String notificationContactNumber;
	@Expose private String contact;
	@Expose private String notificationEmail;
	@Expose private String eventFee;
	@Expose private String showSeats;
	@Expose private String totalseats;
	@Expose private String bookedseats;
	@Expose private String timeZoneOffset;
	@Expose private String filelocation;
	@Expose private BannerEntity bannerEntity;
	@Expose private double earliestMoreQuantity;
	@Expose private double minimumQuantityBreak;
	@Expose private double maximumQuantityBreak;
	@Expose private double customerPriceBreak;
	@Expose private String poJobName;
	@Expose private String toEmail;
	@Expose private Integer quantity;
	@Expose private Integer otherBranchAvailability; // Quantity Available in other Branch
	@Expose private double longitude;
	@Expose private double latitude;
	@Expose private double distance;
	@Expose private double promoPrice;
	@Expose private double imapPrice1;
	@Expose private String currencyCode;
	@Expose private double salesQuantity;
	@Expose private String uomPack;
	@Expose private double orderQuantityInterval;
	@Expose private String featuredCategory;
	@Expose private String productStatus;
	@Expose private String stockStatus;
	@Expose private String orderOrQuoteNumber;
	@Expose private ArrayList<Integer> partIdentifierQuantity; 
	@Expose private int uomQuantity;
	@Expose private String uomBg;
	@Expose private double uomQtyERP;
	@Expose private double discountAmount;
	@Expose private double replacementCost;
	@Expose private String supercedeurl;
	@Expose private String availableDescription;
	@Expose private String sharedBy;
	@Expose private HttpSession session;

	@Expose private int designId;
	@Expose private String rushFlag;
	@Expose private boolean calculateTax;
	@Expose private boolean designFees;
	@Expose private int bcAddressBookId;
	@Expose private ArrayList<EventModel> eventCustomFieldList;
	@Expose private String categoryDesc;
	@Expose private double discountAmountValue;
	@Expose private String supplierSku;
	@Expose private String storeSku;
	@Expose private String supplierCode;
	@Expose private boolean itemDiscontinued;
	@Expose private String additionalProperties;
	@Expose private String warehouseErrorMsg;
	@Expose private String blanketPoID;
	@Expose private String itemType;
	@Expose private LinkedHashMap<String, String> customFieldListValues;
	@Expose private double otherAmount;
	
	@Expose private String prop65;
	@Expose private String prop65Agent;
	@Expose private int thresholdQty;
	@Expose private int replenishToQty;
	@Expose private String addToCartFalg;
	@Expose private Integer itemQty;
	@Expose private boolean priceChanged;
	@Expose private boolean unitPriceChanged;
	@Expose private double oldPrice;
	@Expose private double oldUnitPrice;
	@Expose private double priceDifference;
	@Expose private double unitPriceDifference;
	@Expose private int days;
	@Expose private int hours;
	@Expose private int minutes;
	@Expose private int seconds;
	@Expose private String rentals;
	@Expose private double uomPrice;
	@Expose private String dueDate;
	@Expose private String customDescription;
	@Expose private String fromMonth;
	@Expose private String fromYear;
	@Expose private String toMonth;
	@Expose private String toYear;
	@Expose private String uomDescription;
	@Expose private String productNumber;
	@Expose private String standardApprovals;
	
	public String getStandardApprovals() {
		return standardApprovals;
	}
	public void setStandardApprovals(String standardApprovals) {
		this.standardApprovals = standardApprovals;
	}
	
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	public String getUomDescription() {
		return uomDescription;
	}
	public void setUomDescription(String uomDescription) {
		this.uomDescription = uomDescription;
	}
	public String getFromMonth() {
		return fromMonth;
	}
	public void setFromMonth(String fromMonth) {
		this.fromMonth = fromMonth;
	}
	public String getFromYear() {
		return fromYear;
	}
	public void setFromYear(String fromYear) {
		this.fromYear = fromYear;
	}
	public String getToMonth() {
		return toMonth;
	}
	public void setToMonth(String toMonth) {
		this.toMonth = toMonth;
	}
	public String getToYear() {
		return toYear;
	}
	public void setToYear(String toYear) {
		this.toYear = toYear;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public double getUomPrice() {
		return uomPrice;
	}
	public void setUomPrice(double uomPrice) {
		this.uomPrice = uomPrice;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	public double getOldPrice() {
		return oldPrice;
	}
	public void setOldPrice(double oldPrice) {
		this.oldPrice = oldPrice;
	}
	public double getOldUnitPrice() {
		return oldUnitPrice;
	}
	public void setOldUnitPrice(double oldUnitPrice) {
		this.oldUnitPrice = oldUnitPrice;
	}
	public double getPriceDifference() {
		return priceDifference;
	}
	public void setPriceDifference(double priceDifference) {
		this.priceDifference = priceDifference;
	}
	public double getUnitPriceDifference() {
		return unitPriceDifference;
	}
	public void setUnitPriceDifference(double unitPriceDifference) {
		this.unitPriceDifference = unitPriceDifference;
	}
	public boolean isUnitPriceChanged() {
		return unitPriceChanged;
	}
	public void setUnitPriceChanged(boolean unitPriceChanged) {
		this.unitPriceChanged = unitPriceChanged;
	}
	public boolean isPriceChanged() {
		return priceChanged;
	}
	public void setPriceChanged(boolean priceChanged) {
		this.priceChanged = priceChanged;
	}
	public Integer getItemQty() {
		return itemQty;
	}
	public void setItemQty(Integer itemQty) {
		this.itemQty = itemQty;
	}
	public String getAddToCartFalg() {
		return addToCartFalg;
	}
	public void setAddToCartFalg(String addToCartFalg) {
		this.addToCartFalg = addToCartFalg;
	}
	public int getThresholdQty() {
		return thresholdQty;
	}
	public void setThresholdQty(int thresholdQty) {
		this.thresholdQty = thresholdQty;
	}
	public int getReplenishToQty() {
		return replenishToQty;
	}
	public void setReplenishToQty(int replenishToQty) {
		this.replenishToQty = replenishToQty;
	}
	public String getProp65() {
		return prop65;
	}
	public void setProp65(String prop65) {
		this.prop65 = prop65;
	}
	public String getProp65Agent() {
		return prop65Agent;
	}
	public void setProp65Agent(String prop65Agent) {
		this.prop65Agent = prop65Agent;
	}
	public double getOtherAmount() {
		return otherAmount;
	}
	public void setOtherAmount(double otherAmount) {
		this.otherAmount = otherAmount;
	}	
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public String getEcommerceProductTitle() {
		return ecommerceProductTitle;
	}
	public void setEcommerceProductTitle(String ecommerceProductTitle) {
		this.ecommerceProductTitle = ecommerceProductTitle;
	}
	public String getEcommerceProductDescriptionOne() {
		return ecommerceProductDescriptionOne;
	}
	public void setEcommerceProductDescriptionOne(String ecommerceProductDescriptionOne) {
		this.ecommerceProductDescriptionOne = ecommerceProductDescriptionOne;
	}
	public String getEcommerceProductDescriptionTwo() {
		return ecommerceProductDescriptionTwo;
	}
	public void setEcommerceProductDescriptionTwo(String ecommerceProductDescriptionTwo) {
		this.ecommerceProductDescriptionTwo = ecommerceProductDescriptionTwo;
	}
	public double getReplacementCost() {
		return replacementCost;
	}
	public void setReplacementCost(double replacementCost) {
		this.replacementCost = replacementCost;
	}
	public double getUomQtyERP() {
		return uomQtyERP;
	}
	public void setUomQtyERP(double uomQtyERP) {
		
		this.uomQtyERP = uomQtyERP;
	}
	public String getUomBg() {
		return uomBg;
	}
	public void setUomBg(String uomBg) {
		this.uomBg = uomBg;
	}
	public double getDiscountAmountValue() {
		return discountAmountValue;
	}
	public void setDiscountAmountValue(double discountAmountValue) {
		this.discountAmountValue = discountAmountValue;
	}
	public ArrayList<EventModel> getEventCustomFieldList() {
		return eventCustomFieldList;
	}
	public void setEventCustomFieldList(ArrayList<EventModel> eventCustomFieldList) {
		this.eventCustomFieldList = eventCustomFieldList;
	}
	public String getUomPack() {
		return uomPack;
	}
	public void setUomPack(String uomPack) {
		this.uomPack = uomPack;
	}
	public double getSalesQuantity() {
		return salesQuantity;
	}
	public void setSalesQuantity(double salesQuantity) {
		this.salesQuantity = salesQuantity;
	}
	public Integer getOtherBranchAvailability() {
		return otherBranchAvailability;
	}
	public void setOtherBranchAvailability(Integer otherBranchAvailability) {
		this.otherBranchAvailability = otherBranchAvailability;
	}
	public double getImapPrice1() {
		return imapPrice1;
	}
	public void setImapPrice1(double imapPrice1) {
		this.imapPrice1 = imapPrice1;
	}
	private ArrayList<Cimm2BCentralShipMethodQuantity> shipMethodQuantity;

	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public static Comparator<ProductsModel> getBranchAscendingComparator() {
		return branchAscendingComparator;
	}
	public String getPoJobName() {
		return poJobName;
	}
	public void setPoJobName(String poJobName) {
		this.poJobName = poJobName;
	}
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	public static void setBranchAscendingComparator(Comparator<ProductsModel> branchAscendingComparator) {
		ProductsModel.branchAscendingComparator = branchAscendingComparator;
	}
	public static Comparator<ProductsModel> getCpnAscendingComparator() {
		return cpnAscendingComparator;
	}
	public static void setCpnAscendingComparator(Comparator<ProductsModel> cpnAscendingComparator) {
		ProductsModel.cpnAscendingComparator = cpnAscendingComparator;
	}
	public static Comparator<ProductsModel> getCpnDescendingComparator() {
		return cpnDescendingComparator;
	}
	public static void setCpnDescendingComparator(Comparator<ProductsModel> cpnDescendingComparator) {
		ProductsModel.cpnDescendingComparator = cpnDescendingComparator;
	}
	public static Comparator<ProductsModel> getPartNumberAscendingComparator() {
		return partNumberAscendingComparator;
	}
	public static void setPartNumberAscendingComparator(Comparator<ProductsModel> partNumberAscendingComparator) {
		ProductsModel.partNumberAscendingComparator = partNumberAscendingComparator;
	}
	public static Comparator<ProductsModel> getPartNumberDescendingComparator() {
		return partNumberDescendingComparator;
	}
	public static void setPartNumberDescendingComparator(Comparator<ProductsModel> partNumberDescendingComparator) {
		ProductsModel.partNumberDescendingComparator = partNumberDescendingComparator;
	}
	public String getRestrictToMultiples() {
		return restrictToMultiples;
	}
	public void setRestrictToMultiples(String restrictToMultiples) {
		this.restrictToMultiples = restrictToMultiples;
	}
	public ArrayList<ProductsModel> getRestrictionInfo() {
		return restrictionInfo;
	}
	public void setRestrictionInfo(ArrayList<ProductsModel> restrictionInfo) {
		this.restrictionInfo = restrictionInfo;
	}
	public boolean isQuantityBreakFlag() {
		return quantityBreakFlag;
	}
	public void setQuantityBreakFlag(boolean quantityBreakFlag) {
		this.quantityBreakFlag = quantityBreakFlag;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAddressOne() {
		return addressOne;
	}
	public void setAddressOne(String addressOne) {
		this.addressOne = addressOne;
	}
	public String getNotificationContactNumber() {
		return notificationContactNumber;
	}
	public void setNotificationContactNumber(String notificationContactNumber) {
		this.notificationContactNumber = notificationContactNumber;
	}
	public BannerEntity getBannerEntity() {
		return bannerEntity;
	}
	public void setBannerEntity(BannerEntity bannerEntity) {
		this.bannerEntity = bannerEntity;
	}
	public double getMinimumQuantityBreak() {
		return minimumQuantityBreak;
	}
	public void setMinimumQuantityBreak(double minimumQuantityBreak) {
		this.minimumQuantityBreak = minimumQuantityBreak;
	}
	public double getMaximumQuantityBreak() {
		return maximumQuantityBreak;
	}
	public void setMaximumQuantityBreak(double maximumQuantityBreak) {
		this.maximumQuantityBreak = maximumQuantityBreak;
	}
	public double getCustomerPriceBreak() {
		return customerPriceBreak;
	}
	public void setCustomerPriceBreak(double customerPriceBreak) {
		this.customerPriceBreak = customerPriceBreak;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getNotificationEmail() {
		return notificationEmail;
	}
	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
	}
	public String getEventFee() {
		return eventFee;
	}
	public void setEventFee(String eventFee) {
		this.eventFee = eventFee;
	}
	public String getShowSeats() {
		return showSeats;
	}
	public void setShowSeats(String showSeats) {
		this.showSeats = showSeats;
	}
	public String getTotalseats() {
		return totalseats;
	}
	public void setTotalseats(String totalseats) {
		this.totalseats = totalseats;
	}
	public String getBookedseats() {
		return bookedseats;
	}
	public void setBookedseats(String bookedseats) {
		this.bookedseats = bookedseats;
	}
	public String getTimeZoneOffset() {
		return timeZoneOffset;
	}
	public void setTimeZoneOffset(String timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}
	public String getFilelocation() {
		return filelocation;
	}
	public void setFilelocation(String filelocation) {
		this.filelocation = filelocation;
	}
	public void calculateEarliestMoreDays() {
		try {
			if(CommonUtility.validateString(earliestMoreDate).length()>0){
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	  	  		Date earlyAvailDate;
				earlyAvailDate = df.parse(earliestMoreDate);
				long diff = earlyAvailDate.getTime() - System.currentTimeMillis() ;
				long mili2days = 1000 * 60 * 60 * 24;
				earliestMoreDays = (int) (diff / mili2days);
			}
  		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	public void calculatePlentyDays() {
		try {
			if(CommonUtility.validateString(plentyDate).length()>0){
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				Date plentyAvailDate;
				plentyAvailDate = df.parse(plentyDate);
				long diff = plentyAvailDate.getTime() - System.currentTimeMillis() ;
				long mili2days = 1000 * 60 * 60 * 24;
				plentyDays =  (int) (diff / mili2days);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	@Override
	public int compare(ProductsModel o1, ProductsModel o2) {
		return o1.getBranchDistance().compareTo(o2.getBranchDistance());
	}
	public String getAltPartNumber1() {
		return altPartNumber1;
	}
	public String getAltPartNumber2() {
		return altPartNumber2;
	}
	public String getAppvalsenderUserID() {
		return appvalsenderUserID;
	}
	public int getAssignedShipTo() {
		return assignedShipTo;
	}
	public LinkedHashMap<String, ArrayList<ProductsModel>> getAttrFilterList() {
		return attrFilterList;
	}
	public ArrayList<ProductsModel> getAttributeDataList() {
		return attributeDataList;
	}
	public int getAttrId() {
		return attrId;
	}
	public String getAttrName() {
		return attrName;
	}
	public String getAttrNameEncoded() {
		return attrNameEncoded;
	}
	public int getAttrType() {
		return attrType;
	}
	public String getAttrUom() {
		return attrUom;
	}
	public int getAttrUomId() {
		return attrUomId;
	}
	public String getAttrValue() {
		return attrValue;
	}
	public String getAttrValueEncoded() {
		return attrValueEncoded;
	}
	public int getAttrValueId() {
		return attrValueId;
	}
	public int getAvailQty() {
		return availQty;
	}
	public double getBasePrice() {
		return basePrice;
	}
	public String getBranchAddress1() {
		return branchAddress1;
	}
	public String getBranchAddress2() {
		return branchAddress2;
	}
	public ArrayList<ProductsModel> getBranchAvail() {
		return branchAvail;
	}
	public String getBranchAvailability() {
		return branchAvailability;
	}
	public String getBranchAvailQty() {
		return branchAvailQty;
	}
	public String getBranchCity() {
		return branchCity;
	}
	public String getBranchCountry() {
		return branchCountry;
	}
	public Double getBranchDistance() {
		return branchDistance;
	}
		
	public String getBranchEarliestAvailableInfo() {
		return branchEarliestAvailableInfo;
	}
	public String getBranchEmail() {
		return branchEmail;
	}
	public String getBranchID() {
		return branchID;
	}
	public String getBranchLatitude() {
		return branchLatitude;
	}
	public String getBranchLongitude() {
		return branchLongitude;
	}
	public ArrayList<ProductsModel> getBranchMinOrderOrderIntervalList() {
		return branchMinOrderOrderIntervalList;
	}
	public String getBranchName() {
		return branchName;
	}
	public String getBranchPhoneNumber() {
		return branchPhoneNumber;
	}
	public String getBranchPostalCode() {
		return branchPostalCode;
	}
	public String getBranchState() {
		return branchState;
	}
	
	public LinkedHashMap<String, ProductsModel> getBranchTeritory() {
		return branchTeritory;
	}
	public String getBranchTollFreeNumber() {
		return branchTollFreeNumber;
	}
	public int getBranchTotalQty() {
		return branchTotalQty;
	}
	public String getBranchWorkingHours() {
		return branchWorkingHours;
	}
	public int getBrandId() {
		return brandId;
	}
	public String getBrandImage() {
		return brandImage;
	}
	public String getBrandName() {
		return brandName;
	}
	public String getBrandUrl() {
		return brandUrl;
	}
	public int getCartCaseCount() {
		return cartCaseCount;
	}
	public int getCartId() {
		return cartId;
	}
	public int getCartIndividualItemQuantitySum() {
		return cartIndividualItemQuantitySum;
	}
	public int getCartItemCount() {
		return cartItemCount;
	}
	public double getCartTotal() {
		return cartTotal;
	}
	public String getCatalogId() {
		return catalogId;
	}
	public String getCatalogNumber() {
		return catalogNumber;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public int getCategoryCount() {
		return categoryCount;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public Cimm2BCentralWarehouse getCimm2BCentralPricingWarehouse() {
		return cimm2BCentralPricingWarehouse;
	}
	public String getClearance() {
		return clearance;
	}
	public String getCollectionAttr() {
		return collectionAttr;
	}
	public String getComments() {
		return comments;
	}
	public int getCouponCounter() {
		return couponCounter;
	}
	public String getCpnSyncResult() {
		return cpnSyncResult;
	}
	public String getCurrency() {
		return currency;
	}
	public String getCustomerPartNumber() {
		return customerPartNumber;
	}
	public ArrayList<ProductsModel> getCustomerPartNumberList() {
		return customerPartNumberList;
	}
	public double getCustomerPrice() {
		return customerPrice;
	}
	public LinkedHashMap<String, Object> getCustomFieldVal() {
		return customFieldVal;
	}
	public String getDate() {
		return date;
	}
	public double getdDiscountValue() {
		return dDiscountValue;
	}
	public LinkedHashMap<String, String> getDefaultHashMap() {
		return defaultHashMap;
	}
	public String getDescription() {
		return description;
	}
	public String getDiscountCouponCode() {
		return discountCouponCode;
	}

	public String getDiscountType() {
		return discountType;
	}
	public String getDiscountValue() {
		return discountValue;
	}
	public String getDisplayFilterText() {
		return displayFilterText;
	}
	public String getDisplayFrieghtAlert() {
		return displayFrieghtAlert;
	}
	public String getDisplayPrice() {
		return displayPrice;
	}
	public String getDisplayPricing() {
		return displayPricing;
	}
	public String getDistributionCenter() {
		return distributionCenter;
	}
	public int getDistributionCenterAvailablity() {
		return distributionCenterAvailablity;
	}
	public String getDocCategoryType() {
		return docCategoryType;
	}
	public String getDocDesc() {
		return docDesc;
	}
	public String getDocFrom() {
		return docFrom;
	}
	public String getDocName() {
		return docName;
	}


	public String getDocType() {
		return docType;
	}
	public String getEarliestMoreDate() {
		return earliestMoreDate;
	}
	public int getEarliestMoreDays() {
		return earliestMoreDays;
	}
	public int getEarliestMoreQty() {
		return earliestMoreQty;
	}
	public String getEndDate() {
		return endDate;
	}
	public String getErpPartNumber() {
		return erpPartNumber;
	}
	public int getErpQty() {
		return erpQty;
	}
	public String getExactMatchNotFound() {
		return exactMatchNotFound;
	}
	public double getExtendedPrice() {
		return extendedPrice;
	}
	public List<RangeFacet> getFacetRange() {
		return facetRange;
	}
	public String getFeaturedEvent() {
		return featuredEvent;
	}
	public String getFrom() {
		return from;
	}
	public int getGeneralSubsetId() {
		return generalSubsetId;
	}
	public String getGetPriceFrom() {
		return getPriceFrom;
	}
	public String getGroupItemCount() {
		return groupItemCount;
	}
	public String getGroupName() {
		return groupName;
	}
	public String getGroupType() {
		return groupType;
	}
	public String getHazardousMaterial() {
		return hazardousMaterial;
	}
	public double getHeight() {
		return height;
	}
	public int getHomeBranchavailablity() {
		return homeBranchavailablity;
	}
	public double getHomeBranchavailablityDouble() {
		return homeBranchavailablityDouble;
	}
	public ArrayList<ProductsModel> getHomeBranchDetails() {
		return homeBranchDetails;
	}
	public String getIdList() {
		return idList;
	}
	public String getImageCaption() {
		return imageCaption;
	}
	public String getImageFilter() {
		return imageFilter;
	}
	public String getImageFrom() {
		return imageFrom;
	}
	public String getImageName() {
		return imageName;
	}
	public String getImagePosition() {
		return imagePosition;
	}
	public String getImageType() {
		return imageType;
	}
	public String getImageURL() {
		return imageURL;
	}
	public double getImapPrice() {
		return imapPrice;
	}
	public int getInterActiveOfferId() {
		return interActiveOfferId;
	}
	public String getInterActiveOfferName() {
		return interActiveOfferName;
	}
	public int getInterActiveOfferPosition() {
		return interActiveOfferPosition;
	}
	public String getInvoiceDesc() {
		return invoiceDesc;
	}
	public String getIsEclipseItem() {
		return isEclipseItem;
	}
	public String getIsErpItem() {
		return isErpItem;
	}
	public String getIsImap() {
		return isImap;
	}
	public String getIsSharedCart() {
		return isSharedCart;
	}
	public String getItemAttributes() {
		return itemAttributes;
	}
	public ArrayList<ProductsModel> getItemBreadCrumb() {
		return itemBreadCrumb;
	}
	public ArrayList<ProductsModel> getItemDataList() {
		return itemDataList;
	}
	public String getItemDisplayFilterText() {
		return itemDisplayFilterText;
	}
	public String getItemDocuments() {
		return itemDocuments;
	}
	public String getItemFeature() {
		return itemFeature;
	}
	public int getItemId() {
		return itemId;
	}
	public ArrayList<Integer> getItemIdList() {
		return itemIdList;
	}
	public String getItemImageFilter() {
		return itemImageFilter;
	}
	public String getItemImages() {
		return itemImages;
	}
	public String getItemLevelRequiredByDate() {
		return itemLevelRequiredByDate;
	}
	public int getItemPriceId() {
		return itemPriceId;
	}
	public int getItemResultCount() {
		return itemResultCount;
	}
	public String getItemSubset() {
		return itemSubset;
	}
	public String getItemTitleString() {
		return itemTitleString;
	}
	public String getItemUrl() {
		return itemUrl;
	}
	public String getKeyword() {
		return keyword;
	}
	public int getLeadTime() {
		return leadTime;
	}
	public double getLength() {
		return length;
	}
	public String getLevel1Menu() {
		return level1Menu;
	}
	public String getLevel2Menu() {
		return level2Menu;
	}
	public String getLevel3Menu() {
		return level3Menu;
	}
	public String getLevel4Menu() {
		return level4Menu;
	}
	public String getLevel5Menu() {
		return level5Menu;
	}
	public String getLevel6Menu() {
		return level6Menu;
	}
	public String getLevel7Menu() {
		return level7Menu;
	}
	public int getLevelNumber() {
		return levelNumber;
	}
	public String getLineItemComment() {
		return lineItemComment;
	}
	public String getLineItemCommentShipVia() {
		return lineItemCommentShipVia;
	}
	public String getLinkedItems() {
		return linkedItems;
	}
	public LinkedHashMap<String, ArrayList<ProductsModel>> getLinkedItemsList() {
		return linkedItemsList;
	}
	public String getLinkTypeName() {
		return linkTypeName;
	}
	public double getListPrice() {
		return listPrice;
	}
	public String getLongDesc() {
		return longDesc;
	}
	public int getManufacturerId() {
		return manufacturerId;
	}
	public String getManufacturerLogo() {
		return manufacturerLogo;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public String getManufacturerPartNumber() {
		return manufacturerPartNumber;
	}
	public String getMarketingText() {
		return marketingText;
	}
	public String getMaterialGroup() {
		return materialGroup;
	}
	public String getMenuId() {
		return menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public String getMetaDesc() {
		return metaDesc;
	}
	public String getMetaKeyword() {
		return metaKeyword;
	}
	public double getMinimumOrderInterval() {
		return MinimumOrderInterval;
	}
	
	
	public double getMinimumOrderQuantity() {
		return MinimumOrderQuantity;
	}
	public int getMinOrderQty() {
		return minOrderQty;
	}
	public String getModelName() {
		return modelName;
	}
	public String getMultipleShipVia() {
		return multipleShipVia;
	}
	public String getMultipleShipViaDesc() {
		return multipleShipViaDesc;
	}
	public double getNetPrice() {
		return netPrice;
	}
	public String getNotes() {
		return notes;
	}
	public int getOrderInterval() {
		return orderInterval;
	}
	public int getOrderItemId() {
		return orderItemId;
	}
	public String getOverRidePriceRule() {
		return overRidePriceRule;
	}
	public int getPackageFlag() {
		return packageFlag;
	}
	public int getPackageQty() {
		return packageQty;
	}
	public String getPackDesc() {
		return packDesc;
	}
	public String getPageName() {
		return pageName;
	}
	
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	
	public ArrayList<ProductsModel> getStaticpageBreadCrumb() {
		return staticpageBreadCrumb;
	}
	public void setStaticpageBreadCrumb(ArrayList<ProductsModel> staticpageBreadCrumb) {
		this.staticpageBreadCrumb = staticpageBreadCrumb;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public String getParentCategory() {
		return parentCategory;
	}
	public ArrayList<ProductsModel> getPartIdentifiersList() {
		return PartIdentifiersList;
	}
	public int getPartListSearchCount() {
		return partListSearchCount;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public String getPlentyDate() {
		return plentyDate;
	}
	public int getPlentyDays() {
		return plentyDays;
	}
	public double getPrice() {
		return price;
	}
	public String getPriceType() {
		return priceType;
	}
	public String getPriceTypeDesc() {
		return priceTypeDesc;
	}
	public String getPriceUOM() {
		return priceUOM;
	}
	public int getPricingQtyMultiple() {
		return pricingQtyMultiple;
	}
	public String getProductCategory() {
		return productCategory;
	}
	public String getProductCategoryImageName() {
		return productCategoryImageName;
	}
	public String getProductCategoryImageType() {
		return productCategoryImageType;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public String getProductFeature() {
		return productFeature;
	}
	public int getProductId() {
		return productId;
	}
	public int getProductItemCount() {
		return productItemCount;
	}
	public String getProductKeywords() {
		return productKeywords;
	}
	public String getProductLine() {
		return productLine;
	}
	public int getProductListId() {
		return productListId;
	}
	public String getProductListName() {
		return productListName;
	}
	public String getProductName() {
		return productName;
	}
	public int getProductSeardId() {
		return productSeardId;
	}
	public String getPromoCode() {
		return promoCode;
	}
	public int getQty() {
		return qty;
	}
	public String getQtyUOM() {
		return qtyUOM;
	}
	public ArrayList<String> getQuantityBreak() {
		return quantityBreak;
	}
	public ArrayList<ProductsModel> getQuantityBreakList() {
		return quantityBreakList;
	}
	public String getQueryString() {
		return queryString;
	}
	public int getQuoteCartId() {
		return quoteCartId;
	}
	public double getRate() {
		return rate;
	}
	public float getRating() {
		return rating;
	}
	public String getRecordDescription() {
		return recordDescription;
	}
	public String getRecordType() {
		return recordType;
	}
	public String getReferenceKey() {
		return referenceKey;
	}
	public String getRequestType() {
		return requestType;
	}
	public String getRestrictiveProduct() {
		return restrictiveProduct;
	}
	public int getResultCount() {
		return resultCount;
	}
	public String getResultDescription() {
		return resultDescription;
	}
	public float getReviewCount() {
		return reviewCount;
	}
	public String getReviewId() {
		return reviewId;
	}
	public String getReviewTime() {
		return reviewTime;
	}
	public int getSaleQty() {
		return saleQty;
	}
	public double getSalesTax() {
		return salesTax;
	}
	public String getSalesUom() {
		return salesUom;
	}
	public int getSelectedOption() {
		return selectedOption;
	}
	public LinkedHashMap<String, ProductsModel> getShipOrderQtyAndIntervalFieldVal() {
		return shipOrderQtyAndIntervalFieldVal;
	}
	public String getShipToId() {
		return shipToId;
	}
	public String getShipViaCode() {
		return shipViaCode;
	}
	public String getShipViaDesc() {
		return shipViaDesc;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public SolrQuery getSolrQuery() {
		return solrQuery;
	}
	public String getSolrSearchField() {
		return solrSearchField;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getStaticContent() {
		return staticContent;
	}
	public String getStaticPageId() {
		return staticPageId;
	}
	public String getStaticPageTitle() {
		return staticPageTitle;
	}
	public String getStatus() {
		return status;
	}
	public int getSubsetId() {
		return subsetId;
	}
	public String getSuggestedValue() {
		return suggestedValue;
	}
	public ArrayList<String> getSuggestedValueList() {
		return suggestedValueList;
	}
	public String getSwatchColorCode() {
		return swatchColorCode;
	}
	public String getTitle() {
		return title;
	}
	public String getTo() {
		return to;
	}
	public double getTotal() { 
		return total;
	}
	public int getType() {
		return type;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public double getUnitPriceCalculation() {
		return unitPriceCalculation;
	}
	public double getUnits() {
		return units;
	}
	public double getUnitsPerStocking() {
		return unitsPerStocking;
	}
	public String getUnitsPerStockingString() {
		return unitsPerStockingString;
	}
	public String getUnspc() {
		return unspc;
	}
	public String getUom() {
		return uom;
	}
	public ArrayList<ProductsModel> getUomList() {
		return uomList;
	}
	public int getUomQty() {
		return uomQty;
	}
	public String getUpc() {
		return upc;
	}
	public String getUserAdded() {
		return userAdded;
	}
	public int getUserId() {
		return userId;
	}
	public double getVendorNumber() {
		return vendorNumber;
	}
	public String getVideoCaption() {
		return videoCaption;
	}
	public String getVideoName() {
		return videoName;
	}
	public String getVideoType() {
		return videoType;
	}
	public String getWareHouseCode() {
		return wareHouseCode;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public double getWeight() {
		return weight;
	}
	public String getWeightUom() {
		return weightUom;
	}
	public double getWidth() {
		return width;
	}
	public boolean isFlag() {
		return flag;
	}
	public boolean isFoundInWhseFlag() {
		return isFoundInWhseFlag;
	}
	public boolean isPriceBreaksExist() {
		return isPriceBreaksExist;
	}
	public void setAltPartNumber1(String altPartNumber1) {
		this.altPartNumber1 = altPartNumber1;
	}
	public void setAltPartNumber2(String altPartNumber2) {
		this.altPartNumber2 = altPartNumber2;
	}
	public void setAppvalsenderUserID(String appvalsenderUserID) {
		this.appvalsenderUserID = appvalsenderUserID;
	}
	public void setAssignedShipTo(int assignedShipTo) {
		this.assignedShipTo = assignedShipTo;
	}
	public void setAttrFilterList(
			LinkedHashMap<String, ArrayList<ProductsModel>> attrFilterList) {
		this.attrFilterList = attrFilterList;
	}
	public void setAttributeDataList(ArrayList<ProductsModel> attributeDataList) {
		this.attributeDataList = attributeDataList;
	}
	public void setAttrId(int attrId) {
		this.attrId = attrId;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public void setAttrNameEncoded(String attrNameEncoded) {
		this.attrNameEncoded = attrNameEncoded;
	}
	public void setAttrType(int attrType) {
		this.attrType = attrType;
	}
	public void setAttrUom(String attrUom) {
		this.attrUom = attrUom;
	}
	public void setAttrUomId(int attrUomId) {
		this.attrUomId = attrUomId;
	}
	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}
	public void setAttrValueEncoded(String attrValueEncoded) {
		this.attrValueEncoded = attrValueEncoded;
	}
	public void setAttrValueId(int attrValueId) {
		this.attrValueId = attrValueId;
	}
	public void setAvailQty(int availQty) {
		this.availQty = availQty;
	}
	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
	public void setBranchAddress1(String branchAddress1) {
		this.branchAddress1 = branchAddress1;
	}
	public void setBranchAddress2(String branchAddress2) {
		this.branchAddress2 = branchAddress2;
	}
	public void setBranchAvail(ArrayList<ProductsModel> branchAvail) {
		this.branchAvail = branchAvail;
	}
	public void setBranchAvailability(String branchAvailability) {
		this.branchAvailability = branchAvailability;
	}
	public void setBranchAvailQty(String branchAvailQty) {
		this.branchAvailQty = branchAvailQty;
	}
	public void setBranchCity(String branchCity) {
		this.branchCity = branchCity;
	}
	public void setBranchCountry(String branchCountry) {
		this.branchCountry = branchCountry;
	}
	public void setBranchDistance(Double branchDistance) {
		this.branchDistance = branchDistance;
	}
	public void setBranchEarliestAvailableInfo(String branchEarliestAvailableInfo) {
		this.branchEarliestAvailableInfo = branchEarliestAvailableInfo;
	}
	public void setBranchEmail(String branchEmail) {
		this.branchEmail = branchEmail;
	}
	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}
	public void setBranchLatitude(String branchLatitude) {
		this.branchLatitude = branchLatitude;
	}
	public void setBranchLongitude(String branchLongitude) {
		this.branchLongitude = branchLongitude;
	}
	public void setBranchMinOrderOrderIntervalList(
			ArrayList<ProductsModel> branchMinOrderOrderIntervalList) {
		this.branchMinOrderOrderIntervalList = branchMinOrderOrderIntervalList;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public void setBranchPhoneNumber(String branchPhoneNumber) {
		this.branchPhoneNumber = branchPhoneNumber;
	}
	public void setBranchPostalCode(String branchPostalCode) {
		this.branchPostalCode = branchPostalCode;
	}
	public void setBranchState(String branchState) {
		this.branchState = branchState;
	}
	public void setBranchTeritory(
			LinkedHashMap<String, ProductsModel> branchTeritory) {
		this.branchTeritory = branchTeritory;
	}
	public void setBranchTollFreeNumber(String branchTollFreeNumber) {
		this.branchTollFreeNumber = branchTollFreeNumber;
	}
	public void setBranchTotalQty(int branchTotalQty) {
		this.branchTotalQty = branchTotalQty;
	}
	public void setBranchWorkingHours(String branchWorkingHours) {
		this.branchWorkingHours = branchWorkingHours;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	public void setBrandImage(String brandImage) {
		this.brandImage = brandImage;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public void setBrandUrl(String brandUrl) {
		this.brandUrl = brandUrl;
	}
	public void setCartCaseCount(int cartCaseCount) {
		this.cartCaseCount = cartCaseCount;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public void setCartIndividualItemQuantitySum(int cartIndividualItemQuantitySum) {
		this.cartIndividualItemQuantitySum = cartIndividualItemQuantitySum;
	}
	public void setCartItemCount(int cartItemCount) {
		this.cartItemCount = cartItemCount;
	}
	public void setCartTotal(double cartTotal) {
		this.cartTotal = cartTotal;
	}
	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}
	public void setCatalogNumber(String catalogNumber) {
		this.catalogNumber = catalogNumber;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public void setCategoryCount(int categoryCount) {
		this.categoryCount = categoryCount;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public void setCimm2BCentralPricingWarehouse(Cimm2BCentralWarehouse cimm2bCentralPricingWarehouse) {
		cimm2BCentralPricingWarehouse = cimm2bCentralPricingWarehouse;
	}
	public void setClearance(String clearance) {
		this.clearance = clearance;
	}
	public void setCollectionAttr(String collectionAttr) {
		this.collectionAttr = collectionAttr;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public void setCouponCounter(int couponCounter) {
		this.couponCounter = couponCounter;
	}
	public void setCpnSyncResult(String cpnSyncResult) {
		this.cpnSyncResult = cpnSyncResult;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public void setCustomerPartNumber(String customerPartNumber) {
		this.customerPartNumber = customerPartNumber;
	}
	public void setCustomerPartNumberList(ArrayList<ProductsModel> customerPartNumberList) {
		this.customerPartNumberList = customerPartNumberList;
	}
	public void setCustomerPrice(double customerPrice) {
		this.customerPrice = customerPrice;
	}
	public void setCustomFieldVal(LinkedHashMap<String, Object> customFieldVal) {
		this.customFieldVal = customFieldVal;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setdDiscountValue(double dDiscountValue) {
		this.dDiscountValue = dDiscountValue;
	}
	public void setDefaultHashMap(LinkedHashMap<String, String> defaultHashMap) {
		this.defaultHashMap = defaultHashMap;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setDiscountCouponCode(String discountCouponCode) {
		this.discountCouponCode = discountCouponCode;
	}
	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}
	public void setDiscountValue(String discountValue) {
		this.discountValue = discountValue;
	}
	public void setDisplayFilterText(String displayFilterText) {
		this.displayFilterText = displayFilterText;
	}
	public void setDisplayFrieghtAlert(String displayFrieghtAlert) {
		this.displayFrieghtAlert = displayFrieghtAlert;
	}
	public void setDisplayPrice(String displayPrice) {
		this.displayPrice = displayPrice;
	}
	public void setDisplayPricing(String displayPricing) {
		this.displayPricing = displayPricing;
	}
	public void setDistributionCenter(String distributionCenter) {
		this.distributionCenter = distributionCenter;
	}
	public void setDistributionCenterAvailablity(int distributionCenterAvailablity) {
		this.distributionCenterAvailablity = distributionCenterAvailablity;
	}
	public void setDocCategoryType(String docCategoryType) {
		this.docCategoryType = docCategoryType;
	}
	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}
	public void setDocFrom(String docFrom) {
		this.docFrom = docFrom;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public void setEarliestMoreDate(String earliestMoreDate) {
		this.earliestMoreDate = earliestMoreDate;
	}
	public void setEarliestMoreDays(int earliestMoreDays) {
		this.earliestMoreDays = earliestMoreDays;
	}
	public void setEarliestMoreQty(int earliestMoreQty) {
		this.earliestMoreQty = earliestMoreQty;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public void setErpPartNumber(String erpPartNumber) {
		this.erpPartNumber = erpPartNumber;
	}
	public void setErpQty(int erpQty) {
		this.erpQty = erpQty;
	}
	public void setExactMatchNotFound(String exactMatchNotFound) {
		this.exactMatchNotFound = exactMatchNotFound;
	}
	public void setExtendedPrice(double extendedPrice) {
		this.extendedPrice = extendedPrice;
	}
	public void setFacetRange(List<RangeFacet> facetRange) {
		this.facetRange = facetRange;
	}
	public void setFeaturedEvent(String featuredEvent) {
		this.featuredEvent = featuredEvent;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public void setFoundInWhseFlag(boolean isFoundInWhseFlag) {
		this.isFoundInWhseFlag = isFoundInWhseFlag;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public void setGeneralSubsetId(int generalSubsetId) {
		this.generalSubsetId = generalSubsetId;
	}
	public void setGetPriceFrom(String getPriceFrom) {
		this.getPriceFrom = getPriceFrom;
	}
	public void setGroupItemCount(String groupItemCount) {
		this.groupItemCount = groupItemCount;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public void setHazardousMaterial(String hazardousMaterial) {
		this.hazardousMaterial = hazardousMaterial;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public void setHomeBranchavailablity(int homeBranchavailablity) {
		this.homeBranchavailablity = homeBranchavailablity;
	}
	public void setHomeBranchavailablityDouble(double homeBranchavailablityDouble) {
		this.homeBranchavailablityDouble = homeBranchavailablityDouble;
	}
	public void setHomeBranchDetails(ArrayList<ProductsModel> homeBranchDetails) {
		this.homeBranchDetails = homeBranchDetails;
	}
	public void setIdList(String idList) {
		this.idList = idList;
	}
	public void setImageCaption(String imageCaption) {
		this.imageCaption = imageCaption;
	}
	public void setImageFilter(String imageFIlter) {
		this.imageFilter = imageFIlter;
	}
	public void setImageFrom(String imageFrom) {
		this.imageFrom = imageFrom;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public void setImagePosition(String imagePosition) {
		this.imagePosition = imagePosition;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public void setImapPrice(double imapPrice) {
		this.imapPrice = imapPrice;
	}
	public void setInterActiveOfferId(int interActiveOfferId) {
		this.interActiveOfferId = interActiveOfferId;
	}
	public void setInterActiveOfferName(String interActiveOfferName) {
		this.interActiveOfferName = interActiveOfferName;
	}
	public void setInterActiveOfferPosition(int interActiveOfferPosition) {
		this.interActiveOfferPosition = interActiveOfferPosition;
	}
	public void setInvoiceDesc(String invoiceDesc) {
		this.invoiceDesc = invoiceDesc;
	}
	public void setIsEclipseItem(String isEclipseItem) {
		this.isEclipseItem = isEclipseItem;
	}
	public void setIsErpItem(String isErpItem) {
		this.isErpItem = isErpItem;
	}
	public void setIsImap(String isImap) {
		this.isImap = isImap;
	}
	public void setIsSharedCart(String isSharedCart) {
		this.isSharedCart = isSharedCart;
	}
	public void setItemAttributes(String itemAttributes) {
		this.itemAttributes = itemAttributes;
	}
	public void setItemBreadCrumb(ArrayList<ProductsModel> itemBreadCrumb) {
		this.itemBreadCrumb = itemBreadCrumb;
	}
	public void setItemDataList(ArrayList<ProductsModel> itemDataList) {
		this.itemDataList = itemDataList;
	}
	public void setItemDisplayFilterText(String itemDisplayFilterText) {
		this.itemDisplayFilterText = itemDisplayFilterText;
	}
	public void setItemDocuments(String itemDocuments) {
		this.itemDocuments = itemDocuments;
	}
	public void setItemFeature(String itemFeature) {
		this.itemFeature = itemFeature;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public void setItemIdList(ArrayList<Integer> itemIdList) {
		this.itemIdList = itemIdList;
	}
	public void setItemImageFilter(String itemImageFilter) {
		this.itemImageFilter = itemImageFilter;
	}
	public void setItemImages(String itemImages) {
		this.itemImages = itemImages;
	}
	public void setItemLevelRequiredByDate(String itemLevelRequiredByDate) {
		this.itemLevelRequiredByDate = itemLevelRequiredByDate;
	}
	public void setItemPriceId(int itemPriceId) {
		this.itemPriceId = itemPriceId;
	}
	public void setItemResultCount(int itemResultCount) {
		this.itemResultCount = itemResultCount;
	}
	public void setItemSubset(String itemSubset) {
		this.itemSubset = itemSubset;
	}
	public void setItemTitleString(String itemTitleString) {
		this.itemTitleString = itemTitleString;
	}
	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public void setLeadTime(int leadTime) {
		this.leadTime = leadTime;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public void setLevel1Menu(String level1Menu) {
		this.level1Menu = level1Menu;
	}
	public void setLevel2Menu(String level2Menu) {
		this.level2Menu = level2Menu;
	}
	public void setLevel3Menu(String level3Menu) {
		this.level3Menu = level3Menu;
	}
	public void setLevel4Menu(String level4Menu) {
		this.level4Menu = level4Menu;
	}
	public void setLevel5Menu(String level5Menu) {
		this.level5Menu = level5Menu;
	}
	
	public void setLevel6Menu(String level6Menu) {
		this.level6Menu = level6Menu;
	}
	public void setLevel7Menu(String level7Menu) {
		this.level7Menu = level7Menu;
	}
	
	public void setLevelNumber(int levelNumber) {
		this.levelNumber = levelNumber;
	}
	public void setLineItemComment(String lineItemComment) {
		this.lineItemComment = lineItemComment;
	}
	public void setLineItemCommentShipVia(String lineItemCommentShipVia) {
		this.lineItemCommentShipVia = lineItemCommentShipVia;
	}
	public void setLinkedItems(String linkedItems) {
		this.linkedItems = linkedItems;
	}
	public void setLinkedItemsList(LinkedHashMap<String, ArrayList<ProductsModel>> linkedItemsList) {
		this.linkedItemsList = linkedItemsList;
	}
	public void setLinkTypeName(String linkTypeName) {
		this.linkTypeName = linkTypeName;
	}
	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}
	public void setManufacturerId(int manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public void setManufacturerLogo(String manufacturerLogo) {
		this.manufacturerLogo = manufacturerLogo;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public void setManufacturerPartNumber(String manufacturerPartNumber) {
		this.manufacturerPartNumber = manufacturerPartNumber;
	}
	public void setMarketingText(String marketingText) {
		this.marketingText = marketingText;
	}
	public void setMaterialGroup(String materialGroup) {
		this.materialGroup = materialGroup;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public void setMetaDesc(String metaDesc) {
		this.metaDesc = metaDesc;
	}
	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}
	
	public void setMinimumOrderInterval(double minimumOrderInterval) {
		MinimumOrderInterval = minimumOrderInterval;
	}
	public void setMinimumOrderQuantity(double minimumOrderQuantity) {
		MinimumOrderQuantity = minimumOrderQuantity;
	}
	public void setMinOrderQty(int minOrderQty) {
		this.minOrderQty = minOrderQty;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public void setMultipleShipVia(String multipleShipVia) {
		this.multipleShipVia = multipleShipVia;
	}
	public void setMultipleShipViaDesc(String multipleShipViaDesc) {
		this.multipleShipViaDesc = multipleShipViaDesc;
	}
	public void setNetPrice(double netPrice) {
		this.netPrice = netPrice;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public void setOrderInterval(int orderInterval) {
		this.orderInterval = orderInterval;
	}
	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}
	public void setOverRidePriceRule(String overRidePriceRule) {
		this.overRidePriceRule = overRidePriceRule;
	}
	public void setPackageFlag(int packageFlag) {
		this.packageFlag = packageFlag;
	}
	public void setPackageQty(int packageQty) {
		this.packageQty = packageQty;
	}
	public void setPackDesc(String packDesc) {
		this.packDesc = packDesc;
	}
	
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
	
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public void setParentCategory(String parentCategory) {
		this.parentCategory = parentCategory;
	}

	public void setPartIdentifiersList(ArrayList<ProductsModel> partIdentifiersList) {
		PartIdentifiersList = partIdentifiersList;
	}
	public void setPartListSearchCount(int partListSearchCount) {
		this.partListSearchCount = partListSearchCount;
	}

	




	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public void setPlentyDate(String plentyDate) {
		this.plentyDate = plentyDate;
	}
	public void setPlentyDays(int plentyDays) {
		this.plentyDays = plentyDays;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public void setPriceBreaksExist(boolean isPriceBreaksExist) {
		this.isPriceBreaksExist = isPriceBreaksExist;
	}
	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	public void setPriceTypeDesc(String priceTypeDesc) {
		this.priceTypeDesc = priceTypeDesc;
	}
	public void setPriceUOM(String priceUOM) {
		this.priceUOM = priceUOM;
	}
	public void setPricingQtyMultiple(int pricingQtyMultiple) {
		this.pricingQtyMultiple = pricingQtyMultiple;
	}
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	public void setProductCategoryImageName(String productCategoryImageName) {
		this.productCategoryImageName = productCategoryImageName;
	}
	public void setProductCategoryImageType(String productCategoryImageType) {
		this.productCategoryImageType = productCategoryImageType;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public void setProductFeature(String productFeature) {
		this.productFeature = productFeature;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public void setProductItemCount(int productItemCount) {
		this.productItemCount = productItemCount;
	}
	public void setProductKeywords(String productKeywords) {
		this.productKeywords = productKeywords;
	}
	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}
	public void setProductListId(int productListId) {
		this.productListId = productListId;
	}
	public void setProductListName(String productListName) {
		this.productListName = productListName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setProductSeardId(int productSeardId) {
		this.productSeardId = productSeardId;
	}
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public void setQtyUOM(String qtyUOM) {
		this.qtyUOM = qtyUOM;
	}
	public void setQuantityBreak(ArrayList<String> quantityBreak) {
		this.quantityBreak = quantityBreak;
	}
	public void setQuantityBreakList(ArrayList<ProductsModel> quantityBreakList) {
		this.quantityBreakList = quantityBreakList;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public void setQuoteCartId(int quoteCartId) {
		this.quoteCartId = quoteCartId;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public void setRecordDescription(String recordDescription) {
		this.recordDescription = recordDescription;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public void setReferenceKey(String referenceKey) {
		this.referenceKey = referenceKey;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public void setRestrictiveProduct(String restrictiveProduct) {
		this.restrictiveProduct = restrictiveProduct;
	}
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}
	public void setReviewCount(float reviewCount) {
		this.reviewCount = reviewCount;
	}
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	public void setReviewTime(String reviewTime) {
		this.reviewTime = reviewTime;
	}
	public void setSaleQty(int saleQty) {
		this.saleQty = saleQty;
	}
	public void setSalesTax(double salesTax) {
		this.salesTax = salesTax;
	}
	public void setSalesUom(String salesUom) {
		this.salesUom = salesUom;
	}
	public void setSelectedOption(int selectedOption) {
		this.selectedOption = selectedOption;
	}
	public void setShipOrderQtyAndIntervalFieldVal(
			LinkedHashMap<String, ProductsModel> shipOrderQtyAndIntervalFieldVal) {
		this.shipOrderQtyAndIntervalFieldVal = shipOrderQtyAndIntervalFieldVal;
	}
	public void setShipToId(String shipToId) {
		this.shipToId = shipToId;
	}
	public void setShipViaCode(String shipViaCode) {
		this.shipViaCode = shipViaCode;
	}
	public void setShipViaDesc(String shipViaDesc) {
		this.shipViaDesc = shipViaDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public void setSolrQuery(SolrQuery solrQuery) {
		this.solrQuery = solrQuery;
	}
	public void setSolrSearchField(String solrSearchField) {
		this.solrSearchField = solrSearchField;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setStaticContent(String staticContent) {
		this.staticContent = staticContent;
	}
	public void setStaticPageId(String staticPageId) {
		this.staticPageId = staticPageId;
	}
	public void setStaticPageTitle(String staticPageTitle) {
		this.staticPageTitle = staticPageTitle;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setSubsetId(int subsetId) {
		this.subsetId = subsetId;
	}
	public void setSuggestedValue(String suggestedValue) {
		this.suggestedValue = suggestedValue;
	}
	public void setSuggestedValueList(ArrayList<String> suggestedValueList) {
		this.suggestedValueList = suggestedValueList;
	}
	public void setSwatchColorCode(String swatchColorCode) {
		this.swatchColorCode = swatchColorCode;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public void setUnitPriceCalculation(double unitPriceCalculation) {
		this.unitPriceCalculation = unitPriceCalculation;
	}
	public void setUnits(double units) {
		this.units = units;
	}
	public void setUnitsPerStocking(double unitsPerStocking) {
		this.unitsPerStocking = unitsPerStocking;
	}
	public void setUnitsPerStockingString(String unitsPerStockingString) {
		this.unitsPerStockingString = unitsPerStockingString;
	}

	public void setUnspc(String unspc) {
		this.unspc = unspc;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public void setUomList(ArrayList<ProductsModel> uomList) {
		this.uomList = uomList;
	}
	public void setUomQty(int uomQty) {
		this.uomQty = uomQty;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	public void setUserAdded(String userAdded) {
		this.userAdded = userAdded;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setVendorNumber(double vendorNumber) {
		this.vendorNumber = vendorNumber;
	}
	public void setVideoCaption(String videoCaption) {
		this.videoCaption = videoCaption;
	}
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}
	public void setWareHouseCode(String wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}


	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public ArrayList<Cimm2BCentralShipMethodQuantity> getShipMethodQuantity() {
		return shipMethodQuantity;
	}
	public void setShipMethodQuantity(
			ArrayList<Cimm2BCentralShipMethodQuantity> shipMethodQuantity) {
		this.shipMethodQuantity = shipMethodQuantity;
	}
	public void setWeightUom(String weightUom) {
		this.weightUom = weightUom;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getEarliestMoreQuantity() {
		return earliestMoreQuantity;
	}
	public void setEarliestMoreQuantity(double earliestMoreQuantity) {
		this.earliestMoreQuantity = earliestMoreQuantity;
	}
	public double getPromoPrice() {
		return promoPrice;
	}
	public void setPromoPrice(double promoPrice) {
		this.promoPrice = promoPrice;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public double getOrderQuantityInterval() {
		return orderQuantityInterval;
	}
	public void setOrderQuantityInterval(double orderQuantityInterval) {
		this.orderQuantityInterval = orderQuantityInterval;
	}
	public String getFeaturedCategory() {
		return featuredCategory;
	}
	public void setFeaturedCategory(String featuredCategory) {
		this.featuredCategory = featuredCategory;
	}
	public String getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}
	public String getOrderOrQuoteNumber() {
		return orderOrQuoteNumber;
	}
	public void setOrderOrQuoteNumber(String orderOrQuoteNumber) {
		this.orderOrQuoteNumber = orderOrQuoteNumber;
	}
	public ArrayList<Integer> getPartIdentifierQuantity() {
		return partIdentifierQuantity;
	}
	public void setPartIdentifierQuantity(ArrayList<Integer> partIdentifierQuantity) {
		this.partIdentifierQuantity = partIdentifierQuantity;
	}
	public int getUomQuantity() {
		return uomQuantity;
	}
	public void setUomQuantity(int uomQuantity) {
		this.uomQuantity = uomQuantity;
	}
	public int getBcAddressBookId() {
		return bcAddressBookId;
	}
	public void setBcAddressBookId(int bcAddressBookId) {
		this.bcAddressBookId = bcAddressBookId;
	}
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	public String getSupplierSku() {
		return supplierSku;
	}
	public void setSupplierSku(String supplierSku) {
		this.supplierSku = supplierSku;
	}
	public String getStoreSku() {
		return storeSku;
	}
	public void setStoreSku(String storeSku) {
		this.storeSku = storeSku;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public boolean isItemDiscontinued() {
		return itemDiscontinued;
	}
	public void setItemDiscontinued(boolean itemDiscontinued) {
		this.itemDiscontinued = itemDiscontinued;
	}
	public String getAdditionalProperties() {
		return additionalProperties;
	}
	public void setAdditionalProperties(String additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	public String getSupercedeurl() {
		return supercedeurl;
	}
	public void setSupercedeurl(String supercedeurl) {
		this.supercedeurl = supercedeurl;
	}
	public String getSharedBy() {
		return sharedBy;
	}
	public void setSharedBy(String sharedBy) {
		this.sharedBy = sharedBy;
	}
	public String getStockStatus() {
		return stockStatus;
	}
	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}
	public int getDesignId() {
		return designId;
	}
	public void setDesignId(int designId) {
		this.designId = designId;
	}
	public String getRushFlag() {
		return rushFlag;
	}
	public void setRushFlag(String rushFlag) {
		this.rushFlag = rushFlag;
	}
	public boolean isCalculateTax() {
		return calculateTax;
	}
	public void setCalculateTax(boolean calculateTax) {
		this.calculateTax = calculateTax;
	}
	public boolean isDesignFees() {
		return designFees;
	}
	public void setDesignFees(boolean designFees) {
		this.designFees = designFees;
	}
	public String getAvailableDescription() {
		return availableDescription;
	}
	public void setAvailableDescription(String availableDescription) {
		this.availableDescription = availableDescription;
	}
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
	public String getWarehouseErrorMsg() {
		return warehouseErrorMsg;
	}
	public void setWarehouseErrorMsg(String warehouseErrorMsg) {
		this.warehouseErrorMsg = warehouseErrorMsg;
	}
	public String getBlanketPoID() {
		return blanketPoID;
	}
	public void setBlanketPoID(String blanketPoID) {
		this.blanketPoID = blanketPoID;
	}
	public LinkedHashMap<String, String> getCustomFieldListValues() {
		return customFieldListValues;
	}
	public void setCustomFieldListValues(LinkedHashMap<String, String> customFieldList) {
		this.customFieldListValues = customFieldList;
	}
	public String getRentals() {
		return rentals;
	}
	public void setRentals(String rentals) {
		this.rentals = rentals;
	}
	public String getCustomDescription() {
		return customDescription;
	}
	public void setCustomDescription(String customDescription) {
		this.customDescription = customDescription;
	}
	
}