package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * @author nico
 */
public final class Physics
{
    private Physics() {}

    /**
     * Pixel pro Meter
     * Wird benötigt, damit Box2D die Physik korrekt berechnet.
     *
     * Eigentlich sollte die Kamera dieses Verhalten regeln und Pixel sollten komplett vermieden werden.
     * Bei Entwicklungsbeginn sollte aber eigentlich gar keine Physik Engine zum Einsatz kommen.
     * Da das Spiel Pixelorientiert ist, wird an so ziehmlich jeder Stelle mit Pixeln gerechnet.
     * Dieser Ansatz erweist sich im Nachhinein als Fehler. Da allerdings die Zeit fehlt alles
     * umzuprogrammieren setzten wir auf diesen Umrechnungsfaktor.
     */
    public static final float PPM = 32;

    /**
     * Meter pro Pixel
     * Das Inverse von PPM
     *
     * @see Physics#PPM
     */
    public static final float MPP = 1f / PPM;


    public static final short CATEGORY_PLAYER          = 0x0001;
    public static final short CATEGORY_WORLD           = 0x0002;
    public static final short CATEGORY_ENTITIES        = 0x0004;
    public static final short CATEGORY_TRIGGER         = 0x0008;


    public static final short MASK_PLAYER              = -1; // Alles
    public static final short MASK_WORLD               = CATEGORY_PLAYER | CATEGORY_ENTITIES;
    public static final short MASK_ENTITIES            = CATEGORY_PLAYER | CATEGORY_ENTITIES | CATEGORY_WORLD;
    public static final short MASK_TRIGGER             = CATEGORY_PLAYER;

    public static PolygonShape createRectangle(float width, float height, Vector2 center)
    {
        return createRectangle(width, height, center, 0f);
    }

    public static PolygonShape createRectangle(float width, float height, Vector2 center, float angle)
    {
        PolygonShape rectangle = new PolygonShape();

        rectangle.setAsBox(width * 0.5f * MPP, height * 0.5f * MPP, center.cpy().scl(MPP), angle);

        return rectangle;
    }

    public static ContactListener createContactListener()
    {
        return new ContactListener()
        {
            @Override
            public void beginContact(Contact contact)
            {
                Object userDataA = contact.getFixtureA().getUserData();
                Object userDataB = contact.getFixtureB().getUserData();

                if (userDataA != null && userDataA instanceof PhysicsListener)
                {
                    ((PhysicsListener) userDataA).beginContact(contact.getFixtureB(), userDataB);
                }

                if (userDataB != null && userDataB instanceof PhysicsListener)
                {
                    ((PhysicsListener) userDataB).beginContact(contact.getFixtureA(), userDataA);
                }
            }

            @Override
            public void endContact(Contact contact)
            {
                Object userDataA = contact.getFixtureA().getUserData();
                Object userDataB = contact.getFixtureB().getUserData();

                if (userDataA != null && userDataA instanceof PhysicsListener)
                {
                    ((PhysicsListener) userDataA).endContact(contact.getFixtureB(), userDataB);
                }

                if (userDataB != null && userDataB instanceof PhysicsListener)
                {
                    ((PhysicsListener) userDataB).endContact(contact.getFixtureA(), userDataA);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold)
            {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse)
            {
            }
        };
    }
}
