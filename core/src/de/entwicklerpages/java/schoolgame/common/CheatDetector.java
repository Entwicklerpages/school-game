package de.entwicklerpages.java.schoolgame.common;

import com.badlogic.gdx.InputAdapter;

import java.util.Arrays;

/**
 * Erkennt eine festgelegte Testensequenz.
 * Kann als zusätzlicher InputAdapter registiert werden.
 *
 * Bei einer Erkennung wird das Callback aufgerufen.
 *
 * @author nico
 */
public class CheatDetector extends InputAdapter {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Speichert die festgelegte Tastensequenz nach der gesucht werden soll.
     */
    private int[] keySequence;

    /**
     * Speichert die letzten Tasten, die gedrückt wurden.
     */
    private int[] previousKeys;

    /**
     * Das Callback, das aufgerufen werden soll, wenn die Tastensequenz erkannt wurde.
     */
    private ActionCallback callback;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Standardkonstruktor.
     */
    public CheatDetector()
    {
        this.keySequence = null;
        this.previousKeys = null;
    }

    /**
     * Legt die Sequenz fest, nach der gesucht werden soll.
     *
     * @param keySequence Die Tastensequenz
     */
    public void setKeySequence(int[] keySequence) {
        this.keySequence = keySequence;
        this.previousKeys = new int[keySequence.length];
    }

    /**
     * Legt das Callback fest.
     * @param callback Das Callback, dass aufgerufen werden soll.
     */
    public void setCallback(ActionCallback callback) {
        this.callback = callback;
    }

    /**
     * Wird bei jeder Tasteneingabe aufgerufen.
     * Speichert die Taste ab und prüft, ob die Sequenz auftaucht.
     *
     * @param keycode Die Taste, die eingegeben wurde
     * @return true wenn die Sequenz erkannt wurde
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keySequence != null && keySequence.length > 0)
        {
            shiftKeys(keycode);
            if (Arrays.equals(keySequence, previousKeys))
            {
                callback.run();
                return true;
            }
        }
        return false;
    }

    /**
     * Verschiebt alle Elemente des Arrays, das die letzten Tasten speichert, nach vorne.
     *
     * @param keycode die neue Taste
     */
    private void shiftKeys(int keycode)
    {
        /*
        for (int i = 0; i < previousKeys.length - 1; i++)
        {
            previousKeys[i] = previousKeys[i + 1];
        }
        */
        System.arraycopy(previousKeys, 1, previousKeys, 0, previousKeys.length - 1);
        previousKeys[previousKeys.length - 1] = keycode;
    }

}
