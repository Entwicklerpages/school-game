package de.entwicklerpages.java.schoolgame.game;


import com.badlogic.gdx.graphics.OrthographicCamera;

import de.entwicklerpages.java.schoolgame.SchoolGame;

public abstract class Level {
    protected SchoolGame game;

    private LevelManager manager;

    // BASE METHODS

    public final void create(SchoolGame game, LevelManager manager)
    {
        this.game = game;
        this.manager = manager;
    }

    public final void update(float deltaTime)
    {

    }

    public final void render(OrthographicCamera camera, float deltaTime)
    {

    }

    public final void dispose()
    {

    }

    // NORMAL METHODS

    public boolean keyDown(int keycode)
    {
        return false;
    }

    public boolean keyUp(int keycode)
    {
        return false;
    }

    public CutScene getIntroCutScene()
    {
        return null;
    }

    public CutScene getOutroCutScene()
    {
        return null;
    }

    // FINAL METHODS

    protected final void changeLevel(String newLevel)
    {
        manager.changeLevel(newLevel);
    }
    protected final void exitToMenu()
    {
        manager.exitToMenu();
    }
    protected final void exitToCredits()
    {
        manager.exitToCredits();
    }

    // ABSTRACT METHODS

    public abstract String getTitle();
}
