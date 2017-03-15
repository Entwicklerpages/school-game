
package de.entwicklerpages.java.schoolgame.game.dialog;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für statementType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "statementType", propOrder = {
    "player",
    "display",
    "sound",
    "texts"
})
public class StatementType {

    protected PlayerType player;
    protected List<DisplayType> display;
    protected String sound;
    @XmlElement(required = true)
    protected TextsType texts;
    @XmlAttribute(name = "talking", required = true)
    protected String talking;

    /**
     * Ruft den Wert der player-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PlayerType }
     *     
     */
    public PlayerType getPlayer() {
        return player;
    }

    /**
     * Legt den Wert der player-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PlayerType }
     *     
     */
    public void setPlayer(PlayerType value) {
        this.player = value;
    }

    public List<DisplayType> getDisplay() {
        if (display == null) {
            display = new ArrayList<DisplayType>();
        }
        return this.display;
    }

    /**
     * Ruft den Wert der sound-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSound() {
        return sound;
    }

    /**
     * Legt den Wert der sound-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSound(String value) {
        this.sound = value;
    }

    /**
     * Ruft den Wert der texts-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TextsType }
     *     
     */
    public TextsType getTexts() {
        return texts;
    }

    /**
     * Legt den Wert der texts-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TextsType }
     *     
     */
    public void setTexts(TextsType value) {
        this.texts = value;
    }

    /**
     * Ruft den Wert der talking-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTalking() {
        return talking;
    }

    /**
     * Legt den Wert der talking-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTalking(String value) {
        this.talking = value;
    }

}
