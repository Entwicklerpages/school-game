package de.entwicklerpages.java.schoolgame.game.objects;

/**
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
