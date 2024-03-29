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
 *         &lt;element ref="{}Money"/>
 *         &lt;element ref="{}Description"/>
 *         &lt;element ref="{}TaxDetail" maxOccurs="unbounded" minOccurs="0"/>
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
    "money",
    "description",
    "taxDetail"
})
@XmlRootElement(name = "Tax")
public class Tax
    implements Serializable, Equals, HashCode, ToString
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6945870637045272451L;
	@XmlElement(name = "Money", required = true)
    protected Money money;
    @XmlElement(name = "Description", required = true)
    protected Description description;
    @XmlElement(name = "TaxDetail")
    protected List<TaxDetail> taxDetail;

    /**
     * Gets the value of the money property.
     * 
     * @return
     *     possible object is
     *     {@link Money }
     *     
     */
    public Money getMoney() {
        return money;
    }

    /**
     * Sets the value of the money property.
     * 
     * @param value
     *     allowed object is
     *     {@link Money }
     *     
     */
    public void setMoney(Money value) {
        this.money = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Description }
     *     
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Description }
     *     
     */
    public void setDescription(Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the taxDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taxDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaxDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaxDetail }
     * 
     * 
     */
    public List<TaxDetail> getTaxDetail() {
        if (taxDetail == null) {
            taxDetail = new ArrayList<TaxDetail>();
        }
        return this.taxDetail;
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
            Money theMoney;
            theMoney = this.getMoney();
            strategy.appendField(locator, this, "money", buffer, theMoney);
        }
        {
            Description theDescription;
            theDescription = this.getDescription();
            strategy.appendField(locator, this, "description", buffer, theDescription);
        }
        {
            List<TaxDetail> theTaxDetail;
            theTaxDetail = (((this.taxDetail!= null)&&(!this.taxDetail.isEmpty()))?this.getTaxDetail():null);
            strategy.appendField(locator, this, "taxDetail", buffer, theTaxDetail);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Tax)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Tax that = ((Tax) object);
        {
            Money lhsMoney;
            lhsMoney = this.getMoney();
            Money rhsMoney;
            rhsMoney = that.getMoney();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "money", lhsMoney), LocatorUtils.property(thatLocator, "money", rhsMoney), lhsMoney, rhsMoney)) {
                return false;
            }
        }
        {
            Description lhsDescription;
            lhsDescription = this.getDescription();
            Description rhsDescription;
            rhsDescription = that.getDescription();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "description", lhsDescription), LocatorUtils.property(thatLocator, "description", rhsDescription), lhsDescription, rhsDescription)) {
                return false;
            }
        }
        {
            List<TaxDetail> lhsTaxDetail;
            lhsTaxDetail = (((this.taxDetail!= null)&&(!this.taxDetail.isEmpty()))?this.getTaxDetail():null);
            List<TaxDetail> rhsTaxDetail;
            rhsTaxDetail = (((that.taxDetail!= null)&&(!that.taxDetail.isEmpty()))?that.getTaxDetail():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "taxDetail", lhsTaxDetail), LocatorUtils.property(thatLocator, "taxDetail", rhsTaxDetail), lhsTaxDetail, rhsTaxDetail)) {
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
            Money theMoney;
            theMoney = this.getMoney();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "money", theMoney), currentHashCode, theMoney);
        }
        {
            Description theDescription;
            theDescription = this.getDescription();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "description", theDescription), currentHashCode, theDescription);
        }
        {
            List<TaxDetail> theTaxDetail;
            theTaxDetail = (((this.taxDetail!= null)&&(!this.taxDetail.isEmpty()))?this.getTaxDetail():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "taxDetail", theTaxDetail), currentHashCode, theTaxDetail);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
