package de.entwicklerpages.java.schoolgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.lang.reflect.Field;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.entwicklerpages.java.schoolgame.GameState;
import de.entwicklerpages.java.schoolgame.SchoolGame;
import de.entwicklerpages.java.schoolgame.menu.MainMenu;

public class LevelManager implements GameState {

    private SaveData saveData;
    private SchoolGame game;
    private SaveData.Slot lastSlot;

    private Map<String, Class<? extends Level>> levelMap = new HashMap<String, Class<? extends Level>>();

    public LevelManager(SaveData.Slot slot)
    {
        this.lastSlot = slot;

        mapAllLevels();
    }

    private void mapAllLevels()
    {
        Reflections reflections = new Reflections(this.getClass().getPackage().getName() + ".levels");
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
                            levelMap.put(key, level);
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
    }

    private Level createLevelWithName(String name)
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
    }

    @Override
    public void render(OrthographicCamera camera, float deltaTime) {

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public String getStateName() {
        return "LEVEL_MANAGER";
    }
}
