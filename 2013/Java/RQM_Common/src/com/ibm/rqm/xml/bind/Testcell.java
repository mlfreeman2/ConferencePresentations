//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.04.28 at 12:49:50 PM EDT
//

package com.ibm.rqm.xml.bind;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://jazz.net/xmlns/alm/qm/v0.1/}reportableArtifact">
 *       &lt;sequence>
 *         &lt;element ref="{http://purl.org/dc/elements/1.1/}title"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element ref="{http://jazz.net/xmlns/alm/v0.1/}owner"/>
 *         &lt;element ref="{http://purl.org/dc/elements/1.1/}description"/>
 *         &lt;element name="longdescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="configuration" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="href" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *                 &lt;attribute ref="{http://schema.ibm.com/vega/2008/}id"/>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="topology" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="labresource">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="href" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *                           &lt;attribute ref="{http://schema.ibm.com/vega/2008/}id"/>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element ref="{http://jazz.net/xmlns/alm/qm/v0.1/}com.ibm.rational.test.lm.config.lrdid"/>
 *                   &lt;element name="note" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"title", "type", "owner", "description", "longdescription", "configuration", "topology"})
@XmlRootElement(name = "testcell")
public class Testcell extends ReportableArtifact
{
    
    @XmlElement(namespace = "http://purl.org/dc/elements/1.1/", required = true)
    protected String                  title;
    
    protected Integer                 type;
    
    @XmlElement(namespace = "http://jazz.net/xmlns/alm/v0.1/", required = true)
    protected Owner                   owner;
    
    @XmlElement(namespace = "http://purl.org/dc/elements/1.1/", required = true)
    protected String                  description;
    
    @XmlElement(required = true)
    protected String                  longdescription;
    
    protected Testcell.Configuration  configuration;
    
    protected List<Testcell.Topology> topology;
    
    /**
     * The name of the Test Cell.
     * 
     * @return possible object is {@link String }
     */
    public String getTitle()
    {
        return title;
    }
    
    /**
     * Sets the value of the title property.
     * 
     * @param value
     *            allowed object is {@link String }
     */
    public void setTitle(String value)
    {
        this.title = value;
    }
    
    /**
     * Gets the value of the type property.
     * 
     * @return possible object is {@link Integer }
     */
    public Integer getType()
    {
        return type;
    }
    
    /**
     * Sets the value of the type property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     */
    public void setType(Integer value)
    {
        this.type = value;
    }
    
    /**
     * The contributor that owns the test cell.
     * 
     * @return possible object is {@link Owner }
     */
    public Owner getOwner()
    {
        return owner;
    }
    
    /**
     * Sets the value of the owner property.
     * 
     * @param value
     *            allowed object is {@link Owner }
     */
    public void setOwner(Owner value)
    {
        this.owner = value;
    }
    
    /**
     * A summary description of the Test Cell.
     * 
     * @return possible object is {@link String }
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Sets the value of the description property.
     * 
     * @param value
     *            allowed object is {@link String }
     */
    public void setDescription(String value)
    {
        this.description = value;
    }
    
    /**
     * Gets the value of the longdescription property.
     * 
     * @return possible object is {@link String }
     */
    public String getLongdescription()
    {
        return longdescription;
    }
    
    /**
     * Sets the value of the longdescription property.
     * 
     * @param value
     *            allowed object is {@link String }
     */
    public void setLongdescription(String value)
    {
        this.longdescription = value;
    }
    
    /**
     * Gets the value of the configuration property.
     * 
     * @return possible object is {@link Testcell.Configuration }
     */
    public Testcell.Configuration getConfiguration()
    {
        return configuration;
    }
    
    /**
     * Sets the value of the configuration property.
     * 
     * @param value
     *            allowed object is {@link Testcell.Configuration }
     */
    public void setConfiguration(Testcell.Configuration value)
    {
        this.configuration = value;
    }
    
    /**
     * Gets the value of the topology property.
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the topology property.
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getTopology().add(newItem);
     * </pre>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Testcell.Topology }
     */
    public List<Testcell.Topology> getTopology()
    {
        if (topology == null)
        {
            topology = new ArrayList<Testcell.Topology>();
        }
        return this.topology;
    }
    
