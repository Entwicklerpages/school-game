package de.entwicklerpages.java.schoolgame.common;

/**
 * Hilft bei der Abfrage von Eingaben
 *
 * @author nico
 */
public final class InputHelper
{
    /**
     * Privater Konstruktor.
     *
     * Verhindert, das unsinniger weise ein Objekt dieser Klasse erstellt wird.
     */
    private InputHelper() {}

    /**
     * Prüft ob EINER der übergebenen Key Codes dem gedrücktem entspricht.
     *
     * Logisches ODER.
     *
     * @param keycode der Keycode der gedrückten Tase
     * @param keys die Tasten die abgefragt werden sollen
     * @return true wenn eine der Tasten gedrückt wurde, sonst false
     */
    public static boolean checkKeys(int keycode, int... keys)
    {
        for (int key : keys)
        {
            if (key == keycode) return true;
        }

        return false;
    }
}
