package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Interface für Objekte, die im ExtendedOrthogonalTiledMapRenderer angezeigt werden sollen.
 *
 * @see ExtendedOrthogonalTiledMapRenderer
 *
 * @author nico
 */
public interface ExtendedMapDisplayObject
{
    /**
     * Die Y Position des Objektes.
     * Diese wird virtuell als Z gehandhabt und steuert damit die Tiefe des Objektes.
     * Auf diese Weise werden die Objekte nach Tiefe sortiert und zur richtigen Zeit gerendert.
     *
     * @return die Y Position
     */
    float getPosY();

    /**
     * Wird aufgerufen, wenn sich das Objekt selbst rendern soll.
     *
     * @param batch der Batch, in den gerendert werden soll
     * @param deltaTime die Zeit, die seit dem letztem Frame vergangen ist
     */
    void render(Batch batch, float deltaTime);
}
