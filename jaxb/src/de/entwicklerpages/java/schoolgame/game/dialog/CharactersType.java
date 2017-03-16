
package de.entwicklerpages.java.schoolgame.game.dialog;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für charactersType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "charactersType", propOrder = {
    "character"
})
public class CharactersType {

    @XmlElement(required = true)
    protected List<CharacterType> character;

    public List<CharacterType> getCharacter() {
        if (character == null) {
            character = new ArrayList<CharacterType>();
        }
        return this.character;
    }

}
