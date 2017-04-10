package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.math.Vector2;
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
}
