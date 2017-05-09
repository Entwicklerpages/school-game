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
 *
 * Erstellt automatisch Box2D Körper für TiledMaps und
 * bietet außerdem Hilfsfunktionen, die aus Map Objekten Shapes erstellen.
 */
public final class PhysicsTileMapBuilder
{
    public static final short TYPE_ALL                  = 0x0FFF;
    public static final short TYPE_RECTANGLE            = 0x0001;
    public static final short TYPE_POLYGON              = 0x0002;
    public static final short TYPE_CIRCLE               = 0x0004;
    public static final short TYPE_POLYLINE             = 0x0008;

    public static final short TYPE_COLLISION            = TYPE_RECTANGLE | TYPE_POLYGON | TYPE_POLYLINE;
    public static final short TYPE_TRIGGER              = TYPE_RECTANGLE | TYPE_POLYGON | TYPE_CIRCLE;

    /**
     * Privater Konstruktor.
     *
     * Diese Klasse besitzt nur statische Hilfsfunktionen.
     */
    private PhysicsTileMapBuilder() {}

    /**
     * Erzeugt aus einem ObjectLayer Box2D Körper.
     *
     * @param layer die Ebene
     * @param world die Box2D Welt
     */
    public static void buildBodiesFromLayer(MapLayer layer, World world)
    {
        for(MapObject tileObject : layer.getObjects())
        {
            if (
                    tileObject instanceof TextureMapObject ||
                    tileObject instanceof CircleMapObject ||
                    tileObject instanceof EllipseMapObject)
                continue;

            Shape shape = createShape(tileObject, TYPE_COLLISION);

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

    /**
     * Erzeugt eine Form aus einem MapObject.
     *
     * @param mapObject das MapObject
     * @param mask Bitmaske, um Shape Typen auszuschließen
     * @return die entsprechende Form
     */
    public static Shape createShape(MapObject mapObject, short mask)
    {
        Shape shape = null;

        if (mapObject instanceof RectangleMapObject && (mask & TYPE_RECTANGLE) != 0)
        {
            shape = PhysicsTileMapBuilder.createRectangle((RectangleMapObject) mapObject);
        }
        else if (mapObject instanceof PolygonMapObject && (mask & TYPE_POLYGON) != 0)
        {
            shape = PhysicsTileMapBuilder.createPolygon((PolygonMapObject) mapObject);
        }
        else if (mapObject instanceof EllipseMapObject && (mask & TYPE_CIRCLE) != 0)
        {
            shape = PhysicsTileMapBuilder.createCircle((EllipseMapObject) mapObject);
        }
        else if (mapObject instanceof PolylineMapObject && (mask & TYPE_POLYLINE) != 0)
        {
            shape = createPolyline((PolylineMapObject) mapObject);
        }

        return shape;
    }

    /**
     * Erzeugt aus einem RectangleMapObject ein rechteckiges PolygonShape.
     *
     * Rotationen werden NICHT unterstützt.
     *
     * @param rectObject das MapObject
     * @return die entsprechende Form
     */
    public static PolygonShape createRectangle(RectangleMapObject rectObject)
    {
        Rectangle rectangle = rectObject.getRectangle();

        Vector2 center = new Vector2(rectangle.x + rectangle.width * 0.5f, rectangle.y + rectangle.height * 0.5f);

        return Physics.createRectangle(rectangle.width, rectangle.height, center);
    }

    /**
     * Erzeugt aus einem PolygonMapObject ein PolygonShape.
     *
     * @param polyObject das MapObject
     * @return die entsprechende Form
     */
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

    /**
     * Erzeugt aus einem PolylineMapObject ein ChainShape.
     *
     * @param polyObject das MapObject
     * @return die entsprechende Form
     */
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

    /**
     * Erzeugt aus einem EllipseMapObject ein CircleShape.
     *
     * Ellipsen werden von Box2D nicht unterstützt, Tiled erzeugt aber auch bei Kreisen EllipseMapObjects.
     * Deshalb ermittelt diese Methode den kleineren Radius und benutzt diesen um einen Kreis zu erstellen.
     *
     * @param ellipseObject das MapObject
     * @return die entsprechende Form
     */
    public static CircleShape createCircle(EllipseMapObject ellipseObject)
    {
        Ellipse ellipse = ellipseObject.getEllipse();
        CircleShape circle = new CircleShape();

        circle.setPosition(new Vector2(ellipse.x + ellipse.width * 0.5f, ellipse.y + ellipse.height * 0.5f).scl(Physics.MPP));
        circle.setRadius(Math.min(ellipse.width, ellipse.height) * 0.5f * Physics.MPP);

        return circle;
    }
}
