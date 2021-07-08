package com.unilog.utility;

import java.util.List;

public class WordPressMenuModel {
private long ID;
private String post_author;
private String post_date;
private String post_date_gmt;
private String post_title;
private String post_name;
private String menu_order;
private String post_type;
private long db_id;
private String openIn;
private String menu_item_parent;
private String object_id;
private String title;
private String url;
private List<String> classes;
private String current = "false";
private String current_item_ancestor="false";
private String current_item_parent = "false";
public long getID() {
	return ID;
}
public void setID(long iD) {
	ID = iD;
}
public String getPost_author() {
	return post_author;
}
public void setPost_author(String post_author) {
	this.post_author = post_author;
}
public String getPost_date() {
	return post_date;
}
public void setPost_date(String post_date) {
	this.post_date = post_date;
}
public String getPost_date_gmt() {
	return post_date_gmt;
}
public void setPost_date_gmt(String post_date_gmt) {
	this.post_date_gmt = post_date_gmt;
}
public String getPost_title() {
	return post_title;
}
public void setPost_title(String post_title) {
	this.post_title = post_title;
}
public String getPost_name() {
	return post_name;
}
public void setPost_name(String post_name) {
	this.post_name = post_name;
}
public String getMenu_order() {
	return menu_order;
}
public void setMenu_order(String menu_order) {
	this.menu_order = menu_order;
}
public String getPost_type() {
	return post_type;
}
public void setPost_type(String post_type) {
	this.post_type = post_type;
}
public long getDb_id() {
	return db_id;
}
public void setDb_id(long db_id) {
	this.db_id = db_id;
}
public String getMenu_item_parent() {
	return menu_item_parent;
}
public void setMenu_item_parent(String menu_item_parent) {
	this.menu_item_parent = menu_item_parent;
}
public String getObject_id() {
	return object_id;
}
public void setObject_id(String object_id) {
	this.object_id = object_id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public List<String> getClasses() {
	return classes;
}
public void setClasses(List<String> classes) {
	this.classes = classes;
}
public String getCurrent() {
	return current;
}
public void setCurrent(String current) {
	this.current = current;
}
public String getCurrent_item_ancestor() {
	return current_item_ancestor;
}
public void setCurrent_item_ancestor(String current_item_ancestor) {
	this.current_item_ancestor = current_item_ancestor;
}
public String getCurrent_item_parent() {
	return current_item_parent;
}
public void setCurrent_item_parent(String current_item_parent) {
	this.current_item_parent = current_item_parent;
}
public void setOpenIn(String openIn) {
	this.openIn = openIn;
}
public String getOpenIn() {
	return openIn;
}


}
