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
 *         &lt;element ref="{}Accounting"/>
 *         &lt;element ref="{}Charge"/>
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
    "accounting",
    "charge"
})
@XmlRootElement(name = "Distribution")
public class Distribution
    implements Serializable, Equals, HashCode, ToString
{

    @XmlElement(name = "Accounting", required = true)
    protected Accounting accounting;
    @XmlElement(name = "Charge", required = true)
    protected Charge charge;

    /**
     * Gets the value of the accounting property.
     * 
     * @return
     *     possible object is
     *     {@link Accounting }
     *     
     */
    public Accounting getAccounting() {
        return accounting;
    }

    /**
     * Sets the value of the accounting property.
     * 
     * @param value
     *     allowed object is
     *     {@link Accounting }
     *     
     */
    public void setAccounting(Accounting value) {
        this.accounting = value;
    }

    /**
     * Gets the value of the charge property.
     * 
     * @return
     *     possible object is
     *     {@link Charge }
     *     
     */
    public Charge getCharge() {
        return charge;
    }

    /**
     * Sets the value of the charge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Charge }
     *     
     */
    public void setCharge(Charge value) {
        this.charge = value;
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
            Accounting theAccounting;
            theAccounting = this.getAccounting();
            strategy.appendField(locator, this, "accounting", buffer, theAccounting);
        }
        {
            Charge theCharge;
            theCharge = this.getCharge();
            strategy.appendField(locator, this, "charge", buffer, theCharge);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Distribution)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Distribution that = ((Distribution) object);
        {
            Accounting lhsAccounting;
            lhsAccounting = this.getAccounting();
            Accounting rhsAccounting;
            rhsAccounting = that.getAccounting();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "accounting", lhsAccounting), LocatorUtils.property(thatLocator, "accounting", rhsAccounting), lhsAccounting, rhsAccounting)) {
                return false;
            }
        }
        {
            Charge lhsCharge;
            lhsCharge = this.getCharge();
            Charge rhsCharge;
            rhsCharge = that.getCharge();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "charge", lhsCharge), LocatorUtils.property(thatLocator, "charge", rhsCharge), lhsCharge, rhsCharge)) {
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
            Accounting theAccounting;
            theAccounting = this.getAccounting();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "accounting", theAccounting), currentHashCode, theAccounting);
        }
        {
            Charge theCharge;
            theCharge = this.getCharge();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "charge", theCharge), currentHashCode, theCharge);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
