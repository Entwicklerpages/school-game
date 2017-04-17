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
 * Reagiert auf Spielerinteraktionen und trägt sich für solche automatisch ein,
 * wenn der Spieler einen Trigger betritt.
 *
 * @see WorldObjectManager#registerForInteraction(Interactable)
 * @see WorldObjectManager#unregisterForInteraction(Interactable)
 *
 * @author nico
 */
public class InteractionHandler implements Interactable, PhysicsListener
{
    protected WorldObjectManager manager;
    protected int priority;
    protected ActionCallback actionCallback = null;
    protected InteractionCallback interactionCallback = null;

    /**
     * Konstruktor.
     *
     * @param manager Zugriff auf den WorldObjectManager
     * @param priority die Priorität
     */
    public InteractionHandler(WorldObjectManager manager, int priority)
    {
        this.manager = manager;
        this.priority = priority;
    }

    /**
     * Erzeugt automatisch aus einem MapObject einen statischen Trigger.
     *
     * @see InteractionHandler#createInteractionFixture(Body, Shape)
     *
     * @param mapObject das MapObject
     */
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

    /**
     * Erzeugt automatisch einen Kreis als statischen Trigger.
     *
     * @see InteractionHandler#createInteractionFixture(Body, Shape)
     *
     * @param position der Mittelpunkt des Kreises
     * @param radius der Radius des Kreises
     */
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

    /**
     * Erzeugt aus einer Form ein Fixture und fügt dieses einem Körper hinzu.
     * Das Fixture wird als Trigger für Interaktionen benutzt.
     *
     * @param body der Körper
     * @param shape die Form
     */
    public void createInteractionFixture(Body body, Shape shape)
    {
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;
        fixture.filter.categoryBits = Physics.CATEGORY_TRIGGER;
        fixture.filter.maskBits = Physics.MASK_TRIGGER;

        body.createFixture(fixture).setUserData(this);
    }

    /**
     * Ruft die Priorität dieses Handlers ab.
     *
     * @return die Priorität
     */
    @Override
    public int getInteractionPriority()
    {
        return priority;
    }

    /**
     * Wird bei eine Spielerinteraktion aufgerufen.
     * Führt die Callbacks aus.
     *
     * @param player der Spieler
     */
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

    /**
     * Wird aufgerufen, wenn der Spieler den definierten Bereich betritt.
     * Registriert dieses Objekt, damit es auf Interaktionen reagieren kann.
     *
     * @see WorldObjectManager#registerForInteraction(Interactable)
     *
     * @param other der Körper mit dem die Kollision statt fand
     * @param otherUserData ein optionales User Data Objekt des Kollisionspartners
     */
    @Override
    public void beginContact(Fixture other, Object otherUserData)
    {
        manager.registerForInteraction(this);
    }

    /**
     * Wird aufgerugen, wenn der Spieler den definierten Bereich verlässt.
     * Hebt die Registrierung dieses Objektes wieder auf.
     *
     * @see WorldObjectManager#unregisterForInteraction(Interactable)
     *
     * @param other der Körper mit dem die Kollision vorher statt fand
     * @param otherUserData ein optionales User Data Objekt des Kollisionspartners
     */
    @Override
    public void endContact(Fixture other, Object otherUserData)
    {
        manager.unregisterForInteraction(this);
    }

    /**
     * Legt die Priorität fest.
     *
     * @param priority die Priorität
     */
    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    /**
     * Legt ein Callback fest. Dieses Callback bekommt bei einer Interkation Zugriff auf die Spielerinstanz.
     *
     * @param interactionCallback das Callback
     */
    public void setInteractionCallback(InteractionCallback interactionCallback)
    {
        this.interactionCallback = interactionCallback;
    }

    /**
     * Legt ein Callback fest. Dieses Callback bekommt bei einer Interkation KEINEN Zugriff auf die Spielerinstanz.
     *
     * @param actionCallback das Callback
     */
    public void setActionCallback(ActionCallback actionCallback)
    {
        this.actionCallback = actionCallback;
    }

    /**
     * Ein spezielles Callback.
     * Bei einem Aufruf wird die Spielerinstanz übermittelt.
     *
     * @author nico
     */
    public interface InteractionCallback
    {
        void run(Player player);
    }
}
