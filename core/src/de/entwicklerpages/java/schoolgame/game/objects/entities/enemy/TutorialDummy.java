package de.entwicklerpages.java.schoolgame.game.objects.entities.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.Physics;
import de.entwicklerpages.java.schoolgame.game.Player;
import de.entwicklerpages.java.schoolgame.game.objects.AttackHandler;
import de.entwicklerpages.java.schoolgame.game.objects.entities.BaseEntity;

/**
 * Ein Dummy für das Tutorial. Kann angegriffen werden, greift aber selbst nicht an.
 *
 * @author nico
 */
public class TutorialDummy extends BaseEntity
{
    private int health = 5;
    private ActionCallback deathCallback = null;
    private AttackHandler handler;

    protected Body body;

    private TextureAtlas dummyAtlas;
    private TextureRegion dummy = null;
    private TextureRegion dummy_hit = null;

    private float hitTimer = 0f;

    /**
     * Konstruktor.
     *
     * @param objectId die ID des MapObjects
     */
    public TutorialDummy(String objectId)
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
            return body.getPosition().y * Physics.PPM;
        else
            return super.getPosY();
    }

    /**
     * Initialisierung
     */
    @Override
    public void onInit()
    {
        super.onInit();

        dummyAtlas = new TextureAtlas(Gdx.files.internal("data/graphics/packed/chicken.atlas"));
        dummy = dummyAtlas.findRegion("chicken");
        dummy_hit = dummyAtlas.findRegion("chicken_hit");

        if (rawObject instanceof EllipseMapObject)
        {
            EllipseMapObject ellipseObject = (EllipseMapObject) rawObject;
            Ellipse ellipse = ellipseObject.getEllipse();

            Vector2 startPos = new Vector2(ellipse.x + ellipse.width / 2f, ellipse.y + ellipse.height / 2f);

            body = createCircleBody(startPos, 10);

            handler = new AttackHandler(worldObjectManager);
            handler.setAttackedCallback(new AttackHandler.AttackedCallback()
            {
                @Override
                public boolean run(Player player, int damage)
                {
                    health -= damage;

                    if (health <= 0)
                    {
                        if (deathCallback != null)
                            deathCallback.run();

                        worldObjectManager.removeObject(TutorialDummy.this);
                    } else {
                        hitTimer += 0.6f;
                    }

                    return true;
                }
            });

            handler.createAttackFixture(body, Physics.createRectangle(64, 64, new Vector2(0, 0)));

        } else {
            Gdx.app.log("WARNING", "Tutorial Dummy " + objectId + " must have an EllipseMapObject!");
            worldObjectManager.removeObject(this);
        }
    }

    public void setDeathCallback(ActionCallback deathCallback)
    {
        this.deathCallback = deathCallback;
    }

    @Override
    public void onUpdate(float deltaTime)
    {
        if (!MathUtils.isZero(hitTimer))
        {
            hitTimer -= deltaTime;

            if (hitTimer <= 0f)
                hitTimer = 0f;
        }

        body.setLinearVelocity(Vector2.Zero);
    }

    @Override
    public void render(Batch batch, float deltaTime)
    {
        if (body == null) return;

        Color oldColor = batch.getColor();

        batch.setColor(1f, (health + 5)/10f, (health + 5)/10f, 1f);

        Vector2 pos = body.getPosition();
        pos.scl(Physics.PPM);

        TextureRegion region = MathUtils.isZero(hitTimer) ? dummy : dummy_hit;

        batch.draw(region,                                    // TextureRegion (front, back, side)
                pos.x - dummy.getRegionWidth() / 2,           // Offset to the X position (character center)
                pos.y,                                        // Y position is at the foots
                dummy.getRegionWidth() / 2,                   // Origin X (important for flipping)
                dummy.getRegionHeight(),                      // Origin Y
                dummy.getRegionWidth(),                       // Width
                dummy.getRegionHeight(),                      // Height
                1f,                                           // Scale X (-1 to flip)
                1f,                                           // Scale Y
                0f);                                          // Rotation

        batch.setColor(oldColor);
    }

    @Override
    public void onDispose()
    {
        dummyAtlas.dispose();
        handler.dispose();

        worldObjectManager.getPhysicalWorld().destroyBody(body);
    }
}