    /**
     * <p>
     * Java class for anonymous complex type.
     * <p>
     * The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="href" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *       &lt;attribute ref="{http://schema.ibm.com/vega/2008/}id"/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Configuration
    {
        
        @XmlAttribute(required = true)
        @XmlSchemaType(name = "anyURI")
        protected String href;
        
        @XmlAttribute(namespace = "http://schema.ibm.com/vega/2008/")
        protected String id;
        
        /**
         * Gets the value of the href property.
         * 
         * @return possible object is {@link String }
         */
        public String getHref()
        {
            return href;
        }
        
        /**
         * Sets the value of the href property.
         * 
         * @param value
         *            allowed object is {@link String }
         */
        public void setHref(String value)
        {
            this.href = value;
        }
        
        /**
         * [READ-ONLY] UUID of the configuration.
         * 
         * @return possible object is {@link String }
         */
        public String getId()
        {
            return id;
        }
        
        /**
         * Sets the value of the id property.
         * 
         * @param value
         *            allowed object is {@link String }
         */
        public void setId(String value)
        {
            this.id = value;
        }
        
    }
    
    /**
     * <p>
     * Java class for anonymous complex type.
     * <p>
     * The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="labresource">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="href" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *                 &lt;attribute ref="{http://schema.ibm.com/vega/2008/}id"/>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element ref="{http://jazz.net/xmlns/alm/qm/v0.1/}com.ibm.rational.test.lm.config.lrdid"/>
     *         &lt;element name="note" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"labresource", "comIbmRationalTestLmConfigLrdid", "note"})
    public static class Topology
    {
        
        @XmlElement(required = true)
        protected Testcell.Topology.Labresource   labresource;
        
        @XmlElement(name = "com.ibm.rational.test.lm.config.lrdid", required = true)
        protected ComIbmRationalTestLmConfigLrdid comIbmRationalTestLmConfigLrdid;
        
        protected String                          note;
        
        /**
         * Gets the value of the labresource property.
         * 
         * @return possible object is {@link Testcell.Topology.Labresource }
         */
        public Testcell.Topology.Labresource getLabresource()
        {
            return labresource;
        }
        
        /**
         * Sets the value of the labresource property.
         * 
         * @param value
         *            allowed object is {@link Testcell.Topology.Labresource }
         */
        public void setLabresource(Testcell.Topology.Labresource value)
        {
            this.labresource = value;
        }
        
        /**
         * This is the identifier of the lab resource description from the test environment that the Test Cell is associated with. Typically this means that the associated lab resource matches or satisfies the lab resource description.
         * 
         * @return possible object is {@link ComIbmRationalTestLmConfigLrdid }
         */
        public ComIbmRationalTestLmConfigLrdid getComIbmRationalTestLmConfigLrdid()
        {
            return comIbmRationalTestLmConfigLrdid;
        }
        
        /**
         * Sets the value of the comIbmRationalTestLmConfigLrdid property.
         * 
         * @param value
         *            allowed object is {@link ComIbmRationalTestLmConfigLrdid }
         */
        public void setComIbmRationalTestLmConfigLrdid(ComIbmRationalTestLmConfigLrdid value)
        {
            this.comIbmRationalTestLmConfigLrdid = value;
        }
        
        /**
         * Gets the value of the note property.
         * 
         * @return possible object is {@link String }
         */
        public String getNote()
        {
            return note;
        }
        
        /**
         * Sets the value of the note property.
         * 
         * @param value
         *            allowed object is {@link String }
         */
        public void setNote(String value)
        {
            this.note = value;
        }
        
        /**
         * <p>
         * Java class for anonymous complex type.
         * <p>
         * The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="href" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
         *       &lt;attribute ref="{http://schema.ibm.com/vega/2008/}id"/>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Labresource
        {
            
            @XmlAttribute(required = true)
            @XmlSchemaType(name = "anyURI")
            protected String href;
            
            @XmlAttribute(namespace = "http://schema.ibm.com/vega/2008/")
            protected String id;
            
            /**
             * Gets the value of the href property.
             * 
             * @return possible object is {@link String }
             */
            public String getHref()
            {
                return href;
            }
            
            /**
             * Sets the value of the href property.
             * 
             * @param value
             *            allowed object is {@link String }
             */
            public void setHref(String value)
            {
                this.href = value;
            }
            
            /**
             * [READ-ONLY] UUID of the labresource.
             * 
             * @return possible object is {@link String }
             */
            public String getId()
            {
                return id;
            }
            
            /**
             * Sets the value of the id property.
             * 
             * @param value
             *            allowed object is {@link String }
             */
            public void setId(String value)
            {
                this.id = value;
            }
            
        }
        
    }
    
}
