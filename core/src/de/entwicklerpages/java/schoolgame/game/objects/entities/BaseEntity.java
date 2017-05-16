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
    protected EntityOrientation orientation;

    public BaseEntity(String objectId)
    {
        super(objectId);
        orientation = EntityOrientation.LOOK_FORWARD;
    }

    @Override
    public float getPosY()
    {
        return 0;
    }

    @Override
    public abstract void onUpdate(float deltaTime);

    @Override
    public abstract void render(Batch batch, float deltaTime);

    protected Body createBoxBody(Vector2 position, float width, float height)
    {
        return createBoxBody(position, width, height, 0f, 0f);
    }

    protected Body createBoxBody(Vector2 position, float width, float height, float offsetX, float offsetY)
    {
        return createEntityBody(position, Physics.createRectangle(width, height, new Vector2(offsetX, offsetY)));
    }

    protected Body createCircleBody(Vector2 position, float radius)
    {
        CircleShape circle = new CircleShape();

        circle.setPosition(Vector2.Zero);
        circle.setRadius(radius * Physics.MPP);

        return createEntityBody(position, circle);
    }

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
