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
 *       &lt;choice>
 *         &lt;element ref="{}CIFContent"/>
 *         &lt;element ref="{}Index"/>
 *         &lt;element ref="{}Contract"/>
 *       &lt;/choice>
 *       &lt;attribute name="filename" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "cifContent",
    "index",
    "contract"
})
@XmlRootElement(name = "SubscriptionContent")
public class SubscriptionContent
    implements Serializable, Equals, HashCode, ToString
{

    @XmlElement(name = "CIFContent")
    protected CIFContent cifContent;
    @XmlElement(name = "Index")
    protected Index index;
    @XmlElement(name = "Contract")
    protected Contract contract;
    @XmlAttribute(name = "filename")
    @XmlSchemaType(name = "anySimpleType")
    protected String filename;

    /**
     * Gets the value of the cifContent property.
     * 
     * @return
     *     possible object is
     *     {@link CIFContent }
     *     
     */
    public CIFContent getCIFContent() {
        return cifContent;
    }

    /**
     * Sets the value of the cifContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link CIFContent }
     *     
     */
    public void setCIFContent(CIFContent value) {
        this.cifContent = value;
    }

    /**
     * Gets the value of the index property.
     * 
     * @return
     *     possible object is
     *     {@link Index }
     *     
     */
    public Index getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     * 
     * @param value
     *     allowed object is
     *     {@link Index }
     *     
     */
    public void setIndex(Index value) {
        this.index = value;
    }

    /**
     * Gets the value of the contract property.
     * 
     * @return
     *     possible object is
     *     {@link Contract }
     *     
     */
    public Contract getContract() {
        return contract;
    }

    /**
     * Sets the value of the contract property.
     * 
     * @param value
     *     allowed object is
     *     {@link Contract }
     *     
     */
    public void setContract(Contract value) {
        this.contract = value;
    }

    /**
     * Gets the value of the filename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the value of the filename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilename(String value) {
        this.filename = value;
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
            CIFContent theCIFContent;
            theCIFContent = this.getCIFContent();
            strategy.appendField(locator, this, "cifContent", buffer, theCIFContent);
        }
        {
            Index theIndex;
            theIndex = this.getIndex();
            strategy.appendField(locator, this, "index", buffer, theIndex);
        }
        {
            Contract theContract;
            theContract = this.getContract();
            strategy.appendField(locator, this, "contract", buffer, theContract);
        }
        {
            String theFilename;
            theFilename = this.getFilename();
            strategy.appendField(locator, this, "filename", buffer, theFilename);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof SubscriptionContent)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final SubscriptionContent that = ((SubscriptionContent) object);
        {
            CIFContent lhsCIFContent;
            lhsCIFContent = this.getCIFContent();
            CIFContent rhsCIFContent;
            rhsCIFContent = that.getCIFContent();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "cifContent", lhsCIFContent), LocatorUtils.property(thatLocator, "cifContent", rhsCIFContent), lhsCIFContent, rhsCIFContent)) {
                return false;
            }
        }
        {
            Index lhsIndex;
            lhsIndex = this.getIndex();
            Index rhsIndex;
            rhsIndex = that.getIndex();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "index", lhsIndex), LocatorUtils.property(thatLocator, "index", rhsIndex), lhsIndex, rhsIndex)) {
                return false;
            }
        }
        {
            Contract lhsContract;
            lhsContract = this.getContract();
            Contract rhsContract;
            rhsContract = that.getContract();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "contract", lhsContract), LocatorUtils.property(thatLocator, "contract", rhsContract), lhsContract, rhsContract)) {
                return false;
            }
        }
        {
            String lhsFilename;
            lhsFilename = this.getFilename();
            String rhsFilename;
            rhsFilename = that.getFilename();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "filename", lhsFilename), LocatorUtils.property(thatLocator, "filename", rhsFilename), lhsFilename, rhsFilename)) {
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
            CIFContent theCIFContent;
            theCIFContent = this.getCIFContent();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "cifContent", theCIFContent), currentHashCode, theCIFContent);
        }
        {
            Index theIndex;
            theIndex = this.getIndex();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "index", theIndex), currentHashCode, theIndex);
        }
        {
            Contract theContract;
            theContract = this.getContract();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "contract", theContract), currentHashCode, theContract);
        }
        {
            String theFilename;
            theFilename = this.getFilename();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "filename", theFilename), currentHashCode, theFilename);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}