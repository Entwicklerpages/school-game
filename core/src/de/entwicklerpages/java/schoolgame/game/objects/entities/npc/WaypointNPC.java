package de.entwicklerpages.java.schoolgame.game.objects.entities.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.entwicklerpages.java.schoolgame.game.EntityOrientation;
import de.entwicklerpages.java.schoolgame.game.Physics;

/**
 * Klasse für einen einfachen NPC der immer die gleiche Strecke abläuft.
 *
 * @author nico
 */
public class WaypointNPC extends BaseNPC
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private static final float ACCELERATION = 0.28f;
    private static final float DIAGONAL_SWITCH_TIME = 0.4f;
    private static final float TARGET_DISTANCE_SQUARED = 16f;

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public WaypointNPC(String objectId, String baseName)
    {
        super(objectId, baseName);
    }

    /**
     * Initialisiert den NPC.
     * Erstellt eine Liste aller Wegpunkte.
     */
    @Override
    public void onInit()
    {
        super.onInit();

        float[] vertices;

        if (rawObject instanceof PolylineMapObject)
        {
            PolylineMapObject polyline = (PolylineMapObject) rawObject;
            loop = false;

            vertices = polyline.getPolyline().getTransformedVertices();
        }
        else if (rawObject instanceof PolygonMapObject)
        {
            PolygonMapObject polygon = (PolygonMapObject) rawObject;
            loop = true;

            vertices = polygon.getPolygon().getTransformedVertices();
        }
        else
        {
            Gdx.app.log("WARNING", objectId + ": MapObject must be Polyline or Polygon.");

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
    }

    /**
     * Wird in jedem Frame aufgerufen um die Bewegung des NPCs zu steuern.
     * Geht zu dem aktuellen Ziel.
     * Legt ein neues fest, falls das alte erreicht wurde.
     *
     * @param deltaTime die Zeit, die seit dem letzten Frame vergangen ist
     */
    @Override
    public void onUpdate(float deltaTime)
    {
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

        if (!MathUtils.isZero(deltaX))
        {
            orientation = deltaX > 0 ? EntityOrientation.LOOK_RIGHT : EntityOrientation.LOOK_LEFT;
        }
        else if (!MathUtils.isZero(deltaY))
        {
            orientation = deltaY > 0 ? EntityOrientation.LOOK_BACKWARD : EntityOrientation.LOOK_FORWARD;
        }

        lastDeltaX = MathUtils.lerp(lastDeltaX, deltaX, ACCELERATION);
        lastDeltaY = MathUtils.lerp(lastDeltaY, deltaY, ACCELERATION);

        body.setLinearVelocity(lastDeltaX, lastDeltaY);
    }

    /**
     * Wird nur bei Polylines beachtet. Sagt aus, ob der NPC am Ende der Polyline wieder die ganze Strecke zurück gehen soll,
     * oder direkt zum Start teleoprtiert werden soll.
     *
     * @param repeating true, für den Rückweg, false zum teleportieren
     */
    public void setRepeating(boolean repeating)
    {
        this.repeating = repeating;
    }

    /**
     * Legt fest, in welche Richtung der NPC gehen soll. Die Richtung hängt allerdings von der Reihenfolge der Wegpunkte in der Map ab.
     * Diese Einstellung ist nützlich um die Richtung umzukehren.
     *
     * @see WaypointNPC#toggleDirection()
     *
     * @param direction die Richtung
     */
    public void setDirection(boolean direction)
    {
        this.direction = direction;
    }

    /**
     * Wechselt die Richtung, in die der NPC die Wegpunkte abgklappert.
     */
    public void toggleDirection()
    {
        this.direction = !this.direction;
    }

    /**
     * Ruft die aktuelle Richtung ab.
     *
     * @return die aktuelle Richtung
     */
    public boolean getDirection()
    {
        return direction;
    }

    /**
     * Ruft die Geschwindigkeit des NPCs ab.
     *
     * @return die Geschwindigkeit
     */
    public float getSpeed()
    {
        return speed;
    }

    /**
     * Legt die Geschwindigkeit des NPCs fest.
     *
     * @param speed die neue Geschwindigkeit
     */
    public void setSpeed(float speed)
    {
        this.speed = speed;
    }
}
