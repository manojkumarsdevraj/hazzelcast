package com.unilog.logocustomization;

import java.util.ArrayList;
import java.util.List;
import com.unilog.products.ProductsModel;

public class LogoCustomizationModule {
	private int status;
	private int jobId;
	private int designId;
	private CustomizationCharges fees = new CustomizationCharges();
	private CustomizationCharges singleQuantityFees = new CustomizationCharges();
	private ArrayList<ProductsModel> lineItems = new ArrayList<ProductsModel>();
	private String comments;
	private String success;
	private String errors;
	private int logoCustomizationId;

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public CustomizationCharges getFees() {
		return fees;
	}
	public void setFees(CustomizationCharges fees) {
		this.fees = fees;
	}
	public CustomizationCharges getSingleQuantityFees() {
		return singleQuantityFees;
	}
	public void setSingleQuantityFees(CustomizationCharges singleQuantityFees) {
		this.singleQuantityFees = singleQuantityFees;
	}
	public ArrayList<ProductsModel> getLineItems() {
		return lineItems;
	}
	public void setLineItems(ArrayList<ProductsModel> lineItems) {
		this.lineItems = lineItems;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getErrors() {
		return errors;
	}
	public void setErrors(String errors) {
		this.errors = errors;
	}
	public int getLogoCustomizationId() {
		return logoCustomizationId;
	}
	public void setLogoCustomizationId(int logoCustomizationId) {
		this.logoCustomizationId = logoCustomizationId;
	}
	public int getDesignId() {
		return designId;
	}
	public void setDesignId(int designId) {
		this.designId = designId;
	}
	
}
