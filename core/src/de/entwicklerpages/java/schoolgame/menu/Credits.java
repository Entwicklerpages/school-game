package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.entwicklerpages.java.schoolgame.GameState;
import de.entwicklerpages.java.schoolgame.SchoolGame;

public class Credits implements GameState, InputProcessor {

    private SpriteBatch batch;
    private SchoolGame game;

    @Override
    public void create(SchoolGame game) {
        this.game = game;
        batch = new SpriteBatch();
    }

    @Override
    public void render(OrthographicCamera camera, float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.end();
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public String getStateName() {
        return "CREDITS";
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.ENTER)
        {
            game.setGameState(new MainMenu());
            return true;
        }

        return false;
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
}
