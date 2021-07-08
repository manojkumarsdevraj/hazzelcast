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
 *         &lt;element ref="{}Subscription"/>
 *         &lt;element ref="{}SubscriptionContent" maxOccurs="unbounded"/>
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
    "subscription",
    "subscriptionContent"
})
@XmlRootElement(name = "SubscriptionContentResponse")
public class SubscriptionContentResponse
    implements Serializable, Equals, HashCode, ToString
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 7441359833436674723L;
	@XmlElement(name = "Subscription", required = true)
    protected Subscription subscription;
    @XmlElement(name = "SubscriptionContent", required = true)
    protected List<SubscriptionContent> subscriptionContent;

    /**
     * Gets the value of the subscription property.
     * 
     * @return
     *     possible object is
     *     {@link Subscription }
     *     
     */
    public Subscription getSubscription() {
        return subscription;
    }

    /**
     * Sets the value of the subscription property.
     * 
     * @param value
     *     allowed object is
     *     {@link Subscription }
     *     
     */
    public void setSubscription(Subscription value) {
        this.subscription = value;
    }

    /**
     * Gets the value of the subscriptionContent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subscriptionContent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubscriptionContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubscriptionContent }
     * 
     * 
     */
    public List<SubscriptionContent> getSubscriptionContent() {
        if (subscriptionContent == null) {
            subscriptionContent = new ArrayList<SubscriptionContent>();
        }
        return this.subscriptionContent;
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
            Subscription theSubscription;
            theSubscription = this.getSubscription();
            strategy.appendField(locator, this, "subscription", buffer, theSubscription);
        }
        {
            List<SubscriptionContent> theSubscriptionContent;
            theSubscriptionContent = (((this.subscriptionContent!= null)&&(!this.subscriptionContent.isEmpty()))?this.getSubscriptionContent():null);
            strategy.appendField(locator, this, "subscriptionContent", buffer, theSubscriptionContent);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof SubscriptionContentResponse)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final SubscriptionContentResponse that = ((SubscriptionContentResponse) object);
        {
            Subscription lhsSubscription;
            lhsSubscription = this.getSubscription();
            Subscription rhsSubscription;
            rhsSubscription = that.getSubscription();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "subscription", lhsSubscription), LocatorUtils.property(thatLocator, "subscription", rhsSubscription), lhsSubscription, rhsSubscription)) {
                return false;
            }
        }
        {
            List<SubscriptionContent> lhsSubscriptionContent;
            lhsSubscriptionContent = (((this.subscriptionContent!= null)&&(!this.subscriptionContent.isEmpty()))?this.getSubscriptionContent():null);
            List<SubscriptionContent> rhsSubscriptionContent;
            rhsSubscriptionContent = (((that.subscriptionContent!= null)&&(!that.subscriptionContent.isEmpty()))?that.getSubscriptionContent():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "subscriptionContent", lhsSubscriptionContent), LocatorUtils.property(thatLocator, "subscriptionContent", rhsSubscriptionContent), lhsSubscriptionContent, rhsSubscriptionContent)) {
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
            Subscription theSubscription;
            theSubscription = this.getSubscription();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "subscription", theSubscription), currentHashCode, theSubscription);
        }
        {
            List<SubscriptionContent> theSubscriptionContent;
            theSubscriptionContent = (((this.subscriptionContent!= null)&&(!this.subscriptionContent.isEmpty()))?this.getSubscriptionContent():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "subscriptionContent", theSubscriptionContent), currentHashCode, theSubscriptionContent);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
