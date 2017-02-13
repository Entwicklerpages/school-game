package de.entwicklerpages.java.schoolgame.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.entwicklerpages.java.schoolgame.GameState;
import de.entwicklerpages.java.schoolgame.SchoolGame;


public class Splashscreen implements GameState {
    private SpriteBatch batch;
    private Sprite screenSprite;
    private float timer;
    private SchoolGame game;

    @Override
    public void create(SchoolGame game) {
        this.game = game;
        batch = new SpriteBatch();

        Texture splashImg = new Texture(Gdx.files.internal("misc/splashscreen.jpg"));
        screenSprite = new Sprite(splashImg);
        screenSprite.setPosition(-screenSprite.getWidth() / 2, -screenSprite.getHeight() / 2);

        timer = 4; // 4 Sekunden
    }

    @Override
    public void render(OrthographicCamera camera, float deltaTime) {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        screenSprite.draw(batch);
        batch.end();
    }

    @Override
    public void update(float deltaTime) {
        timer -= deltaTime;

        if (timer <= 0)
        {
            game.setGameState(new MainMenu());
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        screenSprite.getTexture().dispose();
    }

    @Override
    public String getStateName() {
        return "SPLASHSCREEN";
    }
}
