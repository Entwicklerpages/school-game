package de.entwicklerpages.java.schoolgame.game.objects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
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
 * @author nico
 */
public class Trigger extends WorldObject implements PhysicsListener
{
    protected ActionCallback triggerEntered = null;
    protected ActionCallback triggerLeaved = null;

    protected Body trigger;

    public Trigger(String objectId)
    {
        super(objectId);
    }

    public Trigger(String objectId, ActionCallback triggerEntered)
    {
        super(objectId);
        this.triggerEntered = triggerEntered;
    }

    public Trigger(String objectId, ActionCallback triggerEntered, ActionCallback triggerLeaved)
    {
        super(objectId);
        this.triggerEntered = triggerEntered;
        this.triggerLeaved = triggerLeaved;
    }

    @Override
    public void onInit()
    {
        World world = worldObjectManager.getPhysicalWorld();

        Shape shape = null;

        if (rawObject instanceof RectangleMapObject)
        {
            shape = PhysicsTileMapBuilder.createRectangle((RectangleMapObject) rawObject);
        }
        else if (rawObject instanceof PolygonMapObject)
        {
            shape = PhysicsTileMapBuilder.createPolygon((PolygonMapObject) rawObject);
        }
        else if (rawObject instanceof EllipseMapObject)
        {
            shape = PhysicsTileMapBuilder.createCircle((EllipseMapObject) rawObject);
        }

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

    @Override
    public void beginContact(Fixture other, Object otherUserData)
    {
        if (triggerEntered != null && otherUserData != null && otherUserData instanceof Player)
        {
            triggerEntered.run();
        }
    }

    @Override
    public void endContact(Fixture other, Object otherUserData)
    {
        if (triggerLeaved != null && otherUserData != null && otherUserData instanceof Player)
        {
            triggerLeaved.run();
        }
    }

    public ActionCallback getTriggerEntered()
    {
        return triggerEntered;
    }

    public void setTriggerEntered(ActionCallback triggerEntered)
    {
        this.triggerEntered = triggerEntered;
    }

    public ActionCallback getTriggerLeaved()
    {
        return triggerLeaved;
    }

    public void setTriggerLeaved(ActionCallback triggerLeaved)
    {
        this.triggerLeaved = triggerLeaved;
    }
}
