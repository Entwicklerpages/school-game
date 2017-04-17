package de.entwicklerpages.java.schoolgame.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

import de.entwicklerpages.java.schoolgame.common.ActionCallback;
import de.entwicklerpages.java.schoolgame.game.Physics;
import de.entwicklerpages.java.schoolgame.game.PhysicsListener;
import de.entwicklerpages.java.schoolgame.game.PhysicsTileMapBuilder;
import de.entwicklerpages.java.schoolgame.game.Player;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * @author nico
 */
public class InteractionHandler implements Interactable, PhysicsListener
{
    protected WorldObjectManager manager;
    protected int priority;
    protected ActionCallback actionCallback = null;
    protected InteractionCallback interactionCallback = null;

    public InteractionHandler(WorldObjectManager manager, int priority)
    {
        this.manager = manager;
        this.priority = priority;
    }

    public void createStaticInteractionBody(MapObject mapObject)
    {
        Shape shape = PhysicsTileMapBuilder.createShape(mapObject, PhysicsTileMapBuilder.TYPE_TRIGGER);

        if (shape == null)
        {
            Gdx.app.log("WARNING", "Unkown trigger type! The interaction object " + mapObject.getName() + " will be ignored!");
            return;
        }

        BodyDef triggerDef = new BodyDef();
        triggerDef.type = BodyDef.BodyType.StaticBody;

        Body trigger = manager.getPhysicalWorld().createBody(triggerDef);

        createInteractionFixture(trigger, shape);

        shape.dispose();
    }

    public void createStaticInteractionBody(Vector2 position, float radius)
    {

        CircleShape circle = new CircleShape();

        circle.setPosition(new Vector2(position.x, position.y).scl(Physics.MPP));
        circle.setRadius(radius * Physics.MPP);

        BodyDef triggerDef = new BodyDef();
        triggerDef.type = BodyDef.BodyType.StaticBody;

        Body trigger = manager.getPhysicalWorld().createBody(triggerDef);

        createInteractionFixture(trigger, circle);

        circle.dispose();
    }

    public void createInteractionFixture(Body body, Shape shape)
    {
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;
        fixture.filter.categoryBits = Physics.CATEGORY_TRIGGER;
        fixture.filter.maskBits = Physics.MASK_TRIGGER;

        body.createFixture(fixture).setUserData(this);
    }

    @Override
    public int getInteractionPriority()
    {
        return priority;
    }

    @Override
    public void interaction(Player player)
    {
        if (interactionCallback != null && player != null)
        {
            interactionCallback.run(player);
        }

        if (actionCallback != null)
        {
            actionCallback.run();
        }
    }

    @Override
    public void beginContact(Fixture other, Object otherUserData)
    {
        manager.registerForInteraction(this);
    }

    @Override
    public void endContact(Fixture other, Object otherUserData)
    {
        manager.unregisterForInteraction(this);
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public void setInteractionCallback(InteractionCallback interactionCallback)
    {
        this.interactionCallback = interactionCallback;
    }

    public void setActionCallback(ActionCallback actionCallback)
    {
        this.actionCallback = actionCallback;
    }

    public interface InteractionCallback
    {
        void run(Player player);
    }
}
