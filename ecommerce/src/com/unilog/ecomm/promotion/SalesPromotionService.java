package com.unilog.ecomm.promotion;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.drools.core.io.impl.UrlResource;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.unilog.database.CommonDBQuery;
import com.unilog.ecomm.model.Cart;
import com.unilog.ecomm.model.Coupon;
import com.unilog.ecomm.model.CouponCategory;
import com.unilog.ecomm.model.Discount;
import com.unilog.ecomm.model.DiscountType;
import com.unilog.ecomm.model.LineItem;
import com.unilog.ecomm.model.Status;
import com.unilog.ecomm.promotion.dao.SalesPromotionDAOService;
import com.unilog.ecomm.promotion.exception.SalesPromotionException;


public class SalesPromotionService {
	
	private static Logger logger = Logger.getLogger(SalesPromotionService.class.getName());
	private static SalesPromotionService serviceInstance;
	
	private KieModule kModule;
	private KieServices ks;
	private boolean loadModule;
	
	private SalesPromotionService() {
		// avoid direct instance
	}
	
	public static SalesPromotionService getInstance() {
		synchronized(SalesPromotionService.class) {
			if(serviceInstance==null) {
				serviceInstance = new SalesPromotionService();
			}
		}
		return serviceInstance;
	}
	
	
	/**
	 * Checks order and item level discounts and updates the Cart and Line Item Instances
	 *  with Discount instances to availed discount property and discount values if available.
	 * 
	 * @param cart Cart with Line item, Customer group Name, User Group Name etc. See Rules 
	 * template in Cimm for details of the parameters used.
	 * 
	 * @param discountCoupons List of coupons Entered by User / A dummy coupon if user does not 
	 * enter and for auto applied coupons at first time
	 * 
	 * @throws SalesPromotionException If any error occurs during process.
	 */
	public void checkDiscount(Cart cart,List<Coupon> discountCoupons)throws SalesPromotionException{
	
		String droolsDiscountSession = "discountsession";
		
		Map<String, Long> couponsUsed = null;
		
		try{
			 if(cart == null){
				throw new IllegalArgumentException("Cart is null");
			 }
			 if(CommonDBQuery.getSystemParamtersList().get("discountsession")!=null){
			    droolsDiscountSession = CommonDBQuery.getSystemParamtersList().get("discountsession").trim();
			 }
		    loadKieModule();
		    KieContainer kContainer = ks.newKieContainer(kModule.getReleaseId());
		    KieSession kSession = kContainer.newKieSession(droolsDiscountSession);    
			kSession.insert(cart);
			//inserting invalid coupon
			Coupon coupon = new Coupon();
			coupon.setCopounCode("zzzzzzzz");
			kSession.insert(coupon);
			for(LineItem lineItem : cart.getLineItems()){
				kSession.insert(lineItem);
			}
			kSession.fireAllRules();
			kSession.dispose();
			// fetch coupons usage from db 
			couponsUsed = SalesPromotionDAOService.getInstance().getCouponsUsed(cart.getUserId(),cart.getBuyingCompanyId());
			
			calculateItemsDiscount(cart,discountCoupons,couponsUsed);
			calcualteCartTotal(cart);
			
			// order level discount check
		    kSession = kContainer.newKieSession(droolsDiscountSession);    
			kSession.insert(cart);
			
			//inserting invalid coupon
			coupon = new Coupon();
			coupon.setCopounCode("zzzzzzzz");
			kSession.insert(coupon);
			
			kSession.fireAllRules();
			kSession.dispose();
			
			calculateOrderDiscount(cart,discountCoupons,couponsUsed);
			
		}catch(Exception e){
			logger.error("Exception occured while calculating the sales discount", e);
			throw new SalesPromotionException("Error Ocuured while calculating sales discounts", e);
		}
	}
	
	
	/**
	 * Stores the coupon record in data base at user / Customer level.
	 * 
	 * @param discount Discount object to save in data base.
	 * @param orderNo Cimm Order no in for which discount applied.
	 * @param userId Currently logged in User Id. This will be used for storing user level discounts and User Updated field value.
	 * @param buyingCompanyId Cimm Buying company id
	 * @param website Website in which the Discount is used
	 * @return true if Discount is stored  successfully in db, else false. 
	 * @throws Exception if any error occurs during the process.
	 */
	public boolean storeCouponUsed(Discount discount, String orderNo, long userId, long buyingCompanyId,
			String website) throws Exception{
		
		if(discount == null ){
			
			throw new IllegalArgumentException("Discount is null");
		}
		
		if( orderNo == null) {
			
			throw new IllegalArgumentException("Order no is null");
			
		}
		
		if(  userId <= 0 ){
			
			throw new IllegalArgumentException("User Id is invalid");
			
		}
		
		if(website == null){
			
			throw new IllegalArgumentException("Website is null");
			
		}
		
		if(buyingCompanyId <= 0){
			throw new IllegalArgumentException("Buying company id  is not valid");
			
		}
		
		//validate the coupons before saving 
		Map<String,Long> couponsUsed = SalesPromotionDAOService.getInstance().getCouponsUsed(userId,buyingCompanyId);
		
		String couponCode = discount.getDiscountCoupon().getCopounCode().toUpperCase();
		if(couponsUsed.get(couponCode) != null 
				&& couponsUsed.get(couponCode) >= discount.getDiscountCoupon().getTotalUses()){
			discount.getDiscountCoupon().setValid(false);
		} 
			
		if(discount.getDiscountCoupon().isValid()){
			return SalesPromotionDAOService.getInstance().storeCouponUsed(discount,orderNo,userId,buyingCompanyId,website);
		}
			
		return false;
	}
	
