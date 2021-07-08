package com.unilog.supplier.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.supplier.model.SupplierModel;
import com.unilog.utility.CommonUtility;

public class SupplierDAO {
	public static ArrayList<SupplierModel> getSupplierDetails(int itemId, int subsetId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<SupplierModel> supplierResult = new ArrayList<SupplierModel>();
		try {
			//int siteId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID"));
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getSupplierDetails");
			System.out.println(sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, itemId);
			pstmt.setInt(2, subsetId);
			rs = pstmt.executeQuery();
			SupplierModel supplierModel =  new SupplierModel();			
			while(rs.next()){
				supplierModel.setSupplierId(rs.getInt("SUPPLIER_ID"));
				supplierModel.setSupplierName(rs.getString("SUPPLIER_NAME"));
				supplierModel.setShortName(rs.getString("SHORT_NAME"));
				supplierModel.setCustomerType(rs.getString("CUSTOMER_TYPE"));
				supplierModel.setAddress1(rs.getString("ADDRESS1"));
				supplierModel.setCity(rs.getString("CITY"));
				supplierModel.setState(rs.getString("STATE"));
				supplierModel.setZip(rs.getString("ZIP"));
				supplierModel.setCountry(rs.getString("COUNTRY"));
				supplierModel.setEmail(rs.getString("EMAIL"));
				supplierModel.setUrl(rs.getString("URL"));
				supplierModel.setDunsNumber(rs.getString("DUNS_NUMBER"));
				supplierModel.setTaxId(rs.getString("TAXID"));				
				supplierModel.setSubsetId(rs.getInt("SUBSET_ID"));
				supplierModel.setLogo(rs.getString("LOGO"));
				supplierModel.setStatus(rs.getString("STATUS"));
				supplierModel.setAllowSubmitRFQ(rs.getString("ALLOW_SUBMIT_RFQ"));
				supplierModel.setAllowSubmitNPR(rs.getString("ALLOW_SUBMIT_NPR"));
				supplierModel.setAllowSubmitPO(rs.getString("ALLOW_SUBMIT_PO"));
				supplierModel.setAllowmarketBasket(rs.getString("ALLOW_MARKET_BASKET"));
				supplierModel.setUserEdited(rs.getInt("USER_EDITED"));
				supplierModel.setUpdatedDateTime(rs.getDate("UPDATED_DATETIME"));
				supplierModel.setAddress2(rs.getString("ADDRESS2"));
				supplierModel.setCneBatchId(rs.getInt("CNE_BATCH_ID"));
				supplierModel.setWflPhaseId(rs.getInt("WFL_PHASE_ID"));
				supplierModel.setTaxonomyId(rs.getInt("TAXONOMY_ID"));
				supplierModel.setSupplierCode(rs.getString("SUPPLIER_CODE"));
				supplierModel.setPaymentTerms(rs.getString("PAYMENT_TERMS"));
				supplierModel.setAcceptsWillCall(rs.getString("ACCEPTSWILLCALL"));
				supplierModel.setAddress3(rs.getString("ADDRESS3"));
				supplierModel.setBuyerName(rs.getString("BUYER_NAME"));
				supplierModel.setPhone(rs.getString("PHONE"));
				supplierModel.setFax(rs.getString("FAX"));
				supplierModel.setCustomerId(rs.getString("CUSTOMER_ID"));
				supplierModel.setAlsoKnownAs(rs.getString("ALSO_KNOWN_AS"));
				supplierModel.setDropShipPPDFreight(rs.getString("DROP_SHIP_PPD_FREIGHT"));
				supplierModel.setDropShipOrderMin(rs.getString("DROP_SHIP_ORDER_MIN"));
				supplierModel.setReturnPolicy(rs.getString("RETURN_POLICY"));
				supplierModel.setProductLines(rs.getString("PRODUCT_LINES"));
				supplierModel.setRepName(rs.getString("REP_NAME"));
				supplierModel.setRepAddress(rs.getString("REP_ADDRESS"));
				supplierModel.setRepPhone(rs.getString("REP_PHONE"));
				supplierModel.setRepCell(rs.getString("REP_CELL"));
				supplierModel.setRepFax(rs.getString("REP_FAX"));
				supplierModel.setRepEmail(rs.getString("REP_EMAIL"));
				supplierModel.setRepAgency(rs.getString("REP_AGENCY"));
				supplierResult.add(supplierModel);
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
		}		
		return supplierResult;
	}
}
