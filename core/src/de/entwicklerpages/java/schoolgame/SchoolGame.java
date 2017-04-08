package de.entwicklerpages.java.schoolgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.entwicklerpages.java.schoolgame.menu.Splashscreen;

public class SchoolGame implements ApplicationListener {
    private OrthographicCamera camera;
    private Viewport viewport;

    private InputMultiplexer inputMultiplexer;
    private Preferences preferences;
    private BitmapFont defaultFont;
    private BitmapFont longTextFont;
    private BitmapFont titleFont;
    private AudioManager audioManager;

    private GameState gameState;

    /***
     * Wird zum Start einmal aufgerufen.
     * Hier werden alle benötigten Variablen initialisiert.
     */
    @Override
    public void create() {
        Gdx.app.getApplicationLogger().log("INFO", "Init game...");

        camera = new OrthographicCamera();

        viewport = new FitViewport(1280, 720, camera);
        viewport.apply();

        gameState = new Splashscreen();
        gameState.create(this);

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        preferences = Gdx.app.getPreferences("de.entwicklerpages.java.schoolgame");

        String fontName = "Cormorant";
        if (!preferences.contains("use_pixel_font")) {
            preferences.putBoolean("use_pixel_font", false);
            preferences.flush();
        }

        if (preferences.getBoolean("use_pixel_font", false))
        {
            fontName = "VT323";
        }

        defaultFont = new BitmapFont(Gdx.files.internal("data/font/" + fontName + "_60.fnt"), false);
        //defaultFont.getData().setScale(4); // Skalierung (sollte vermieden werden)

        longTextFont = new BitmapFont(Gdx.files.internal("data/font/" + fontName + "_30.fnt"), false);
        titleFont = new BitmapFont(Gdx.files.internal("data/font/" + fontName + "_76.fnt"), false);

        if (shouldBeFullscreen())
        {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            Gdx.input.setCursorCatched(true);
        } else if (preferences.contains("window_width") && preferences.contains("window_height")) {
            Gdx.graphics.setWindowedMode(preferences.getInteger("window_width"), preferences.getInteger("window_height"));
        }
        Gdx.graphics.setVSync(shouldVSync());

        audioManager = new AudioManager(this);
        audioManager.selectMusic("zakarra_menu", 0f); // Nur als Test

        Gdx.app.getApplicationLogger().log("INFO", "Finished.");
    }

    /***
     * Wird aufgerufen, wenn sich die Fenstergröße ändert.
     *
     * @param width    Die neue Breite des Fensters
     * @param height Die neue Höhe des Fensters
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    /***
     * Hauptschleife des Spiels.
     */
    @Override
    public void render() {

        float deltaTime = Gdx.graphics.getDeltaTime();

        audioManager.update(deltaTime);

        camera.update();

        gameState.update(deltaTime);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        gameState.render(camera, deltaTime);
    }

    /***
     * Wird kurz vor dem Beenden aufgerufen.
     * Brauchen wir nicht. 
     */
    @Override
    public void pause() {
        if (!Gdx.graphics.isFullscreen()) {
            preferences.putInteger("window_width", Gdx.graphics.getWidth());
            preferences.putInteger("window_height", Gdx.graphics.getHeight());
        }
        preferences.flush();

        Gdx.input.setCursorCatched(false);
    }

    /**
     * Wird auf der Desktop-Platform nie aufgerufen.
     */
    @Override
    public void resume() {
    }

    /***
     * Hier werden alle Resourcen freigegeben.
     */
    @Override
    public void dispose() {
        Gdx.app.getApplicationLogger().log("INFO", "Dispose resources...");

        gameState.dispose();

        defaultFont.dispose();
        longTextFont.dispose();
        titleFont.dispose();

        audioManager.dispose();

        Gdx.app.getApplicationLogger().log("INFO", "Finished.");
        Gdx.app.getApplicationLogger().log("INFO", "Quit.");
    }

    public void addInputProcessor(InputProcessor inputProcessor)
    {
        inputMultiplexer.addProcessor(inputProcessor);
    }

    public void removeInputProccessor(InputProcessor inputProcessor)
    {
        inputMultiplexer.removeProcessor(inputProcessor);
    }

    public void setGameState(GameState newState)
    {
        if (newState == null)
            return;

        Gdx.app.getApplicationLogger().log("INFO", "Change state from " + gameState.getStateName() + " to " + newState.getStateName());

        gameState.dispose();

        inputMultiplexer.clear();

        camera.position.set(0, 0, 0);

        gameState = newState;

        gameState.create(this);

        if (gameState instanceof InputProcessor) {
            inputMultiplexer.addProcessor((InputProcessor) gameState);
        }
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Preferences getPreferences()
    {
        return preferences;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public boolean shouldBeFullscreen()
    {
        return preferences.getBoolean("fullscreen", false);
    }

    public void setFullscreen(boolean fullscreen)
    {
        preferences.putBoolean("fullscreen", fullscreen);
    }

    public boolean shouldVSync()
    {
        return preferences.getBoolean("vsync", false);
    }

    public void setVSync(boolean vsync)
    {
        preferences.putBoolean("vsync", vsync);
    }

    public BitmapFont getDefaultFont()
    {
        return defaultFont;
    }

    public BitmapFont getLongTextFont()
    {
        return longTextFont;
    }

    public BitmapFont getTitleFont()
    {
        return titleFont;
    }
}
