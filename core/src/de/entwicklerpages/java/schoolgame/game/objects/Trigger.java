package de.entwicklerpages.java.schoolgame.game.objects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.Physics;
import de.entwicklerpages.java.schoolgame.game.PhysicsListener;
import de.entwicklerpages.java.schoolgame.game.PhysicsTileMapBuilder;
import de.entwicklerpages.java.schoolgame.game.Player;

/**
 * Trigger Objekt
 *
 * Generischer, unischtbarer Trigger.
 *
 * @author nico
 */
public class Trigger extends WorldObject implements PhysicsListener
{
    protected ActionCallback triggerEntered = null;
    protected ActionCallback triggerLeaved = null;

    protected Body trigger;

    /**
     * Standardkonstruktor.
     *
     * @param objectId die ID des Objektes
     */
    public Trigger(String objectId)
    {
        super(objectId);
    }

    /**
     * Konstruktor.
     *
     * @param objectId die ID des Objektes
     * @param triggerEntered das Callback für das Betreten des Triggers
     */
    public Trigger(String objectId, ActionCallback triggerEntered)
    {
        super(objectId);
        this.triggerEntered = triggerEntered;
    }

    /**
     * Konstruktor.
     *
     * @param objectId die ID des Objektes
     * @param triggerEntered das Callback für das Betreten des Triggers
     * @param triggerLeaved das Callback für das Verlassen des Triggers
     */
    public Trigger(String objectId, ActionCallback triggerEntered, ActionCallback triggerLeaved)
    {
        super(objectId);
        this.triggerEntered = triggerEntered;
        this.triggerLeaved = triggerLeaved;
    }

    /**
     * Erstellt einen Box2D Körper und macht ihm zum Sensor.
     */
    @Override
    public void onInit()
    {
        World world = worldObjectManager.getPhysicalWorld();

        Shape shape = PhysicsTileMapBuilder.createShape(rawObject, PhysicsTileMapBuilder.TYPE_TRIGGER);

        if (shape == null)
        {
            Gdx.app.log("WARNING", "Unkown trigger type! The trigger " + objectId + " will be ignored!");
            return;
        }

        BodyDef triggerDef = new BodyDef();
        triggerDef.type = BodyDef.BodyType.StaticBody;

        trigger = world.createBody(triggerDef);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;
        fixture.filter.categoryBits = Physics.CATEGORY_TRIGGER;
        fixture.filter.maskBits = Physics.MASK_TRIGGER;

        trigger.createFixture(fixture).setUserData(this);

        shape.dispose();
    }

    /**
     * Wird automatisch aufgerufen, wenn dieser Trigger mit dem Spieler in Berührung kommt.
     *
     * @param other der Körper mit dem die Kollision statt fand
     * @param otherUserData ein optionales User Data Objekt des Kollisionspartners
     */
    @Override
    public void beginContact(Fixture other, Object otherUserData)
    {
        if (triggerEntered != null && otherUserData != null && otherUserData instanceof Player)
        {
            triggerEntered.run();
        }
    }

    /**
     * Wird automatisch aufgerufen, wenn dieser Trigger nicht mehr vom Spieler berührt wird.
     *
     * @param other der Körper mit dem die Kollision vorher statt fand
     * @param otherUserData ein optionales User Data Objekt des Kollisionspartners
     */
    @Override
    public void endContact(Fixture other, Object otherUserData)
    {
        if (triggerLeaved != null && otherUserData != null && otherUserData instanceof Player)
        {
            triggerLeaved.run();
        }
    }

    /**
     * Setzt das Callback für das Betreten des Triggers.
     *
     * @param triggerEntered das Callback
     */
    public void setTriggerEntered(ActionCallback triggerEntered)
    {
        this.triggerEntered = triggerEntered;
    }

    /**
     * Setzt das Callback für das Verlssen des Triggers.
     *
     * @param triggerLeaved das Callback
     */
    public void setTriggerLeaved(ActionCallback triggerLeaved)
    {
        this.triggerLeaved = triggerLeaved;
    }
}
