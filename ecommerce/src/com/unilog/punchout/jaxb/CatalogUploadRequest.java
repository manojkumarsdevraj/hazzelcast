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
 *         &lt;element ref="{}CatalogName"/>
 *         &lt;element ref="{}Description"/>
 *         &lt;element ref="{}Attachment"/>
 *         &lt;element ref="{}Commodities" minOccurs="0"/>
 *         &lt;element ref="{}AutoPublish" minOccurs="0"/>
 *         &lt;element ref="{}Notification"/>
 *       &lt;/sequence>
 *       &lt;attribute name="operation" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="new"/>
 *             &lt;enumeration value="update"/>
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
    "catalogName",
    "description",
    "attachment",
    "commodities",
    "autoPublish",
    "notification"
})
@XmlRootElement(name = "CatalogUploadRequest")
public class CatalogUploadRequest
    implements Serializable, Equals, HashCode, ToString
{

    @XmlElement(name = "CatalogName", required = true)
    protected CatalogName catalogName;
    @XmlElement(name = "Description", required = true)
    protected Description description;
    @XmlElement(name = "Attachment", required = true)
    protected Attachment attachment;
    @XmlElement(name = "Commodities")
    protected Commodities commodities;
    @XmlElement(name = "AutoPublish")
    protected AutoPublish autoPublish;
    @XmlElement(name = "Notification", required = true)
    protected Notification notification;
    @XmlAttribute(name = "operation", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String operation;

    /**
     * Gets the value of the catalogName property.
     * 
     * @return
     *     possible object is
     *     {@link CatalogName }
     *     
     */
    public CatalogName getCatalogName() {
        return catalogName;
    }

    /**
     * Sets the value of the catalogName property.
     * 
     * @param value
     *     allowed object is
     *     {@link CatalogName }
     *     
     */
    public void setCatalogName(CatalogName value) {
        this.catalogName = value;
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
     * Gets the value of the attachment property.
     * 
     * @return
     *     possible object is
     *     {@link Attachment }
     *     
     */
    public Attachment getAttachment() {
        return attachment;
    }

    /**
     * Sets the value of the attachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Attachment }
     *     
     */
    public void setAttachment(Attachment value) {
        this.attachment = value;
    }

    /**
     * Gets the value of the commodities property.
     * 
     * @return
     *     possible object is
     *     {@link Commodities }
     *     
     */
    public Commodities getCommodities() {
        return commodities;
    }

    /**
     * Sets the value of the commodities property.
     * 
     * @param value
     *     allowed object is
     *     {@link Commodities }
     *     
     */
    public void setCommodities(Commodities value) {
        this.commodities = value;
    }

    /**
     * Gets the value of the autoPublish property.
     * 
     * @return
     *     possible object is
     *     {@link AutoPublish }
     *     
     */
    public AutoPublish getAutoPublish() {
        return autoPublish;
    }

    /**
     * Sets the value of the autoPublish property.
     * 
     * @param value
     *     allowed object is
     *     {@link AutoPublish }
     *     
     */
    public void setAutoPublish(AutoPublish value) {
        this.autoPublish = value;
    }

    /**
     * Gets the value of the notification property.
     * 
     * @return
     *     possible object is
     *     {@link Notification }
     *     
     */
    public Notification getNotification() {
        return notification;
    }

    /**
     * Sets the value of the notification property.
     * 
     * @param value
     *     allowed object is
     *     {@link Notification }
     *     
     */
    public void setNotification(Notification value) {
        this.notification = value;
    }

    /**
     * Gets the value of the operation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Sets the value of the operation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperation(String value) {
        this.operation = value;
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
            CatalogName theCatalogName;
            theCatalogName = this.getCatalogName();
            strategy.appendField(locator, this, "catalogName", buffer, theCatalogName);
        }
        {
            Description theDescription;
            theDescription = this.getDescription();
            strategy.appendField(locator, this, "description", buffer, theDescription);
        }
        {
            Attachment theAttachment;
            theAttachment = this.getAttachment();
            strategy.appendField(locator, this, "attachment", buffer, theAttachment);
        }
        {
            Commodities theCommodities;
            theCommodities = this.getCommodities();
            strategy.appendField(locator, this, "commodities", buffer, theCommodities);
        }
        {
            AutoPublish theAutoPublish;
            theAutoPublish = this.getAutoPublish();
            strategy.appendField(locator, this, "autoPublish", buffer, theAutoPublish);
        }
        {
            Notification theNotification;
            theNotification = this.getNotification();
            strategy.appendField(locator, this, "notification", buffer, theNotification);
        }
        {
            String theOperation;
            theOperation = this.getOperation();
            strategy.appendField(locator, this, "operation", buffer, theOperation);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof CatalogUploadRequest)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final CatalogUploadRequest that = ((CatalogUploadRequest) object);
        {
            CatalogName lhsCatalogName;
            lhsCatalogName = this.getCatalogName();
            CatalogName rhsCatalogName;
            rhsCatalogName = that.getCatalogName();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "catalogName", lhsCatalogName), LocatorUtils.property(thatLocator, "catalogName", rhsCatalogName), lhsCatalogName, rhsCatalogName)) {
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
            Attachment lhsAttachment;
            lhsAttachment = this.getAttachment();
            Attachment rhsAttachment;
            rhsAttachment = that.getAttachment();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "attachment", lhsAttachment), LocatorUtils.property(thatLocator, "attachment", rhsAttachment), lhsAttachment, rhsAttachment)) {
                return false;
            }
        }
        {
            Commodities lhsCommodities;
            lhsCommodities = this.getCommodities();
            Commodities rhsCommodities;
            rhsCommodities = that.getCommodities();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "commodities", lhsCommodities), LocatorUtils.property(thatLocator, "commodities", rhsCommodities), lhsCommodities, rhsCommodities)) {
                return false;
            }
        }
        {
            AutoPublish lhsAutoPublish;
            lhsAutoPublish = this.getAutoPublish();
            AutoPublish rhsAutoPublish;
            rhsAutoPublish = that.getAutoPublish();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "autoPublish", lhsAutoPublish), LocatorUtils.property(thatLocator, "autoPublish", rhsAutoPublish), lhsAutoPublish, rhsAutoPublish)) {
                return false;
            }
        }
        {
            Notification lhsNotification;
            lhsNotification = this.getNotification();
            Notification rhsNotification;
            rhsNotification = that.getNotification();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "notification", lhsNotification), LocatorUtils.property(thatLocator, "notification", rhsNotification), lhsNotification, rhsNotification)) {
                return false;
            }
        }
        {
            String lhsOperation;
            lhsOperation = this.getOperation();
            String rhsOperation;
            rhsOperation = that.getOperation();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "operation", lhsOperation), LocatorUtils.property(thatLocator, "operation", rhsOperation), lhsOperation, rhsOperation)) {
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
            CatalogName theCatalogName;
            theCatalogName = this.getCatalogName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "catalogName", theCatalogName), currentHashCode, theCatalogName);
        }
        {
            Description theDescription;
            theDescription = this.getDescription();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "description", theDescription), currentHashCode, theDescription);
        }
        {
            Attachment theAttachment;
            theAttachment = this.getAttachment();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "attachment", theAttachment), currentHashCode, theAttachment);
        }
        {
            Commodities theCommodities;
            theCommodities = this.getCommodities();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "commodities", theCommodities), currentHashCode, theCommodities);
        }
        {
            AutoPublish theAutoPublish;
            theAutoPublish = this.getAutoPublish();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "autoPublish", theAutoPublish), currentHashCode, theAutoPublish);
        }
        {
            Notification theNotification;
            theNotification = this.getNotification();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "notification", theNotification), currentHashCode, theNotification);
        }
        {
            String theOperation;
            theOperation = this.getOperation();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "operation", theOperation), currentHashCode, theOperation);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}