package com.unilog.socialmedia.service;

import java.util.ArrayList;
import com.unilog.socialmedia.blog.model.EntiesModel;

/**
 * @author bhks
 *
 */
public class BlogFeedData {
	
	private String title;
	private String link;
	private String publishedDate;
	private String imageLink;
	private String imageTitle;
	private String description;
	private String author;
	private ArrayList<EntiesModel> entries;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getPublishedDate() {
		return publishedDate;
	}
	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	public String getImageTitle() {
		return imageTitle;
	}
	public void setImageTitle(String imageTitle) {
		this.imageTitle = imageTitle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public ArrayList<EntiesModel> getEntries() {
		return entries;
	}
	public void setEntries(ArrayList<EntiesModel> entries) {
		this.entries = entries;
	}
	
	

}
