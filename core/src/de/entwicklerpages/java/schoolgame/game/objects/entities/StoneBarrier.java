package de.entwicklerpages.java.schoolgame.game.objects.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import de.entwicklerpages.java.schoolgame.game.Physics;

/**
 * Blockade, um Wege zu versperren.
 *
 * @author nico
 */
public class StoneBarrier extends BaseEntity
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    protected Body body;

    private TextureAtlas stoneAtlas;
    private TextureRegion bigStone = null;

    private Vector2 position;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public StoneBarrier(String objectId)
    {
        super(objectId);
    }

    /**
     * Ruft die Y Position als "Tiefe" ab.
     *
     * @see de.entwicklerpages.java.schoolgame.game.ExtendedMapDisplayObject
     * @return die Y Position
     */
    @Override
    public float getPosY()
    {
        if (body != null)
            return position.y;
        else
            return super.getPosY();
    }

    /**
     * Inititalisiert den NPC.
     * Lädt alle Grafiken und Animationen.
     */
    @Override
    public void onInit()
    {
        super.onInit();

        stoneAtlas = new TextureAtlas(Gdx.files.internal("data/graphics/packed/stone.atlas"));
        bigStone = stoneAtlas.findRegion("stone_big");

        if (rawObject instanceof RectangleMapObject)
        {
            RectangleMapObject rectObject = (RectangleMapObject) rawObject;
            Rectangle rect = rectObject.getRectangle();

            position = new Vector2(rect.getX() + rect.getWidth() / 2f, rect.getY());

            body = createEntityBody(new Vector2(rect.getX(), rect.getY()), Physics.createRectangle(rect.getWidth(), rect.getHeight(), new Vector2(rect.getWidth() / 2f, rect.getHeight() / 2f)), BodyDef.BodyType.KinematicBody);

        } else {
            Gdx.app.log("WARNING", "Stone Barrier " + objectId + " must have an RectangleMapObject!");
            worldObjectManager.removeObject(this);
        }
    }

    /**
     * Macht nichts.
     *
     * @param deltaTime die Zeit, die seit dem letzten Frame vergangen ist
     */
    @Override
    public void onUpdate(float deltaTime)
    {
    }

    @Override
    public void render(Batch batch, float deltaTime)
    {
        if (body == null) return;

        batch.draw(bigStone,                                    // TextureRegion
                position.x - bigStone.getRegionWidth() / 2,     // Offset to the X position (character center)
                position.y,                                     // Y position is at the foots
                bigStone.getRegionWidth() / 2,                  // Origin X (important for flipping)
                bigStone.getRegionHeight(),                     // Origin Y
                bigStone.getRegionWidth(),                      // Width
                bigStone.getRegionHeight(),                     // Height
                1f,                                             // Scale X (-1 to flip)
                1f,                                             // Scale Y
                0f);                                            // Rotation
    }

    public void destroy()
    {
        worldObjectManager.removeObject(this);
    }

    /**
     * Aufräumarbeiten.
     */
    @Override
    public void onDispose()
    {
        super.onDispose();
        worldObjectManager.getPhysicalWorld().destroyBody(body);
        stoneAtlas.dispose();
    }
}
