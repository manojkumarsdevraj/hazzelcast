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
 *       &lt;choice>
 *         &lt;element ref="{}Phone"/>
 *         &lt;element ref="{}Email"/>
 *         &lt;element ref="{}Fax"/>
 *         &lt;element ref="{}URL"/>
 *         &lt;element ref="{}OtherOrderTarget"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "phone",
    "email",
    "fax",
    "url",
    "otherOrderTarget"
})
@XmlRootElement(name = "OrderTarget")
public class OrderTarget
    implements Serializable, Equals, HashCode, ToString
{

    @XmlElement(name = "Phone")
    protected Phone phone;
    @XmlElement(name = "Email")
    protected Email email;
    @XmlElement(name = "Fax")
    protected Fax fax;
    @XmlElement(name = "URL")
    protected URL url;
    @XmlElement(name = "OtherOrderTarget")
    protected OtherOrderTarget otherOrderTarget;

    /**
     * Gets the value of the phone property.
     * 
     * @return
     *     possible object is
     *     {@link Phone }
     *     
     */
    public Phone getPhone() {
        return phone;
    }

    /**
     * Sets the value of the phone property.
     * 
     * @param value
     *     allowed object is
     *     {@link Phone }
     *     
     */
    public void setPhone(Phone value) {
        this.phone = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link Email }
     *     
     */
    public Email getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link Email }
     *     
     */
    public void setEmail(Email value) {
        this.email = value;
    }

    /**
     * Gets the value of the fax property.
     * 
     * @return
     *     possible object is
     *     {@link Fax }
     *     
     */
    public Fax getFax() {
        return fax;
    }

    /**
     * Sets the value of the fax property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fax }
     *     
     */
    public void setFax(Fax value) {
        this.fax = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link URL }
     *     
     */
    public URL getURL() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link URL }
     *     
     */
    public void setURL(URL value) {
        this.url = value;
    }

    /**
     * Gets the value of the otherOrderTarget property.
     * 
     * @return
     *     possible object is
     *     {@link OtherOrderTarget }
     *     
     */
    public OtherOrderTarget getOtherOrderTarget() {
        return otherOrderTarget;
    }

    /**
     * Sets the value of the otherOrderTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link OtherOrderTarget }
     *     
     */
    public void setOtherOrderTarget(OtherOrderTarget value) {
        this.otherOrderTarget = value;
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
            Phone thePhone;
            thePhone = this.getPhone();
            strategy.appendField(locator, this, "phone", buffer, thePhone);
        }
        {
            Email theEmail;
            theEmail = this.getEmail();
            strategy.appendField(locator, this, "email", buffer, theEmail);
        }
        {
            Fax theFax;
            theFax = this.getFax();
            strategy.appendField(locator, this, "fax", buffer, theFax);
        }
        {
            URL theURL;
            theURL = this.getURL();
            strategy.appendField(locator, this, "url", buffer, theURL);
        }
        {
            OtherOrderTarget theOtherOrderTarget;
            theOtherOrderTarget = this.getOtherOrderTarget();
            strategy.appendField(locator, this, "otherOrderTarget", buffer, theOtherOrderTarget);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof OrderTarget)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final OrderTarget that = ((OrderTarget) object);
        {
            Phone lhsPhone;
            lhsPhone = this.getPhone();
            Phone rhsPhone;
            rhsPhone = that.getPhone();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "phone", lhsPhone), LocatorUtils.property(thatLocator, "phone", rhsPhone), lhsPhone, rhsPhone)) {
                return false;
            }
        }
        {
            Email lhsEmail;
            lhsEmail = this.getEmail();
            Email rhsEmail;
            rhsEmail = that.getEmail();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "email", lhsEmail), LocatorUtils.property(thatLocator, "email", rhsEmail), lhsEmail, rhsEmail)) {
                return false;
            }
        }
        {
            Fax lhsFax;
            lhsFax = this.getFax();
            Fax rhsFax;
            rhsFax = that.getFax();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "fax", lhsFax), LocatorUtils.property(thatLocator, "fax", rhsFax), lhsFax, rhsFax)) {
                return false;
            }
        }
        {
            URL lhsURL;
            lhsURL = this.getURL();
            URL rhsURL;
            rhsURL = that.getURL();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "url", lhsURL), LocatorUtils.property(thatLocator, "url", rhsURL), lhsURL, rhsURL)) {
                return false;
            }
        }
        {
            OtherOrderTarget lhsOtherOrderTarget;
            lhsOtherOrderTarget = this.getOtherOrderTarget();
            OtherOrderTarget rhsOtherOrderTarget;
            rhsOtherOrderTarget = that.getOtherOrderTarget();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "otherOrderTarget", lhsOtherOrderTarget), LocatorUtils.property(thatLocator, "otherOrderTarget", rhsOtherOrderTarget), lhsOtherOrderTarget, rhsOtherOrderTarget)) {
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
            Phone thePhone;
            thePhone = this.getPhone();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "phone", thePhone), currentHashCode, thePhone);
        }
        {
            Email theEmail;
            theEmail = this.getEmail();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "email", theEmail), currentHashCode, theEmail);
        }
        {
            Fax theFax;
            theFax = this.getFax();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "fax", theFax), currentHashCode, theFax);
        }
        {
            URL theURL;
            theURL = this.getURL();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "url", theURL), currentHashCode, theURL);
        }
        {
            OtherOrderTarget theOtherOrderTarget;
            theOtherOrderTarget = this.getOtherOrderTarget();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "otherOrderTarget", theOtherOrderTarget), currentHashCode, theOtherOrderTarget);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
