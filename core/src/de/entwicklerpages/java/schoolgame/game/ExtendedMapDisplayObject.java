package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * @author nico
 */
public interface ExtendedMapDisplayObject
{
    float getPosY();
    void render(Batch batch, float deltaTime);
}
