package de.entwicklerpages.java.schoolgame.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.I18NBundle;

import de.entwicklerpages.java.schoolgame.SchoolGame;

public abstract class Level {
    protected SchoolGame game;
    private LevelManager manager;

    private LevelState levelState = LevelState.INTRO;

    private I18NBundle localeBundle;
    private IngameMenu ingameMenu;

    private OrthographicCamera camera;

    private String mapName;
    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer tileMapRenderer;

    private Player player;

    // BASE METHODS

    protected Level(String map)
    {
        mapName = map;
    }

    public final void create(SchoolGame game, LevelManager manager, SaveData saveData)
    {
        Gdx.app.log("INFO", "Load level " + mapName + " ...");

        this.game = game;
        this.manager = manager;

        if (this.getIntroCutScene() == null)
            levelState = LevelState.PLAYING;

        camera = game.getCamera();

        player = new Player(saveData.getPlayerName(), saveData.isMale());

        FileHandle baseFileHandle = Gdx.files.internal("I18n/Game");
        localeBundle = I18NBundle.createBundle(baseFileHandle);

        FileHandle mapFile = Gdx.files.internal("maps/" + mapName + ".tmx");

        if (!mapFile.exists() || mapFile.isDirectory())
        {
            Gdx.app.error("ERROR", "The map file " + mapName + ".tmx doesn't exists!");
            manager.exitToMenu();
            return;
        }

        tileMap = new TmxMapLoader().load(mapFile.path());
        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap, 1f);

        ingameMenu = new IngameMenu(game, this);

        Gdx.app.log("INFO", "Level loaded.");
    }

    public final void update(float deltaTime)
    {
        if (levelState != LevelState.PLAYING) return;

        player.update(deltaTime);

        camera.position.set(player.getPosX(), player.getPosY(), 0);
        camera.update();
    }

    public final void render(float deltaTime)
    {
        tileMapRenderer.setView(camera);
        tileMapRenderer.render();

        if (levelState == LevelState.PAUSE)
            ingameMenu.render(camera);
    }

    public final void dispose()
    {
        if (tileMapRenderer != null)
            tileMapRenderer.dispose();

        if (tileMap != null)
            tileMap.dispose();
    }

    public final boolean keyDown(int keycode)
    {
        if (keycode == Input.Keys.ESCAPE)
        {
            if (levelState == LevelState.PLAYING)
            {
                ingameMenu.reset();
                setPause();
            }
            else if (levelState == LevelState.PAUSE)
            {
                setPlaying();
            }
            return true;
        }

        if (levelState == LevelState.PAUSE)
            return ingameMenu.handleInput(keycode);

        return false;
    }

    // NORMAL METHODS

    public CutScene getIntroCutScene()
    {
        return null;
    }

    public CutScene getOutroCutScene()
    {
        return null;
    }

    // FINAL METHODS

    public final void exitToMenu()
    {
        manager.exitToMenu();
    }
    public final void exitToCredits()
    {
        manager.exitToCredits();
    }
    public final void setPause() {
        levelState = LevelState.PAUSE;
    }
    public final void setPlaying() {
        levelState = LevelState.PLAYING;
    }

    public final I18NBundle getLocaleBundle()
    {
        return localeBundle;
    }

    protected final void changeLevel(String newLevel)
    {
        if (getOutroCutScene() == null)
            manager.changeLevel(newLevel);
        else {
            levelState = LevelState.OUTRO;
        }
    }

    // ABSTRACT METHODS

    public abstract String getTitle();

    private enum LevelState
    {
        INTRO,
        PLAYING,
        PAUSE,
        OUTRO
    }
}
