package de.entwicklerpages.java.schoolgame;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Definiert einen Zustand des Spieles, zum Beispiel ein Menü oder
 * das "Spiel" selbst.
 *
 * @author nico
 */
public interface GameState {

    /**
     * Wird einmal beim Statuswechsel aufgerufen.
     *
     * @param game zeigt auf das SchoolGame, dass das Spiel verwaltet
     */
    void create(SchoolGame game);

    /**
     * Wird bei jedem Frame aufgerufen, um Zeichenoperationen durchzuführen.
     *
     * @param camera  die aktuelle Kamera
     * @param deltaTime die vergangene Zeit seit dem letztem Frame
     */
    void render(OrthographicCamera camera, float deltaTime);

    /**
     * Wird in jedem Frame aufgerufen, um Berechnungen durchzuführen.
     *
     * @param deltaTime die vergangene Zeit seit dem letztem Frame
     */
    void update(float deltaTime);

    /**
     * Wird aufgerufen, bevor der Status gewechselt wird.
     * Hier können Resourcen freigegeben werden.
     */
    void dispose();

    /**
     * Gibt den Namen des aktuellen Zustands zurück
     *
     * @return der Name dieses Zustands
     */
    String getStateName();
}
