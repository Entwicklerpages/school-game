
package de.entwicklerpages.java.schoolgame.game.dialog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "characters",
    "dialogs"
})
@XmlRootElement(name = "level")
public class Level {

    @XmlElement(required = true)
    protected CharactersType characters;
    @XmlElement(required = true)
    protected DialogsType dialogs;
    @XmlAttribute(name = "atlas", required = true)
    protected String atlas;

    /**
     * Ruft den Wert der characters-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CharactersType }
     *     
     */
    public CharactersType getCharacters() {
        return characters;
    }

    /**
     * Legt den Wert der characters-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CharactersType }
     *     
     */
    public void setCharacters(CharactersType value) {
        this.characters = value;
    }

    /**
     * Ruft den Wert der dialogs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DialogsType }
     *     
     */
    public DialogsType getDialogs() {
        return dialogs;
    }

    /**
     * Legt den Wert der dialogs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DialogsType }
     *     
     */
    public void setDialogs(DialogsType value) {
        this.dialogs = value;
    }

    /**
     * Ruft den Wert der atlas-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAtlas() {
        return atlas;
    }

    /**
     * Legt den Wert der atlas-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAtlas(String value) {
        this.atlas = value;
    }

}
