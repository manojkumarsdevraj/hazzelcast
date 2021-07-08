package com.unilog.utility;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CustomTable implements Comparable<CustomTable> {
private String EntityId;
private String EntityName;
private int DisplaySequence;
private String TableId;
private String TableName;
private Map<String,String> EntityFieldValue;
private List<Map<String, String>> TableDetails;
public String getEntityId() {
	return EntityId;
}
public void setEntityId(String entityId) {
	EntityId = entityId;
}
public String getTableId() {
	return TableId;
}
public void setTableId(String tableId) {
	TableId = tableId;
}
public String getTableName() {
	return TableName;
}
public void setTableName(String tableName) {
	TableName = tableName;
}
public void setTableDetails(List<Map<String, String>> tableDetails) {
	TableDetails = tableDetails;
}
public List<Map<String, String>> getTableDetails() {
	return TableDetails;
}
public void setEntityFieldValue(Map<String,String> entityFieldValue) {
	EntityFieldValue = entityFieldValue;
}
public Map<String,String> getEntityFieldValue() {
	return EntityFieldValue;
}
public String getEntityName() {
	return EntityName;
}
public void setEntityName(String entityName) {
	EntityName = entityName;
}
public int compareTo(CustomTable compareVal) {
	return compareVal.getEntityName().compareToIgnoreCase(this.EntityName);
}

public int getDisplaySequence() {
	return DisplaySequence;
}
public void setDisplaySequence(int displaySequence) {
	DisplaySequence = displaySequence;
}

public static Comparator<CustomTable> EntityNameComparator = new Comparator<CustomTable>() {

	public int compare(CustomTable firstItem, CustomTable secondItem) {
		//ascending order
		return firstItem.getEntityName().compareToIgnoreCase(secondItem.getEntityName());
		//descending order
		//return fruitName2.compareTo(fruitName1);
	}

};
public static Comparator<CustomTable> DisplaySeqComparator = new Comparator<CustomTable>() {

	public int compare(CustomTable firstItem, CustomTable secondItem) {
		//ascending order
		return firstItem.getDisplaySequence() - secondItem.getDisplaySequence();
		//descending order
		//return fruitName2.compareTo(fruitName1);
	}

};
}
