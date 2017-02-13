package de.entwicklerpages.java.schoolgame.menu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import de.entwicklerpages.java.schoolgame.GameState;
import de.entwicklerpages.java.schoolgame.SchoolGame;

public abstract class MenuState implements GameState {

    @Override
    public void create(SchoolGame game) {
        Gdx.app.getApplicationLogger().log("INFO", "Menu init");
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
}
