//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.07 at 10:13:20 AM EDT 
//


package com.unilog.punchout.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}SupplierID" maxOccurs="unbounded"/>
 *         &lt;element ref="{}Comments" minOccurs="0"/>
 *         &lt;element ref="{}SearchGroup" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}IndexItem" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "supplierID",
    "comments",
    "searchGroup",
    "indexItem"
})
@XmlRootElement(name = "Index")
public class Index
    implements Serializable, Equals, HashCode, ToString
{

  
	private static final long serialVersionUID = -6796727722611127965L;
	@XmlElement(name = "SupplierID", required = true)
    protected List<SupplierID> supplierID;
    @XmlElement(name = "Comments")
    protected Comments comments;
    @XmlElement(name = "SearchGroup")
    protected List<SearchGroup> searchGroup;
    @XmlElement(name = "IndexItem", required = true)
    protected List<IndexItem> indexItem;

    /**
     * Gets the value of the supplierID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supplierID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSupplierID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SupplierID }
     * 
     * 
     */
    public List<SupplierID> getSupplierID() {
        if (supplierID == null) {
            supplierID = new ArrayList<SupplierID>();
        }
        return this.supplierID;
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link Comments }
     *     
     */
    public Comments getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link Comments }
     *     
     */
    public void setComments(Comments value) {
        this.comments = value;
    }

    /**
     * Gets the value of the searchGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the searchGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSearchGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SearchGroup }
     * 
     * 
     */
    public List<SearchGroup> getSearchGroup() {
        if (searchGroup == null) {
            searchGroup = new ArrayList<SearchGroup>();
        }
        return this.searchGroup;
    }

    /**
     * Gets the value of the indexItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the indexItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndexItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IndexItem }
     * 
     * 
     */
    public List<IndexItem> getIndexItem() {
        if (indexItem == null) {
            indexItem = new ArrayList<IndexItem>();
        }
        return this.indexItem;
    }

    public String toString() {
        final ToStringStrategy strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        {
            List<SupplierID> theSupplierID;
            theSupplierID = (((this.supplierID!= null)&&(!this.supplierID.isEmpty()))?this.getSupplierID():null);
            strategy.appendField(locator, this, "supplierID", buffer, theSupplierID);
        }
        {
            Comments theComments;
            theComments = this.getComments();
            strategy.appendField(locator, this, "comments", buffer, theComments);
        }
        {
            List<SearchGroup> theSearchGroup;
            theSearchGroup = (((this.searchGroup!= null)&&(!this.searchGroup.isEmpty()))?this.getSearchGroup():null);
            strategy.appendField(locator, this, "searchGroup", buffer, theSearchGroup);
        }
        {
            List<IndexItem> theIndexItem;
            theIndexItem = (((this.indexItem!= null)&&(!this.indexItem.isEmpty()))?this.getIndexItem():null);
            strategy.appendField(locator, this, "indexItem", buffer, theIndexItem);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Index)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Index that = ((Index) object);
        {
            List<SupplierID> lhsSupplierID;
            lhsSupplierID = (((this.supplierID!= null)&&(!this.supplierID.isEmpty()))?this.getSupplierID():null);
            List<SupplierID> rhsSupplierID;
            rhsSupplierID = (((that.supplierID!= null)&&(!that.supplierID.isEmpty()))?that.getSupplierID():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "supplierID", lhsSupplierID), LocatorUtils.property(thatLocator, "supplierID", rhsSupplierID), lhsSupplierID, rhsSupplierID)) {
                return false;
            }
        }
        {
            Comments lhsComments;
            lhsComments = this.getComments();
            Comments rhsComments;
            rhsComments = that.getComments();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "comments", lhsComments), LocatorUtils.property(thatLocator, "comments", rhsComments), lhsComments, rhsComments)) {
                return false;
            }
        }
        {
            List<SearchGroup> lhsSearchGroup;
            lhsSearchGroup = (((this.searchGroup!= null)&&(!this.searchGroup.isEmpty()))?this.getSearchGroup():null);
            List<SearchGroup> rhsSearchGroup;
            rhsSearchGroup = (((that.searchGroup!= null)&&(!that.searchGroup.isEmpty()))?that.getSearchGroup():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "searchGroup", lhsSearchGroup), LocatorUtils.property(thatLocator, "searchGroup", rhsSearchGroup), lhsSearchGroup, rhsSearchGroup)) {
                return false;
            }
        }
        {
            List<IndexItem> lhsIndexItem;
            lhsIndexItem = (((this.indexItem!= null)&&(!this.indexItem.isEmpty()))?this.getIndexItem():null);
            List<IndexItem> rhsIndexItem;
            rhsIndexItem = (((that.indexItem!= null)&&(!that.indexItem.isEmpty()))?that.getIndexItem():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "indexItem", lhsIndexItem), LocatorUtils.property(thatLocator, "indexItem", rhsIndexItem), lhsIndexItem, rhsIndexItem)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            List<SupplierID> theSupplierID;
            theSupplierID = (((this.supplierID!= null)&&(!this.supplierID.isEmpty()))?this.getSupplierID():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "supplierID", theSupplierID), currentHashCode, theSupplierID);
        }
        {
            Comments theComments;
            theComments = this.getComments();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "comments", theComments), currentHashCode, theComments);
        }
        {
            List<SearchGroup> theSearchGroup;
            theSearchGroup = (((this.searchGroup!= null)&&(!this.searchGroup.isEmpty()))?this.getSearchGroup():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "searchGroup", theSearchGroup), currentHashCode, theSearchGroup);
        }
        {
            List<IndexItem> theIndexItem;
            theIndexItem = (((this.indexItem!= null)&&(!this.indexItem.isEmpty()))?this.getIndexItem():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "indexItem", theIndexItem), currentHashCode, theIndexItem);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
