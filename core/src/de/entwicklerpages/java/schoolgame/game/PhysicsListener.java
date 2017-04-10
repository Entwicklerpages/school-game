package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * @author nico
 */
public interface PhysicsListener
{
    void beginContact(Fixture other, Object otherUserData);
    void endContact(Fixture other, Object otherUserData);
}
