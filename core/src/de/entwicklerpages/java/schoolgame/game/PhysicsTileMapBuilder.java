package de.entwicklerpages.java.schoolgame.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Übernommen von
 * http://gamedev.stackexchange.com/a/70448
 * und angepasst an die Bedürfnisse des Spiels.
 *
 * createCircle wurde selbst hinzugefügt.
 */
public final class PhysicsTileMapBuilder
{
    private PhysicsTileMapBuilder()
    {
    }

    public static void buildBodiesFromLayer(MapLayer layer, World world)
    {
        for(MapObject tileObject : layer.getObjects())
        {
            if (
                    tileObject instanceof TextureMapObject ||
                    tileObject instanceof CircleMapObject ||
                    tileObject instanceof EllipseMapObject)
                continue;

            Shape shape;

            if (tileObject instanceof RectangleMapObject)
            {
                shape = createRectangle((RectangleMapObject) tileObject);
            }
            else if (tileObject instanceof PolygonMapObject)
            {
                shape = createPolygon((PolygonMapObject) tileObject);
            }
            else if (tileObject instanceof PolylineMapObject)
            {
                shape = createPolyline((PolylineMapObject) tileObject);
            }
            else
            {
                continue;
            }

            if (shape == null)
                continue;

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            Body body = world.createBody(bodyDef);

            FixtureDef fixture = new FixtureDef();
            fixture.shape = shape;
            fixture.filter.categoryBits = Physics.CATEGORY_WORLD;
            fixture.filter.maskBits = Physics.MASK_WORLD;

            body.createFixture(fixture);

            shape.dispose();
        }
    }

    public static PolygonShape createRectangle(RectangleMapObject rectObject)
    {
        Rectangle rectangle = rectObject.getRectangle();

        Vector2 center = new Vector2(rectangle.x + rectangle.width * 0.5f, rectangle.y + rectangle.height * 0.5f);

        return Physics.createRectangle(rectangle.width, rectangle.height, center);
    }

    public static PolygonShape createPolygon(PolygonMapObject polyObject)
    {
        PolygonShape polygon = new PolygonShape();

        float[] vertices = polyObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        if (worldVertices.length > 8)
        {
            Gdx.app.error("ERROR", "Too many polygon vertices. A polygon may have up to 8 vertices but no more.");
            return null;
        }

        for (int i = 0; i < vertices.length / 2; i++)
        {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] * Physics.MPP;
            worldVertices[i].y = vertices[i * 2 + 1] * Physics.MPP;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    public static ChainShape createPolyline(PolylineMapObject polyObject)
    {
        float[] vertices = polyObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; i++)
        {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] * Physics.MPP;
            worldVertices[i].y = vertices[i * 2 + 1] * Physics.MPP;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);

        return chain;
    }

    public static CircleShape createCircle(EllipseMapObject ellipseObject)
    {
        Ellipse ellipse = ellipseObject.getEllipse();
        CircleShape circle = new CircleShape();

        circle.setPosition(new Vector2(ellipse.x + ellipse.width * 0.5f, ellipse.y + ellipse.height * 0.5f).scl(Physics.MPP));
        circle.setRadius(Math.min(ellipse.width, ellipse.height) * 0.5f * Physics.MPP);

        return circle;
    }
}
