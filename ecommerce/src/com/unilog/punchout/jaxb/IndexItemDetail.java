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
 *         &lt;element ref="{}LeadTime"/>
 *         &lt;element ref="{}ExpirationDate" minOccurs="0"/>
 *         &lt;element ref="{}EffectiveDate" minOccurs="0"/>
 *         &lt;element ref="{}SearchGroupData" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}TerritoryAvailable" maxOccurs="unbounded" minOccurs="0"/>
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
    "leadTime",
    "expirationDate",
    "effectiveDate",
    "searchGroupData",
    "territoryAvailable"
})
@XmlRootElement(name = "IndexItemDetail")
public class IndexItemDetail
    implements Serializable, Equals, HashCode, ToString
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -4030837918295708692L;
	@XmlElement(name = "LeadTime", required = true)
    protected LeadTime leadTime;
    @XmlElement(name = "ExpirationDate")
    protected ExpirationDate expirationDate;
    @XmlElement(name = "EffectiveDate")
    protected EffectiveDate effectiveDate;
    @XmlElement(name = "SearchGroupData")
    protected List<SearchGroupData> searchGroupData;
    @XmlElement(name = "TerritoryAvailable")
    protected List<TerritoryAvailable> territoryAvailable;

    /**
     * Gets the value of the leadTime property.
     * 
     * @return
     *     possible object is
     *     {@link LeadTime }
     *     
     */
    public LeadTime getLeadTime() {
        return leadTime;
    }

    /**
     * Sets the value of the leadTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link LeadTime }
     *     
     */
    public void setLeadTime(LeadTime value) {
        this.leadTime = value;
    }

    /**
     * Gets the value of the expirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link ExpirationDate }
     *     
     */
    public ExpirationDate getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the value of the expirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExpirationDate }
     *     
     */
    public void setExpirationDate(ExpirationDate value) {
        this.expirationDate = value;
    }

    /**
     * Gets the value of the effectiveDate property.
     * 
     * @return
     *     possible object is
     *     {@link EffectiveDate }
     *     
     */
    public EffectiveDate getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * Sets the value of the effectiveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link EffectiveDate }
     *     
     */
    public void setEffectiveDate(EffectiveDate value) {
        this.effectiveDate = value;
    }

    /**
     * Gets the value of the searchGroupData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the searchGroupData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSearchGroupData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SearchGroupData }
     * 
     * 
     */
    public List<SearchGroupData> getSearchGroupData() {
        if (searchGroupData == null) {
            searchGroupData = new ArrayList<SearchGroupData>();
        }
        return this.searchGroupData;
    }

    /**
     * Gets the value of the territoryAvailable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the territoryAvailable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTerritoryAvailable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TerritoryAvailable }
     * 
     * 
     */
    public List<TerritoryAvailable> getTerritoryAvailable() {
        if (territoryAvailable == null) {
            territoryAvailable = new ArrayList<TerritoryAvailable>();
        }
        return this.territoryAvailable;
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
            LeadTime theLeadTime;
            theLeadTime = this.getLeadTime();
            strategy.appendField(locator, this, "leadTime", buffer, theLeadTime);
        }
        {
            ExpirationDate theExpirationDate;
            theExpirationDate = this.getExpirationDate();
            strategy.appendField(locator, this, "expirationDate", buffer, theExpirationDate);
        }
        {
            EffectiveDate theEffectiveDate;
            theEffectiveDate = this.getEffectiveDate();
            strategy.appendField(locator, this, "effectiveDate", buffer, theEffectiveDate);
        }
        {
            List<SearchGroupData> theSearchGroupData;
            theSearchGroupData = (((this.searchGroupData!= null)&&(!this.searchGroupData.isEmpty()))?this.getSearchGroupData():null);
            strategy.appendField(locator, this, "searchGroupData", buffer, theSearchGroupData);
        }
        {
            List<TerritoryAvailable> theTerritoryAvailable;
            theTerritoryAvailable = (((this.territoryAvailable!= null)&&(!this.territoryAvailable.isEmpty()))?this.getTerritoryAvailable():null);
            strategy.appendField(locator, this, "territoryAvailable", buffer, theTerritoryAvailable);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof IndexItemDetail)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final IndexItemDetail that = ((IndexItemDetail) object);
        {
            LeadTime lhsLeadTime;
            lhsLeadTime = this.getLeadTime();
            LeadTime rhsLeadTime;
            rhsLeadTime = that.getLeadTime();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "leadTime", lhsLeadTime), LocatorUtils.property(thatLocator, "leadTime", rhsLeadTime), lhsLeadTime, rhsLeadTime)) {
                return false;
            }
        }
        {
            ExpirationDate lhsExpirationDate;
            lhsExpirationDate = this.getExpirationDate();
            ExpirationDate rhsExpirationDate;
            rhsExpirationDate = that.getExpirationDate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "expirationDate", lhsExpirationDate), LocatorUtils.property(thatLocator, "expirationDate", rhsExpirationDate), lhsExpirationDate, rhsExpirationDate)) {
                return false;
            }
        }
        {
            EffectiveDate lhsEffectiveDate;
            lhsEffectiveDate = this.getEffectiveDate();
            EffectiveDate rhsEffectiveDate;
            rhsEffectiveDate = that.getEffectiveDate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "effectiveDate", lhsEffectiveDate), LocatorUtils.property(thatLocator, "effectiveDate", rhsEffectiveDate), lhsEffectiveDate, rhsEffectiveDate)) {
                return false;
            }
        }
        {
            List<SearchGroupData> lhsSearchGroupData;
            lhsSearchGroupData = (((this.searchGroupData!= null)&&(!this.searchGroupData.isEmpty()))?this.getSearchGroupData():null);
            List<SearchGroupData> rhsSearchGroupData;
            rhsSearchGroupData = (((that.searchGroupData!= null)&&(!that.searchGroupData.isEmpty()))?that.getSearchGroupData():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "searchGroupData", lhsSearchGroupData), LocatorUtils.property(thatLocator, "searchGroupData", rhsSearchGroupData), lhsSearchGroupData, rhsSearchGroupData)) {
                return false;
            }
        }
        {
            List<TerritoryAvailable> lhsTerritoryAvailable;
            lhsTerritoryAvailable = (((this.territoryAvailable!= null)&&(!this.territoryAvailable.isEmpty()))?this.getTerritoryAvailable():null);
            List<TerritoryAvailable> rhsTerritoryAvailable;
            rhsTerritoryAvailable = (((that.territoryAvailable!= null)&&(!that.territoryAvailable.isEmpty()))?that.getTerritoryAvailable():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "territoryAvailable", lhsTerritoryAvailable), LocatorUtils.property(thatLocator, "territoryAvailable", rhsTerritoryAvailable), lhsTerritoryAvailable, rhsTerritoryAvailable)) {
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
            LeadTime theLeadTime;
            theLeadTime = this.getLeadTime();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "leadTime", theLeadTime), currentHashCode, theLeadTime);
        }
        {
            ExpirationDate theExpirationDate;
            theExpirationDate = this.getExpirationDate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "expirationDate", theExpirationDate), currentHashCode, theExpirationDate);
        }
        {
            EffectiveDate theEffectiveDate;
            theEffectiveDate = this.getEffectiveDate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "effectiveDate", theEffectiveDate), currentHashCode, theEffectiveDate);
        }
        {
            List<SearchGroupData> theSearchGroupData;
            theSearchGroupData = (((this.searchGroupData!= null)&&(!this.searchGroupData.isEmpty()))?this.getSearchGroupData():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "searchGroupData", theSearchGroupData), currentHashCode, theSearchGroupData);
        }
        {
            List<TerritoryAvailable> theTerritoryAvailable;
            theTerritoryAvailable = (((this.territoryAvailable!= null)&&(!this.territoryAvailable.isEmpty()))?this.getTerritoryAvailable():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "territoryAvailable", theTerritoryAvailable), currentHashCode, theTerritoryAvailable);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
