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
import com.badlogic.gdx.utils.Disposable;

import de.entwicklerpages.java.schoolgame.game.Physics;
import de.entwicklerpages.java.schoolgame.game.PhysicsListener;
import de.entwicklerpages.java.schoolgame.game.PhysicsTileMapBuilder;
import de.entwicklerpages.java.schoolgame.game.Player;
import de.entwicklerpages.java.schoolgame.game.WorldObjectManager;

/**
 * Reagiert auf Spielerangriffe und trägt sich für solche automatisch ein,
 * wenn der Spieler einen Trigger betritt.
 * Außerdem erlaubt diese Klasse auch Angriffe auf den Spieler.
 *
 * @see WorldObjectManager#registerForAttack(Attackable)
 * @see WorldObjectManager#unregisterForAttack(Attackable)
 *
 * @author nico
 */
public class AttackHandler implements Attackable, PhysicsListener, Disposable
{
    protected WorldObjectManager manager;
    protected AttackedCallback attackedCallback = null;
    protected boolean _canAttack = false;

    /**
     * Konstruktor.
     *
     * @param manager Zugriff auf den WorldObjectManager
     */
    public AttackHandler(WorldObjectManager manager)
    {
        this.manager = manager;
    }

    /**
     * Erzeugt automatisch aus einem MapObject einen statischen Trigger.
     *
     * @see AttackHandler#createAttackFixture(Body, Shape)
     *
     * @param mapObject das MapObject
     */
    public void createStaticAttackBody(MapObject mapObject)
    {
        Shape shape = PhysicsTileMapBuilder.createShape(mapObject, PhysicsTileMapBuilder.TYPE_TRIGGER);

        if (shape == null)
        {
            Gdx.app.log("WARNING", "Unkown trigger type! The attack object " + mapObject.getName() + " will be ignored!");
            return;
        }

        BodyDef triggerDef = new BodyDef();
        triggerDef.type = BodyDef.BodyType.StaticBody;

        Body trigger = manager.getPhysicalWorld().createBody(triggerDef);

        createAttackFixture(trigger, shape);

        shape.dispose();
    }

    /**
     * Erzeugt automatisch einen Kreis als statischen Trigger.
     *
     * @see AttackHandler#createAttackFixture(Body, Shape)
     *
     * @param position der Mittelpunkt des Kreises
     * @param radius der Radius des Kreises
     */
    public void createStaticAttackBody(Vector2 position, float radius)
    {
        CircleShape circle = new CircleShape();

        circle.setPosition(new Vector2(position.x, position.y).scl(Physics.MPP));
        circle.setRadius(radius * Physics.MPP);

        BodyDef triggerDef = new BodyDef();
        triggerDef.type = BodyDef.BodyType.StaticBody;

        Body trigger = manager.getPhysicalWorld().createBody(triggerDef);

        createAttackFixture(trigger, circle);

        circle.dispose();
    }

    /**
     * Erzeugt aus einer Form ein Fixture und fügt dieses einem Körper hinzu.
     * Das Fixture wird als Trigger für Angriffe benutzt.
     *
     * @param body der Körper
     * @param shape die Form
     */
    public void createAttackFixture(Body body, Shape shape)
    {
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;
        fixture.filter.categoryBits = Physics.CATEGORY_TRIGGER;
        fixture.filter.maskBits = Physics.MASK_TRIGGER;

        body.createFixture(fixture).setUserData(this);
    }

    /**
     * Wird bei einem Spielerangriff aufgerufen.
     * Führt die Callbacks aus.
     *
     * @param player der Spieler
     * @param damage der Schaden duch den Spieler
     */
    @Override
    public boolean onPlayerAttack(Player player, int damage)
    {
        if (attackedCallback != null && player != null)
        {
            return attackedCallback.run(player, damage);
        }

        return false;
    }

    /**
     * Wird aufgerufen, wenn der Spieler den definierten Bereich betritt.
     * Registriert dieses Objekt, damit es auf Angriffe reagieren und selber welche durchführen kann.
     *
     * @see WorldObjectManager#registerForAttack(Attackable)
     *
     * @param other der Körper mit dem die Kollision statt fand
     * @param otherUserData ein optionales User Data Objekt des Kollisionspartners
     */
    @Override
    public void beginContact(Fixture other, Object otherUserData)
    {
        manager.registerForAttack(this);
        _canAttack = true;
    }

    /**
     * Wird aufgerugen, wenn der Spieler den definierten Bereich verlässt.
     * Hebt die Registrierung dieses Objektes wieder auf.
     *
     * @see WorldObjectManager#unregisterForAttack(Attackable)
     *
     * @param other der Körper mit dem die Kollision vorher statt fand
     * @param otherUserData ein optionales User Data Objekt des Kollisionspartners
     */
    @Override
    public void endContact(Fixture other, Object otherUserData)
    {
        manager.unregisterForAttack(this);
        _canAttack = false;
    }

    /**
     * Legt ein Callback fest. Dieses Callback bekommt bei einem Angriff Zugriff auf die Spielerinstanz und den Schaden.
     *
     * @param attackedCallback das Callback
     */
    public void setAttackedCallback(AttackedCallback attackedCallback)
    {
        this.attackedCallback = attackedCallback;
    }

    /**
     * Prüft, ob das Objekt den Spieler angreifen kann.
     *
     * @return true, wenn ja
     */
    public boolean canAttack()
    {
        return _canAttack;
    }

    /**
     * Greift den Spieler an, sofern dies möglich ist.
     *
     * @param damage der Schaden, den der Spieler erhalten soll
     */
    public void attack(int damage)
    {
        if (_canAttack)
        {
            manager.attackPlayer(damage);
        }
    }

    /**
     * Entfernt den Handler als Listener.
     */
    public void dispose()
    {
        manager.unregisterForAttack(this);
        _canAttack = false;
    }

    /**
     * Ein spezielles Callback.
     * Bei einem Aufruf wird die Spielerinstanz übermittelt.
     *
     * @author nico
     */
    public interface AttackedCallback
    {
        boolean run(Player player, int damage);
    }
}
