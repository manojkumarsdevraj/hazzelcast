//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.07 at 10:13:20 AM EDT 
//


package com.unilog.punchout.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element ref="{}ItemID"/>
 *         &lt;element ref="{}Path" minOccurs="0"/>
 *         &lt;element ref="{}ItemDetail"/>
 *         &lt;element ref="{}SupplierID" minOccurs="0"/>
 *         &lt;element ref="{}ShipTo" minOccurs="0"/>
 *         &lt;element ref="{}Shipping" minOccurs="0"/>
 *         &lt;element ref="{}Tax" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="quantity" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="lineNumber" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "itemID",
    "path",
    "itemDetail",
    "supplierID",
    "shipTo",
    "shipping",
    "tax"
})
@XmlRootElement(name = "ItemIn")
public class ItemIn
    implements Serializable, Equals, HashCode, ToString
{

    @XmlElement(name = "ItemID", required = true)
    protected ItemID itemID;
    @XmlElement(name = "Path")
    protected Path path;
    @XmlElement(name = "ItemDetail", required = true)
    protected ItemDetail itemDetail;
    @XmlElement(name = "SupplierID")
    protected SupplierID supplierID;
    @XmlElement(name = "ShipTo")
    protected ShipTo shipTo;
    @XmlElement(name = "Shipping")
    protected Shipping shipping;
    @XmlElement(name = "Tax")
    protected Tax tax;
    @XmlAttribute(name = "quantity", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String quantity;
    @XmlAttribute(name = "lineNumber")
    @XmlSchemaType(name = "anySimpleType")
    protected String lineNumber;

    /**
     * Gets the value of the itemID property.
     * 
     * @return
     *     possible object is
     *     {@link ItemID }
     *     
     */
    public ItemID getItemID() {
        return itemID;
    }

    /**
     * Sets the value of the itemID property.
     * 
     * @param value
     *     allowed object is
     *     {@link ItemID }
     *     
     */
    public void setItemID(ItemID value) {
        this.itemID = value;
    }

    /**
     * Gets the value of the path property.
     * 
     * @return
     *     possible object is
     *     {@link Path }
     *     
     */
    public Path getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     * 
     * @param value
     *     allowed object is
     *     {@link Path }
     *     
     */
    public void setPath(Path value) {
        this.path = value;
    }

    /**
     * Gets the value of the itemDetail property.
     * 
     * @return
     *     possible object is
     *     {@link ItemDetail }
     *     
     */
    public ItemDetail getItemDetail() {
        return itemDetail;
    }

    /**
     * Sets the value of the itemDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link ItemDetail }
     *     
     */
    public void setItemDetail(ItemDetail value) {
        this.itemDetail = value;
    }

    /**
     * Gets the value of the supplierID property.
     * 
     * @return
     *     possible object is
     *     {@link SupplierID }
     *     
     */
    public SupplierID getSupplierID() {
        return supplierID;
    }

    /**
     * Sets the value of the supplierID property.
     * 
     * @param value
     *     allowed object is
     *     {@link SupplierID }
     *     
     */
    public void setSupplierID(SupplierID value) {
        this.supplierID = value;
    }

    /**
     * Gets the value of the shipTo property.
     * 
     * @return
     *     possible object is
     *     {@link ShipTo }
     *     
     */
    public ShipTo getShipTo() {
        return shipTo;
    }

    /**
     * Sets the value of the shipTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShipTo }
     *     
     */
    public void setShipTo(ShipTo value) {
        this.shipTo = value;
    }

    /**
     * Gets the value of the shipping property.
     * 
     * @return
     *     possible object is
     *     {@link Shipping }
     *     
     */
    public Shipping getShipping() {
        return shipping;
    }

    /**
     * Sets the value of the shipping property.
     * 
     * @param value
     *     allowed object is
     *     {@link Shipping }
     *     
     */
    public void setShipping(Shipping value) {
        this.shipping = value;
    }

    /**
     * Gets the value of the tax property.
     * 
     * @return
     *     possible object is
     *     {@link Tax }
     *     
     */
    public Tax getTax() {
        return tax;
    }

    /**
     * Sets the value of the tax property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tax }
     *     
     */
    public void setTax(Tax value) {
        this.tax = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuantity(String value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the lineNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLineNumber() {
        return lineNumber;
    }

    /**
     * Sets the value of the lineNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLineNumber(String value) {
        this.lineNumber = value;
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
            ItemID theItemID;
            theItemID = this.getItemID();
            strategy.appendField(locator, this, "itemID", buffer, theItemID);
        }
        {
            Path thePath;
            thePath = this.getPath();
            strategy.appendField(locator, this, "path", buffer, thePath);
        }
        {
            ItemDetail theItemDetail;
            theItemDetail = this.getItemDetail();
            strategy.appendField(locator, this, "itemDetail", buffer, theItemDetail);
        }
        {
            SupplierID theSupplierID;
            theSupplierID = this.getSupplierID();
            strategy.appendField(locator, this, "supplierID", buffer, theSupplierID);
        }
        {
            ShipTo theShipTo;
            theShipTo = this.getShipTo();
            strategy.appendField(locator, this, "shipTo", buffer, theShipTo);
        }
        {
            Shipping theShipping;
            theShipping = this.getShipping();
            strategy.appendField(locator, this, "shipping", buffer, theShipping);
        }
        {
            Tax theTax;
            theTax = this.getTax();
            strategy.appendField(locator, this, "tax", buffer, theTax);
        }
        {
            String theQuantity;
            theQuantity = this.getQuantity();
            strategy.appendField(locator, this, "quantity", buffer, theQuantity);
        }
        {
            String theLineNumber;
            theLineNumber = this.getLineNumber();
            strategy.appendField(locator, this, "lineNumber", buffer, theLineNumber);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof ItemIn)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final ItemIn that = ((ItemIn) object);
        {
            ItemID lhsItemID;
            lhsItemID = this.getItemID();
            ItemID rhsItemID;
            rhsItemID = that.getItemID();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "itemID", lhsItemID), LocatorUtils.property(thatLocator, "itemID", rhsItemID), lhsItemID, rhsItemID)) {
                return false;
            }
        }
        {
            Path lhsPath;
            lhsPath = this.getPath();
            Path rhsPath;
            rhsPath = that.getPath();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "path", lhsPath), LocatorUtils.property(thatLocator, "path", rhsPath), lhsPath, rhsPath)) {
                return false;
            }
        }
        {
            ItemDetail lhsItemDetail;
            lhsItemDetail = this.getItemDetail();
            ItemDetail rhsItemDetail;
            rhsItemDetail = that.getItemDetail();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "itemDetail", lhsItemDetail), LocatorUtils.property(thatLocator, "itemDetail", rhsItemDetail), lhsItemDetail, rhsItemDetail)) {
                return false;
            }
        }
        {
            SupplierID lhsSupplierID;
            lhsSupplierID = this.getSupplierID();
            SupplierID rhsSupplierID;
            rhsSupplierID = that.getSupplierID();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "supplierID", lhsSupplierID), LocatorUtils.property(thatLocator, "supplierID", rhsSupplierID), lhsSupplierID, rhsSupplierID)) {
                return false;
            }
        }
        {
            ShipTo lhsShipTo;
            lhsShipTo = this.getShipTo();
            ShipTo rhsShipTo;
            rhsShipTo = that.getShipTo();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "shipTo", lhsShipTo), LocatorUtils.property(thatLocator, "shipTo", rhsShipTo), lhsShipTo, rhsShipTo)) {
                return false;
            }
        }
        {
            Shipping lhsShipping;
            lhsShipping = this.getShipping();
            Shipping rhsShipping;
            rhsShipping = that.getShipping();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "shipping", lhsShipping), LocatorUtils.property(thatLocator, "shipping", rhsShipping), lhsShipping, rhsShipping)) {
                return false;
            }
        }
        {
            Tax lhsTax;
            lhsTax = this.getTax();
            Tax rhsTax;
            rhsTax = that.getTax();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "tax", lhsTax), LocatorUtils.property(thatLocator, "tax", rhsTax), lhsTax, rhsTax)) {
                return false;
            }
        }
        {
            String lhsQuantity;
            lhsQuantity = this.getQuantity();
            String rhsQuantity;
            rhsQuantity = that.getQuantity();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "quantity", lhsQuantity), LocatorUtils.property(thatLocator, "quantity", rhsQuantity), lhsQuantity, rhsQuantity)) {
                return false;
            }
        }
        {
            String lhsLineNumber;
            lhsLineNumber = this.getLineNumber();
            String rhsLineNumber;
            rhsLineNumber = that.getLineNumber();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "lineNumber", lhsLineNumber), LocatorUtils.property(thatLocator, "lineNumber", rhsLineNumber), lhsLineNumber, rhsLineNumber)) {
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
            ItemID theItemID;
            theItemID = this.getItemID();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "itemID", theItemID), currentHashCode, theItemID);
        }
        {
            Path thePath;
            thePath = this.getPath();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "path", thePath), currentHashCode, thePath);
        }
        {
            ItemDetail theItemDetail;
            theItemDetail = this.getItemDetail();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "itemDetail", theItemDetail), currentHashCode, theItemDetail);
        }
        {
            SupplierID theSupplierID;
            theSupplierID = this.getSupplierID();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "supplierID", theSupplierID), currentHashCode, theSupplierID);
        }
        {
            ShipTo theShipTo;
            theShipTo = this.getShipTo();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "shipTo", theShipTo), currentHashCode, theShipTo);
        }
        {
            Shipping theShipping;
            theShipping = this.getShipping();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "shipping", theShipping), currentHashCode, theShipping);
        }
        {
            Tax theTax;
            theTax = this.getTax();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "tax", theTax), currentHashCode, theTax);
        }
        {
            String theQuantity;
            theQuantity = this.getQuantity();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "quantity", theQuantity), currentHashCode, theQuantity);
        }
        {
            String theLineNumber;
            theLineNumber = this.getLineNumber();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "lineNumber", theLineNumber), currentHashCode, theLineNumber);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}