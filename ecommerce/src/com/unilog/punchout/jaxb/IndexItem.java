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
 *       &lt;choice>
 *         &lt;element ref="{}IndexItemAdd" maxOccurs="unbounded"/>
 *         &lt;element ref="{}IndexItemDelete" maxOccurs="unbounded"/>
 *         &lt;element ref="{}IndexItemPunchout" maxOccurs="unbounded"/>
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
    "indexItemAdd",
    "indexItemDelete",
    "indexItemPunchout"
})
@XmlRootElement(name = "IndexItem")
public class IndexItem
    implements Serializable, Equals, HashCode, ToString
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 7201304911086129017L;
	@XmlElement(name = "IndexItemAdd")
    protected List<IndexItemAdd> indexItemAdd;
    @XmlElement(name = "IndexItemDelete")
    protected List<IndexItemDelete> indexItemDelete;
    @XmlElement(name = "IndexItemPunchout")
    protected List<IndexItemPunchout> indexItemPunchout;

    /**
     * Gets the value of the indexItemAdd property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the indexItemAdd property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndexItemAdd().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IndexItemAdd }
     * 
     * 
     */
    public List<IndexItemAdd> getIndexItemAdd() {
        if (indexItemAdd == null) {
            indexItemAdd = new ArrayList<IndexItemAdd>();
        }
        return this.indexItemAdd;
    }

    /**
     * Gets the value of the indexItemDelete property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the indexItemDelete property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndexItemDelete().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IndexItemDelete }
     * 
     * 
     */
    public List<IndexItemDelete> getIndexItemDelete() {
        if (indexItemDelete == null) {
            indexItemDelete = new ArrayList<IndexItemDelete>();
        }
        return this.indexItemDelete;
    }

    /**
     * Gets the value of the indexItemPunchout property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the indexItemPunchout property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndexItemPunchout().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IndexItemPunchout }
     * 
     * 
     */
    public List<IndexItemPunchout> getIndexItemPunchout() {
        if (indexItemPunchout == null) {
            indexItemPunchout = new ArrayList<IndexItemPunchout>();
        }
        return this.indexItemPunchout;
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
            List<IndexItemAdd> theIndexItemAdd;
            theIndexItemAdd = (((this.indexItemAdd!= null)&&(!this.indexItemAdd.isEmpty()))?this.getIndexItemAdd():null);
            strategy.appendField(locator, this, "indexItemAdd", buffer, theIndexItemAdd);
        }
        {
            List<IndexItemDelete> theIndexItemDelete;
            theIndexItemDelete = (((this.indexItemDelete!= null)&&(!this.indexItemDelete.isEmpty()))?this.getIndexItemDelete():null);
            strategy.appendField(locator, this, "indexItemDelete", buffer, theIndexItemDelete);
        }
        {
            List<IndexItemPunchout> theIndexItemPunchout;
            theIndexItemPunchout = (((this.indexItemPunchout!= null)&&(!this.indexItemPunchout.isEmpty()))?this.getIndexItemPunchout():null);
            strategy.appendField(locator, this, "indexItemPunchout", buffer, theIndexItemPunchout);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof IndexItem)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final IndexItem that = ((IndexItem) object);
        {
            List<IndexItemAdd> lhsIndexItemAdd;
            lhsIndexItemAdd = (((this.indexItemAdd!= null)&&(!this.indexItemAdd.isEmpty()))?this.getIndexItemAdd():null);
            List<IndexItemAdd> rhsIndexItemAdd;
            rhsIndexItemAdd = (((that.indexItemAdd!= null)&&(!that.indexItemAdd.isEmpty()))?that.getIndexItemAdd():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "indexItemAdd", lhsIndexItemAdd), LocatorUtils.property(thatLocator, "indexItemAdd", rhsIndexItemAdd), lhsIndexItemAdd, rhsIndexItemAdd)) {
                return false;
            }
        }
        {
            List<IndexItemDelete> lhsIndexItemDelete;
            lhsIndexItemDelete = (((this.indexItemDelete!= null)&&(!this.indexItemDelete.isEmpty()))?this.getIndexItemDelete():null);
            List<IndexItemDelete> rhsIndexItemDelete;
            rhsIndexItemDelete = (((that.indexItemDelete!= null)&&(!that.indexItemDelete.isEmpty()))?that.getIndexItemDelete():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "indexItemDelete", lhsIndexItemDelete), LocatorUtils.property(thatLocator, "indexItemDelete", rhsIndexItemDelete), lhsIndexItemDelete, rhsIndexItemDelete)) {
                return false;
            }
        }
        {
            List<IndexItemPunchout> lhsIndexItemPunchout;
            lhsIndexItemPunchout = (((this.indexItemPunchout!= null)&&(!this.indexItemPunchout.isEmpty()))?this.getIndexItemPunchout():null);
            List<IndexItemPunchout> rhsIndexItemPunchout;
            rhsIndexItemPunchout = (((that.indexItemPunchout!= null)&&(!that.indexItemPunchout.isEmpty()))?that.getIndexItemPunchout():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "indexItemPunchout", lhsIndexItemPunchout), LocatorUtils.property(thatLocator, "indexItemPunchout", rhsIndexItemPunchout), lhsIndexItemPunchout, rhsIndexItemPunchout)) {
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
            List<IndexItemAdd> theIndexItemAdd;
            theIndexItemAdd = (((this.indexItemAdd!= null)&&(!this.indexItemAdd.isEmpty()))?this.getIndexItemAdd():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "indexItemAdd", theIndexItemAdd), currentHashCode, theIndexItemAdd);
        }
        {
            List<IndexItemDelete> theIndexItemDelete;
            theIndexItemDelete = (((this.indexItemDelete!= null)&&(!this.indexItemDelete.isEmpty()))?this.getIndexItemDelete():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "indexItemDelete", theIndexItemDelete), currentHashCode, theIndexItemDelete);
        }
        {
            List<IndexItemPunchout> theIndexItemPunchout;
            theIndexItemPunchout = (((this.indexItemPunchout!= null)&&(!this.indexItemPunchout.isEmpty()))?this.getIndexItemPunchout():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "indexItemPunchout", theIndexItemPunchout), currentHashCode, theIndexItemPunchout);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
