package de.entwicklerpages.java.schoolgame.game.objects.entities.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.EntityOrientation;
import de.entwicklerpages.java.schoolgame.game.Physics;
import de.entwicklerpages.java.schoolgame.game.Player;
import de.entwicklerpages.java.schoolgame.game.objects.AttackHandler;
import de.entwicklerpages.java.schoolgame.game.objects.entities.BaseEntity;

/**
 * Böööse Hasen
 */
public class Rabbit extends BaseEntity
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private static final float ACCELERATION = 0.28f;
    private static final float DIAGONAL_SWITCH_TIME = 0.4f;
    private static final float TARGET_DISTANCE_SQUARED = 16f;

    private int health = 5;
    private ActionCallback deathCallback = null;
    private AttackHandler handler;

    protected Body body;

    /**
     * Interesant für Polylines. Sagt aus, ob der Charakter am Ende des Pfades
     * wieder zum Anfang gehen soll.
     * Bei false wird der Charakter zum Anfang teleportiert.
     */
    protected boolean repeating = true;

    /**
     * Handelt es sich bei den Wegpunkten um einen geschlossenen Kreis?
     * Wird automatisch bestimmt.
     */
    protected boolean loop = true;

    /**
     * Kann benutzt werden, um die Richtung des NPCs zu ändern.
     */
    protected boolean direction = true;

    /**
     * Die Wegpunkte als Array.
     */
    protected Vector2[] waypoints = null;

    /**
     * Das aktuelle Ziel.
     */
    protected int targetIndex;

    /**
     * Die Geschwindigkeit dieses NPCs.
     */
    protected float speed = 3f;

    /**
     * Die letzte X Geschwindigkeit.
     */
    protected float lastDeltaX = 0;

    /**
     * Die letzte Y Geschwindigkeit.
     */
    protected float lastDeltaY = 0;

    /**
     * Kompensiert ein starkes Ungleichgewicht in der Entfernung zwischen der X und der Y Achse.
     * Verhindert zittriges, diagonales gehen.
     */
    protected boolean proportionCompensation = false;

    /**
     * Timer, um zwischen X und Y Bewegungen zu wechseln.
     */
    private float diagonalSwitchTimer = 0f;

    private TextureAtlas npcAtlas;

    private TextureRegion rabbit = null;


    private float animationTime = 0f;

    private float hitTimer = 0f;
    private float attackTimer = 1.3f;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Konstruktor.
     *
     * @param objectId die ID des MapObjects
     */
    public Rabbit(String objectId)
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

        npcAtlas = new TextureAtlas(Gdx.files.internal("data/graphics/packed/rabbit.atlas"));

        rabbit = npcAtlas.findRegion("rabbit_mean");

        float[] vertices;

        if (rawObject instanceof PolygonMapObject)
        {
            PolygonMapObject polygon = (PolygonMapObject) rawObject;
            loop = true;

            vertices = polygon.getPolygon().getTransformedVertices();
        }
        else
        {
            Gdx.app.log("WARNING", objectId + ": MapObject must be Polygon.");

            worldObjectManager.removeObject(this);

            return;
        }

        waypoints = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; i++)
        {
            waypoints[i] = new Vector2();
            waypoints[i].x = vertices[i * 2];
            waypoints[i].y = vertices[i * 2 + 1];
        }

        if (waypoints.length < 2)
        {
            Gdx.app.log("WARNING", objectId + ": Must have at least 2 Waypoints.");

            worldObjectManager.removeObject(this);

            return;
        }

        body = createCircleBody(waypoints[0].cpy(), 10);
        targetIndex = 1;


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

                    worldObjectManager.removeObject(Rabbit.this);
                } else {
                    hitTimer += 0.6f;
                }

                return true;
            }
        });

        handler.createAttackFixture(body, Physics.createRectangle(64, 64, new Vector2(0, 0)));
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

                handler.attack(6);
            }
        }

        Vector2 target = waypoints[targetIndex];
        Vector2 pos = body.getPosition();
        pos.scl(Physics.PPM);

        if (pos.dst2(target) < TARGET_DISTANCE_SQUARED)
        {
            if (direction)
                targetIndex++;
            else
                targetIndex--;

            if (loop)
            {
                if (targetIndex < 0) targetIndex = waypoints.length - 1;
                if (targetIndex >= waypoints.length) targetIndex = 0;
            } else {
                if (repeating)
                {
                    if (targetIndex < 0)
                    {
                        targetIndex = 1;
                        direction = true;
                    }
                    if (targetIndex >= waypoints.length)
                    {
                        targetIndex = waypoints.length - 2;
                        direction = false;
                    }
                } else {
                    if (targetIndex < 0)
                    {
                        targetIndex = waypoints.length - 1;
                        body.setTransform(waypoints[targetIndex].cpy().scl(Physics.MPP), 0f);
                    }
                    if (targetIndex >= waypoints.length)
                    {
                        targetIndex = 0;
                        body.setTransform(waypoints[targetIndex].cpy().scl(Physics.MPP), 0f);
                    }
                }
            }
            return;
        }

        float deltaX = 0;
        float deltaY = 0;
        float dX = target.x - pos.x;
        float dY = target.y - pos.y;
        float proportion = Math.abs(dX) / (Math.abs(dX) + Math.abs(dY));

        if (proportionCompensation)
        {
            if (MathUtils.isEqual(proportion, 0.5f, 0.06f))
            {
                proportionCompensation = false;
            }
            else if (proportion > 0.5f)
            {
                deltaX = (dX > 0) ? speed : -speed;
            }
            else {
                deltaY = (dY > 0) ? speed : -speed;
            }
        } else
        {
            if (proportion > 0.85f)
            {
                deltaX = (dX > 0) ? speed : -speed;
                proportionCompensation = true;
            }
            else if (proportion < 0.15f)
            {
                deltaY = (dY > 0) ? speed : -speed;
                proportionCompensation = true;
            }
            else
            {
                deltaX = (dX > 0) ? speed : -speed;
                deltaY = (dY > 0) ? speed : -speed;
            }
        }

        if (!MathUtils.isZero(deltaX) && !MathUtils.isZero(deltaY))
        {
            diagonalSwitchTimer += deltaTime;

            if (diagonalSwitchTimer < 0f)
            {
                deltaX = 0f;
            } else {
                deltaY = 0f;

                if (diagonalSwitchTimer >= DIAGONAL_SWITCH_TIME)
                {
                    diagonalSwitchTimer = -DIAGONAL_SWITCH_TIME;
                }
            }
        } else {
            diagonalSwitchTimer = 0f;
        }

        lastDeltaX = MathUtils.lerp(lastDeltaX, deltaX, ACCELERATION);
        lastDeltaY = MathUtils.lerp(lastDeltaY, deltaY, ACCELERATION);

        body.setLinearVelocity(lastDeltaX, lastDeltaY);
    }

    @Override
    public void render(Batch batch, float deltaTime)
    {
        if (body == null) return;

        Color oldColor = batch.getColor();

        batch.setColor(1f, (health + 5)/10f, (health + 5)/10f, 1f);

        Vector2 pos = body.getPosition();
        pos.scl(Physics.PPM);

        float scaleX = 1f;

        animationTime += deltaTime;


        batch.draw(rabbit,                                      // TextureRegion (front, back, side)
                pos.x - rabbit.getRegionWidth() / 2,           // Offset to the X position (character center)
                pos.y,                                          // Y position is at the foots
                rabbit.getRegionWidth() / 2,                   // Origin X (important for flipping)
                rabbit.getRegionHeight(),                      // Origin Y
                rabbit.getRegionWidth(),                       // Width
                rabbit.getRegionHeight(),                      // Height
                scaleX,                                         // Scale X (-1 to flip)
                1f,                                             // Scale Y
                0f);                                            // Rotation

        batch.setColor(oldColor);
    }

    @Override
    public void onDispose()
    {
        npcAtlas.dispose();
        handler.dispose();

        worldObjectManager.getPhysicalWorld().destroyBody(body);
    }
}
