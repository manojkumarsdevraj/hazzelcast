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
 *         &lt;element ref="{}Name"/>
 *         &lt;element ref="{}SearchDataElement" maxOccurs="unbounded"/>
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
    "nameElement",
    "searchDataElement"
})
@XmlRootElement(name = "SearchGroupData")
public class SearchGroupData
    implements Serializable, Equals, HashCode, ToString
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2718583005531640980L;
	@XmlElement(name = "Name", required = true)
    protected Name nameElement;
    @XmlElement(name = "SearchDataElement", required = true)
    protected List<SearchDataElement> searchDataElement;

    /**
     * Gets the value of the nameElement property.
     * 
     * @return
     *     possible object is
     *     {@link Name }
     *     
     */
    public Name getNameElement() {
        return nameElement;
    }

    /**
     * Sets the value of the nameElement property.
     * 
     * @param value
     *     allowed object is
     *     {@link Name }
     *     
     */
    public void setNameElement(Name value) {
        this.nameElement = value;
    }

    /**
     * Gets the value of the searchDataElement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the searchDataElement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSearchDataElement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SearchDataElement }
     * 
     * 
     */
    public List<SearchDataElement> getSearchDataElement() {
        if (searchDataElement == null) {
            searchDataElement = new ArrayList<SearchDataElement>();
        }
        return this.searchDataElement;
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
            Name theNameElement;
            theNameElement = this.getNameElement();
            strategy.appendField(locator, this, "nameElement", buffer, theNameElement);
        }
        {
            List<SearchDataElement> theSearchDataElement;
            theSearchDataElement = (((this.searchDataElement!= null)&&(!this.searchDataElement.isEmpty()))?this.getSearchDataElement():null);
            strategy.appendField(locator, this, "searchDataElement", buffer, theSearchDataElement);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof SearchGroupData)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final SearchGroupData that = ((SearchGroupData) object);
        {
            Name lhsNameElement;
            lhsNameElement = this.getNameElement();
            Name rhsNameElement;
            rhsNameElement = that.getNameElement();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "nameElement", lhsNameElement), LocatorUtils.property(thatLocator, "nameElement", rhsNameElement), lhsNameElement, rhsNameElement)) {
                return false;
            }
        }
        {
            List<SearchDataElement> lhsSearchDataElement;
            lhsSearchDataElement = (((this.searchDataElement!= null)&&(!this.searchDataElement.isEmpty()))?this.getSearchDataElement():null);
            List<SearchDataElement> rhsSearchDataElement;
            rhsSearchDataElement = (((that.searchDataElement!= null)&&(!that.searchDataElement.isEmpty()))?that.getSearchDataElement():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "searchDataElement", lhsSearchDataElement), LocatorUtils.property(thatLocator, "searchDataElement", rhsSearchDataElement), lhsSearchDataElement, rhsSearchDataElement)) {
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
            Name theNameElement;
            theNameElement = this.getNameElement();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "nameElement", theNameElement), currentHashCode, theNameElement);
        }
        {
            List<SearchDataElement> theSearchDataElement;
            theSearchDataElement = (((this.searchDataElement!= null)&&(!this.searchDataElement.isEmpty()))?this.getSearchDataElement():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "searchDataElement", theSearchDataElement), currentHashCode, theSearchDataElement);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
