package de.entwicklerpages.java.schoolgame.game.objects.entities.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
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
 * Kleine Krabbe.
 */
public class Crab extends BaseEntity
{
    private int health = 4;
    private ActionCallback deathCallback = null;
    private AttackHandler handler;

    protected Body body;

    private TextureAtlas crabAtlas;
    private Animation<TextureRegion> crabAnimation = null;

    private float hitTimer = 0f;
    private float attackTimer = 1.3f;
    private float animationTime = 0f;

    private Vector2 direction;
    private Vector2 bodyPosition;
    private Vector2 oldPosition = Vector2.Zero;

    /**
     * Konstruktor.
     *
     * @param objectId die ID des MapObjects
     */
    public Crab(String objectId)
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

        direction = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f)).scl(4f);

        crabAtlas = new TextureAtlas(Gdx.files.internal("data/graphics/packed/krab.atlas"));
        crabAnimation = new Animation<TextureRegion>(1/7f, crabAtlas.findRegions("krab"), Animation.PlayMode.LOOP);

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

                        worldObjectManager.removeObject(Crab.this);
                    } else {
                        hitTimer += 0.6f;
                    }

                    return true;
                }
            });

            handler.createAttackFixture(body, Physics.createRectangle(64, 64, new Vector2(0, 0)));

        } else {
            Gdx.app.log("WARNING", "Crab " + objectId + " must have an EllipseMapObject!");
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

        if (attackTimer > 0f)
            attackTimer -= deltaTime;

        if (handler.canAttack())
        {
            if (attackTimer <= 0f)
            {
                attackTimer = 1.6f;

                handler.attack(3);
            }
        }

        bodyPosition = body.getPosition();

        if (body.getLinearVelocity().sub(direction).len2() > 1 || (MathUtils.isEqual(oldPosition.x, bodyPosition.x) && MathUtils.isEqual(oldPosition.y, bodyPosition.y)))
        {
            direction.set(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
            direction.scl(4f);
        }

        oldPosition = bodyPosition;

        body.applyForceToCenter(direction, true);
    }

    @Override
    public void render(Batch batch, float deltaTime)
    {
        if (body == null) return;

        animationTime += deltaTime;

        Color oldColor = batch.getColor();

        batch.setColor(1f, (health + 4)/8f, (health + 4)/8f, 1f);

        Vector2 pos = body.getPosition();
        pos.scl(Physics.PPM);

        TextureRegion region = crabAnimation.getKeyFrame(animationTime);

        batch.draw(region,                                    // TextureRegion (front, back, side)
                pos.x - region.getRegionWidth() / 2,           // Offset to the X position (character center)
                pos.y,                                        // Y position is at the foots
                region.getRegionWidth() / 2,                   // Origin X (important for flipping)
                region.getRegionHeight(),                      // Origin Y
                region.getRegionWidth(),                       // Width
                region.getRegionHeight(),                      // Height
                1f,                                           // Scale X (-1 to flip)
                1f,                                           // Scale Y
                0f);                                          // Rotation

        batch.setColor(oldColor);
    }

    @Override
    public void onDispose()
    {
        crabAtlas.dispose();
        handler.dispose();

        worldObjectManager.getPhysicalWorld().destroyBody(body);
    }
}
