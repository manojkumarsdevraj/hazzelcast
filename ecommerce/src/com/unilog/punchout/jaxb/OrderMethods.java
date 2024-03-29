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
 *         &lt;element ref="{}OrderMethod" maxOccurs="unbounded"/>
 *         &lt;element ref="{}Contact" minOccurs="0"/>
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
    "orderMethod",
    "contact"
})
@XmlRootElement(name = "OrderMethods")
public class OrderMethods
    implements Serializable, Equals, HashCode, ToString
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -3604321820448407543L;
	@XmlElement(name = "OrderMethod", required = true)
    protected List<OrderMethod> orderMethod;
    @XmlElement(name = "Contact")
    protected Contact contact;

    /**
     * Gets the value of the orderMethod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the orderMethod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrderMethod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OrderMethod }
     * 
     * 
     */
    public List<OrderMethod> getOrderMethod() {
        if (orderMethod == null) {
            orderMethod = new ArrayList<OrderMethod>();
        }
        return this.orderMethod;
    }

    /**
     * Gets the value of the contact property.
     * 
     * @return
     *     possible object is
     *     {@link Contact }
     *     
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Sets the value of the contact property.
     * 
     * @param value
     *     allowed object is
     *     {@link Contact }
     *     
     */
    public void setContact(Contact value) {
        this.contact = value;
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
            List<OrderMethod> theOrderMethod;
            theOrderMethod = (((this.orderMethod!= null)&&(!this.orderMethod.isEmpty()))?this.getOrderMethod():null);
            strategy.appendField(locator, this, "orderMethod", buffer, theOrderMethod);
        }
        {
            Contact theContact;
            theContact = this.getContact();
            strategy.appendField(locator, this, "contact", buffer, theContact);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof OrderMethods)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final OrderMethods that = ((OrderMethods) object);
        {
            List<OrderMethod> lhsOrderMethod;
            lhsOrderMethod = (((this.orderMethod!= null)&&(!this.orderMethod.isEmpty()))?this.getOrderMethod():null);
            List<OrderMethod> rhsOrderMethod;
            rhsOrderMethod = (((that.orderMethod!= null)&&(!that.orderMethod.isEmpty()))?that.getOrderMethod():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "orderMethod", lhsOrderMethod), LocatorUtils.property(thatLocator, "orderMethod", rhsOrderMethod), lhsOrderMethod, rhsOrderMethod)) {
                return false;
            }
        }
        {
            Contact lhsContact;
            lhsContact = this.getContact();
            Contact rhsContact;
            rhsContact = that.getContact();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "contact", lhsContact), LocatorUtils.property(thatLocator, "contact", rhsContact), lhsContact, rhsContact)) {
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
            List<OrderMethod> theOrderMethod;
            theOrderMethod = (((this.orderMethod!= null)&&(!this.orderMethod.isEmpty()))?this.getOrderMethod():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "orderMethod", theOrderMethod), currentHashCode, theOrderMethod);
        }
        {
            Contact theContact;
            theContact = this.getContact();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "contact", theContact), currentHashCode, theContact);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
