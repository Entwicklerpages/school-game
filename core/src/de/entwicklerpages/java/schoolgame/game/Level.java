package de.entwicklerpages.java.schoolgame.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import de.entwicklerpages.java.schoolgame.SchoolGame;

public abstract class Level {
    protected SchoolGame game;
    private LevelManager manager;

    private LevelState levelState = LevelState.INTRO;

    // RENDERING

    private OrthographicCamera camera;

    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer tileMapRenderer;

    // OBJECTS

    private Player player;

    // BASE METHODS

    public final void create(SchoolGame game, LevelManager manager, SaveData saveData)
    {
        this.game = game;
        this.manager = manager;

        if (this.getIntroCutScene() == null)
            levelState = LevelState.PLAYING;

        camera = game.getCamera();

        player = new Player(saveData.getPlayerName(), saveData.isMale());

        tileMap = new TmxMapLoader().load(Gdx.files.internal("maps/test.tmx").path());
        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);
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
    }

    public final void dispose()
    {
        tileMapRenderer.dispose();
        tileMap.dispose();
    }

    public final boolean keyDown(int keycode)
    {
        if (keycode == Input.Keys.ESCAPE)
        {
            // TODO: Iname menu
            exitToMenu();
            return true;
        }

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

    protected final void exitToMenu()
    {
        manager.exitToMenu();
    }
    protected final void exitToCredits()
    {
        manager.exitToCredits();
    }

    protected final void changeLevel(String newLevel)
    {
        manager.changeLevel(newLevel);
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
