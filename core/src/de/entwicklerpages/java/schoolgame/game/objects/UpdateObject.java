package de.entwicklerpages.java.schoolgame.game.objects;

/**
 * Interface für Klassen, dessen Objekte bei jedem Frame geupdated werden wollen.
 *
 * @author nico
 */
public interface UpdateObject
{
    /**
     * Wird einmal pro Frame aufgerufen.
     *
     * @param deltaTime die Zeit, die seit dem letzten Frame vergangen ist
     */
    void onUpdate(float deltaTime);
}
