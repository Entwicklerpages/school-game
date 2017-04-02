package de.entwicklerpages.java.schoolgame.common;

/**
 * Hilft bei der Abfrage von Eingaben
 *
 * @author nico
 */
public final class InputHelper
{
    private InputHelper() {}

    public static boolean checkKeys(int keycode, int... keys)
    {
        for (int key : keys)
        {
            if (key == keycode) return true;
        }

        return false;
    }
}
