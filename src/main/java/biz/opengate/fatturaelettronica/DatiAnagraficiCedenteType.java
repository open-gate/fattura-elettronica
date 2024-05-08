//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2024.04.29 at 04:16:59 PM CEST 
//


package biz.opengate.fatturaelettronica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for DatiAnagraficiCedenteType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DatiAnagraficiCedenteType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdFiscaleIVA" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}IdFiscaleType"/>
 *         &lt;element name="CodiceFiscale" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}CodiceFiscaleType" minOccurs="0"/>
 *         &lt;element name="Anagrafica" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}AnagraficaType"/>
 *         &lt;element name="AlboProfessionale" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String60LatinType" minOccurs="0"/>
 *         &lt;element name="ProvinciaAlbo" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}ProvinciaType" minOccurs="0"/>
 *         &lt;element name="NumeroIscrizioneAlbo" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String60Type" minOccurs="0"/>
 *         &lt;element name="DataIscrizioneAlbo" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="RegimeFiscale" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}RegimeFiscaleType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiAnagraficiCedenteType", propOrder = {
    "idFiscaleIVA",
    "codiceFiscale",
    "anagrafica",
    "alboProfessionale",
    "provinciaAlbo",
    "numeroIscrizioneAlbo",
    "dataIscrizioneAlbo",
    "regimeFiscale"
})
public class DatiAnagraficiCedenteType {

    @XmlElement(name = "IdFiscaleIVA", required = true)
    protected IdFiscaleType idFiscaleIVA;
    @XmlElement(name = "CodiceFiscale")
    protected String codiceFiscale;
    @XmlElement(name = "Anagrafica", required = true)
    protected AnagraficaType anagrafica;
    @XmlElement(name = "AlboProfessionale")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String alboProfessionale;
    @XmlElement(name = "ProvinciaAlbo")
    protected String provinciaAlbo;
    @XmlElement(name = "NumeroIscrizioneAlbo")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String numeroIscrizioneAlbo;
    @XmlElement(name = "DataIscrizioneAlbo")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataIscrizioneAlbo;
    @XmlElement(name = "RegimeFiscale", required = true)
    @XmlSchemaType(name = "string")
    protected RegimeFiscaleType regimeFiscale;

    /**
     * Gets the value of the idFiscaleIVA property.
     * 
     * @return
     *     possible object is
     *     {@link IdFiscaleType }
     *     
     */
    public IdFiscaleType getIdFiscaleIVA() {
        return idFiscaleIVA;
    }

    /**
     * Sets the value of the idFiscaleIVA property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdFiscaleType }
     *     
     */
    public void setIdFiscaleIVA(IdFiscaleType value) {
        this.idFiscaleIVA = value;
    }

    /**
     * Gets the value of the codiceFiscale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Sets the value of the codiceFiscale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscale(String value) {
        this.codiceFiscale = value;
    }

    /**
     * Gets the value of the anagrafica property.
     * 
     * @return
     *     possible object is
     *     {@link AnagraficaType }
     *     
     */
    public AnagraficaType getAnagrafica() {
        return anagrafica;
    }

    /**
     * Sets the value of the anagrafica property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnagraficaType }
     *     
     */
    public void setAnagrafica(AnagraficaType value) {
        this.anagrafica = value;
    }

    /**
     * Gets the value of the alboProfessionale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlboProfessionale() {
        return alboProfessionale;
    }

    /**
     * Sets the value of the alboProfessionale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlboProfessionale(String value) {
        this.alboProfessionale = value;
    }

    /**
     * Gets the value of the provinciaAlbo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinciaAlbo() {
        return provinciaAlbo;
    }

    /**
     * Sets the value of the provinciaAlbo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinciaAlbo(String value) {
        this.provinciaAlbo = value;
    }

    /**
     * Gets the value of the numeroIscrizioneAlbo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroIscrizioneAlbo() {
        return numeroIscrizioneAlbo;
    }

    /**
     * Sets the value of the numeroIscrizioneAlbo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroIscrizioneAlbo(String value) {
        this.numeroIscrizioneAlbo = value;
    }

    /**
     * Gets the value of the dataIscrizioneAlbo property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataIscrizioneAlbo() {
        return dataIscrizioneAlbo;
    }

    /**
     * Sets the value of the dataIscrizioneAlbo property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataIscrizioneAlbo(XMLGregorianCalendar value) {
        this.dataIscrizioneAlbo = value;
    }

    /**
     * Gets the value of the regimeFiscale property.
     * 
     * @return
     *     possible object is
     *     {@link RegimeFiscaleType }
     *     
     */
    public RegimeFiscaleType getRegimeFiscale() {
        return regimeFiscale;
    }

    /**
     * Sets the value of the regimeFiscale property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegimeFiscaleType }
     *     
     */
    public void setRegimeFiscale(RegimeFiscaleType value) {
        this.regimeFiscale = value;
    }

}
