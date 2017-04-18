package de.entwicklerpages.java.schoolgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.entwicklerpages.java.schoolgame.common.InputManager;
import de.entwicklerpages.java.schoolgame.menu.Splashscreen;

/**
 * Einstiegspunkt in das Spiel
 *
 * Das ist die Hauptklasse. Sie verwaltet die Kamera und den Viewport und welcher GameState aktiv ist.
 *
 * @see GameState
 * @see AudioManager
 */
public class SchoolGame implements ApplicationListener
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private OrthographicCamera camera;
    private Viewport viewport;

    private InputMultiplexer inputMultiplexer;
    private Preferences preferences;
    private BitmapFont defaultFont;
    private BitmapFont longTextFont;
    private BitmapFont titleFont;
    private AudioManager audioManager;

    private GameState gameState;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /***
     * Initialisierung
     *
     * Diese Methode wird zum Start einmal aufgerufen.
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

        InputManager.getInstance().setGame(this);

        Gdx.app.getApplicationLogger().log("INFO", "Finished.");
    }

    /***
     * Wird aufgerufen, wenn sich die Fenstergröße ändert.
     *
     * @param width    Die neue Breite des Fensters
     * @param height   Die neue Höhe des Fensters
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
     * Brauchen wir eigentlich nicht, da wir auch dispose haben.
     * Allerdings nutzen wir sie, um die Fenstergröße zu speichern und den Cursor wieder frei zu geben.
     *
     * @see SchoolGame#dispose()
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
     * Aufräumen
     *
     * Hier werden alle Resourcen freigegeben. Dazu wird auch der aktive GameState informiert.
     *
     * @see GameState#dispose()
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

    /**
     * Fügt dem Spiel einen InputProcessor hinzu.
     * Dieser ist in der Lage auf Tastatur Events
     * zu reagieren.
     *
     * Es können mehrere gleichzeitig aktiv sein, da im Hintergrund ein Multiplexer benutzt wird.
     * Beim wechseln des GameStates werden automatisch alle Prozessoren entfernt.
     *
     * @see SchoolGame#setGameState(GameState)
     * @see SchoolGame#removeInputProccessor(InputProcessor)
     *
     * @param inputProcessor Der neue InputProcessor
     */
    public void addInputProcessor(InputProcessor inputProcessor)
    {
        inputMultiplexer.addProcessor(inputProcessor);
    }

    /**
     * Entfernt einen InputProcessor.
     *
     * Selten benötigt, da die Prozessoren beim Zustandswechsel automatisch entfernt werden.
     *
     * @see SchoolGame#addInputProcessor(InputProcessor)
     *
     * @param inputProcessor
     */
    public void removeInputProccessor(InputProcessor inputProcessor)
    {
        inputMultiplexer.removeProcessor(inputProcessor);
    }

    /**
     * Wechselt den Spielzustand.
     *
     * Kann benutzt werden um z.B. zwischen Menüs zu wechseln.
     *
     * Dabei wird automatisch
     * <ul>
     *     <li>die Kamera zurückgesetzt</li>
     *     <li>der InputMultiplexer geleert</li>
     *     <li>der neue Zustand als InputProcessor hinzugefügt, sofern dieser InputProcessor implementiert</li>
     * </ul>
     *
     * @param newState der neue Zustand
     */
    public void setGameState(GameState newState)
    {
        if (newState == null)
            return;

        Gdx.app.getApplicationLogger().log("INFO", "Change state from " + gameState.getStateName() + " to " + newState.getStateName());

        gameState.dispose();

        inputMultiplexer.clear();

        camera.position.set(0, 0, 0);

        gameState = newState;

        InputManager.getInstance().requestMenuMode();

        gameState.create(this);

        if (gameState instanceof InputProcessor) {
            inputMultiplexer.addProcessor((InputProcessor) gameState);
            InputManager.getInstance().setFeedForwardProcessor((InputProcessor) gameState);
        }
    }

    /**
     * Gibt die Spielkamera zurück
     *
     * @return die Kamera
     */
    public OrthographicCamera getCamera() {
        return camera;
    }

    /**
     * Gibt Zugriff auf die Spieleinstellungen und Speicherstände
     *
     * @return die Einstellungen
     */
    public Preferences getPreferences()
    {
        return preferences;
    }

    /**
     * Gibt Zugriff auf den AudioManager um z.B. Sounds abzuspielen.
     *
     * @see AudioManager#playSound(AudioManager.SoundKey)
     * @see AudioManager#selectMusic(String)
     *
     * @return der Audio Manager
     */
    public AudioManager getAudioManager() {
        return audioManager;
    }

    /**
     * Ermittelt, ob das Spiel als Vollbild angezeigt werden soll.
     *
     * @see SchoolGame#setFullscreen(boolean)
     *
     * @return true wenn Vollbild, false wenn Fenstermodus
     */
    public boolean shouldBeFullscreen()
    {
        return preferences.getBoolean("fullscreen", false);
    }

    /**
     * Speichert, ob das Spiel als Vollbild angezeigt werden soll.
     *
     * <b>WARNUNG:</b> Diese Methode ändert nicht die Darstellungsart. Dies muss manuell erledigt werden.
     *
     * @see SchoolGame#shouldBeFullscreen()
     *
     * @param fullscreen true wenn Vollbild, false wenn Fenstermodus
     */
    public void setFullscreen(boolean fullscreen)
    {
        preferences.putBoolean("fullscreen", fullscreen);
    }

    /**
     * Ermittelt, ob das Spiel V-Sync benutzen soll.
     *
     * @see SchoolGame#setVSync(boolean)
     *
     * @return true wenn V-Sync an, false wenn V-Sync aus
     */
    public boolean shouldVSync()
    {
        return preferences.getBoolean("vsync", false);
    }

    /**
     * Speichert, ob das Spiel V-Sync soll.
     *
     * <b>WARNUNG:</b> Diese Methode ändert nicht die Darstellungsart. Dies muss manuell erledigt werden.
     *
     * @see SchoolGame#shouldVSync()
     *
     * @param vsync true wenn V-Sync an, false wenn V-Sync aus
     */
    public void setVSync(boolean vsync)
    {
        preferences.putBoolean("vsync", vsync);
    }

    /**
     * Diese Methode gibt Zugriff auf die Standard-Schrift. (Mittlere Größe)
     *
     * Die Schriften werden durch SchoolGame verwaltet. Dadurch wird ständiges laden und entladen der Schriften vermieden.
     *
     * @return mittlere Schrift
     */
    public BitmapFont getDefaultFont()
    {
        return defaultFont;
    }

    /**
     * Diese Methode gibt Zugriff auf die Lange-Texte-Schrift. (Kleine Größe)
     *
     * Die Schriften werden durch SchoolGame verwaltet. Dadurch wird ständiges laden und entladen der Schriften vermieden.
     *
     * @return kleine Schrift
     */
    public BitmapFont getLongTextFont()
    {
        return longTextFont;
    }

    /**
     * Diese Methode gibt Zugriff auf die Titel-Schrift. (Große Größe)
     *
     * Die Schriften werden durch SchoolGame verwaltet. Dadurch wird ständiges laden und entladen der Schriften vermieden.
     *
     * @return große Schrift
     */
    public BitmapFont getTitleFont()
    {
        return titleFont;
    }
}
