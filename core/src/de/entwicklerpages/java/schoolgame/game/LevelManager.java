package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Disposable;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.entwicklerpages.java.schoolgame.GameState;
import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.common.InputManager;
import de.entwicklerpages.java.schoolgame.menu.Credits;
import de.entwicklerpages.java.schoolgame.menu.MainMenu;

public class LevelManager implements GameState, InputProcessor, Disposable {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// EIGENSCHAFTEN ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Erlaubt Zugriff auf die Spielerdaten.
     *
     * @see SaveData
     */
    protected SaveData saveData;

    /**
     * Erlaubt Zugriff auf die Spielinstanz.
     */
    protected SchoolGame game;

    /**
     * Legt fest, welcher Slot zuletzt zum Speichern benutzt wurde.
     */
    protected SaveData.Slot lastSlot;

    /**
     * Speichert die Liste aller verfügbarer Level.
     */
    protected Map<String, Class<? extends Level>> levelMap = new HashMap<String, Class<? extends Level>>();

    /**
     * Das aktuelle Level.
     */
    private Level activeLevel = null;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// METHODEN /////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Standardkonstruktor.
     * Stößt das Laden der Level an.
     *
     * @param slot der Slot, der zum Laden/Speichern benutzt werden soll.
     */
    public LevelManager(SaveData.Slot slot)
    {
        Gdx.app.log("INFO", "Init Level Manager ...");

        this.lastSlot = slot;

        mapAllLevels();

        Gdx.app.log("INFO", "Level Manager finished.");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // REFLECTION

    /**
     * Lädt alle verfügbaren Level mithilfe von Reflection.
     */
    private void mapAllLevels()
    {
        Gdx.app.log("INFO", "Load Levels ...");

        Reflections reflections = new Reflections(LevelManager.class.getPackage().getName() + ".levels");
        Set<Class<? extends Level>> levels = reflections.getSubTypesOf(Level.class);

        for (Class<? extends Level> level: levels) {
            try {
                Field levelName = level.getField("LEVEL_NAME");

                Class<?> levelNameType = levelName.getType();

                if(levelNameType == String.class){
                    try {
                        String key = (String) levelName.get(null);

                        key = key.toLowerCase();
                        if (levelMap.containsKey(key))
                        {
                            Gdx.app.error("ERROR", "Level " + level.getSimpleName() + " uses a LEVEL_NAME (" + key + ") which is already in use.");
                        } else {
                            if (hasParameterlessPublicConstructor(level)) {
                                levelMap.put(key, level);
                            } else {
                                Gdx.app.error("ERROR", "Level " + level.getSimpleName() + " has no public, parameterless constructor.");
                            }
                        }

                    } catch (IllegalAccessException ignored) {
                        Gdx.app.error("ERROR", "Level " + level.getSimpleName() + " have a LEVEL_NAME but it cannot be accessed.");
                    }
                } else {
                    Gdx.app.error("ERROR", "Level " + level.getSimpleName() + " have a LEVEL_NAME but it is not a String.");
                }
            } catch (NoSuchFieldException ignored) {
                Gdx.app.error("ERROR", "Level " + level.getSimpleName() + " does not have a public static LEVEL_NAME.");
            }
        }

        Gdx.app.log("INFO", "Finished. Found " + levelMap.size() + " Levels.");
    }

    /**
     * Hilfsmethode, die prüft, ob eine Klasse einen Konstruktor hat,
     * der öffentlich ist und keine Parameter erwartet.
     *
     * @param level die Levelklasse, die geprüft werden soll
     * @return true wenn die Klasse einen solchen Konstruktor hat
     */
    private boolean hasParameterlessPublicConstructor(Class<? extends Level> level)
    {
        for (Constructor<?> constructor : level.getConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Erzeigt einen Level mit einem entsprechenden Namen.
     * Gelingt nur, wenn das Level erfolgreich in die Levelliste aufgenommen werden konnte.
     *
     * @param name der Name des Levels
     * @return eine Instanz des Levels
     */
    protected Level createLevelWithName(String name)
    {
        name = name.toLowerCase();

        if (!levelMap.containsKey(name))
        {
            Gdx.app.error("ERROR", "Level " + name + " is unknown.");
            return null;
        }

        Class<? extends Level> levelClass = levelMap.get(name);

        Level level = null;

        try {
            level = levelClass.newInstance();
        } catch (Exception e) {
            Gdx.app.error("ERROR", "Exception while creating level instance of " + name, e);
            // e.printStackTrace();
        }

        return level;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BASISMETHODEN

    /**
     * Bereitet ein Level vor, geladen zu werden.
     *
     * @param game zeigt auf das SchoolGame, dass das Spiel verwaltet.
     *
     * @see Level#create(SchoolGame, LevelManager, SaveData)
     */
    @Override
    public void create(SchoolGame game) {
        this.game = game;

        if (lastSlot == null)
        {
            game.setGameState(new MainMenu()); // Sollte nie passieren.
            return;
        }

        saveData = new SaveData(game, lastSlot);

        activeLevel = createLevelWithName(saveData.getLevelId());

        if (activeLevel == null)
        {
            game.setGameState(new MainMenu()); // Sollte auch nie passieren. Hoffentlich.
            return;
        }

        saveData.setLevelName(activeLevel.getTitle());
        saveData.save(null);

        activeLevel.create(game, this, saveData);
    }

    /**
     * Rendert das aktive Level.
     *
     * @param camera  die aktuelle Kamera
     * @param deltaTime die vergangene Zeit seit dem letztem Frame
     *
     * @see Level#render(float)
     */
    @Override
    public void render(OrthographicCamera camera, float deltaTime) {
        activeLevel.render(deltaTime);
    }

    /**
     * Wird bei jedem Frame aufgerufen.
     * Das Ereignis wird an das aktive Level weitergeleitet.
     *
     * @param deltaTime die vergangene Zeit seit dem letztem Frame
     *
     * @see Level#update(float)
     */
    @Override
    public void update(float deltaTime) {
        activeLevel.update(deltaTime);
    }

    /**
     * Wird aufgerufen, wenn das Spiel beendet wird bzw. der Spieler ins Hauptmenü geht.
     * Wenn ein Level aktiv ist, wird dessen dispose Methode aufgerufen.
     *
     * @see Level#dispose()
     */
    @Override
    public void dispose() {
        if (activeLevel != null)
        {
            activeLevel.dispose();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // AKTIONS METHODEN

    /**
     * Wechselt das aktive Level.
     * Dabei wird das neue Level im Slot gespeichert.
     *
     * @param levelId die ID des neuen Levels
     *
     * @see SaveData
     * @see Level
     */
    public void changeLevel(String levelId)
    {
        Gdx.app.log("INFO", "Change level ...");

        Level newLevel = createLevelWithName(levelId);

        if (newLevel == null)
        {
            Gdx.app.log("WARNING", "Aborted.");
            return;
        }

        saveData.setLevelId(levelId);
        saveData.setLevelName(newLevel.getTitle());
        saveData.save(null);

        activeLevel.dispose();

        activeLevel = newLevel;

        activeLevel.create(game, this, saveData);

        Gdx.app.log("INFO", "Finished.");
    }

    /**
     * Startet das aktive Level neu.
     * Intern wird ein neues Level mit der gleichen ID erstellt.
     *
     * @see LevelManager#changeLevel(String)
     */
    public void reloadLevel()
    {
        Gdx.app.log("INFO", "Reload level ...");

        changeLevel(saveData.getLevelId());
    }

    /**
     * Beendet das Spiel und wechselt ins Hauptmenü.
     * Wird vermutlich von einem Level aus aufgerufen.
     *
     * @see Level#exitToMenu()
     */
    public void exitToMenu()
    {
        game.setGameState(new MainMenu());
    }

    /**
     * Beendet das Spiel und wechselt in die Credits.
     * Wird vermutlich von einem Level aus aufgerufen.
     *
     * @see Level#exitToCredits()
     */
    public void exitToCredits()
    {
        game.setGameState(new Credits());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // EINGABE EVENT METHODEN

    /**
     * Leitet Tastatureingaben an das aktive Level weiter.
     *
     * @param keycode der Tastencode, der gedrückten Taste
     * @return true, wenn das Ereignis benutzt wurde, sonst false
     *
     * @see Level#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        return activeLevel.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return activeLevel.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ANDERE METHODEN

    /**
     * Gibt den Namen des aktuellen Zustands zurück
     *
     * @return der Name dieses Zustands
     */
    @Override
    public String getStateName() {
        return "LEVEL_MANAGER";
    }
}
