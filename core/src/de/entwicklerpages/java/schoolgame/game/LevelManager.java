package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.entwicklerpages.java.schoolgame.GameState;
import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.menu.Credits;
import de.entwicklerpages.java.schoolgame.menu.MainMenu;

public class LevelManager implements GameState, InputProcessor {

    protected SaveData saveData;
    protected SchoolGame game;
    protected SaveData.Slot lastSlot;

    protected Map<String, Class<? extends Level>> levelMap = new HashMap<String, Class<? extends Level>>();

    protected Level activeLevel = null;

    public LevelManager(SaveData.Slot slot)
    {
        Gdx.app.log("INFO", "Init Level Manager ...");

        this.lastSlot = slot;

        mapAllLevels();

        Gdx.app.log("INFO", "Level Manager finished.");
    }

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

    private boolean hasParameterlessPublicConstructor(Class<? extends Level> level)
    {
        for (Constructor<?> constructor : level.getConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                return true;
            }
        }
        return false;
    }

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

    @Override
    public void render(OrthographicCamera camera, float deltaTime) {
        activeLevel.render(deltaTime);
    }

    @Override
    public void update(float deltaTime) {
        activeLevel.update(deltaTime);
    }

    @Override
    public void dispose() {
        if (activeLevel != null)
        {
            activeLevel.dispose();
        }
    }

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

    public void exitToMenu()
    {
        game.setGameState(new MainMenu());
    }

    public void exitToCredits()
    {
        game.setGameState(new Credits());
    }

    @Override
    public boolean keyDown(int keycode) {
        return activeLevel.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
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

    @Override
    public String getStateName() {
        return "LEVEL_MANAGER";
    }
}
