package com.unilog.express.transaction.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Items")
public class Items {
	private Item item;

	@XmlElement(name = "Item")
	public void setItem(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}
}
