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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
 *         &lt;element ref="{}Total"/>
 *         &lt;element ref="{}ShipTo" minOccurs="0"/>
 *         &lt;element ref="{}BillTo"/>
 *         &lt;element ref="{}Shipping" minOccurs="0"/>
 *         &lt;element ref="{}Tax" minOccurs="0"/>
 *         &lt;element ref="{}Payment" minOccurs="0"/>
 *         &lt;element ref="{}Contact" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}Comments" minOccurs="0"/>
 *         &lt;element ref="{}Followup" minOccurs="0"/>
 *         &lt;element ref="{}DocumentReference" minOccurs="0"/>
 *         &lt;element ref="{}Extrinsic" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="orderID" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="orderDate" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="orderType" default="regular">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="regular"/>
 *             &lt;enumeration value="release"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="type" default="new">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="new"/>
 *             &lt;enumeration value="delete"/>
 *             &lt;enumeration value="update"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="agreementID" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="agreementPayloadID" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="requisitionID" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="shipComplete">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="yes"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "total",
    "shipTo",
    "billTo",
    "shipping",
    "tax",
    "payment",
    "contact",
    "comments",
    "followup",
    "documentReference",
    "extrinsic"
})
@XmlRootElement(name = "OrderRequestHeader")
public class OrderRequestHeader
    implements Serializable, Equals, HashCode, ToString
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 8891690304569562766L;
	@XmlElement(name = "Total", required = true)
    protected Total total;
    @XmlElement(name = "ShipTo")
    protected ShipTo shipTo;
    @XmlElement(name = "BillTo", required = true)
    protected BillTo billTo;
    @XmlElement(name = "Shipping")
    protected Shipping shipping;
    @XmlElement(name = "Tax")
    protected Tax tax;
    @XmlElement(name = "Payment")
    protected Payment payment;
    @XmlElement(name = "Contact")
    protected List<Contact> contact;
    @XmlElement(name = "Comments")
    protected Comments comments;
    @XmlElement(name = "Followup")
    protected Followup followup;
    @XmlElement(name = "DocumentReference")
    protected DocumentReference documentReference;
    @XmlElement(name = "Extrinsic")
    protected List<Extrinsic> extrinsic;
    @XmlAttribute(name = "orderID", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String orderID;
    @XmlAttribute(name = "orderDate", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String orderDate;
    @XmlAttribute(name = "orderType")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String orderType;
    @XmlAttribute(name = "type")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;
    @XmlAttribute(name = "agreementID")
    @XmlSchemaType(name = "anySimpleType")
    protected String agreementID;
    @XmlAttribute(name = "agreementPayloadID")
    @XmlSchemaType(name = "anySimpleType")
    protected String agreementPayloadID;
    @XmlAttribute(name = "requisitionID")
    @XmlSchemaType(name = "anySimpleType")
    protected String requisitionID;
    @XmlAttribute(name = "shipComplete")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String shipComplete;

    /**
     * Gets the value of the total property.
     * 
     * @return
     *     possible object is
     *     {@link Total }
     *     
     */
    public Total getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     * 
     * @param value
     *     allowed object is
     *     {@link Total }
     *     
     */
    public void setTotal(Total value) {
        this.total = value;
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
     * Gets the value of the billTo property.
     * 
     * @return
     *     possible object is
     *     {@link BillTo }
     *     
     */
    public BillTo getBillTo() {
        return billTo;
    }

    /**
     * Sets the value of the billTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillTo }
     *     
     */
    public void setBillTo(BillTo value) {
        this.billTo = value;
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
     * Gets the value of the payment property.
     * 
     * @return
     *     possible object is
     *     {@link Payment }
     *     
     */
    public Payment getPayment() {
        return payment;
    }

    /**
     * Sets the value of the payment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Payment }
     *     
     */
    public void setPayment(Payment value) {
        this.payment = value;
    }

    /**
     * Gets the value of the contact property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contact property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContact().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Contact }
     * 
     * 
     */
    public List<Contact> getContact() {
        if (contact == null) {
            contact = new ArrayList<Contact>();
        }
        return this.contact;
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
     * Gets the value of the followup property.
     * 
     * @return
     *     possible object is
     *     {@link Followup }
     *     
     */
    public Followup getFollowup() {
        return followup;
    }

    /**
     * Sets the value of the followup property.
     * 
     * @param value
     *     allowed object is
     *     {@link Followup }
     *     
     */
    public void setFollowup(Followup value) {
        this.followup = value;
    }

    /**
     * Gets the value of the documentReference property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentReference }
     *     
     */
    public DocumentReference getDocumentReference() {
        return documentReference;
    }

    /**
     * Sets the value of the documentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentReference }
     *     
     */
    public void setDocumentReference(DocumentReference value) {
        this.documentReference = value;
    }

    /**
     * Gets the value of the extrinsic property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extrinsic property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtrinsic().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Extrinsic }
     * 
     * 
     */
    public List<Extrinsic> getExtrinsic() {
        if (extrinsic == null) {
            extrinsic = new ArrayList<Extrinsic>();
        }
        return this.extrinsic;
    }

    /**
     * Gets the value of the orderID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderID() {
        return orderID;
    }

    /**
     * Sets the value of the orderID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderID(String value) {
        this.orderID = value;
    }

    /**
     * Gets the value of the orderDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the value of the orderDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderDate(String value) {
        this.orderDate = value;
    }

    /**
     * Gets the value of the orderType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderType() {
        if (orderType == null) {
            return "regular";
        } else {
            return orderType;
        }
    }

    /**
     * Sets the value of the orderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderType(String value) {
        this.orderType = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        if (type == null) {
            return "new";
        } else {
            return type;
        }
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the agreementID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgreementID() {
        return agreementID;
    }

    /**
     * Sets the value of the agreementID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgreementID(String value) {
        this.agreementID = value;
    }

    /**
     * Gets the value of the agreementPayloadID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgreementPayloadID() {
        return agreementPayloadID;
    }

    /**
     * Sets the value of the agreementPayloadID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgreementPayloadID(String value) {
        this.agreementPayloadID = value;
    }

    /**
     * Gets the value of the requisitionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequisitionID() {
        return requisitionID;
    }

    /**
     * Sets the value of the requisitionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequisitionID(String value) {
        this.requisitionID = value;
    }

    /**
     * Gets the value of the shipComplete property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipComplete() {
        return shipComplete;
    }

    /**
     * Sets the value of the shipComplete property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipComplete(String value) {
        this.shipComplete = value;
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
            Total theTotal;
            theTotal = this.getTotal();
            strategy.appendField(locator, this, "total", buffer, theTotal);
        }
        {
            ShipTo theShipTo;
            theShipTo = this.getShipTo();
            strategy.appendField(locator, this, "shipTo", buffer, theShipTo);
        }
        {
            BillTo theBillTo;
            theBillTo = this.getBillTo();
            strategy.appendField(locator, this, "billTo", buffer, theBillTo);
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
            Payment thePayment;
            thePayment = this.getPayment();
            strategy.appendField(locator, this, "payment", buffer, thePayment);
        }
        {
            List<Contact> theContact;
            theContact = (((this.contact!= null)&&(!this.contact.isEmpty()))?this.getContact():null);
            strategy.appendField(locator, this, "contact", buffer, theContact);
        }
        {
            Comments theComments;
            theComments = this.getComments();
            strategy.appendField(locator, this, "comments", buffer, theComments);
        }
        {
            Followup theFollowup;
            theFollowup = this.getFollowup();
            strategy.appendField(locator, this, "followup", buffer, theFollowup);
        }
        {
            DocumentReference theDocumentReference;
            theDocumentReference = this.getDocumentReference();
            strategy.appendField(locator, this, "documentReference", buffer, theDocumentReference);
        }
        {
            List<Extrinsic> theExtrinsic;
            theExtrinsic = (((this.extrinsic!= null)&&(!this.extrinsic.isEmpty()))?this.getExtrinsic():null);
            strategy.appendField(locator, this, "extrinsic", buffer, theExtrinsic);
        }
        {
            String theOrderID;
            theOrderID = this.getOrderID();
            strategy.appendField(locator, this, "orderID", buffer, theOrderID);
        }
        {
            String theOrderDate;
            theOrderDate = this.getOrderDate();
            strategy.appendField(locator, this, "orderDate", buffer, theOrderDate);
        }
        {
            String theOrderType;
            theOrderType = this.getOrderType();
            strategy.appendField(locator, this, "orderType", buffer, theOrderType);
        }
        {
            String theType;
            theType = this.getType();
            strategy.appendField(locator, this, "type", buffer, theType);
        }
        {
            String theAgreementID;
            theAgreementID = this.getAgreementID();
            strategy.appendField(locator, this, "agreementID", buffer, theAgreementID);
        }
        {
            String theAgreementPayloadID;
            theAgreementPayloadID = this.getAgreementPayloadID();
            strategy.appendField(locator, this, "agreementPayloadID", buffer, theAgreementPayloadID);
        }
        {
            String theRequisitionID;
            theRequisitionID = this.getRequisitionID();
            strategy.appendField(locator, this, "requisitionID", buffer, theRequisitionID);
        }
        {
            String theShipComplete;
            theShipComplete = this.getShipComplete();
            strategy.appendField(locator, this, "shipComplete", buffer, theShipComplete);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof OrderRequestHeader)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final OrderRequestHeader that = ((OrderRequestHeader) object);
        {
            Total lhsTotal;
            lhsTotal = this.getTotal();
            Total rhsTotal;
            rhsTotal = that.getTotal();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "total", lhsTotal), LocatorUtils.property(thatLocator, "total", rhsTotal), lhsTotal, rhsTotal)) {
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
            BillTo lhsBillTo;
            lhsBillTo = this.getBillTo();
            BillTo rhsBillTo;
            rhsBillTo = that.getBillTo();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "billTo", lhsBillTo), LocatorUtils.property(thatLocator, "billTo", rhsBillTo), lhsBillTo, rhsBillTo)) {
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
            Payment lhsPayment;
            lhsPayment = this.getPayment();
            Payment rhsPayment;
            rhsPayment = that.getPayment();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "payment", lhsPayment), LocatorUtils.property(thatLocator, "payment", rhsPayment), lhsPayment, rhsPayment)) {
                return false;
            }
        }
        {
            List<Contact> lhsContact;
            lhsContact = (((this.contact!= null)&&(!this.contact.isEmpty()))?this.getContact():null);
            List<Contact> rhsContact;
            rhsContact = (((that.contact!= null)&&(!that.contact.isEmpty()))?that.getContact():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "contact", lhsContact), LocatorUtils.property(thatLocator, "contact", rhsContact), lhsContact, rhsContact)) {
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
            Followup lhsFollowup;
            lhsFollowup = this.getFollowup();
            Followup rhsFollowup;
            rhsFollowup = that.getFollowup();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "followup", lhsFollowup), LocatorUtils.property(thatLocator, "followup", rhsFollowup), lhsFollowup, rhsFollowup)) {
                return false;
            }
        }
        {
            DocumentReference lhsDocumentReference;
            lhsDocumentReference = this.getDocumentReference();
            DocumentReference rhsDocumentReference;
            rhsDocumentReference = that.getDocumentReference();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "documentReference", lhsDocumentReference), LocatorUtils.property(thatLocator, "documentReference", rhsDocumentReference), lhsDocumentReference, rhsDocumentReference)) {
                return false;
            }
        }
        {
            List<Extrinsic> lhsExtrinsic;
            lhsExtrinsic = (((this.extrinsic!= null)&&(!this.extrinsic.isEmpty()))?this.getExtrinsic():null);
            List<Extrinsic> rhsExtrinsic;
            rhsExtrinsic = (((that.extrinsic!= null)&&(!that.extrinsic.isEmpty()))?that.getExtrinsic():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "extrinsic", lhsExtrinsic), LocatorUtils.property(thatLocator, "extrinsic", rhsExtrinsic), lhsExtrinsic, rhsExtrinsic)) {
                return false;
            }
        }
        {
            String lhsOrderID;
            lhsOrderID = this.getOrderID();
            String rhsOrderID;
            rhsOrderID = that.getOrderID();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "orderID", lhsOrderID), LocatorUtils.property(thatLocator, "orderID", rhsOrderID), lhsOrderID, rhsOrderID)) {
                return false;
            }
        }
        {
            String lhsOrderDate;
            lhsOrderDate = this.getOrderDate();
            String rhsOrderDate;
            rhsOrderDate = that.getOrderDate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "orderDate", lhsOrderDate), LocatorUtils.property(thatLocator, "orderDate", rhsOrderDate), lhsOrderDate, rhsOrderDate)) {
                return false;
            }
        }
        {
            String lhsOrderType;
            lhsOrderType = this.getOrderType();
            String rhsOrderType;
            rhsOrderType = that.getOrderType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "orderType", lhsOrderType), LocatorUtils.property(thatLocator, "orderType", rhsOrderType), lhsOrderType, rhsOrderType)) {
                return false;
            }
        }
        {
            String lhsType;
            lhsType = this.getType();
            String rhsType;
            rhsType = that.getType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "type", lhsType), LocatorUtils.property(thatLocator, "type", rhsType), lhsType, rhsType)) {
                return false;
            }
        }
        {
            String lhsAgreementID;
            lhsAgreementID = this.getAgreementID();
            String rhsAgreementID;
            rhsAgreementID = that.getAgreementID();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "agreementID", lhsAgreementID), LocatorUtils.property(thatLocator, "agreementID", rhsAgreementID), lhsAgreementID, rhsAgreementID)) {
                return false;
            }
        }
        {
            String lhsAgreementPayloadID;
            lhsAgreementPayloadID = this.getAgreementPayloadID();
            String rhsAgreementPayloadID;
            rhsAgreementPayloadID = that.getAgreementPayloadID();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "agreementPayloadID", lhsAgreementPayloadID), LocatorUtils.property(thatLocator, "agreementPayloadID", rhsAgreementPayloadID), lhsAgreementPayloadID, rhsAgreementPayloadID)) {
                return false;
            }
        }
        {
            String lhsRequisitionID;
            lhsRequisitionID = this.getRequisitionID();
            String rhsRequisitionID;
            rhsRequisitionID = that.getRequisitionID();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "requisitionID", lhsRequisitionID), LocatorUtils.property(thatLocator, "requisitionID", rhsRequisitionID), lhsRequisitionID, rhsRequisitionID)) {
                return false;
            }
        }
        {
            String lhsShipComplete;
            lhsShipComplete = this.getShipComplete();
            String rhsShipComplete;
            rhsShipComplete = that.getShipComplete();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "shipComplete", lhsShipComplete), LocatorUtils.property(thatLocator, "shipComplete", rhsShipComplete), lhsShipComplete, rhsShipComplete)) {
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
            Total theTotal;
            theTotal = this.getTotal();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "total", theTotal), currentHashCode, theTotal);
        }
        {
            ShipTo theShipTo;
            theShipTo = this.getShipTo();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "shipTo", theShipTo), currentHashCode, theShipTo);
        }
        {
            BillTo theBillTo;
            theBillTo = this.getBillTo();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "billTo", theBillTo), currentHashCode, theBillTo);
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
            Payment thePayment;
            thePayment = this.getPayment();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "payment", thePayment), currentHashCode, thePayment);
        }
        {
            List<Contact> theContact;
            theContact = (((this.contact!= null)&&(!this.contact.isEmpty()))?this.getContact():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "contact", theContact), currentHashCode, theContact);
        }
        {
            Comments theComments;
            theComments = this.getComments();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "comments", theComments), currentHashCode, theComments);
        }
        {
            Followup theFollowup;
            theFollowup = this.getFollowup();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "followup", theFollowup), currentHashCode, theFollowup);
        }
        {
            DocumentReference theDocumentReference;
            theDocumentReference = this.getDocumentReference();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "documentReference", theDocumentReference), currentHashCode, theDocumentReference);
        }
        {
            List<Extrinsic> theExtrinsic;
            theExtrinsic = (((this.extrinsic!= null)&&(!this.extrinsic.isEmpty()))?this.getExtrinsic():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "extrinsic", theExtrinsic), currentHashCode, theExtrinsic);
        }
        {
            String theOrderID;
            theOrderID = this.getOrderID();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "orderID", theOrderID), currentHashCode, theOrderID);
        }
        {
            String theOrderDate;
            theOrderDate = this.getOrderDate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "orderDate", theOrderDate), currentHashCode, theOrderDate);
        }
        {
            String theOrderType;
            theOrderType = this.getOrderType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "orderType", theOrderType), currentHashCode, theOrderType);
        }
        {
            String theType;
            theType = this.getType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "type", theType), currentHashCode, theType);
        }
        {
            String theAgreementID;
            theAgreementID = this.getAgreementID();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "agreementID", theAgreementID), currentHashCode, theAgreementID);
        }
        {
            String theAgreementPayloadID;
            theAgreementPayloadID = this.getAgreementPayloadID();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "agreementPayloadID", theAgreementPayloadID), currentHashCode, theAgreementPayloadID);
        }
        {
            String theRequisitionID;
            theRequisitionID = this.getRequisitionID();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "requisitionID", theRequisitionID), currentHashCode, theRequisitionID);
        }
        {
            String theShipComplete;
            theShipComplete = this.getShipComplete();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "shipComplete", theShipComplete), currentHashCode, theShipComplete);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}