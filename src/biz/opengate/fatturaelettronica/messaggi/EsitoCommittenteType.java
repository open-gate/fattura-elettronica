//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.27 at 04:50:47 PM CET 
//


package biz.opengate.fatturaelettronica.messaggi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EsitoCommittente_Type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EsitoCommittente_Type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EC01"/>
 *     &lt;enumeration value="EC02"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EsitoCommittente_Type", namespace = "http://www.fatturapa.gov.it/sdi/messaggi/v1.0")
@XmlEnum
public enum EsitoCommittenteType {


    /**
     * 
     * 						EC01 = ACCETTAZIONE
     * 					
     * 
     */
    @XmlEnumValue("EC01")
    EC_01("EC01"),

    /**
     * 
     * 						EC02 = RIFIUTO
     * 					
     * 
     */
    @XmlEnumValue("EC02")
    EC_02("EC02");
    private final String value;

    EsitoCommittenteType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EsitoCommittenteType fromValue(String v) {
        for (EsitoCommittenteType c: EsitoCommittenteType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
