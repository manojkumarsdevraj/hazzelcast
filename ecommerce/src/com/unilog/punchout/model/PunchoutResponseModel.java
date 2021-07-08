package com.unilog.punchout.model;

import java.util.ArrayList;

import com.unilog.products.ProductsModel;

public class PunchoutResponseModel {
private String fromId;
private String toId;
private String fromDomain;
private String toDomain;
private String senderId;
private String senderDomain;
private String cartTotal;
private String punchoutUrl;
private String buyerCookie;
private ArrayList<ProductsModel> itemList;
public String getFromId() {
	return fromId;
}
public void setFromId(String fromId) {
	this.fromId = fromId;
}
public String getToId() {
	return toId;
}
public void setToId(String toId) {
	this.toId = toId;
}
public String getFromDomain() {
	return fromDomain;
}
public void setFromDomain(String fromDomain) {
	this.fromDomain = fromDomain;
}
public String getToDomain() {
	return toDomain;
}
public void setToDomain(String toDomain) {
	this.toDomain = toDomain;
}
public String getSenderId() {
	return senderId;
}
public void setSenderId(String senderId) {
	this.senderId = senderId;
}
public String getSenderDomain() {
	return senderDomain;
}
public void setSenderDomain(String senderDomain) {
	this.senderDomain = senderDomain;
}
public void setCartTotal(String cartTotal) {
	this.cartTotal = cartTotal;
}
public String getCartTotal() {
	return cartTotal;
}
public void setPunchoutUrl(String punchoutUrl) {
	this.punchoutUrl = punchoutUrl;
}
public String getPunchoutUrl() {
	return punchoutUrl;
}
public void setItemList(ArrayList<ProductsModel> itemList) {
	this.itemList = itemList;
}
public ArrayList<ProductsModel> getItemList() {
	return itemList;
}
public void setBuyerCookie(String buyerCookie) {
	this.buyerCookie = buyerCookie;
}
public String getBuyerCookie() {
	return buyerCookie;
}
}
