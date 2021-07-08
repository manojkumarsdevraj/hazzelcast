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
 *         &lt;element ref="{}Description" maxOccurs="unbounded"/>
 *         &lt;element ref="{}URL"/>
 *         &lt;element ref="{}Classification" maxOccurs="unbounded"/>
 *         &lt;element ref="{}ManufacturerName" minOccurs="0"/>
 *         &lt;element ref="{}ManufacturerPartID" minOccurs="0"/>
 *         &lt;element ref="{}ExpirationDate" minOccurs="0"/>
 *         &lt;element ref="{}EffectiveDate" minOccurs="0"/>
 *         &lt;element ref="{}SearchGroupData" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}TerritoryAvailable" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}Extrinsic" maxOccurs="unbounded" minOccurs="0"/>
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
    "description",
    "url",
    "classification",
    "manufacturerName",
    "manufacturerPartID",
    "expirationDate",
    "effectiveDate",
    "searchGroupData",
    "territoryAvailable",
    "extrinsic"
})
@XmlRootElement(name = "PunchoutDetail")
public class PunchoutDetail
    implements Serializable, Equals, HashCode, ToString
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 444286727402672272L;
	@XmlElement(name = "Description", required = true)
    protected List<Description> description;
    @XmlElement(name = "URL", required = true)
    protected URL url;
    @XmlElement(name = "Classification", required = true)
    protected List<Classification> classification;
    @XmlElement(name = "ManufacturerName")
    protected ManufacturerName manufacturerName;
    @XmlElement(name = "ManufacturerPartID")
    protected ManufacturerPartID manufacturerPartID;
    @XmlElement(name = "ExpirationDate")
    protected ExpirationDate expirationDate;
    @XmlElement(name = "EffectiveDate")
    protected EffectiveDate effectiveDate;
    @XmlElement(name = "SearchGroupData")
    protected List<SearchGroupData> searchGroupData;
    @XmlElement(name = "TerritoryAvailable")
    protected List<TerritoryAvailable> territoryAvailable;
    @XmlElement(name = "Extrinsic")
    protected List<Extrinsic> extrinsic;

    /**
     * Gets the value of the description property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the description property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Description }
     * 
     * 
     */
    public List<Description> getDescription() {
        if (description == null) {
            description = new ArrayList<Description>();
        }
        return this.description;
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
     * Gets the value of the classification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Classification }
     * 
     * 
     */
    public List<Classification> getClassification() {
        if (classification == null) {
            classification = new ArrayList<Classification>();
        }
        return this.classification;
    }

    /**
     * Gets the value of the manufacturerName property.
     * 
     * @return
     *     possible object is
     *     {@link ManufacturerName }
     *     
     */
    public ManufacturerName getManufacturerName() {
        return manufacturerName;
    }

    /**
     * Sets the value of the manufacturerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManufacturerName }
     *     
     */
    public void setManufacturerName(ManufacturerName value) {
        this.manufacturerName = value;
    }

    /**
     * Gets the value of the manufacturerPartID property.
     * 
     * @return
     *     possible object is
     *     {@link ManufacturerPartID }
     *     
     */
    public ManufacturerPartID getManufacturerPartID() {
        return manufacturerPartID;
    }

    /**
     * Sets the value of the manufacturerPartID property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManufacturerPartID }
     *     
     */
    public void setManufacturerPartID(ManufacturerPartID value) {
        this.manufacturerPartID = value;
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
            List<Description> theDescription;
            theDescription = (((this.description!= null)&&(!this.description.isEmpty()))?this.getDescription():null);
            strategy.appendField(locator, this, "description", buffer, theDescription);
        }
        {
            URL theURL;
            theURL = this.getURL();
            strategy.appendField(locator, this, "url", buffer, theURL);
        }
        {
            List<Classification> theClassification;
            theClassification = (((this.classification!= null)&&(!this.classification.isEmpty()))?this.getClassification():null);
            strategy.appendField(locator, this, "classification", buffer, theClassification);
        }
        {
            ManufacturerName theManufacturerName;
            theManufacturerName = this.getManufacturerName();
            strategy.appendField(locator, this, "manufacturerName", buffer, theManufacturerName);
        }
        {
            ManufacturerPartID theManufacturerPartID;
            theManufacturerPartID = this.getManufacturerPartID();
            strategy.appendField(locator, this, "manufacturerPartID", buffer, theManufacturerPartID);
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
        {
            List<Extrinsic> theExtrinsic;
            theExtrinsic = (((this.extrinsic!= null)&&(!this.extrinsic.isEmpty()))?this.getExtrinsic():null);
            strategy.appendField(locator, this, "extrinsic", buffer, theExtrinsic);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof PunchoutDetail)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final PunchoutDetail that = ((PunchoutDetail) object);
        {
            List<Description> lhsDescription;
            lhsDescription = (((this.description!= null)&&(!this.description.isEmpty()))?this.getDescription():null);
            List<Description> rhsDescription;
            rhsDescription = (((that.description!= null)&&(!that.description.isEmpty()))?that.getDescription():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "description", lhsDescription), LocatorUtils.property(thatLocator, "description", rhsDescription), lhsDescription, rhsDescription)) {
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
            List<Classification> lhsClassification;
            lhsClassification = (((this.classification!= null)&&(!this.classification.isEmpty()))?this.getClassification():null);
            List<Classification> rhsClassification;
            rhsClassification = (((that.classification!= null)&&(!that.classification.isEmpty()))?that.getClassification():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "classification", lhsClassification), LocatorUtils.property(thatLocator, "classification", rhsClassification), lhsClassification, rhsClassification)) {
                return false;
            }
        }
        {
            ManufacturerName lhsManufacturerName;
            lhsManufacturerName = this.getManufacturerName();
            ManufacturerName rhsManufacturerName;
            rhsManufacturerName = that.getManufacturerName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "manufacturerName", lhsManufacturerName), LocatorUtils.property(thatLocator, "manufacturerName", rhsManufacturerName), lhsManufacturerName, rhsManufacturerName)) {
                return false;
            }
        }
        {
            ManufacturerPartID lhsManufacturerPartID;
            lhsManufacturerPartID = this.getManufacturerPartID();
            ManufacturerPartID rhsManufacturerPartID;
            rhsManufacturerPartID = that.getManufacturerPartID();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "manufacturerPartID", lhsManufacturerPartID), LocatorUtils.property(thatLocator, "manufacturerPartID", rhsManufacturerPartID), lhsManufacturerPartID, rhsManufacturerPartID)) {
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
        {
            List<Extrinsic> lhsExtrinsic;
            lhsExtrinsic = (((this.extrinsic!= null)&&(!this.extrinsic.isEmpty()))?this.getExtrinsic():null);
            List<Extrinsic> rhsExtrinsic;
            rhsExtrinsic = (((that.extrinsic!= null)&&(!that.extrinsic.isEmpty()))?that.getExtrinsic():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "extrinsic", lhsExtrinsic), LocatorUtils.property(thatLocator, "extrinsic", rhsExtrinsic), lhsExtrinsic, rhsExtrinsic)) {
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
            List<Description> theDescription;
            theDescription = (((this.description!= null)&&(!this.description.isEmpty()))?this.getDescription():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "description", theDescription), currentHashCode, theDescription);
        }
        {
            URL theURL;
            theURL = this.getURL();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "url", theURL), currentHashCode, theURL);
        }
        {
            List<Classification> theClassification;
            theClassification = (((this.classification!= null)&&(!this.classification.isEmpty()))?this.getClassification():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "classification", theClassification), currentHashCode, theClassification);
        }
        {
            ManufacturerName theManufacturerName;
            theManufacturerName = this.getManufacturerName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "manufacturerName", theManufacturerName), currentHashCode, theManufacturerName);
        }
        {
            ManufacturerPartID theManufacturerPartID;
            theManufacturerPartID = this.getManufacturerPartID();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "manufacturerPartID", theManufacturerPartID), currentHashCode, theManufacturerPartID);
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
        {
            List<Extrinsic> theExtrinsic;
            theExtrinsic = (((this.extrinsic!= null)&&(!this.extrinsic.isEmpty()))?this.getExtrinsic():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "extrinsic", theExtrinsic), currentHashCode, theExtrinsic);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
