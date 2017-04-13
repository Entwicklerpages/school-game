package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Interface für Objekte die auf Kollisionen reagieren wollen (Trigger).
 *
 * @author nico
 */
public interface PhysicsListener
{
    /**
     * Wird aufgerufen, wenn es zu einer Kollision kam.
     *
     * @param other der Körper mit dem die Kollision statt fand
     * @param otherUserData ein optionales User Data Objekt des Kollisionspartners
     */
    void beginContact(Fixture other, Object otherUserData);

    /**
     * Wird aufgerufen, wenn eine Kollision nicht mehr stattfindet.
     *
     * @param other der Körper mit dem die Kollision vorher statt fand
     * @param otherUserData ein optionales User Data Objekt des Kollisionspartners
     */
    void endContact(Fixture other, Object otherUserData);
}
