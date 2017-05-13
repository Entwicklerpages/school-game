package de.entwicklerpages.java.schoolgame.game.objects.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

import de.entwicklerpages.java.schoolgame.game.EntityOrientation;
import de.entwicklerpages.java.schoolgame.game.ExtendedMapDisplayObject;
import de.entwicklerpages.java.schoolgame.game.Physics;
import de.entwicklerpages.java.schoolgame.game.objects.UpdateObject;
import de.entwicklerpages.java.schoolgame.game.objects.WorldObject;

/**
 * Basisklasse für "lebende" Objekte
 *
 * @author nico
 */
public abstract class BaseEntity extends WorldObject implements ExtendedMapDisplayObject, UpdateObject
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    protected EntityOrientation orientation;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public BaseEntity(String objectId)
    {
        super(objectId);
        orientation = EntityOrientation.LOOK_FORWARD;
    }

    /**
     * Ruft die Y Position als "Tiefe" ab.
     * Sollte von den Unterklassen überschrieben werden.
     *
     * @see de.entwicklerpages.java.schoolgame.game.ExtendedMapDisplayObject
     * @return immer 0
     */
    @Override
    public float getPosY()
    {
        return 0;
    }

    /**
     * Wird in jedem Frame einmal aufgerufen.
     * Zwingt die Unterklassen eine eigene onUpdate Methode zu implementieren.
     *
     * @param deltaTime die Zeit, die seit dem letzten Frame vergangen ist
     */
    @Override
    public abstract void onUpdate(float deltaTime);

    /**
     * Wird zum rendern in die Map aufgerufen.
     * Zwingt die Unterklassen eine eigene onRender Methode zu implementieren.
     *
     * @param batch der Batch, in den gerendert werden soll
     * @param deltaTime die Zeit, die seit dem letztem Frame vergangen ist
     */
    @Override
    public abstract void render(Batch batch, float deltaTime);

    /**
     * Erlaubt es den Unterklassen möglichst einfach einen rechteckigen Box2D Körper zu erstellen.
     *
     * @param position die Startposition des Body
     * @param width die Breite der Form
     * @param height die Höhe der Form
     * @return ein Box2D Körper
     */
    protected Body createBoxBody(Vector2 position, float width, float height)
    {
        return createBoxBody(position, width, height, 0f, 0f);
    }

    /**
     * Erlaubt es den Unterklassen möglichst einfach einen rechteckigen Box2D Körper zu erstellen.
     *
     * @param position die Startposition des Body
     * @param width die Breite der Form
     * @param height die Höhe der Form
     * @param offsetX die X Verschiebung der Form
     * @param offsetY die Y Verschiebung der Form
     * @return ein Box2D Körper
     */
    protected Body createBoxBody(Vector2 position, float width, float height, float offsetX, float offsetY)
    {
        return createEntityBody(position, Physics.createRectangle(width, height, new Vector2(offsetX, offsetY)));
    }

    /**
     * Erlaubt es den Unterklassen möglichst einfach einen runden Box2D Körper zu erstellen.
     *
     * @param position die Startposition des Body
     * @param radius der Radius der Form
     * @return ein Box2D Körper
     */
    protected Body createCircleBody(Vector2 position, float radius)
    {
        CircleShape circle = new CircleShape();

        circle.setPosition(Vector2.Zero);
        circle.setRadius(radius * Physics.MPP);

        return createEntityBody(position, circle);
    }

    /**
     * Erlaubt es den Unterklassen möglichst einfach einen beliebigen Box2D Körper zu erstellen.
     *
     * @param position die Startposition des Body
     * @param shape die Form, die für dne Body verwendet werden soll
     * @return ein Box2D Körper
     */
    protected Body createEntityBody(Vector2 position, Shape shape)
    {
        position.scl(Physics.MPP);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;

        Body body = worldObjectManager.getPhysicalWorld().createBody(bodyDef);
        body.setUserData(this);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.filter.categoryBits = Physics.CATEGORY_ENTITIES;
        fixture.filter.maskBits = Physics.MASK_ENTITIES;

        body.createFixture(fixture).setUserData(this);

        shape.dispose();

        return body;
    }
}
