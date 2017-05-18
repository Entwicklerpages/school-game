package de.entwicklerpages.java.schoolgame.game.objects.entities.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Vector2;

import de.entwicklerpages.java.schoolgame.game.ExtendedMapDisplayObject;
import de.entwicklerpages.java.schoolgame.game.objects.WorldObject;

/**
 * Wolf für das Waldende.
 *
 * Friedlich. Steht nur rum.
 */
public class EndingWolf extends WorldObject implements ExtendedMapDisplayObject
{
    private TextureRegion wolf;
    private TextureAtlas wolfAtlas;
    private Vector2 position;

    public EndingWolf(String objectId)
    {
        super(objectId);
    }

    @Override
    public void onInit()
    {
        wolfAtlas = new TextureAtlas(Gdx.files.internal("data/graphics/packed/wolf.atlas"));
        wolf = wolfAtlas.findRegion("wolf_side", 4);

        if (rawObject instanceof EllipseMapObject)
        {
            EllipseMapObject ellipseMapObject = (EllipseMapObject) rawObject;
            position = new Vector2(ellipseMapObject.getEllipse().x + ellipseMapObject.getEllipse().width / 2f, ellipseMapObject.getEllipse().y + ellipseMapObject.getEllipse().height / 2f);
        } else {
            Gdx.app.log("WARNING", "EndingWolf " + objectId + " must be an EllipseMapObject.");
            worldObjectManager.removeObject(this);
        }
    }

    @Override
    public float getPosY()
    {
        return position.y;
    }

    @Override
    public void render(Batch batch, float deltaTime)
    {
        batch.draw(wolf,                                     // TextureRegion (front, back, side)
                position.x - wolf.getRegionWidth() / 2,      // Offset to the X position (character center)
                position.y,                                  // Y position is at the foots
                wolf.getRegionWidth() / 2,                   // Origin X (important for flipping)
                wolf.getRegionHeight(),                      // Origin Y
                wolf.getRegionWidth(),                       // Width
                wolf.getRegionHeight(),                      // Height
                2f,                                          // Scale X (-1 to flip)
                2f,                                          // Scale Y
                0f);
    }

    @Override
    public void onDispose()
    {
        wolfAtlas.dispose();
    }
}
