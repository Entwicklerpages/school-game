package de.entwicklerpages.java.schoolgame;

import com.badlogic.gdx.graphics.OrthographicCamera;

public interface GameState {

    /***
     * Wird einmal beim Statuswechsel aufgerufen.
     * @param game Zeigt auf das SchoolGame, dass das Spiel verwaltet.
     */
    void create(SchoolGame game);

    /***
     * Wird bei jedem Frame aufgerufen, um Zeichenoperationen durchzuführen.
     * @param camera  Die aktuelle Kamera. Darf verändert werden.
     * @param deltaTime Die vergangene Zeit seit dem letztem Frame.
     */
    void render(OrthographicCamera camera, float deltaTime);

    /***
     * Wird in jedem Frame aufgerufen, um Berechnungen durchzuführen.
     * @param deltaTime Die vergangene Zeit seit dem letztem Frame.
     */
    void update(float deltaTime);

    /***
     * Wird aufgerufen, bevor der Status gewchselt wird.
     */
    void dispose();

    /***
     * Gibt den Namen des aktuellen Zustands zurück.
     * @return Der Name dieses Zustands.
     */
    String getStateName();
}
