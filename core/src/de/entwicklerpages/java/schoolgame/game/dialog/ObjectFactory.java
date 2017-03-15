
package de.entwicklerpages.java.schoolgame.game.dialog;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.entwicklerpages.java.schoolgame.game.dialog package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.entwicklerpages.java.schoolgame.game.dialog
     * 
     */
    public ObjectFactory() {
    }


    public Level createLevel() {
        return new Level();
    }


    public CharactersType createCharactersType() {
        return new CharactersType();
    }


    public DialogsType createDialogsType() {
        return new DialogsType();
    }


    public DialogType createDialogType() {
        return new DialogType();
    }


    public DisplayType createDisplayType() {
        return new DisplayType();
    }


    public CharacterType createCharacterType() {
        return new CharacterType();
    }


    public TextsType createTextsType() {
        return new TextsType();
    }


    public PlayerType createPlayerType() {
        return new PlayerType();
    }


    public StatementType createStatementType() {
        return new StatementType();
    }

}
