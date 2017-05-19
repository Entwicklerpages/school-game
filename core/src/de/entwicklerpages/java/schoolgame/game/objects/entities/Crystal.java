package de.entwicklerpages.java.schoolgame.game.objects.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Vector2;

import de.entwicklerpages.java.schoolgame.game.ExtendedMapDisplayObject;
import de.entwicklerpages.java.schoolgame.game.objects.WorldObject;

/**
 * Ein schwebender Kristall.
 */
public class Crystal extends WorldObject implements ExtendedMapDisplayObject
{
    private TextureRegion crystal;
    private TextureAtlas crystalAtlas;
    private Vector2 position;

    public Crystal(String objectId)
    {
        super(objectId);
    }

    @Override
    public void onInit()
    {
        crystalAtlas = new TextureAtlas(Gdx.files.internal("data/graphics/packed/crystal.atlas"));
        crystal = crystalAtlas.findRegion("crystal");

        if (rawObject instanceof EllipseMapObject)
        {
            EllipseMapObject ellipseMapObject = (EllipseMapObject) rawObject;
            position = new Vector2(ellipseMapObject.getEllipse().x + ellipseMapObject.getEllipse().width / 2f, ellipseMapObject.getEllipse().y + ellipseMapObject.getEllipse().height / 2f);
        } else {
            Gdx.app.log("WARNING", "Crystal " + objectId + " must be an EllipseMapObject.");
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
        batch.draw(crystal,                                     // TextureRegion (front, back, side)
                position.x - crystal.getRegionWidth() / 2,      // Offset to the X position (character center)
                position.y + 20,                                // Y position is at the foots
                crystal.getRegionWidth() / 2,                   // Origin X (important for flipping)
                crystal.getRegionHeight(),                      // Origin Y
                crystal.getRegionWidth(),                       // Width
                crystal.getRegionHeight(),                      // Height
                1f,                                             // Scale X (-1 to flip)
                1f,                                             // Scale Y
                0f);
    }

    @Override
    public void onDispose()
    {
        crystalAtlas.dispose();
    }
}
