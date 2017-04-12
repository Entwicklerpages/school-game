package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.entwicklerpages.java.schoolgame.GameState;
import de.entwicklerpages.java.schoolgame.SchoolGame;


public class Splashscreen implements GameState {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private static final float SHOW_TIME = 3;
    private static final float FADE_TIME = 1;

    private SpriteBatch batch;
    private Sprite screenSprite;
    private float timer;
    private SchoolGame game;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Initialisierung
     *
     * @param game zeigt auf das SchoolGame, dass das Spiel verwaltet
     */
    @Override
    public void create(SchoolGame game) {
        this.game = game;
        batch = new SpriteBatch();

        Texture splashImg = new Texture(Gdx.files.internal("data/misc/splashscreen.png"));
        screenSprite = new Sprite(splashImg);
        screenSprite.setPosition(-screenSprite.getWidth() / 2, -screenSprite.getHeight() / 2);
        screenSprite.setAlpha(0);

        timer = SHOW_TIME + FADE_TIME * 2;
    }

    /**
     * Zeigt den Splashscreen an.
     *
     * @param camera  die aktuelle Kamera
     * @param deltaTime die vergangene Zeit seit dem letztem Frame
     */
    @Override
    public void render(OrthographicCamera camera, float deltaTime) {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        screenSprite.draw(batch);

        batch.end();
    }

    /**
     * Sorgt für das Faden des Splashscreens und wechselt danach ins Hauptmenü.
     *
     * @param deltaTime die vergangene Zeit seit dem letztem Frame
     */
    @Override
    public void update(float deltaTime) {
        timer -= deltaTime;

        float relativeAlpha = timer - (SHOW_TIME + FADE_TIME);

        if (relativeAlpha > 0)  // Langsam anzeigen
        {
            screenSprite.setAlpha(1 - relativeAlpha / FADE_TIME);
        }
        else if (timer < FADE_TIME)    // Langsam ausblenden
        {
            screenSprite.setAlpha(timer / FADE_TIME);
        }

        if (timer <= 0)
        {
            game.setGameState(new MainMenu());
        }
    }

    /**
     * Aufräumarbeiten.
     */
    @Override
    public void dispose() {
        batch.dispose();
        screenSprite.getTexture().dispose();
    }

    @Override
    public String getStateName() {
        return "SPLASHSCREEN";
    }
}
