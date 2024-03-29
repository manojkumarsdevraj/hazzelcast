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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
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
 *       &lt;attribute name="currency" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="alternateAmount" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="alternateCurrency" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "content"
})
@XmlRootElement(name = "Money")
public class Money
    implements Serializable, Equals, HashCode, ToString
{

    @XmlValue
    protected String content;
    @XmlAttribute(name = "currency", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String currency;
    @XmlAttribute(name = "alternateAmount")
    @XmlSchemaType(name = "anySimpleType")
    protected String alternateAmount;
    @XmlAttribute(name = "alternateCurrency")
    @XmlSchemaType(name = "anySimpleType")
    protected String alternateCurrency;

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the alternateAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlternateAmount() {
        return alternateAmount;
    }

    /**
     * Sets the value of the alternateAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlternateAmount(String value) {
        this.alternateAmount = value;
    }

    /**
     * Gets the value of the alternateCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlternateCurrency() {
        return alternateCurrency;
    }

    /**
     * Sets the value of the alternateCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlternateCurrency(String value) {
        this.alternateCurrency = value;
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
            String theContent;
            theContent = this.getContent();
            strategy.appendField(locator, this, "content", buffer, theContent);
        }
        {
            String theCurrency;
            theCurrency = this.getCurrency();
            strategy.appendField(locator, this, "currency", buffer, theCurrency);
        }
        {
            String theAlternateAmount;
            theAlternateAmount = this.getAlternateAmount();
            strategy.appendField(locator, this, "alternateAmount", buffer, theAlternateAmount);
        }
        {
            String theAlternateCurrency;
            theAlternateCurrency = this.getAlternateCurrency();
            strategy.appendField(locator, this, "alternateCurrency", buffer, theAlternateCurrency);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Money)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Money that = ((Money) object);
        {
            String lhsContent;
            lhsContent = this.getContent();
            String rhsContent;
            rhsContent = that.getContent();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "content", lhsContent), LocatorUtils.property(thatLocator, "content", rhsContent), lhsContent, rhsContent)) {
                return false;
            }
        }
        {
            String lhsCurrency;
            lhsCurrency = this.getCurrency();
            String rhsCurrency;
            rhsCurrency = that.getCurrency();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "currency", lhsCurrency), LocatorUtils.property(thatLocator, "currency", rhsCurrency), lhsCurrency, rhsCurrency)) {
                return false;
            }
        }
        {
            String lhsAlternateAmount;
            lhsAlternateAmount = this.getAlternateAmount();
            String rhsAlternateAmount;
            rhsAlternateAmount = that.getAlternateAmount();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "alternateAmount", lhsAlternateAmount), LocatorUtils.property(thatLocator, "alternateAmount", rhsAlternateAmount), lhsAlternateAmount, rhsAlternateAmount)) {
                return false;
            }
        }
        {
            String lhsAlternateCurrency;
            lhsAlternateCurrency = this.getAlternateCurrency();
            String rhsAlternateCurrency;
            rhsAlternateCurrency = that.getAlternateCurrency();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "alternateCurrency", lhsAlternateCurrency), LocatorUtils.property(thatLocator, "alternateCurrency", rhsAlternateCurrency), lhsAlternateCurrency, rhsAlternateCurrency)) {
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
            String theContent;
            theContent = this.getContent();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "content", theContent), currentHashCode, theContent);
        }
        {
            String theCurrency;
            theCurrency = this.getCurrency();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "currency", theCurrency), currentHashCode, theCurrency);
        }
        {
            String theAlternateAmount;
            theAlternateAmount = this.getAlternateAmount();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "alternateAmount", theAlternateAmount), currentHashCode, theAlternateAmount);
        }
        {
            String theAlternateCurrency;
            theAlternateCurrency = this.getAlternateCurrency();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "alternateCurrency", theAlternateCurrency), currentHashCode, theAlternateCurrency);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
