package de.entwicklerpages.java.schoolgame.game;

/**
 * Speichert eine CutScene.
 *
 * CutScenes tauchen vor oder nach einem Level auf.
 * Sie enthalten ein Hintergrundbild und einen Dialog.
 *
 * @author nico
 */
public class CutScene {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Der Name des zu verwendeneden Hintergrundbildes
     */
    private String cutSceneImage;

    /**
     * Die ID des Dialogs, der angezeigt werden soll.
     * Muss in der Dialogdatei des Levels existieren.
     */
    private String dialogId;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Standardkonstruktor.
     * Der einzige Weg, den Bildnamen und den Dialog festzulegen.
     *
     * @param cutSceneImage der Bildname
     * @param dialogId die DialogID
     */
    public CutScene(String cutSceneImage, String dialogId)
    {
        this.cutSceneImage = cutSceneImage;
        this.dialogId = dialogId;
    }

    /**
     * Ruft den Bildnamen ab.
     *
     * @return der Bildname
     */
    public String getCutSceneImage() {
        return cutSceneImage;
    }

    /**
     * Ruft die DialogID ab.
     *
     * @return die DialogID
     */
    public String getDialogId() {
        return dialogId;
    }
}
