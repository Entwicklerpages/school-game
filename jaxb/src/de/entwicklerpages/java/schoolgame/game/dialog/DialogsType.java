
package de.entwicklerpages.java.schoolgame.game.dialog;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für dialogsType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dialogsType", propOrder = {
    "dialog"
})
public class DialogsType {

    protected List<DialogType> dialog;

    public List<DialogType> getDialog() {
        if (dialog == null) {
            dialog = new ArrayList<DialogType>();
        }
        return this.dialog;
    }

}