	private void loadKieModule() throws Exception{
		
		synchronized(this){
			if(kModule == null ||isLoadModule()){
				try{
					String url = "https://973ecb.cimm2.com/kiewbwildfly862cr4/maven2/com/unilog/salespromov2/1.0/salespromov2-1.0.jar";
					System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
					ks = KieServices.Factory.get();
					KieRepository kr = ks.getRepository();
					
					if(CommonDBQuery.getSystemParamtersList().get("DISCOUNT_RULE_URL")!=null){
						url = CommonDBQuery.getSystemParamtersList().get("DISCOUNT_RULE_URL").trim();
					}
					
					UrlResource urlResource = (UrlResource) ks.getResources().newUrlResource(url);
				    
				    if(CommonDBQuery.getSystemParamtersList().get("DROOLS_USER_ID")!=null){
						
						urlResource.setUsername(CommonDBQuery.getSystemParamtersList().get("DROOLS_USER_ID").trim());
					}
					
					if(CommonDBQuery.getSystemParamtersList().get("DROOLS_PASSWORD")!=null){
						
						 urlResource.setPassword(CommonDBQuery.getSystemParamtersList().get("DROOLS_PASSWORD").trim());
					}
				    
				    urlResource.setBasicAuthentication("enabled");
				    InputStream is = urlResource.getInputStream();
				    kModule = kr.addKieModule(ks.getResources().newInputStreamResource(is));
				    
				   loadModule = false;
					   
				}catch(Exception e){
					//throw e;
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	private  void calculateItemsDiscount(Cart cart,List<Coupon> discountCoupons, Map<String, Long> couponsUsed){
		if(cart != null && cart.getLineItems() != null && !cart.getLineItems().isEmpty()){
			
			for(LineItem item: cart.getLineItems()){
				try{
					for(Discount itemDiscount : item.getDiscounts()){
							
						if(itemDiscount.getDiscountInPercent() > 0.00){
							itemDiscount.setDiscountValue(item.getExtendedPrice() * itemDiscount.getDiscountInPercent() / 100);
						}
							
						validateItemDiscountCoupon(itemDiscount,discountCoupons, couponsUsed);
							
						if(itemDiscount.getDiscountCoupon().isValid() && itemDiscount.getDiscountValue() >  item.getDiscount()){
							item.setAvailedDiscount(itemDiscount);
							item.setDiscount(itemDiscount.getDiscountValue());
							cart.setItemDiscountAvailed(true);
						}
						
					}
				}catch(Exception e){
					logger.error("Exception occured while calculating the sales discount for item " + item, e);
				}
					
				item.setNetPrice(item.getExtendedPrice() - item.getDiscount());
			}
		}	
	}
	
	private void calcualteCartTotal(Cart c){
		List<LineItem> cartItems = c.getLineItems();
		
		for(LineItem item : cartItems){
			c.setTotal(c.getTotal() + item.getNetPrice());
		}	
	}
	
	private void calculateOrderDiscount(Cart cart,List<Coupon> discountCoupons, Map<String, Long> couponsUsed) throws SalesPromotionException{
		List<String> stackableCoupons =  null;
		List<String> nonShipChargeShipMethods = null;
		String selectedShipMethod = null;
		
		try{
			
			stackableCoupons = getStackableDiscounts(cart.getDiscounts());
			selectedShipMethod = cart.getSelectedShipMethod();
			
			if(cart.getNonShipChargeShipMethods()!= null){
				String[] aNonShipChargeShippMethods = cart.getNonShipChargeShipMethods().toUpperCase().split(",");
				nonShipChargeShipMethods = new ArrayList<String>();
				
				for(String shipMethod: aNonShipChargeShippMethods){
					nonShipChargeShipMethods.add(shipMethod);
				}
			}
		
			if(stackableCoupons.size() > 0){
				cart.setAppliedDiscountStrategy("Compound discount calculation");
				calculateOrderCompoundDiscount(cart, discountCoupons, couponsUsed, stackableCoupons,selectedShipMethod,nonShipChargeShipMethods);	
			}else {
				cart.setAppliedDiscountStrategy("Simple discount calculation");
				calculateOrderSimpleDiscount(cart,discountCoupons,couponsUsed,selectedShipMethod,nonShipChargeShipMethods);
			}	
			
			addRejectedCoupons(cart,discountCoupons);
			
		}catch(Exception e){
			throw new SalesPromotionException("Error occured while calculating order discount",e);
		}	
	}
	
	private void calculateOrderSimpleDiscount(Cart cart,List<Coupon> discountCoupons, Map<String, Long> couponsUsed,
			String selectedShipMethod, List<String> nonShipChargeShipMethods) throws SalesPromotionException{
		try{
			
			List<Discount> orderLevelDiscounts = cart.getDiscounts();
						
			for(Discount discount: orderLevelDiscounts){
				validateOrderSimpleDiscountCoupon(discount,cart,discountCoupons,couponsUsed, selectedShipMethod, nonShipChargeShipMethods);
				
				if(discount.getDiscountCoupon().isValid() && discount.getDiscountType() == DiscountType.FREE_SHIPPING){		
					cart.setAvailedShippingDiscount(discount);
					cart.setFreeShipping(true);
					updateCouponsUsed(discount.getDiscountCoupon().getCopounCode(),couponsUsed);
					continue;	
				} else if(discount.getDiscountCoupon().isValid() && discount.getDiscountType() != DiscountType.FREE_SHIPPING) {
					if(discount.getDiscountInPercent() > 0.00){
						discount.setDiscountValue(cart.getTotal() * discount.getDiscountInPercent() / 100);
						updateCouponsUsed(discount.getDiscountCoupon().getCopounCode(),couponsUsed);
					} 
					if(discount.getDiscountValue() > cart.getDiscount()){
						List<Discount> simpleDiscount = new ArrayList<Discount>();
						simpleDiscount.add(discount);
						cart.setAvailedDiscounts(simpleDiscount);
						cart.setDiscount(discount.getDiscountValue());
						updateCouponsUsed(discount.getDiscountCoupon().getCopounCode(),couponsUsed);
					}
					
				}				
			}
			
			cart.setOrderTotal(cart.getTotal() - cart.getDiscount());	
		}catch(Exception e){	
			throw new SalesPromotionException("Error occured while calculating  order simple discount", e);
		}
	}
	
	
	private void calculateOrderCompoundDiscount(Cart cart,List<Coupon> discountCoupons, 
					Map<String, Long> couponsUsed,List<String> stackableCoupons,String selectedShipMethod, 
					List<String> nonShipChargeShipMethods) throws SalesPromotionException {
		
		double orderTotalAfterDiscount = 0.00;
		List<Discount> orderLevelDiscounts = null;
	
		try {

			orderTotalAfterDiscount = cart.getTotal();
			orderLevelDiscounts = cart.getDiscounts();

			// apply cashDiscounts
			for (Discount discount : orderLevelDiscounts) {
				validateOrderCompoundDiscountCoupon(discount, cart, discountCoupons, 
					couponsUsed, selectedShipMethod,nonShipChargeShipMethods, stackableCoupons);
				if (discount.getDiscountCoupon().isValid()
						&& discount.getDiscountInPercent() == 0
						&& discount.getDiscountValue() > 0 
						&& discount.getDiscountType() != DiscountType.FREE_SHIPPING) {

					orderTotalAfterDiscount -= discount.getDiscountValue();
					cart.getAvailedDiscounts().add(discount);
					updateCouponsUsed(discount.getDiscountCoupon().getCopounCode(),couponsUsed);
				}

			}

			// Apply % Discount coupons
			for (Discount discount : orderLevelDiscounts) {
				validateOrderCompoundDiscountCoupon(discount, cart, discountCoupons, couponsUsed, 
						selectedShipMethod,nonShipChargeShipMethods, stackableCoupons);
				
				if (discount.getDiscountCoupon().isValid()
						&& discount.getDiscountInPercent() > 0
						&& discount.getDiscountValue() == 0 
						&& discount.getDiscountType() != DiscountType.FREE_SHIPPING) {
					
					//updating order total for % discount coupons for display purpose in views.
					double discountPercent = discount.getDiscountInPercent();
					double discountForThisCoupon = (discountPercent / 100) * orderTotalAfterDiscount;
					discount.setDiscountValue(discountForThisCoupon);	
					orderTotalAfterDiscount -= discountForThisCoupon;
					
					cart.getAvailedDiscounts().add(discount);
					updateCouponsUsed(discount.getDiscountCoupon().getCopounCode(),couponsUsed);
				}

			}

			// Apply free shipping discount
			for (Discount discount : orderLevelDiscounts) {
				validateOrderCompoundDiscountCoupon(discount, cart, discountCoupons, couponsUsed, selectedShipMethod,
						nonShipChargeShipMethods, stackableCoupons);
				
				if (discount.getDiscountCoupon().isValid() && discount.getDiscountType() == DiscountType.FREE_SHIPPING) {
					cart.setFreeShipping(true);
					cart.setAvailedShippingDiscount(discount);
					updateCouponsUsed(discount.getDiscountCoupon().getCopounCode(),couponsUsed);
				}
			}
			
			cart.setOrderTotal(orderTotalAfterDiscount);
			cart.setDiscount(cart.getTotal() - orderTotalAfterDiscount);
			
		} catch (Exception e) {
			throw new SalesPromotionException("Error occured while calculating order compound discount",e);
		}
	}
	
	private void validateItemDiscountCoupon(Discount discount, List<Coupon> couponsList, Map<String,Long> couponsUsed) throws SalesPromotionException {
		
		try{
		
			if(discount.getDiscountCoupon().getStatus() != Status.ACTIVE 
					|| (discount.getDiscountCoupon().getCategory() == CouponCategory.MANUAL_APPLY 
					&& !couponsList.contains(discount.getDiscountCoupon()))){
				discount.getDiscountCoupon().setValid(false);
				return;
			}
			
			String couponCode = discount.getDiscountCoupon().getCopounCode().toUpperCase();
			
			if(couponsUsed.get(couponCode) == null && discount.getDiscountCoupon().getTotalUses() > 0 ){
				couponsUsed.put(couponCode, 1L);
				discount.getDiscountCoupon().setValid(true);
				return;
			} else if(couponsUsed.get(couponCode) != null 
					&& couponsUsed.get(couponCode) < discount.getDiscountCoupon().getTotalUses()){
				couponsUsed.put(couponCode, couponsUsed.get(couponCode) + 1L);
				discount.getDiscountCoupon().setValid(true);
			}
		}catch(Exception e){
			throw new SalesPromotionException("Error occured while validating discount coupon", e);
		}
	}
	
	
	private void validateOrderSimpleDiscountCoupon(Discount discount,Cart cart,List<Coupon> couponsList,
			Map<String,Long> couponsUsed,String selectedShipMethod,List<String> nonShipChargeShipMethods) throws SalesPromotionException {
		
		try{
			
			if(discount.getDiscountCoupon().getStatus() != Status.ACTIVE){
				discount.getDiscountCoupon().setValid(false);
				discount.getDiscountCoupon().setErrorMessage("Coupon is not active.");
				return;
			}
			
			if((discount.getDiscountType() == DiscountType.FREE_SHIPPING
					&& cart.isFreeShipping())){		
				discount.getDiscountCoupon().setValid(false);
				discount.getDiscountCoupon().setErrorMessage("Free shipping already availed for this order.");
				return;	
			} 
			
			if(discount.getDiscountType() == DiscountType.FREE_SHIPPING && selectedShipMethod != null
					&& nonShipChargeShipMethods.contains(selectedShipMethod.toUpperCase())){		
				discount.getDiscountCoupon().setValid(false);
				discount.getDiscountCoupon().setErrorMessage("Shipping charge not applicable for this order.");
				return;	
			} 
			
			if(discount.getDiscountCoupon().getCategory() == CouponCategory.MANUAL_APPLY){
				boolean contains = false;
				
				for(Coupon userCoupon: couponsList){
					
					if(userCoupon.getCopounCode().trim().equalsIgnoreCase(discount.getDiscountCoupon()
							.getCopounCode().trim())){
						
						contains = true;
						break;
					}
				}
				
				if(!contains){
					discount.getDiscountCoupon().setValid(false);
					discount.getDiscountCoupon().setErrorMessage("Coupon is not applied by user.");
					return;
				}
					
			}
			
			String couponCode = discount.getDiscountCoupon().getCopounCode().toUpperCase();
			if(couponsUsed.get(couponCode) != null 
					&& couponsUsed.get(couponCode) >= discount.getDiscountCoupon().getTotalUses()){
				discount.getDiscountCoupon().setValid(false);
				discount.getDiscountCoupon().setErrorMessage("No of usage exceeded for this coupon.");
				return;
			}
			
			discount.getDiscountCoupon().setValid(true);
				
		}catch(Exception e){
			throw new SalesPromotionException("Error occured while validating discount coupon", e);
		}
	}
	
	
	private void validateOrderCompoundDiscountCoupon(Discount discount,Cart cart,List<Coupon> couponsList,
			Map<String,Long> couponsUsed,String selectedShipMethod,List<String> nonShipChargeShipMethods,
			List<String> stackableCouponList) throws SalesPromotionException{
		
		try{
		
		if(!stackableCouponList.contains(discount.getDiscountCoupon().getCopounCode().trim())){
			discount.getDiscountCoupon().setValid(false);
			discount.getDiscountCoupon().setErrorMessage("Not a stackable coupon");
			return;
		}
		
		validateOrderSimpleDiscountCoupon(discount, cart, couponsList, couponsUsed, selectedShipMethod, nonShipChargeShipMethods);
		
		}catch(Exception e){
			throw new SalesPromotionException("Error occured while validating compound discount coupon", e);
		}
	}
	
	private List<String> getStackableDiscounts(List<Discount> discountCoupons){
		List<String> stackableDiscountCoupons = new ArrayList<String>();

		for (Discount discount : discountCoupons) {
			if (discount.getDiscountCoupon().isStackable()) {
				if (!stackableDiscountCoupons.contains(discount.getDiscountCoupon().getCopounCode().trim())) {
					stackableDiscountCoupons.add(discount.getDiscountCoupon().getCopounCode().trim());
				}

				if(discount.getDiscountCoupon().getStackMates() == null 
						|| discount.getDiscountCoupon().getStackMates().trim().isEmpty()){
					continue;
				}	
					
				String[] stackMates = discount.getDiscountCoupon().getStackMates().trim().split(",");

				for (String couponCode : stackMates) {
					if (!stackableDiscountCoupons.contains(couponCode.trim())) {
						stackableDiscountCoupons.add(couponCode.trim());
					}
				}
			}
		}
		return stackableDiscountCoupons;
	}
	
	private void updateCouponsUsed(String couponCode, Map<String,Long> couponsUsed){
		
		if(couponsUsed.get(couponCode) == null){
			couponsUsed.put(couponCode, 1L);	
		} else {
			couponsUsed.put(couponCode, couponsUsed.get(couponCode) + 1L);
		}
	}
	
	private void addRejectedCoupons(Cart cart, List<Coupon> userAppliedCoupons) {
		
		List<Discount> availedDiscounts = cart.getAvailedDiscounts();
		List<Coupon> availedDiscountCoupons = new ArrayList<Coupon>();
		
		// Cash Discounts
		for(Discount availedDiscount : availedDiscounts){
			availedDiscountCoupons.add(availedDiscount.getDiscountCoupon());	
		}
		
		// SHipping Discount
		if(cart.isFreeShipping() && cart.getAvailedShippingDiscount() != null){
			availedDiscountCoupons.add(cart.getAvailedShippingDiscount().getDiscountCoupon());
		}
		
		for(Coupon userAppliedCoupon: userAppliedCoupons){
			boolean contains = false;
			for(Coupon availedDiscountCoupon: availedDiscountCoupons){
				if(availedDiscountCoupon.getCopounCode().trim().equalsIgnoreCase(userAppliedCoupon
						.getCopounCode().trim())){
					contains = true;
					break;
				}
			}
			
			if(!contains){
				cart.getRejectedCoupons().add(userAppliedCoupon);
			}
		}	
	}
	
	public boolean isLoadModule() {
		return loadModule;
	}

	public void setLoadModule(boolean loadModule) {
		this.loadModule = loadModule;
	}
	
	
}
